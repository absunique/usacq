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

import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.Const;
import com.zpayment.cmn.nview.intf.NView;

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 * @param <T>
 */
public abstract class AbsNviewBean<T> implements NView<T> {

	protected int nviewFieldStyle;

	protected boolean refreshable;

	protected String name;

	protected int nviewType;

	protected List<NviewIndexCfg> nviewIndexs = new LinkedList<NviewIndexCfg>();

	protected NViewTreeCfg nviewTreeCfg;

	protected String[] keyColumns;

	protected String[] relatedColumns;

	public AbsNviewBean(int nviewFieldStyle) {
		this.nviewFieldStyle = nviewFieldStyle;

	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setNviewType(int type) {
		this.nviewType = type;
	}

	@Override
	public void addMapIndex(NviewIndexCfg indexCfg) {
		nviewIndexs.add(indexCfg);
	}

	@Override
	public void setTreeCfg(NViewTreeCfg treeCfg) {
		this.nviewTreeCfg = treeCfg;
		if (nviewTreeCfg != null) {
			this.keyColumns = treeCfg.getKeyColumns().split(
					Const.COLUMN_SEPARATOR);
			this.relatedColumns = treeCfg.getRelatedColumns().split(
					Const.COLUMN_SEPARATOR);
		}
	}

	public boolean isRefreshable() {
		return refreshable;
	}

	public void setRefreshable(boolean refreshable) {
		this.refreshable = refreshable;
	}

	/**
	 * 获取所有索引配置
	 */
	@Override
	public List<NviewIndexCfg> getNviewIndexs() {
		return nviewIndexs;
	}
}
