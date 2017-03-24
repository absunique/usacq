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

package com.zpayment.cmn.nview.intf;

import java.util.List;

import com.zpayment.cmn.nview.Node;
import com.zpayment.cmn.nview.impl.NviewIndexCfg;
import com.zpayment.cmn.nview.impl.NviewKey;
import com.zpayment.cmn.persistent.PersistentService;

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 */
public interface NviewManager {

	public void init(PersistentService ps, String viewDefTable, String path);

	public void reloadAll();

	public List<Class<?>> getAllCacheClasses();

	public <T> void reloadOne(Class<T> clazz);

	public <T> NView<T> getNView(Class<T> clazz);

	public <T> List<T> getList(Class<T> clazz);

	public <T> T getOneByIndexUsingArgs(Class<T> clazz, int index,
			Object... args);

	public <T> List<T> getListByIndexUsingArgs(Class<T> clazz, int index,
			Object... args);

	public <T> T getOneByIndexUsingKey(Class<T> clazz, int index,
			NviewKey viewKey);

	public <T> List<T> getListByIndexUsingKey(Class<T> clazz, int index,
			NviewKey viewKey);

	public <T> List<T> getIndexValueListByIndex(Class<T> clazz, int index);

	public List<NviewKey> getIndexValueListByIndexUsingCond(Class<?> clazz,
			int index, Object... args);

	public <T> Node<NviewKey, T> getRoot(Class<T> clazz);

	public List<NviewIndexCfg> getNviewIndexs(Class<?> clazz);
}
