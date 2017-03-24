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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.persistent.jdbc.param.BinaryParam;
import com.zpayment.cmn.persistent.jdbc.param.DecimalParam;
import com.zpayment.cmn.persistent.jdbc.param.DoubleParam;
import com.zpayment.cmn.persistent.jdbc.param.FloatParam;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.persistent.jdbc.param.StringParam;
import com.zpayment.cmn.persistent.jdbc.param.TimestampParam;
import com.zpayment.cmn.util.StringUtils;

/**
 * 条件表达式构造器
 * <p>
 * 1、指定列名比较<br>
 * 1) 支持常见数据类型（整型、字符串、时间戳），其中时间戳支持Timstamp与String格式，二进制支持byte[]与HexString；<br>
 * 2) 支持常见比较方式（=,<>,>=,<=,in,not in,between）;<br>
 * 3) 函数名命名规则: 类型+比较方式，如intEqual. <br>
 * 4) 条件构造方式： <br>
 * 
 * <pre>
 * ConditionBuilder cb = ConditionBuilder.and().          <br>
 *      .intEqual("EVENT_ID", 1000)                         <br>
 *      .stringIn("TASK_ST", new String[] { "00", "01"})    <br>
 *      .intBetween("REC_ID", 100, 200);                    <br>
 * </pre>
 * 
 * 2、自定义比较，format方式<br>
 * 
 * <pre>
 * ConditionBuilder cb = ConditionBuilder.and().      <br>
 *      .add("EVENT_ID=%d", 1000);                      <br>
 * </pre>
 * 
 * 
 * 
 * 3、组合模式，拼接不同关系（and/or）的条件 <br>
 * 例如：c1=1 and ((c2="2" and c3="3") or (c2<>"2" and c4="3"))<br>
 * 
 * <pre>
 * ConditionBuilder.and().intEqual("c1", 1).add(                                              <br>
 *    ConditionBuilder.or()                                                                   <br>
 *        .add(ConditionBuilder.and().stringEqual("c2", "2").stringEqual("c3", "3"))          <br>
 *        .add(ConditionBuilder.and().stringNotEqual("c2", "2").stringEqual("c4", "3"))       <br>
 *    );
 * </pre>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public final class ConditionBuilder extends BaseBuilder implements
		SingleParamsBuilder {

	private static final String AND = " and ";
	private static final String OR = " or ";

	/** 本层条件节点关系 */
	private boolean and;

	private List<SingleParamsBuilder> conditions = new LinkedList<SingleParamsBuilder>();

	public static ConditionBuilder and() {
		return new ConditionBuilder(true);
	}

	public static ConditionBuilder or() {
		return new ConditionBuilder(false);
	}

	private ConditionBuilder(boolean and) {
		this.and = and;
	}

	/**
	 * 添加其他条件
	 * 
	 * @since
	 * @param condition
	 * @return
	 */
	public ConditionBuilder add(ConditionBuilder condition) {
		this.conditions.add(condition);
		return this;
	}

	/**
	 * 判断条件是否为空
	 * 
	 * @since
	 * @return
	 */
	public boolean isEmpty() {
		if (!conditions.isEmpty()) {
			return false;
		}

		for (SingleParamsBuilder condition : conditions) {
			if (condition instanceof ConditionBuilder) {
				if (!((ConditionBuilder) condition).isEmpty()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 添加format条件
	 * 
	 * @since
	 * @param format
	 * @param params
	 * @return
	 * @throws BaseException
	 */
	public ConditionBuilder add(String format, Object... params)
			throws BaseException {
		PSQLFormat fsql = PSQLFormat.build(format, params);
		this.conditions.add(fsql);
		return this;
	}

	public ConditionBuilder intEqual(String col, int value) {

		SqlParam param = new DecimalParam(value);
		conditions.add(new SingleColumnConditionBuilder(col, param));
		return this;
	}

	public ConditionBuilder longEqual(String col, long value) {
		SqlParam param = new DecimalParam(value);
		conditions.add(new SingleColumnConditionBuilder(col, param));
		return this;
	}

	public ConditionBuilder floatEqual(String col, float value) {
		SqlParam param = new FloatParam(value);
		conditions.add(new SingleColumnConditionBuilder(col, param));
		return this;
	}

	public ConditionBuilder doubleEqual(String col, double value) {
		SqlParam param = new DoubleParam(value);
		conditions.add(new SingleColumnConditionBuilder(col, param));
		return this;
	}

	public ConditionBuilder intNotEqual(String col, int value)
			throws BaseException {
		checkCol(col);
		return this.add(col + "<>%d", value);
	}

	public ConditionBuilder intGreaterThan(String col, int value)
			throws BaseException {
		checkCol(col);
		return this.add(col + ">%d", value);
	}

	public ConditionBuilder intGreaterEqual(String col, int value)
			throws BaseException {
		checkCol(col);
		return this.add(col + ">=%d", value);
	}

	public ConditionBuilder intLessThan(String col, int value)
			throws BaseException {
		checkCol(col);
		return this.add(col + "<%d", value);
	}

	public ConditionBuilder intLessEqual(String col, int value)
			throws BaseException {
		checkCol(col);
		return this.add(col + "<=%d", value);
	}

	public ConditionBuilder intIn(String col, int[] values)
			throws BaseException {
		checkCol(col);

		if (values != null && values.length > 0) {
			add(col + " in %d", values);
		}

		return this;
	}

	public ConditionBuilder intIn(String col, String[] values)
			throws BaseException {
		checkCol(col);

		if (values != null && values.length > 0) {
			add(col + " in %d", (Object) values);
		}

		return this;
	}

	public ConditionBuilder intIn(String col, Collection<?> values)
			throws BaseException {
		checkCol(col);

		if (values != null && !values.isEmpty()) {
			add(col + " in %d", values);
		}

		return this;
	}

	public ConditionBuilder intNotIn(String col, int[] values)
			throws BaseException {
		checkCol(col);

		if (values != null && values.length > 0) {
			add(col + " not in %d", values);
		}

		return this;
	}

	public ConditionBuilder intNotIn(String col, String[] values)
			throws BaseException {
		checkCol(col);

		if (values != null && values.length > 0) {
			add(col + " not in %d", (Object) values);
		}

		return this;
	}

	public ConditionBuilder intNotIn(String col, Collection<?> values)
			throws BaseException {
		checkCol(col);

		if (values != null && !values.isEmpty()) {
			add(col + " not in %d", values);
		}

		return this;
	}

	public ConditionBuilder intBetween(String col, int min, int max)
			throws BaseException {
		checkCol(col);

		return add(col + " between %d and %d", min, max);
	}

	public ConditionBuilder stringEqual(String col, String value) {
		SqlParam param = new StringParam(value);
		conditions.add(new SingleColumnConditionBuilder(col, param));
		return this;
	}

	public ConditionBuilder stringNotEqual(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<>%s", value);
		}

		return this;
	}

	public ConditionBuilder stringGreaterThan(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + ">%s", value);
		}

		return this;
	}

	public ConditionBuilder stringGreaterEqual(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + ">=%s", value);
		}

		return this;
	}

	public ConditionBuilder stringLessThan(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<%s", value);
		}

		return this;
	}

	public ConditionBuilder stringLessEqual(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<=%s", value);
		}

		return this;
	}

	public ConditionBuilder stringBetween(String col, String min, String max)
			throws BaseException {
		checkCol(col);

		if (max == null) {
			return this.stringGreaterEqual(col, min);
		}

		if (min == null) {
			return this.stringLessEqual(col, max);
		}

		return add(col + " between %s and %s", min, max);
	}

	public ConditionBuilder stringIn(String col, String[] values)
			throws BaseException {
		checkCol(col);

		if (values != null && values.length > 0) {
			add(col + " in %s", (Object) values);
		}

		return this;
	}

	public ConditionBuilder stringIn(String col, Collection<?> values)
			throws BaseException {
		checkCol(col);

		if (values != null && !values.isEmpty()) {
			add(col + " in %s", values);
		}

		return this;
	}

	public ConditionBuilder stringNotIn(String col, String[] values)
			throws BaseException {
		checkCol(col);

		if (values != null && values.length > 0) {
			add(col + " not in %s", (Object) values);
		}

		return this;
	}

	public ConditionBuilder stringNotIn(String col, Collection<?> values)
			throws BaseException {
		checkCol(col);

		if (values != null && !values.isEmpty()) {
			add(col + " not in %s", values);
		}

		return this;
	}

	public ConditionBuilder tsEqual(String col, Timestamp value) {
		checkCol(col);

		if (value != null) {
			SqlParam param = new TimestampParam(value);
			conditions.add(new SingleColumnConditionBuilder(col, param));
		}

		return this;
	}

	public ConditionBuilder tsNotEqual(String col, Timestamp value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<>%t", value);
		}

		return this;
	}

	public ConditionBuilder tsGreaterThan(String col, Timestamp value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + ">%t", value);
		}

		return this;
	}

	public ConditionBuilder tsGreaterEqual(String col, Timestamp value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + ">=%t", value);
		}

		return this;
	}

	public ConditionBuilder tsLessThan(String col, Timestamp value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<%t", value);
		}

		return this;
	}

	public ConditionBuilder tsLessEqual(String col, Timestamp value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<=%t", value);
		}

		return this;
	}

	public ConditionBuilder tsBetween(String col, Timestamp min, Timestamp max)
			throws BaseException {
		if (min == null) {
			return tsLessEqual(col, max);
		}
		if (max == null) {
			return tsGreaterEqual(col, min);
		}

		return add(col + " between %t and %t", min, max);
	}

	public ConditionBuilder tsEqual(String col, String value) {
		checkCol(col);

		if (value != null) {
			SqlParam param = new TimestampParam(value);
			conditions.add(new SingleColumnConditionBuilder(col, param));
		}

		return this;
	}

	public ConditionBuilder tsNotEqual(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<>%t", value);
		}

		return this;
	}

	public ConditionBuilder tsGreaterThan(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + ">%t", value);
		}

		return this;
	}

	public ConditionBuilder tsGreaterEqual(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + ">=%t", value);
		}

		return this;
	}

	public ConditionBuilder tsLessThan(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<%t", value);
		}

		return this;
	}

	public ConditionBuilder tsLessEqual(String col, String value)
			throws BaseException {
		checkCol(col);

		if (value != null) {
			this.add(col + "<=%t", value);
		}

		return this;
	}

	public ConditionBuilder tsBetween(String col, String min, String max)
			throws BaseException {
		if (min == null) {
			return tsLessEqual(col, max);
		}
		if (max == null) {
			return tsGreaterEqual(col, min);
		}

		return add(col + " between %t and %t", min, max);
	}

	public ConditionBuilder tsBetween(String col, Timestamp min, String max)
			throws BaseException {
		if (min == null) {
			return tsLessEqual(col, max);
		}
		if (max == null) {
			return tsGreaterEqual(col, min);
		}

		return add(col + " between %t and %t", min, max);
	}

	public ConditionBuilder tsBetween(String col, String min, Timestamp max)
			throws BaseException {
		if (min == null) {
			return tsLessEqual(col, max);
		}
		if (max == null) {
			return tsGreaterEqual(col, min);
		}

		return add(col + " between %t and %t", min, max);
	}

	public ConditionBuilder binEqual(String col, String value) {
		checkCol(col);

		if (value != null) {
			SqlParam param = new BinaryParam(value);
			conditions.add(new SingleColumnConditionBuilder(col, param));
		}

		return this;
	}

	public ConditionBuilder binEqual(String col, byte[] value) {
		checkCol(col);

		if (value != null) {
			SqlParam param = new BinaryParam(value);
			conditions.add(new SingleColumnConditionBuilder(col, param));
		}

		return this;
	}

	/**
	 * 获取条件子句，不带where关键字
	 * 
	 * @see com.cup.ibscmn.dao.builder.AbstractSingleBuilder#getSql()
	 */
	@Override
	public String getSql() {
		return getSql(true);
	}

	/**
	 * 获取节点的SQL语句
	 * 
	 * @since
	 * @param top
	 *            是否顶层节点
	 * @return
	 */
	private String getSql(boolean top) {
		if (isEmpty()) {
			return "";
		}

		final String relation = and ? AND : OR;

		StringBuilder builder = new StringBuilder();

		for (SingleParamsBuilder condition : conditions) {
			if (condition instanceof ConditionBuilder) {
				String sql = ((ConditionBuilder) condition).getSql(false);
				if (!StringUtils.isEmpty(sql)) {
					builder.append(relation).append(sql);
				}
			} else {
				builder.append(relation).append(condition.getSql());
			}
		}

		if (builder.length() == 0) {
			return "";
		}

		// 跳过第一个and/or
		String s = builder.substring(relation.length());

		if (!top) {
			s = "(" + s + ")";
		}
		return s;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.cup.ibscmn.dao.builder.AbstractSingleBuilder#getParams()
	 */
	@Override
	public List<SqlParam> getParams() {
		List<SqlParam> params = new LinkedList<SqlParam>();

		for (SingleParamsBuilder condition : conditions) {
			params.addAll(condition.getParams());
		}

		return params;
	}

	@SuppressWarnings("unused")
	private static class SingleColumnConditionBuilder extends
			ColumnBuilder<SingleColumnConditionBuilder> {
		public SingleColumnConditionBuilder(String col, SqlParam param) {
			add(col, param);
		}

		@Override
		public String getSql() {
			StringBuilder builder = new StringBuilder();

			if (getFlagshipColParams().isEmpty()) {
				return "";
			}

			ColumnBuilder.ColParam cp = getCurrentColParams().get(0);
			return cp.getCol() + "=" + cp.getParam().getPlaceHolder();
		}
	}
}
