/*
 * 
 * Copyright 2012, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: Page.java,v 1.1 2016/08/04 23:15:21 peiwang Exp $
 * 
 * Function:
 * 
 * 分页查询结果
 * 
 * Edit History:
 * 
 * 2012-11-28 - Create By szwang
 */

package com.zpayment.cmn.persistent;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 是否第一次查询 */
	private boolean first = true;
	/** 当前页数，从1开始 */
	private int currentPage;

	/** 页面记录数 */
	public int pageSize;

	/** 总页数 */
	private int totalPage;

	/** 总记录数 */
	private int totalCount;

	/** 结果 */
	private List<T> results;

	public Page() {

	}

	/**
	 * 拷贝页面信息
	 * 
	 * @since
	 * @param other
	 */
	public void copyPageInfoTo(Page<?> other) {
		other.currentPage = this.currentPage;
		other.totalPage = this.totalPage;
		other.pageSize = this.pageSize;
		other.totalCount = this.totalCount;
	}

	/**
	 * 判断是否有数据
	 * 
	 * @since
	 * @return
	 */
	public boolean isEmpty() {
		return results == null || results.isEmpty();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public Page<T> setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Page<T> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public Page<T> setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		return this;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public Page<T> setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	public List<T> getResults() {
		return results;
	}

	public Page<T> setResults(List<T> results) {
		this.results = results;
		return this;
	}

	public void forward(int pageNum) {
		// if (this.currentPage + pageNum >= this.totalPage) {
		// this.currentPage = this.totalPage;
		// } else {
		this.currentPage += pageNum;
		// }
	}

	public void backward(int pageNum) {
		this.currentPage = (this.currentPage > pageNum) ? this.currentPage
				- pageNum : 1;
	}

	public void forward() {
		forward(1);
	}

	public void backward() {
		backward(1);
	}

	public boolean isEndPage() {
		return this.currentPage > this.totalPage;
	}

	@Override
	public String toString() {
		return "Page [currentPage=" + currentPage + ", pageSize=" + pageSize
				+ ", totalPage=" + totalPage + ", totalCount=" + totalCount
				+ ", results=" + results;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
}
