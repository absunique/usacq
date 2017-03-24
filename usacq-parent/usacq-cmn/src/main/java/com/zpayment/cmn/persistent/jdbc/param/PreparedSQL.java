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

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.util.StringUtils;

/**
 * PreparedStatement方式的查询语句，包括预定义sql与对应的参数
 * <p>
 * 特别地，时间戳、二进制等类型直接在sql中写入，不需要使用预定义方式
 * <p>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class PreparedSQL {

	private String sql;

	private List<SqlParam> params = new LinkedList<SqlParam>();

	protected boolean isBatch() {
		return false;
	}

	public PreparedSQL() {

	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getCountSql() {
		return String.format("select count(*) from (%s) as t", sql);
	}

	/**
	 * 添加StringParam
	 * 
	 * @since
	 * @param value
	 * @return 预处理参数
	 */
	public SqlParam addString(String value) {
		SqlParam p = new StringParam(value);
		params.add(p);
		return p;
	}

	/**
	 * 添加IntParam
	 * 
	 * @since
	 * @param value
	 * @return 预处理参数
	 */
	public SqlParam addInt(int value) {
		SqlParam p = new IntParam(value);
		params.add(p);
		return p;
	}

	/**
	 * 添加LongParam
	 * 
	 * @since
	 * @param value
	 * @return 预处理参数
	 */
	public SqlParam addLong(long value) {
		SqlParam p = new LongParam(value);
		params.add(p);
		return p;
	}

	/**
	 * 添加DecimalParam
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public SqlParam addDecimal(long value) {
		SqlParam p = new DecimalParam(value);
		params.add(p);
		return p;
	}

	/**
	 * 添加TimestampParam
	 * 
	 * @since
	 * @param ts
	 * @return
	 */
	public SqlParam addTimestamp(Timestamp ts) {
		SqlParam p = new TimestampParam(ts);
		params.add(p);
		return p;
	}

	/**
	 * 添加TimestampParam
	 * 
	 * @since
	 * @param ts
	 * @return
	 */
	public SqlParam addTimestamp(String ts) {
		SqlParam p = new TimestampParam(ts);
		params.add(p);
		return p;
	}

	/**
	 * 添加FloatParam
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public SqlParam addFloat(float value) {
		SqlParam p = new FloatParam(value);
		params.add(p);
		return p;
	}

	/**
	 * 添加DoubleParam
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public SqlParam addDouble(double value) {
		SqlParam p = new DoubleParam(value);
		params.add(p);
		return p;
	}

	/**
	 * 添加BinaryParam, VARCHAR(CHAR) FOR BIT DATA
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public SqlParam addBinary(byte[] value) {
		SqlParam p = new BinaryParam(value);
		params.add(p);
		return p;
	}

	/**
	 * 添加BinaryParam, VARCHAR(CHAR) FOR BIT DATA
	 * 
	 * @since
	 * @param hex
	 * @return
	 */
	public SqlParam addBinary(String hex) {
		SqlParam p = new BinaryParam(hex);
		params.add(p);
		return p;
	}

	public List<SqlParam> getParams() {
		return params;
	}

	public int getParamCount() {
		return params == null ? 0 : params.size();
	}

	public void setParams(List<SqlParam> params) {
		this.params = params;
	}

	public void clear() {
		this.params.clear();
	}

	@Override
	public String toString() {
		List<String> ps = new LinkedList<String>();
		for (SqlParam p : params) {
			if (p.isPreparedParam()) {
				ps.add(p.toString());
			}
		}

		StringBuilder sb = new StringBuilder(sql).append(", params{");
		sb.append(StringUtils.toString(ps, ","));
		sb.append("}");

		return sb.toString();
	}

}
