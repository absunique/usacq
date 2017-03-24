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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.nview.Node;
import com.zpayment.cmn.nview.intf.NView;
import com.zpayment.cmn.nview.intf.NViewExt;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:48:29
 * @param <T>
 */
public class NviewRefreshWrapper<T> extends AbsNviewBean<T> implements NView<T> {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger
			.getLogger(NviewRefreshWrapper.class);

	private volatile short blockId = -1;
	private static final short MAX_BLOCK_ID = 2;
	private Map<Short, NView<T>> innerViews = new HashMap<Short, NView<T>>();

	private short getNextBlockId() {
		return (short) ((blockId + 1) % MAX_BLOCK_ID);
	}

	public NviewRefreshWrapper(int nViewType) {
		super(nViewType);
	}

	@Override
	public void addMapIndex(NviewIndexCfg indexCfg) {
		super.addMapIndex(indexCfg);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public synchronized void load(List<T> listData, NViewExt<T> extOpr) {
		log.debug("begin load");

		NViewBean newNview = new NViewBean(nviewFieldStyle);
		newNview.setNviewType(nviewType);
		for (NviewIndexCfg indexCfg : nviewIndexs) {
			newNview.addMapIndex(indexCfg);
		}
		newNview.setTreeCfg(nviewTreeCfg);

		if (extOpr != null) {
			extOpr.beforeLoad(newNview, listData);
		}
		newNview.load(listData, extOpr);
		short nextBlockId = getNextBlockId();
		innerViews.put(nextBlockId, newNview);
		blockId = nextBlockId;

		if (extOpr != null) {
			extOpr.afterLoad(this);
		}
	}

	private NView<T> getCurrentNview() {
		return innerViews.get(blockId);
	}

	@Override
	public List<T> getAll() {
		return getCurrentNview().getAll();
	}

	@Override
	public T getOneByIndexUsingArgs(int index, Object... args) {
		return getCurrentNview().getOneByIndexUsingArgs(index, args);
	}

	@Override
	public List<T> getListByIndexUsingArgs(int index, Object... args) {
		return getCurrentNview().getListByIndexUsingArgs(index, args);
	}

	@Override
	public T getOneByIndexUsingKey(int index, NviewKey viewKey) {
		return getCurrentNview().getOneByIndexUsingKey(index, viewKey);
	}

	@Override
	public List<T> getListByIndexUsingKey(int index, NviewKey viewKey) {
		return getCurrentNview().getListByIndexUsingKey(index, viewKey);
	}

	@Override
	public List<T> getIndexValueListByIndex(int index) {
		return getCurrentNview().getIndexValueListByIndex(index);
	}

	@Override
	public List<NviewKey> getIndexValueListByIndexUsingCond(int index,
			Object[] args) {
		return getCurrentNview().getIndexValueListByIndexUsingCond(index, args);
	}

	@Override
	public Node<NviewKey, T> getRoot() {
		return getCurrentNview().getRoot();
	}

}
