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

import java.util.List;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.util.StringUtils;

/**
 * 更新单表的构造器
 * <p>
 * 1、使用当前构造器的方法设置表名、列名、类型与值；<br>
 * 2、使用 {@link #condition()}构造更新条件。<br>
 * 
 * <pre>
 * UpdateBulder ub = UpdateBulder.build().table("TBL_IBMGM_xx")   <br>
 *      .stringCol("TASK_ST", "03")                             <br>
 *      .tsCol("UPD_TS", "CURRENT TIMESTAMP");                   <br>
 * 
 * ub.condition().stringEquals("TASK_ST", "01");                  <br>
 * </pre>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public final class UpdateBuilder extends ColumnBuilder<UpdateBuilder> {

	private String tableName;

	private ConditionBuilder conditionBuilder;

	public static UpdateBuilder build() {
		return new UpdateBuilder();
	}

	private UpdateBuilder() {
	}

	public UpdateBuilder table(String tableName) {
		this.tableName = tableName;
		return this;
	}

	/**
	 * 获取条件构造器
	 * 
	 * @since
	 * @return
	 */
	public ConditionBuilder condition(boolean and) {
		if (this.conditionBuilder == null) {
			this.conditionBuilder = and ? ConditionBuilder.and()
					: ConditionBuilder.or();
		}
		return this.conditionBuilder;
	}

	/**
	 * 获取更新语句
	 * 
	 * @see com.cup.ibscmn.dao.builder.AbstractSingleBuilder#getSql()
	 */
	@Override
	public String getSql() {
		StringBuilder builder = new StringBuilder("update ").append(tableName)
				.append(" set ");

		// 更新列
		boolean first = true;
		for (ColParam cp : getFlagshipColParams()) {
			if (first) {
				first = false;
			} else {
				builder.append(",");
			}

			builder.append(cp.getCol()).append("=")
					.append(cp.getParam().getPlaceHolder());
		}

		//对于更新操作，至少需要一个参数列
		if (first) {
			throw new BaseException(
					BaseErrorCode.COMN_DATA_INVALID_FOMAT_ARG_CNT);
		}

		// 条件
		if (conditionBuilder != null) {
			String condSql = conditionBuilder.getSql();
			if (!StringUtils.isEmpty(condSql)) {
				builder.append(" where ").append(condSql);
			}
		}

		return builder.toString();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.cup.ibscmn.dao.builder.AbstractSingleBuilder#getParams()
	 */
	@Override
	public List<SqlParam> getParams() {
		List<SqlParam> params = super.getParams();
		if (conditionBuilder != null) {
			params.addAll(conditionBuilder.getParams());
		}
		return params;
	}
}
