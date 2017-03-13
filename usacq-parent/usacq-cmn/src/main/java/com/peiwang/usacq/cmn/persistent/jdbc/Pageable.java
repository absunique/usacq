/*
 * 
 * Copyright 2012, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: Pageable.java,v 1.1 2016/08/04 23:15:21 peiwang Exp $
 * 
 * Function:
 * 
 * 分页查询基类
 * 
 * Edit History:
 * 
 * 2012-11-28 - Create By szwang
 * 
 * 
 */

package com.peiwang.usacq.cmn.persistent.jdbc;

import java.io.Serializable;

/**
 * 分页查询基类
 * 
 * @author gys
 * @version
 * @since
 * 
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
