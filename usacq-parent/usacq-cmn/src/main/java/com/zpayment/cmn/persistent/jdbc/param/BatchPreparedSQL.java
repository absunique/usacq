/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc.param;

import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.util.StringUtils;

/**
 * 
 * 批量占位符SQL语句封装
 * <p>
 * 
 * @author peiwang
 * @since 2017年3月17日
 */
public class BatchPreparedSQL extends PreparedSQL {

	private String sql;

	private List<List<SqlParam>> paramsList = new LinkedList<List<SqlParam>>();

	public BatchPreparedSQL() {
	}

	/**
	 * 添加一个参数
	 * 
	 * @since
	 * @param params
	 */
	public void addBatch(List<SqlParam> params) {
		paramsList.add(params);
	}

	public String getSql() {
		return sql;
	}

	/**
	 * 获取批量处理数
	 * 
	 * @since
	 * @return
	 */
	public int getBatchCount() {
		return paramsList.size();
	}

	/**
	 * 获取预定义参数个数
	 * 
	 * @since
	 * @return
	 */
	public int getParamCount() {
		return paramsList.isEmpty() ? 0 : paramsList.get(0).size();
	}

	public List<List<SqlParam>> getParamsList() {
		return paramsList;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setParamsList(List<List<SqlParam>> paramsList) {
		this.paramsList = paramsList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("count(").append(getBatchCount())
				.append("), ").append(sql);

		sb.append(", params{");
		if (paramsList != null) {
			List<SqlParam> params = this.paramsList.get(0);
			List<String> ps = new LinkedList<String>();
			for (SqlParam p : params) {
				if (p.isPreparedParam()) {
					ps.add(p.toString());
				}
			}
			sb.append(StringUtils.toString(ps, ","));
		} else {
			sb.append("null");
		}

		sb.append("}");

		return sb.toString();
	}

}
