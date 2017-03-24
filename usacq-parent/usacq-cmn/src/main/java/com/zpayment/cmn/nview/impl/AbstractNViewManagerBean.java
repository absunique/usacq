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

package com.zpayment.cmn.nview.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zpayment.cmn.nview.Node;
import com.zpayment.cmn.nview.intf.NView;
import com.zpayment.cmn.nview.intf.NviewManager;
import com.zpayment.cmn.persistent.PersistentService;

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 */
public abstract class AbstractNViewManagerBean implements NviewManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zpayment.cmn.nview.intf.NviewManager#init(com.zpayment.cmn.persistent
	 * .PersistentService, java.lang.String, java.lang.String)
	 */
	@Override
	public void init(PersistentService ps, String viewDefTable, String path) {
		// TODO Auto-generated method stub

	}

	// protected boolean isOk = false;
	protected Map<Class<?>, NView<?>> nviewsByClass;

	public AbstractNViewManagerBean() {
		nviewsByClass = new LinkedHashMap<Class<?>, NView<?>>();
	}

	@Override
	public List<Class<?>> getAllCacheClasses() {
		return new ArrayList<Class<?>>(nviewsByClass.keySet());
	}

	@SuppressWarnings("unchecked")
	private <T> NView<T> getViewByClazz(Class<T> clazz) {
		return (NView<T>) nviewsByClass.get(clazz);
	}

	@Override
	public <T> NView<T> getNView(Class<T> clazz) {
		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		return nview;
	}

	public <T> T getOneByIndexUsingArgs(Class<T> clazz, int index,
			Object... args) {
		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getOneByIndexUsingArgs(index, args);
		}
		return null;
	}

	public <T> List<T> getListByIndexUsingArgs(Class<T> clazz, int index,
			Object... args) {

		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getListByIndexUsingArgs(index, args);
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public <T> T getOneByIndexUsingKey(Class<T> clazz, int index,
			NviewKey viewKey) {
		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getListByIndexUsingKey(index, viewKey).get(0);
			// return nview.get
		}
		return null;
	}

	@Override
	public <T> List<T> getListByIndexUsingKey(Class<T> clazz, int index,
			NviewKey viewKey) {
		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getListByIndexUsingKey(index, viewKey);
		}
		return null;
	}

	/**
	 * 返回一个LIST
	 */
	@Override
	public <T> List<T> getList(Class<T> clazz) {
		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getListByIndexUsingArgs(0);
		}
		return null;
	}

	@Override
	public <T> List<T> getIndexValueListByIndex(Class<T> clazz, int index) {

		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getIndexValueListByIndex(index);
		}
		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NviewKey> getIndexValueListByIndexUsingCond(Class clazz,
			int index, Object... args) {
		// if (!isOk) {
		// return null;
		// }
		NView nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getIndexValueListByIndexUsingCond(index, args);
		}
		return null;
	}

	@Override
	public <T> Node<NviewKey, T> getRoot(Class<T> clazz) {
		// if (!isOk) {
		// return null;
		// }
		NView<T> nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getRoot();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NviewIndexCfg> getNviewIndexs(Class clazz) {
		NView nview = getViewByClazz(clazz);
		if (nview != null) {
			return nview.getNviewIndexs();
		}
		return null;
	}
}
