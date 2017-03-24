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

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 */
public class NViewTreeCfg {
	private String keyColumns;

	private String relatedColumns;

	public String getKeyColumns() {
		return keyColumns;
	}

	public void setKeyColumns(String keyColumns) {
		this.keyColumns = keyColumns;
	}

	public String getRelatedColumns() {
		return relatedColumns;
	}

	public void setRelatedColumns(String relatedColumns) {
		this.relatedColumns = relatedColumns;
	}

	@Override
	public String toString() {
		return "NViewTreeCfg [keyColumns=" + keyColumns + ", relatedColumns="
				+ relatedColumns + "]";
	}

}
