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


/**
 * 插入单表的构造器
 * <p>
 * 使用方法：<br>
 * 
 * <pre>
 * InsertBuilder ib = InsertBuilder.build().table("TBL_IBMGM_xx")     <br>
 *      .intCol("EVENT_ID", 1000)                   <br>
 *      .stringCol("TASK_ST", "02");                <br>
 * 
 * PreparedSQL preSql = isql.toPreparedSQL();
 * PersistenceService.sqlExec(preSql);
 * </pre>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class InsertBuilder extends ColumnBuilder<InsertBuilder> {

	private String tableName;

	public static InsertBuilder build() {
		return new InsertBuilder();
	}

	protected InsertBuilder() {
	}

	public InsertBuilder table(String tableName) {
		this.tableName = tableName;
		return this;
	}

	/**
	 * 获取插入语句
	 * 
	 * @see com.cup.ibscmn.dao.builder.AbstractSingleBuilder#getSql()
	 */
	@Override
	public String getSql() {
		if (getFlagshipColParams().isEmpty()) {
			return null;
		}

		StringBuilder builder = new StringBuilder("insert into ").append(
				tableName).append("(");
		StringBuilder valueBuidler = new StringBuilder(") values(");

		boolean first = true;
		for (ColParam cp : getFlagshipColParams()) {
			if (first) {
				first = false;
			} else {
				builder.append(",");
				valueBuidler.append(",");
			}

			builder.append(cp.getCol());
			valueBuidler.append(cp.getParam().getPlaceHolder());
		}

		builder.append(valueBuidler).append(")");

		String psql = builder.toString();
		return psql;
	}
}
