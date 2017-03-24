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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.util.StringUtils;

/**
 * 查询单表的构造器
 * <p>
 * 1、使用当前构造器的方法设置表名与列名；<br>
 * 2、使用 {@link #condition()}构造查询条件；<br>
 * 3、提供设置order by的方法 {@link #setOrderBy(String)}。<br>
 * 4、提供获取总数与分页查询的工具类方法。<br>
 * 
 * <pre>
 * SelectBuilder selectBuilder = SelectBuilder.build().table("TBL_IBMGM_xx")   <br>
 *      .addCol("EVENT_ID", "TASK_ST")                                          <br>
 *      .addCol("UPD_TS");                                                      <br>
 * 
 * selectBuilder.condition().stringEquals("TASK_ST", "01");                     <br>
 * </pre>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public final class SelectBuilder extends AbstractBuilder {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(SelectBuilder.class);

	private String tableName;

	private Set<String> columns = new LinkedHashSet<String>();

	private ConditionBuilder conditionBuilder;

	private String orderBy;

	public static SelectBuilder build() {
		return new SelectBuilder();
	}

	/**
	 * 获取查询总数的PreparedSQL
	 * 
	 * @since
	 * @param tableName
	 * @param condBuilder
	 * @return
	 */
	public static PreparedSQL getCountPSQL(String tableName,
			ConditionBuilder condBuilder) {
		StringBuilder builder = new StringBuilder("select count(*) from ")
				.append(tableName);

		String condSql = condBuilder.getSql();
		if (!StringUtils.isEmpty(condSql)) {
			builder.append(" where ").append(condSql);
		}

		PreparedSQL psql = new PreparedSQL();
		psql.setSql(builder.toString());
		psql.setParams(condBuilder.getParams());

		log.debug("countSql: %s", psql);

		return psql;
	}

	/**
	 * 获取分页查询的PreparedSQL
	 * 
	 * @since
	 * @param psql
	 *            原始查询语句
	 * @param pageNum
	 *            当前页数，从1开始
	 * @param pageSize
	 *            页内记录数
	 * @return
	 */
	public static PreparedSQL getPagePSQL(PreparedSQL psql, int pageNum,
			int pageSize) {
		int start = (pageNum - 1) * pageSize + 1;
		int end = start + pageSize - 1;

		String querySql = "select * from (select rownumber() over() as row_num,s.* from ("
				+ psql.getSql()
				+ ") as s) as t  where row_num between "
				+ start + " and " + end;

		PreparedSQL pageSql = new PreparedSQL();
		pageSql.setSql(querySql);
		pageSql.setParams(psql.getParams());

		log.debug("pageQuerySql: %s", pageSql);

		return psql;
	}

	public SelectBuilder table(String tableName) {
		this.tableName = tableName;
		return this;
	}

	/**
	 * 添加查询列
	 * 
	 * @since
	 * @param cols
	 *            列名支持用逗号分隔的多个列
	 * @return
	 */
	public SelectBuilder addCol(String... cols) {
		if (cols == null || cols.length == 0) {
			return this;
		}

		for (String col : cols) {
			addCol(col);
		}
		return this;
	}

	/**
	 * 添加查询列
	 * 
	 * @since
	 * @param cols
	 *            列名支持用逗号分隔的多个列
	 * @return
	 */
	public SelectBuilder addCol(Collection<String> cols) {
		if (cols == null || cols.isEmpty()) {
			return this;
		}

		for (String col : cols) {
			addCol(col);
		}
		return this;
	}

	/**
	 * 删除查询列
	 * 
	 * @since
	 * @param cols
	 *            列名支持用逗号分隔的多个列
	 * @return
	 */
	public SelectBuilder removeCol(String... cols) {
		if (cols == null || cols.length == 0) {
			return this;
		}

		for (String col : cols) {
			removeCol(col);
		}
		return this;
	}

	/**
	 * 删除查询列
	 * 
	 * @since
	 * @param cols
	 *            列名支持用逗号分隔的多个列
	 * @return
	 */
	public SelectBuilder removeCol(Collection<String> cols) {
		if (cols == null || cols.isEmpty()) {
			return this;
		}

		for (String col : cols) {
			removeCol(col);
		}
		return this;
	}

	/**
	 * 删除所有列
	 * 
	 * @since
	 * @return
	 */
	public SelectBuilder removeAllCols() {
		this.columns.clear();
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
	 * 添加列
	 * 
	 * @since
	 * @param col
	 *            列名为单独的列或者是用逗号分隔的多列
	 */
	private void addCol(String col) {
		if (col == null) {
			return;
		}

		if (col.contains(",")) {
			String[] ary = col.split(",");
			for (String c : ary) {
				columns.add(c.trim());
			}
		} else {
			columns.add(col.trim());
		}
	}

	/**
	 * 删除列
	 * 
	 * @since
	 * @param col
	 *            列名为单独的列或者是用逗号分隔的多列
	 */
	private void removeCol(String col) {
		if (col == null) {
			return;
		}

		if (col.contains(",")) {
			String[] ary = col.split(",");
			for (String c : ary) {
				columns.remove(c.trim());
			}
		} else {
			columns.remove(col.trim());
		}
	}

	/**
	 * 设置orderBy
	 * 
	 * @since
	 * @param orderBy
	 *            形如： c1 asc,c2 desc
	 * @return
	 */
	public SelectBuilder setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	/**
	 * 获取查询语句
	 * 
	 * @see com.cup.ibscmn.dao.builder.AbstractSingleBuilder#getSql()
	 */
	@Override
	public String getSql() {
		StringBuilder builder = new StringBuilder("select ");
		if (columns.isEmpty()) {
			builder.append("*");
		} else {
			builder.append(StringUtils.toString(columns, ","));
		}

		builder.append(" from ").append(tableName);

		// 条件
		if (conditionBuilder != null) {
			String condSql = conditionBuilder.getSql();
			if (!StringUtils.isEmpty(condSql)) {
				builder.append(" where ").append(condSql);
			}
		}

		if (!StringUtils.isEmpty(orderBy)) {
			builder.append(" order by ").append(orderBy);
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

	private SelectBuilder() {

	}
}
