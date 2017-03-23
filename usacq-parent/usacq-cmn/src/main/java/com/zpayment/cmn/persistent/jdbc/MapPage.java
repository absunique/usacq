/*
 * 
 * Copyright 2012, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: MapPage.java,v 1.1 2016/08/04 23:15:21 peiwang Exp $
 * 
 * Function:
 * 
 * 通用分页查询结果
 * 
 * Edit History:
 * 
 * 2012-12-4 - Create By szwang
 */

package com.zpayment.cmn.persistent.jdbc;

import java.util.List;
import java.util.Map;

import com.zpayment.cmn.persistent.Page;

/**
 * 通用分页查询结果
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public class MapPage extends Page<Map<String, String>> {

	private static final long serialVersionUID = 1L;

	/** 列显示名 */
	private String[] titles;

	/** 列名称 */
	private String[] names;

	/** 结果数据中包含主键 */
	private boolean pkIn;

	public MapPage() {

	}

	/**
	 * 设置结果内容
	 * 
	 * @since
	 * @param other
	 */
	public void setContent(Page<Map<String, String>> other) {
		other.copyPageInfoTo(this);
		this.setResults(other.getResults());
	}

	public void setNames(List<String> nameList) {
		this.names = nameList.toArray(new String[nameList.size()]);
	}

	public void setTitles(List<String> titleList) {
		this.titles = titleList.toArray(new String[titleList.size()]);
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public boolean isPkIn() {
		return pkIn;
	}

	public void setPkIn(boolean pkIn) {
		this.pkIn = pkIn;
	}

}
