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

import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.util.StringUtils;

/**
 * 删除单表记录的构造器
 * <p>
 * 1、使用当前构造器的方法设置表名；<br>
 * 2、使用 {@link #condition()}构造删除条件。<br>
 * 
 * <pre>
 * DeleteBuilder db = DeleteBuilder.build().table("TBL_IBMGM_xx");   <br>
 * 
 * db.condition().stringEqual("TASK_ST", "01");                  <br>
 * </pre>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public final class DeleteBuilder extends AbstractBuilder {

	private String tableName;

	private ConditionBuilder conditionBuilder;

	public static DeleteBuilder build() {
		return new DeleteBuilder();
	}

	private DeleteBuilder() {

	}

	public DeleteBuilder table(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public ConditionBuilder condition(boolean and) {
		if (this.conditionBuilder == null) {
			this.conditionBuilder = and ? ConditionBuilder.and()
					: ConditionBuilder.or();
		}
		return this.conditionBuilder;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.cup.ibscmn.dao.builder.AbstractSingleBuilder#getSql()
	 */
	@Override
	public String getSql() {
		StringBuilder builder = new StringBuilder("delete from ")
				.append(tableName);

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
		return conditionBuilder == null ? null : conditionBuilder.getParams();
	}
}
