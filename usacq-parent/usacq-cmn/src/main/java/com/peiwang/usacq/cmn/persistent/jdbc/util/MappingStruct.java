/*
 * 
 * Copyright 2014, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: MappingStruct.java,v 1.1 2016/09/28 04:25:50 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2014-1-3 - Create By szwang
 */

package com.peiwang.usacq.cmn.persistent.jdbc.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.peiwang.usacq.cmn.Const;
import com.peiwang.usacq.cmn.Const.DataType;
import com.peiwang.usacq.cmn.exp.BaseErrorCode;
import com.peiwang.usacq.cmn.exp.BaseException;
import com.peiwang.usacq.cmn.log.Logger;
import com.peiwang.usacq.cmn.persistent.jdbc.JdbcUtils;
import com.peiwang.usacq.cmn.persistent.jdbc.annonation.JdbcColumn;
import com.peiwang.usacq.cmn.persistent.jdbc.annonation.JdbcView;
import com.peiwang.usacq.cmn.persistent.jdbc.param.PreparedSQL;
import com.peiwang.usacq.cmn.persistent.jdbc.param.SqlParam;
import com.peiwang.usacq.cmn.util.ClassUtils;
import com.peiwang.usacq.cmn.util.StringUtils;

/**
 * Annotation映射结构体
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public final class MappingStruct implements Serializable {

	private static final long serialVersionUID = Const.VERSION_ID;

	private static final Logger log = Logger.getLogger(MappingStruct.class);

	/** 原始数据类型 */
	private Class<?> elementType;

	/** 视图定义 */
	private JdbcView jv;

	/** 列名与列结构体的映射关系 */
	private Map<String, MappingField> cfMap = new LinkedHashMap<String, MappingField>();

	/**
	 * 根据类型构建映射结构体
	 * 
	 * @since
	 * @param elementType
	 * @return
	 * @throws BaseException
	 */
	public static MappingStruct create(Class<?> elementType)
			throws BaseException {
		return new MappingStruct(elementType);
	}

	/**
	 * 结果集转换成Java对象
	 * 
	 * @since
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public Object toObject(ResultSet rs) throws SQLException {
		Object result = null;
		try {
			result = ClassUtils.newInstance(elementType);
		} catch (Exception e) {
			log.error("result instruction failed, elementType:" + elementType,
					e);
		}

		if (result == null) {
			// throw new BaseException(BaseErrorCode.FAIL);
		}

		for (MappingField mf : cfMap.values()) {
			mf.setValue(rs, result);
		}

		return result;
	}

	/**
	 * 构建count语句
	 * 
	 * @param table
	 * @return
	 */
	public String buildCountQueryAllSQL(String table) {
		StringBuilder sb = new StringBuilder("select count(*)");
		sb.append(" from ").append(getTableName(table));
		return sb.toString();
	}

	public String buildCountQueryByColumnsSQL(String table,
			String[] columnNames, Object[] values) {
		if (columnNames == null || values == null
				|| columnNames.length != values.length) {
			log.error("column/value is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.FAIL);
		}

		StringBuilder sb = new StringBuilder("select count(*) ");
		sb.append(" from ").append(getTableName(table)).append(" where ");
		appendConditonFields(sb, table, columnNames, values);

		String sql = sb.toString();
		log.debug("buildCountQueryByColumnsSQL, class: "
				+ elementType.getSimpleName() + ", sql: " + sql);
		return sb.toString();
	}

	/**
	 * 构建按主键查询的语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public String buildQuerySQL(Object entity, String table)
			throws BaseException {
		StringBuilder sb = new StringBuilder("select ");
		appendSelectFields(sb);
		sb.append(" from ").append(getTableName(table)).append(" where ");
		appendConditonFields(sb, entity);

		String sql = sb.toString();
		log.debug("buildQuerySql, class: " + entity.getClass().getSimpleName()
				+ ", sql: " + sql);
		return sql;
	}

	/**
	 * 构建查询所有的语句
	 * 
	 * @since
	 * @param table
	 * @return
	 */
	public String buildQueryAllSQL(String table) {
		StringBuilder sb = new StringBuilder("select ");
		appendSelectFields(sb);
		sb.append(" from ").append(getTableName(table));

		String sql = sb.toString();
		log.debug("buildQueryAllSql, class: " + elementType.getSimpleName()
				+ ", sql: " + sql);
		return sql;
	}

	/**
	 * 构建查询所有的语句
	 * 
	 * @since
	 * @param table
	 * @return
	 */
	public PreparedSQL buildQueryAllPSSQL(String table) {

		PreparedSQL psql = new PreparedSQL();

		String sql = String.format("select * from %s ", getTableName(table));
		psql.setSql(sql);
		log.debug("buildQueryAllPSSQL: " + psql);
		return psql;
	}

	/**
	 * 构建按列查询的语句
	 * 
	 * @since
	 * @param table
	 * @param columnNames
	 * @param values
	 * @return
	 * @throws BaseException
	 */
	public String buildQueryByColumnsSQL(String table, String[] columnNames,
			Object[] values) throws BaseException {
		if (columnNames == null || values == null
				|| columnNames.length != values.length) {
			log.error("column/value is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.FAIL);
		}

		StringBuilder sb = new StringBuilder("select ");
		appendSelectFields(sb);
		sb.append(" from ").append(getTableName(table)).append(" where ");
		appendConditonFields(sb, table, columnNames, values);

		String sql = sb.toString();
		log.debug("buildQueryByColumnsSql, class: "
				+ elementType.getSimpleName() + ", sql: " + sql);
		return sb.toString();
	}

	public PreparedSQL buildQueryByColumnsPSQL(String table,
			String[] columnNames, Object[] columnValues) throws BaseException {
		if (columnNames == null || columnValues == null
				|| columnNames.length != columnValues.length) {
			log.error("column/value is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		PreparedSQL psql = new PreparedSQL();
		StringBuilder columns = new StringBuilder("");

		boolean first = true;
		for (MappingField mf : cfMap.values()) {
			if (!first) {
				columns.append(",");
			} else {
				first = false;
			}
			columns.append(mf.column);
		}

		StringBuilder condition = buildColumnsCondition(psql, columnNames,
				columnValues);

		String sql = String.format("select %s from %s where %s ", columns,
				getTableName(table), condition);
		psql.setSql(sql);
		log.debug("buildQueryByColumnsPSQL: " + psql);
		return psql;
	}

	private StringBuilder buildColumnsCondition(PreparedSQL psql,
			String[] columnNames, Object[] columnValues) {
		StringBuilder condition = new StringBuilder("1=1");
		for (int i = 0, c = columnNames.length; i < c; i++) {

			String columnName = columnNames[i];
			MappingField mf = get(columnName);

			if (mf == null) {
				log.error("column not defined, column: " + columnName
						+ ", entity: " + elementType.getSimpleName());
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}

			Object columnValue = columnValues[i];

			if (columnValue == null) {
				log.error("columnValue is null, column: " + columnName
						+ ", entity: " + elementType.getSimpleName());
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}

			if (columnValue instanceof Collection) {// 列表
				@SuppressWarnings("rawtypes")
				Collection values = (Collection) columnValue;
				if (values.isEmpty()) {
					continue;
				}

				List<String> preVals = new LinkedList<String>();
				for (Object value : values) {
					preVals.add(getParamPlaceholder(psql, mf, value));
				}

				String preparedValues = StringUtils.toString(preVals, ",");
				condition.append(" and ").append(columnName).append(" in(")
						.append(preparedValues).append(")");
			} else {
				String preparedValue = getParamPlaceholder(psql, mf,
						columnValue);
				condition.append(" and ").append(columnName).append("=")
						.append(preparedValue);
			}
		}
		return condition;
	}

	/**
	 * 构建插入语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public String buildInsertSQL(Object entity, String table)
			throws BaseException {
		StringBuilder sb = new StringBuilder("insert into ").append(
				getTableName(table)).append(" (");
		appendSelectFields(sb);
		sb.append(") values (");
		appendInsertFileds(sb, entity);
		sb.append(")");

		String sql = sb.toString();
		log.debug("buildInsertSql, class: " + entity.getClass().getSimpleName()
				+ ", sql: " + sql);
		return sql;
	}

	/**
	 * 构建按主键查询的语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public PreparedSQL buildQueryPSQL(Object entity, String table)
			throws BaseException {
		PreparedSQL psql = new PreparedSQL();

		StringBuilder columns = new StringBuilder("");
		StringBuilder condition = new StringBuilder("1=1");

		boolean columnsFirst = true;
		for (MappingField mf : cfMap.values()) {
			if (!columnsFirst) {
				columns.append(",");
			} else {
				columnsFirst = false;
			}
			columns.append(mf.column);

			if (mf.isKey()) {
				condition.append(" and ");

				String v = getFieldPlaceholder(psql, entity, mf);
				condition.append(mf.column).append("=").append(v);
			}
		}

		String sql = String.format("select %s from %s where %s ", columns,
				getTableName(table), condition);
		psql.setSql(sql);
		log.debug("buildQueryPSQL: " + psql);
		return psql;
	}

	public PreparedSQL buildInsertPSQL(Object entity, String table)
			throws BaseException {
		PreparedSQL psql = new PreparedSQL();

		StringBuilder columns = new StringBuilder("");
		StringBuilder values = new StringBuilder("");

		boolean first = true;
		for (MappingField mf : cfMap.values()) {
			if (!first) {
				columns.append(",");
				values.append(",");
			} else {
				first = false;
			}

			columns.append(mf.column);
			String v = getFieldPlaceholder(psql, entity, mf);
			values.append(v);
		}

		String sql = String.format("insert into %s(%s) values(%s)",
				getTableName(table), columns, values);
		psql.setSql(sql);

		log.debug("buildInsertPSQL: " + psql);
		return psql;
	}

	/**
	 * 构建更新语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public PreparedSQL buildUpdatePSQL(Object entity, String table)
			throws BaseException {
		PreparedSQL psql = new PreparedSQL();

		StringBuilder values = new StringBuilder("");
		boolean first = true;
		for (MappingField mf : cfMap.values()) {
			if (!first) {
				values.append(",");
			} else {
				first = false;
			}
			String v = getFieldPlaceholder(psql, entity, mf);
			values.append(mf.column).append("=").append(v);
		}

		boolean keyValid = false;
		StringBuilder condition = new StringBuilder("1=1");
		for (MappingField mf : cfMap.values()) {
			if (mf.isKey()) {
				condition.append(" and ");

				String v = getFieldPlaceholder(psql, entity, mf);
				condition.append(mf.column).append("=").append(v);

				if (!keyValid) {
					keyValid = true;
				}
			}
		}
		if (!keyValid) {
			// TODO
			throw new BaseException(BaseErrorCode.COMN_DATA_TYPE_INVALID);
		}

		String sql = String.format("update %s set %s where %s",
				getTableName(table), values, condition);
		psql.setSql(sql);
		log.debug("buildUpdatePSQL: " + psql);
		return psql;
	}

	/**
	 * 构建删除语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public PreparedSQL buildDeletePSQL(Object entity, String table)
			throws BaseException {
		PreparedSQL psql = new PreparedSQL();

		StringBuilder condition = new StringBuilder("");
		boolean first = true;
		for (MappingField mf : cfMap.values()) {
			if (mf.isKey()) {
				if (!first) {
					condition.append(" and ");
				} else {
					first = false;
				}

				String v = getFieldPlaceholder(psql, entity, mf);
				condition.append(mf.column).append("=").append(v);
			}
		}

		String sql = String.format("delete from %s where %s",
				getTableName(table), condition);
		psql.setSql(sql);
		log.debug("buildDeletePSQL: " + psql);
		return psql;
	}

	/**
	 * 构造实体对象指定字段的预定义参数，并获取占位符
	 * 
	 * @since
	 * @param psql
	 * @param mf
	 * @param entity
	 *            实体对象
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private String getFieldPlaceholder(PreparedSQL psql, Object entity,
			MappingField mf) {
		SqlParam sqlParam = psql.addField(entity, mf.field, mf.jc.type());
		return sqlParam.getPlaceHolder();
	}

	/**
	 * 构造参数值对应的的预定义参数，并获取占位符
	 * 
	 * @since
	 * @param psql
	 * @param mf
	 *            字段定义
	 * @param value
	 *            参数值
	 * @return
	 * @throws BaseException
	 */
	private String getParamPlaceholder(PreparedSQL psql, MappingField mf,
			Object value) throws BaseException {
		SqlParam sqlParam = psql.addParam(mf.field.getType(), mf.jc.type(),
				value);
		return sqlParam.getPlaceHolder();
	}

	/**
	 * 构建更新语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public String buildUpdateSQL(Object entity, String table)
			throws BaseException {
		StringBuilder sb = new StringBuilder("update ").append(
				getTableName(table)).append(" set ");
		appendSetFields(sb, entity);
		sb.append(" where ");
		appendConditonFields(sb, entity);

		String sql = sb.toString();
		log.debug("buildUpdateSql, class: " + entity.getClass().getSimpleName()
				+ ", sql: " + sql);
		return sql;
	}

	/**
	 * 构建指定列更新语句
	 * 
	 * @since
	 * @param table
	 * @param updateColumns
	 * @param updateValues
	 * @param conditionColumns
	 * @param conditionValues
	 * @return
	 */
	public String buildUpdateByColumnsSQL(String table, String[] updateColumns,
			Object[] updateValues, String[] conditionColumns,
			Object[] conditionValues) {
		if (updateColumns == null || updateValues == null
				|| updateColumns.length == 0 || updateValues.length == 0
				|| updateColumns.length != updateValues.length) {
			log.error("column/value to update is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}
		if (conditionColumns == null || conditionValues == null
				|| conditionColumns.length == 0 || conditionValues.length == 0
				|| conditionColumns.length != conditionValues.length) {
			log.error("column/value condition is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		StringBuilder sb = new StringBuilder("update ").append(
				getTableName(table)).append(" set ");
		appendSetFields(sb, table, updateColumns, updateValues);
		sb.append(" where ");
		appendConditonFields(sb, table, conditionColumns, conditionValues);

		String sql = sb.toString();
		log.debug("buildUpdateByColumnsSql, class: "
				+ elementType.getSimpleName() + ", sql: " + sql);
		return sql;
	}

	public PreparedSQL buildUpdateByColumnsPSQL(String table,
			String[] updateColumns, Object[] updateValues,
			String[] conditionColumns, Object[] conditionValues) {
		if (updateColumns == null || updateValues == null
				|| updateColumns.length == 0 || updateValues.length == 0
				|| updateColumns.length != updateValues.length) {
			log.error("column/value to update is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}
		if (conditionColumns == null || conditionValues == null
				|| conditionColumns.length == 0 || conditionValues.length == 0
				|| conditionColumns.length != conditionValues.length) {
			log.error("column/value condition is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		PreparedSQL psql = new PreparedSQL();
		StringBuilder columns = new StringBuilder("");

		boolean first = true;
		for (int i = 0; i < updateColumns.length; i++) {
			if (!first) {
				columns.append(",");
			} else {
				first = false;
			}

			String column = updateColumns[i];
			MappingField mf = get(column);
			if (mf == null) {
				log.error("column not defined, column: " + column
						+ ", entity: " + elementType.getSimpleName());
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}

			Object valObj = updateValues[i];
			String value = getParamPlaceholder(psql, mf, valObj);
			columns.append(column).append("=").append(value);
		}

		StringBuilder condition = buildColumnsCondition(psql, conditionColumns,
				conditionValues);

		String sql = String.format("update %s set %s where %s",
				getTableName(table), columns, condition);
		psql.setSql(sql);
		log.debug("buildUpdateByColumnsPSQL: " + psql);
		return psql;
	}

	/**
	 * 构建删除语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public String buildDeleteSQL(Object entity, String table)
			throws BaseException {
		StringBuilder sb = new StringBuilder("delete from ").append(
				getTableName(table)).append(" where ");
		appendConditonFields(sb, entity);

		String sql = sb.toString();
		log.debug("buildDeleteSql, class: " + entity.getClass().getSimpleName()
				+ ", sql: " + sql);
		return sql;
	}

	/**
	 * 构建按列删除的语句
	 * 
	 * @since
	 * @param table
	 * @param columnNames
	 * @param columnValues
	 * @return
	 * @throws BaseException
	 */
	public String buildDeleteByColumnsSQL(String table, String[] columnNames,
			Object[] columnValues) throws BaseException {
		if (columnNames == null || columnValues == null
				|| columnNames.length == 0 || columnValues.length == 0
				|| columnNames.length != columnValues.length) {
			log.error("column/value is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		StringBuilder sb = new StringBuilder("delete from ").append(
				getTableName(table)).append(" where ");
		appendConditonFields(sb, table, columnNames, columnValues);

		String sql = sb.toString();
		log.debug("buildDeleteByColumnsSql, class: "
				+ elementType.getSimpleName() + ", sql: " + sql);
		return sb.toString();
	}

	/**
	 * 构建按列删除的语句
	 * 
	 * @since
	 * @param table
	 * @param columnNames
	 * @param columnValues
	 * @return
	 * @throws BaseException
	 */
	public PreparedSQL buildDeleteByColumnsPSQL(String table,
			String[] columnNames, Object[] columnValues) throws BaseException {
		if (columnNames == null || columnValues == null
				|| columnNames.length == 0 || columnValues.length == 0
				|| columnNames.length != columnValues.length) {
			log.error("column/value is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		PreparedSQL psql = new PreparedSQL();
		StringBuilder condition = buildColumnsCondition(psql, columnNames,
				columnValues);
		String sql = String.format("delete from %s where %s",
				getTableName(table), condition);
		psql.setSql(sql);
		log.debug("buildDeleteByColumnsSQL, psql: " + sql);
		return psql;
	}

	/**
	 * 添加更新的字段，除key外
	 * 
	 * @since
	 * @param sb
	 * @param entity
	 */
	private void appendSetFields(StringBuilder sb, Object entity) {
		boolean first = true;
		for (MappingField mf : cfMap.values()) {
			if (mf.isKey()) {
				continue;
			}

			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}

			String value = mf.getValue(entity);
			sb.append(mf.column).append("=").append(value);
		}
	}

	/**
	 * 添加更新的字段
	 * 
	 * @since
	 * @param sb
	 * @param table
	 * @param names
	 * @param values
	 */
	private void appendSetFields(StringBuilder sb, String table,
			String[] names, Object[] values) {
		for (int i = 0, c = names.length; i < c; i++) {
			String column = names[i];

			MappingField mf = get(column);

			if (mf == null) {
				log.error("appendSetFields, column not defined, table: "
						+ getTableName(table) + ", column: " + column
						+ ", entity: " + elementType.getSimpleName());
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}

			if (i != 0) {
				sb.append(",");
			}

			String condition = mf.getSetSql(values[i]);
			sb.append(condition);
		}
	}

	/**
	 * 添加条件字段,key
	 * 
	 * @since
	 * @param sb
	 * @param entity
	 */
	private void appendConditonFields(StringBuilder sb, Object entity) {
		boolean first = true;
		for (MappingField mf : cfMap.values()) {
			if (!mf.isKey()) {
				continue;
			}

			if (!first) {
				sb.append(" and ");
			} else {
				first = false;
			}

			String value = mf.getValue(entity);
			sb.append(mf.column).append("=").append(value);
		}
	}

	/**
	 * 添加按列查询的条件
	 * 
	 * @since
	 * @param sb
	 * @param columnNames
	 * @param columnValues
	 */
	private void appendConditonFields(StringBuilder sb, String table,
			String[] columnNames, Object[] columnValues) {
		sb.append(" 1=1 ");
		for (int i = 0, c = columnNames.length; i < c; i++) {
			String column = columnNames[i];
			MappingField mf = get(column);

			if (mf == null) {
				log.error("appendConditonFields, column not defined, table: "
						+ getTableName(table) + ", column: " + column
						+ ", entity: " + elementType.getSimpleName());
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}

			sb.append(" and " + mf.getConditionSql(columnValues[i]));
		}
	}

	/**
	 * 添加查询的列名
	 * 
	 * @since
	 * @param sb
	 */
	private void appendSelectFields(StringBuilder sb) {
		boolean first = true;
		for (String column : cfMap.keySet()) {
			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}
			sb.append(column);
		}
	}

	/**
	 * 添加列值
	 * 
	 * @since
	 * @param sb
	 */
	private void appendInsertFileds(StringBuilder sb, Object entity) {
		boolean first = true;
		for (MappingField mf : cfMap.values()) {
			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}

			String value = mf.getValue(entity);
			sb.append(value);
		}
	}

	/**
	 * 解析注解类结构
	 * 
	 * @param elementType
	 */
	private MappingStruct(Class<?> elementType) {
		if (!elementType.isAnnotationPresent(JdbcView.class)) {
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		this.jv = elementType.getAnnotation(JdbcView.class);
		this.elementType = elementType;

		Field[] fields = elementType.getDeclaredFields();
		for (Field f : fields) {
			JdbcColumn jc = f.getAnnotation(JdbcColumn.class);
			if (jc == null) {
				continue;
			}

			// 列名，取注解中的值，如果没有设置，则取Java字段名
			String column = jc.name();
			if (StringUtils.isEmpty(column)) {
				column = f.getName();
			}

			MappingField mf = new MappingField(column, jc, f);
			put(column, mf);
		}

		if (cfMap.isEmpty()) {
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}
	}

	/**
	 * 获取MappingField，列名统一转大写处理
	 * 
	 * @since
	 * @param column
	 * @return
	 */
	private MappingField get(String column) {
		return StringUtils.isEmpty(column) ? null : cfMap.get(column
				.toUpperCase());
	}

	/**
	 * 添加MappingField，列名统一转大写处理
	 * 
	 * @since
	 * @param column
	 * @param mf
	 */
	private void put(String column, MappingField mf) {
		if (StringUtils.isEmpty(column)) {
			return;
		}

		cfMap.put(column.toUpperCase(), mf);
	}

	/**
	 * 获取实际表名称
	 * 
	 * @since
	 * @param table
	 * @return
	 */
	public String getTableName(String table) {
		return StringUtils.isEmpty(table) ? this.jv.name() : table;
	}

	/**
	 * 字段映射结构
	 * 
	 * @author gys
	 * @version
	 * @since
	 * 
	 */
	private static class MappingField {

		String column;
		JdbcColumn jc;
		Field field;

		public MappingField(String column, JdbcColumn jc, Field field) {
			this.column = column;
			this.jc = jc;
			this.field = field;
			this.field.setAccessible(true);
		}

		// /**
		// * 获取数据类型
		// *
		// * @since
		// * @return
		// */
		// public String getDataType() {
		// String dataType = jc.type();
		// if (!StringUtils.isEmpty(dataType) &&
		// !dataType.equals(DataType.NONE)) {
		// return dataType;
		// }
		//
		// Class<?> requiredType = field.getType();
		// if (String.class.equals(requiredType)) {
		// return DataType.STRING;
		// } else if (int.class.equals(requiredType) ||
		// Integer.class.equals(requiredType)
		// || long.class.equals(requiredType) || Long.class.equals(requiredType)
		// || byte.class.equals(requiredType) || Byte.class.equals(requiredType)
		// || short.class.equals(requiredType) ||
		// Short.class.equals(requiredType)) {
		// return DataType.INT;
		// } else if (java.sql.Timestamp.class.equals(requiredType) ||
		// java.util.Date.class.equals(requiredType)
		// || java.sql.Date.class.equals(requiredType) ||
		// java.sql.Time.class.equals(requiredType)) {
		// return DataType.TIMESTAMP;
		// } else if (float.class.equals(requiredType) ||
		// Float.class.equals(requiredType)
		// || double.class.equals(requiredType) ||
		// Double.class.equals(requiredType)) {
		// return DataType.FLOAT;
		// } else {
		// throw new BaseException(BaseErrorCode.COMN_DATA_TYPE_MIS_MATCH, new
		// Object[] { requiredType });
		// }
		// }

		/**
		 * 获取用于Sql的值
		 * 
		 * @since
		 * @param entity
		 * @return
		 */
		public String getValue(Object entity) {
			String value = null;
			try {
				Object v = field.get(entity);
				value = getColumnValue(v);
				return value;
			} catch (Exception e) {
				String message = "convert field to sql failed, class: "
						+ entity.getClass().getSimpleName() + ", field: "
						+ field.getName() + ", type: "
						+ field.getType().getName() + ", column: " + column
						+ ", dataType: " + jc.type() + ", value: " + value;
				log.error(message, e);
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}
		}

		@SuppressWarnings("unused")
		public Object getObject(Object entity) {
			try {
				return field.get(entity);
			} catch (Exception e) {
				log.error(String.format(
						"getObject(entity[%s]) failed, field[%s]", entity,
						field), e);
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}
		}

		@SuppressWarnings("unused")
		public String getString(Object entity) {
			try {
				return field.get(entity).toString();
			} catch (Exception e) {
				String message = "convert field to sql failed, class: "
						+ entity.getClass().getSimpleName() + ", field: "
						+ field.getName() + ", type: "
						+ field.getType().getName() + ", column: " + column
						+ ", dataType: " + jc.type() + ", value: "
						+ getValue(entity);
				log.error(message, e);
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}
		}

		@SuppressWarnings("unused")
		public int getInt(Object entity) {
			try {
				Class<?> requiredType = field.getType();
				if (int.class.equals(requiredType)
						|| Integer.class.equals(requiredType)
						|| long.class.equals(requiredType)
						|| Long.class.equals(requiredType)
						|| byte.class.equals(requiredType)
						|| Byte.class.equals(requiredType)
						|| short.class.equals(requiredType)
						|| Short.class.equals(requiredType)) {
					return field.getInt(entity);
				} else if (String.class.equals(requiredType)) {
					String s = field.get(entity).toString();
					return Integer.parseInt(s.trim());
				} else {
					throw new Exception("int required: " + requiredType);
				}
			} catch (Exception e) {
				String message = "convert field to sql failed, class: "
						+ entity.getClass().getSimpleName() + ", field: "
						+ field.getName() + ", type: "
						+ field.getType().getName() + ", column: " + column
						+ ", dataType: " + jc.type() + ", value: "
						+ getValue(entity);
				log.error(message, e);
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}
		}

		/**
		 * 构造设置列值得sql
		 * 
		 * @since
		 * @param columnValue
		 * @return
		 */
		public String getSetSql(Object columnValue) {
			try {
				String value = getColumnValue(columnValue);
				return column + "=" + value;
			} catch (Exception e) {
				String message = "getSetSql failed,"
						+ field.getType().getName() + ", column: " + column
						+ ", dataType: " + jc.type() + ", value: "
						+ columnValue;
				log.error(message, e);
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}
		}

		/**
		 * 获取列值对应的条件语句
		 * 
		 * @since
		 * @param columnValue
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public String getConditionSql(Object columnValue) {
			try {
				if (columnValue instanceof Collection) {// 列表
					Collection c = (Collection) columnValue;
					List<String> values = new ArrayList<String>(c.size());
					for (Object obj : c) {
						String value = getColumnValue(obj);
						values.add(value);
					}
					String v = StringUtils.toString(values, ",");
					return column + " in(" + v + ")";
				} else { // 单个数据
					String value = getColumnValue(columnValue);
					return column + "=" + value;
				}
			} catch (Exception e) {
				String message = "getConditionSql failed, field: "
						+ field.getName() + ", type: "
						+ field.getType().getName() + ", column: " + column
						+ ", dataType: " + jc.type() + ", columnValue: "
						+ columnValue;
				log.error(message, e);
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}
		}

		/**
		 * 按列名获取值
		 * 
		 * @since
		 * @param columnValue
		 * @return
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 */
		private String getColumnValue(Object columnValue)
				throws IllegalArgumentException, IllegalAccessException {
			String value = columnValue == null ? null : columnValue.toString();
			String dataType = jc.type();
			int len = jc.length();
			if (!StringUtils.isEmpty(dataType)
					&& !dataType.equals(DataType.NONE)) {
				if (dataType.equals(DataType.STRING)) {
					value = forLenLimit(value, len);
				}

				value = JdbcUtils.valueForDB(value, dataType);
			} else {
				Class<?> requiredType = field.getType();
				if (requiredType.equals(String.class)) {
					value = forLenLimit(value, len);
				}

				value = JdbcUtils.valueForDB(value, requiredType);
			}

			return value;
		}

		/**
		 * 截去超过数据库限制的字符
		 * 
		 * 
		 * @since
		 * @param value
		 * @param len
		 * @return
		 */
		private String forLenLimit(String value, int len) {
			if (value == null || len <= 0) {
				return value;
			}

			String str = "";
			try {
				byte[] bytes = value.getBytes("GBK");
				if (bytes.length > len) {
					byte[] b1 = Arrays.copyOf(bytes, len);
					byte[] b2 = Arrays.copyOf(bytes, len + 1);
					String s1 = new String(b1, "GBK");
					String s2 = new String(b2, "GBK");

					if (s1.length() == s2.length()) {
						byte[] b = Arrays.copyOf(bytes, len - 1);
						str = new String(b, "GBK");
					} else {
						str = s1;
					}
				} else {
					str = value;
				}
			} catch (UnsupportedEncodingException e) {
				log.error(e);
			}

			return str;
		}

		/**
		 * 设置值
		 * 
		 * @since
		 * @param rs
		 * @param entity
		 * @throws SQLException
		 */
		public void setValue(ResultSet rs, Object entity) throws SQLException {
			Object value = JdbcUtils.getResultSetValue(rs, column,
					field.getType());
			try {
				field.set(entity, value);
			} catch (Exception e) {
				String message = "convert result set to entity failed, class: "
						+ entity.getClass().getSimpleName() + ", field: "
						+ field.getName() + ", type: "
						+ field.getType().getName() + ", column: " + column
						+ ", dataType: " + jc.type() + ", value: " + value;
				log.error(message, e);
				throw new BaseException(
						BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
			}
		}

		/**
		 * 是否主键
		 * 
		 * @since
		 * @return
		 */
		public boolean isKey() {
			return jc.key();
		}
	}
}
