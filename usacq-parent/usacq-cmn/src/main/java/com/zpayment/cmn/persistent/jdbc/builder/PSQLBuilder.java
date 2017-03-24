/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月23日 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc.builder;

import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.util.StringUtils;

/**
 * 通用PSQL构造器，可动态构造
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class PSQLBuilder extends AbstractBuilder {

	private List<SingleParamsBuilder> builderList = new LinkedList<SingleParamsBuilder>();

	public static PSQLBuilder build() {
		return new PSQLBuilder();
	}

	private PSQLBuilder() {

	}

	/**
	 * 添加格式化条件，可用于select,orderby等语句的处理
	 * 
	 * @since
	 * @param format
	 * @param args
	 * @return
	 * @throws BaseException
	 */
	public PSQLBuilder append(String format, Object... args)
			throws BaseException {
		return addLast(PSQLFormat.build(format, args));
	}

	/**
	 * 添加条件构造器
	 * 
	 * @since
	 * @param next
	 * @return
	 */
	public PSQLBuilder append(ConditionBuilder next) {
		return addLast(next);
	}

	private PSQLBuilder addLast(SingleParamsBuilder tail) {
		builderList.add(tail);
		return this;
	}

	@Override
	public String getSql() {
		StringBuilder sqlBuilder = new StringBuilder();
		for (SingleParamsBuilder builder : builderList) {
			String sql = builder.getSql();

			if (!StringUtils.isEmpty(sql)) {
				// 多个之间添加空格
				if (sqlBuilder.length() > 0) {
					sqlBuilder.append(" ");
				}

				sqlBuilder.append(sql);
			}
		}
		return sqlBuilder.toString();
	}

	@Override
	public List<SqlParam> getParams() {
		List<SqlParam> sqlParams = new LinkedList<SqlParam>();

		for (SingleParamsBuilder builder : builderList) {
			List<SqlParam> params = builder.getParams();

			if (params != null) {
				sqlParams.addAll(params);
			}
		}
		return sqlParams;
	}
}
