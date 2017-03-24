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
package com.zpayment.cmn.nview.impl.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpayment.cmn.Const;
import com.zpayment.cmn.Const.CacheLoadType;
import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.nview.impl.AbstractNViewManagerBean;
import com.zpayment.cmn.nview.impl.NViewTreeCfg;
import com.zpayment.cmn.nview.impl.NviewIndexCfg;
import com.zpayment.cmn.nview.impl.NviewRefreshWrapper;
import com.zpayment.cmn.nview.intf.NView;
import com.zpayment.cmn.nview.intf.NviewManager;
import com.zpayment.cmn.persistent.PersistentService;
import com.zpayment.cmn.persistent.jdbc.builder.PSQLBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.SelectBuilder;
import com.zpayment.cmn.util.StringUtils;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:46:49
 */
public class NViewManagerDb extends AbstractNViewManagerBean {

	@SuppressWarnings("rawtypes")
	private Map<Class, String> sqlMap;

	/** 持久化接口 */
	private PersistentService ps;

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(NViewManagerDb.class);

	public static NViewManagerDb getInstance() {
		return NViewManagerXmlLazy._instance;
	}

	static class NViewManagerXmlLazy {
		static NViewManagerDb _instance = new NViewManagerDb();
	}

	@SuppressWarnings("rawtypes")
	public NViewManagerDb() {
		super();
		sqlMap = new HashMap<Class, String>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void loadFromDs(PersistentService ps, String viewDefTable,
			Map<Class<?>, NviewManager> managerMap) {
		this.ps = ps;
		SelectBuilder sb = SelectBuilder.build().table(viewDefTable);
		List<ViewDef> defList = ps.query(sb.toPreparedSQL(), ViewDef.class);
		for (ViewDef def : defList) {
			initOneView(def);
		}

		// 初始化
		for (Class _clazz : nviewsByClass.keySet()) {
			NView wrapper = nviewsByClass.get(_clazz);
			PSQLBuilder pb = PSQLBuilder.build().append(sqlMap.get(_clazz));
			List<?> list = ps.query(pb.toPreparedSQL(), _clazz);
			wrapper.load(list, null);
			if (log.isDebugEnabled()) {
				log.debug("init view " + wrapper.getName() + " done");
			}
			managerMap.put(_clazz, this);
		}

		// isOk = true;
	}

	/**
	 * 初始化查询信息
	 * 
	 * @since
	 */
	private String initSql(ViewDef viewDef) {
		String sql = null;
		String tableName = viewDef.getTableNm();
		String loadSql = viewDef.getLoadSql();
		String condSql = viewDef.getCondSql();
		int queryType = viewDef.getQueryType();
		// 0:loadSql表示要查询的字段，或全部字段 1:loadSql表示整个sql
		if (queryType == 0) {
			if (StringUtils.isEmpty(loadSql)) {
				sql = "select * from " + tableName;
			} else {
				sql = "select " + loadSql + " from " + tableName;
			}
			if (!StringUtils.isEmpty(condSql)) {
				sql += (" where 1=1 and " + condSql);
			}
		} else {
			sql = loadSql;
		}
		return sql;
	}

	@SuppressWarnings("rawtypes")
	public void initOneView(ViewDef def) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(def.getCacheNm());
		} catch (ClassNotFoundException e) {
			throw new BaseException(BaseErrorCode.COMN_CACHE_INIT_FAILED, e);
		}
		if (nviewsByClass.containsKey(clazz)) {
			if (def.getDataType() != Const.CacheTp.DATA_TYPE_MAP) {
				log.error("def.getDataType() " + def.getDataType()
						+ " !=  DATA_TYPE_MAP");
				return;
			}
			// LifecyleView wrapper = viewMap.get(def.getCacheNm());
			NView view = nviewsByClass.get(clazz);
			NviewIndexCfg indexCfg = new NviewIndexCfg();
			indexCfg.setId(def.getCacheIndex());
			indexCfg.setColumns(def.getIndexColumns().split(
					Const.COLUMN_SEPARATOR));
			view.addMapIndex(indexCfg);
		} else {
			sqlMap.put(clazz, initSql(def));
			NviewRefreshWrapper nview = new NviewRefreshWrapper(NView.NVIEW_FLD_STYLE_DB);
			if (def.getDataType() == Const.CacheTp.DATA_TYPE_MAP) {
				NviewIndexCfg indexCfg = new NviewIndexCfg();
				indexCfg.setId(def.getCacheIndex());
				indexCfg.setColumns(def.getIndexColumns().split(
						Const.COLUMN_SEPARATOR));
				nview.addMapIndex(indexCfg);
			}
			if(def.getLoadType() != CacheLoadType.NO_REFRESH){
				nview.setRefreshable(true);
			}
			nview.setNviewType(def.getDataType());
			NViewTreeCfg treeCfg = new NViewTreeCfg();
			treeCfg.setKeyColumns(def.getIndexColumns());
			treeCfg.setRelatedColumns(def.getRelatedColumns());
			nview.setTreeCfg(treeCfg);
			nviewsByClass.put(clazz, nview);
		}
		log.debug("init view " + def.getCacheNm());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void reloadAll() {
		for (Class _clazz : nviewsByClass.keySet()) {
			reloadOne(_clazz);
		}
	}

	@Override
	public <T> void reloadOne(Class<T> clazz) {
		NView<T> wrapper = getNView(clazz);
		if (wrapper.isRefreshable()) {
			PSQLBuilder pb = PSQLBuilder.build().append(sqlMap.get(clazz));
			List<T> list = ps.query(pb.toPreparedSQL(), clazz);
			wrapper.load(list, null);
			if (log.isDebugEnabled()) {
				log.debug("init view " + wrapper.getName() + " done");
			}
		}
	}
}
