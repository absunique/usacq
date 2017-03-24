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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zpayment.cmn.Config;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.nview.Node;
import com.zpayment.cmn.nview.impl.db.NViewManagerDb;
import com.zpayment.cmn.nview.impl.xml.NViewManagerXml;
import com.zpayment.cmn.nview.intf.NView;
import com.zpayment.cmn.nview.intf.NviewManager;
import com.zpayment.cmn.persistent.PersistentService;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:48:23
 */
public class NviewManagerWrapper implements NviewManager {
	/** 用于日志记录的Logger */
	private static final Logger log = Logger
			.getLogger(NviewManagerWrapper.class);

	NViewManagerDb dbManager = NViewManagerDb.getInstance();
	NViewManagerXml xmlManager = NViewManagerXml.getInstance();
	private Map<Class<?>, NviewManager> managerMap;

	private NviewManagerWrapper() {
		managerMap = new HashMap<Class<?>, NviewManager>();
	}

	static class NviewManagerWrapperLazy {
		private static NviewManagerWrapper _instance = new NviewManagerWrapper();
	}

	static public NviewManagerWrapper getInstance() {
		return NviewManagerWrapperLazy._instance;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void init(PersistentService ds, String viewDefTable, String path) {
		if (Config.isNviewLoadFromDb() && viewDefTable != null) {
			dbManager.loadFromDs(ds, viewDefTable, managerMap);
		}
		if (Config.isNviewLoadFromXml() && path != null) {
			xmlManager.loadFromPath(path, managerMap);
		}
		for (Class _clazz : dbManager.getAllCacheClasses()) {
			managerMap.put(_clazz, dbManager);
		}
		for (Class _clazz : xmlManager.getAllCacheClasses()) {
			managerMap.put(_clazz, xmlManager);
		}

		for (Class clazz : getAllCacheClasses()) {
			log.info("class[%s],manager[%s]", clazz, managerMap.get(clazz));
		}
	}

	@Override
	public List<Class<?>> getAllCacheClasses() {
		return new ArrayList<Class<?>>(managerMap.keySet());
	}

	@Override
	public void reloadAll() {
		dbManager.reloadAll();
		xmlManager.reloadAll();
	}

	@Override
	public <T> void reloadOne(Class<T> clazz) {
		managerMap.get(clazz).reloadOne(clazz);
	}

	@Override
	public <T> NView<T> getNView(Class<T> clazz) {
		return managerMap.get(clazz).getNView(clazz);
	}

	@Override
	public <T> List<T> getList(Class<T> clazz) {
		return managerMap.get(clazz).getList(clazz);
	}

	@Override
	public <T> T getOneByIndexUsingArgs(Class<T> clazz, int index,
			Object... args) {
		return managerMap.get(clazz).getOneByIndexUsingArgs(clazz, index, args);
	}

	@Override
	public <T> List<T> getListByIndexUsingArgs(Class<T> clazz, int index,
			Object... args) {
		return managerMap.get(clazz)
				.getListByIndexUsingArgs(clazz, index, args);
	}

	@Override
	public <T> T getOneByIndexUsingKey(Class<T> clazz, int index,
			NviewKey viewKey) {
		return managerMap.get(clazz).getOneByIndexUsingKey(clazz, index,
				viewKey);
	}

	@Override
	public <T> List<T> getListByIndexUsingKey(Class<T> clazz, int index,
			NviewKey viewKey) {
		return managerMap.get(clazz).getListByIndexUsingKey(clazz, index,
				viewKey);
	}

	@Override
	public <T> List<T> getIndexValueListByIndex(Class<T> clazz, int index) {
		return managerMap.get(clazz).getIndexValueListByIndex(clazz, index);
	}

	@Override
	public List<NviewKey> getIndexValueListByIndexUsingCond(Class<?> clazz,
			int index, Object... args) {
		return managerMap.get(clazz).getIndexValueListByIndexUsingCond(clazz,
				index, args);
	}

	@Override
	public <T> Node<NviewKey, T> getRoot(Class<T> clazz) {
		return managerMap.get(clazz).getRoot(clazz);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NviewIndexCfg> getNviewIndexs(Class clazz) {
		return managerMap.get(clazz).getNviewIndexs(clazz);
	}
}
