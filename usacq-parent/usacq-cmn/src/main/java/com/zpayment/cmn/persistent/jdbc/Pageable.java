/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc;

import java.io.Serializable;

/**
 * 分页
 * 
 * @author peiwang
 * @since 2017年3月23日
 */
public abstract class Pageable implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 查询所有数据，作为1页数据返回 */
	public static final int QUERY_ALL = 0;

	/** 页号，从1开始 */
	protected int pageNum = QUERY_ALL;

	/** 页内记录数 */
	protected int pageSize;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
