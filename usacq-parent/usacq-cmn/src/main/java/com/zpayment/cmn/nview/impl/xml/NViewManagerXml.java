/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */

package com.zpayment.cmn.nview.impl.xml;

import java.util.HashMap;
import java.util.Map;

import com.zpayment.cmn.Const;
import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.nview.impl.AbstractNViewManagerBean;
import com.zpayment.cmn.nview.impl.NViewTreeCfg;
import com.zpayment.cmn.nview.impl.NviewIndexCfg;
import com.zpayment.cmn.nview.impl.NviewRefreshWrapper;
import com.zpayment.cmn.nview.intf.NView;
import com.zpayment.cmn.nview.intf.NViewExt;
import com.zpayment.cmn.nview.intf.NviewManager;
import com.zpayment.cmn.util.StringUtils;
import com.zpayment.cmn.util.XmlUtils;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:47:37
 */
public class NViewManagerXml extends AbstractNViewManagerBean {
	private static final Logger log = Logger.getLogger(NViewManagerXml.class);

	private Map<Class<?>, String> paths;
	private Map<Class<?>, Class<?>[]> clazzCfgs;
	private Map<Class<?>, NViewExt<?>> extOprs;

	private NViewManagerXml() {
		paths = new HashMap<Class<?>, String>();
		clazzCfgs = new HashMap<Class<?>, Class<?>[]>();
		extOprs = new HashMap<Class<?>, NViewExt<?>>();
	}

	public static NViewManagerXml getInstance() {
		return NViewManagerXmlLazy._instance;
	}

	static class NViewManagerXmlLazy {
		static NViewManagerXml _instance = new NViewManagerXml();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void load(CmnXmlDef viewDef, Map<Class<?>, NviewManager> managerMap) {
		log.info("begin load [%s]", viewDef);
		String[] fullClassNames = viewDef.getClassFullName().split(
				Const.COLUMN_SEPARATOR);
		if (fullClassNames.length < 1) {
			log.error("invalid fullClassNames[%s]", fullClassNames);
			return;
		}

		Class<?> clazz = null;
		Class[] allClasses = new Class[fullClassNames.length];
		try {
			for (int i = 0; i < fullClassNames.length; i++) {
				Class _clazz = Class.forName(fullClassNames[0]);
				allClasses[i] = _clazz;
			}
			clazz = allClasses[0];
		} catch (ClassNotFoundException e) {
			throw new BaseException(BaseErrorCode.COMN_REFLACT_FAIL, e);
		}
		if (nviewsByClass.containsKey(clazz)) {
			throw new BaseException(BaseErrorCode.COMN_CACHE_KEY_DUPLICATE,
					new Object[] { clazz });
		}
		NviewRefreshWrapper nview = new NviewRefreshWrapper(
				NView.NVIEW_FLD_STYLE_POJO);
		nview.setNviewType(viewDef.getType());
		NViewTreeCfg treeCfg = null;
		CmnXmlPojo xmlData = XmlUtils.fromXml(CmnXmlPojo.class, allClasses,
				viewDef.getUrl());
		for (CmnXmlIndexDef xmlDef : viewDef.getFlatViewDefIndexs()) {
			NviewIndexCfg indexCfg = new NviewIndexCfg();
			indexCfg.setId(xmlDef.getId());
			indexCfg.setColumns(xmlDef.getColumns().split(
					Const.COLUMN_SEPARATOR));
			nview.addMapIndex(indexCfg);
			if (treeCfg == null && viewDef.getType() == NView.NVIEW_KEY_TREE) {
				treeCfg = new NViewTreeCfg();
				treeCfg.setKeyColumns(xmlDef.getColumns());
				treeCfg.setRelatedColumns(xmlDef.getRelatedColumns());
				nview.setTreeCfg(treeCfg);
			}
		}
		NViewExt<?> extOpr = null;
		if (!StringUtils.isEmpty(viewDef.getExtOprClass())) {
			try {
				Class extOprClass = Class.forName(viewDef.getExtOprClass());
				extOpr = (NViewExt<?>) extOprClass.newInstance();
			} catch (Exception e) {
				log.error("invalid opr class [%s]",
						new Object[] { viewDef.getExtOprClass() });
				return;
			}
		}
		paths.put(clazz, viewDef.getUrl());
		clazzCfgs.put(clazz, allClasses);
		extOprs.put(clazz, extOpr);
		nview.setName(viewDef.getName());
		nview.setRefreshable(viewDef.isRefreshable());
		nview.load(xmlData.getList(), extOpr);
		nviewsByClass.put(clazz, nview);
		managerMap.put(clazz, this);
	}

	public void loadFromPath(String path, Map<Class<?>, NviewManager> managerMap)
			throws BaseException {
		CmnXmlDefs fvd = XmlUtils.fromXml(CmnXmlDefs.class, new Class[] {
				CmnXmlDef.class, CmnXmlIndexDef.class }, path);
		for (CmnXmlDef viewDef : fvd.getViewDefs()) {
			try {
				load(viewDef, managerMap);
			} catch (Exception e) {
				log.error("init view [%s] fail", new Object[] { viewDef }, e);
			}
		}
		// isOk = true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void reloadOne(Class<T> clazz) {
		NView<T> nview = (NView<T>) nviewsByClass.get(clazz);
		if (nview.isRefreshable()) {

			CmnXmlPojo xmlData = XmlUtils.fromXml(CmnXmlPojo.class,
					clazzCfgs.get(clazz), paths.get(clazz));
			NViewExt<T> extOpr = (NViewExt<T>) extOprs.get(clazz);

			if (extOpr != null) {
				extOpr.beforeReload(nview, xmlData.getList());
			}
			nview.load(xmlData.getList(), extOpr);

			if (extOpr != null) {
				extOpr.afterReload(nview);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void reloadAll() {

		for (Class _clazz : nviewsByClass.keySet()) {
			reloadOne(_clazz);
		}
	}
}
