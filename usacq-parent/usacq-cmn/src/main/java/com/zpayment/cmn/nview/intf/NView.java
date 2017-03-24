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
import com.zpayment.cmn.nview.impl.NViewTreeCfg;
import com.zpayment.cmn.nview.impl.NviewIndexCfg;
import com.zpayment.cmn.nview.impl.NviewKey;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:49:23
 * @param <T>
 */
public interface NView<T> {

	final public int NVIEW_FLD_STYLE_DB = 1;
	final public int NVIEW_FLD_STYLE_POJO = 2;

	final public int NVIEW_KEY_MAP = 1;
	final public int NVIEW_KEY_TREE = 2;

	public String getName();

	public boolean isRefreshable();

	public void setNviewType(int type);

	public void load(List<T> listData, NViewExt<T> extOpr);

	public void addMapIndex(NviewIndexCfg indexCfg);

	public void setTreeCfg(NViewTreeCfg treeCfg);

	public List<T> getAll();

	public T getOneByIndexUsingArgs(int index, Object... args);

	public List<T> getListByIndexUsingArgs(int index, Object... args);

	public T getOneByIndexUsingKey(int index, NviewKey viewKey);

	public List<T> getListByIndexUsingKey(int index, NviewKey viewKey);

	public List<T> getIndexValueListByIndex(int index);

	public List<NviewKey> getIndexValueListByIndexUsingCond(int index,
			Object[] args);

	public Node<NviewKey, T> getRoot();

	public List<NviewIndexCfg> getNviewIndexs();
}
