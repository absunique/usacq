/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zpayment.cmn.Const;
import com.zpayment.cmn.Const.DataType;
import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.persistent.jdbc.JdbcUtils;
import com.zpayment.cmn.persistent.jdbc.annonation.JdbcColumn;
import com.zpayment.cmn.persistent.jdbc.annonation.JdbcView;
import com.zpayment.cmn.persistent.jdbc.builder.ColumnBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.ConditionBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.DeleteBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.InsertBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.SelectBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.UpdateBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.batch.BatchInsertBuilder;
import com.zpayment.cmn.persistent.jdbc.param.BatchPreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.util.ClassUtils;
import com.zpayment.cmn.util.StringUtils;

/**
 * Annotation映射结构体
 * 
 * @author peiwang
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
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		for (MappingField mf : cfMap.values()) {
			mf.setValue(rs, result);
		}

		return result;
	}

	/**
	 * 构建查询所有的占位符语句
	 * 
	 * @since
	 * @param table
	 * @return
	 */
	public PreparedSQL buildQueryAllPSQL(String table) {
		SelectBuilder builder = SelectBuilder.build()
				.table(getTableName(table));
		builder.addCol(cfMap.keySet());

		PreparedSQL psql = builder.toPreparedSQL();
		log.debug("buildQueryAllPSQL: %s", psql);
		return psql;
	}

	/**
	 * 按列查询
	 * 
	 * @since
	 * @param table
	 * @param columnNames
	 * @param columnValues
	 * @return
	 * @throws BaseException
	 */
	public PreparedSQL buildQueryByColumnsPSQL(String table,
			String[] columnNames, Object[] columnValues) throws BaseException {
		SelectBuilder builder = SelectBuilder.build()
				.table(getTableName(table));
		builder.addCol(cfMap.keySet());

		setConditions(builder.condition(true), table, columnNames, columnValues);

		PreparedSQL psql = builder.toPreparedSQL();
		log.debug("buildQueryByColumnsPSQL: %s", psql);
		return psql;
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
		SelectBuilder builder = SelectBuilder.build()
				.table(getTableName(table));

		builder.addCol(cfMap.keySet());

		addKeyConditions(builder.condition(true), entity);

		PreparedSQL psql = builder.toPreparedSQL();
		log.debug("buildQueryPSQL: " + psql);
		return psql;
	}

	/**
	 * 构造插入语句
	 * 
	 * @since
	 * @param entity
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public PreparedSQL buildInsertPSQL(Object entity, String table)
			throws BaseException {
		InsertBuilder builder = InsertBuilder.build()
				.table(getTableName(table));

		// 设置插入列
		setColumns(builder, entity);

		PreparedSQL psql = builder.toPreparedSQL();
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
		UpdateBuilder builder = UpdateBuilder.build()
				.table(getTableName(table));

		// 设置更新列
		setColumns(builder, entity);

		// 设置主键条件
		ConditionBuilder condition = builder.condition(true);
		addKeyConditions(condition, entity);

		if (condition.isEmpty()) {
			log.error("key not found, clazz: %s", new Object[] { elementType });
			throw new BaseException(BaseErrorCode.COMN_DATA_TYPE_INVALID);
		}

		PreparedSQL psql = builder.toPreparedSQL();
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

		DeleteBuilder builder = DeleteBuilder.build()
				.table(getTableName(table));
		addKeyConditions(builder.condition(true), entity);

		PreparedSQL psql = builder.toPreparedSQL();
		log.debug("buildDeletePSQL: %s", psql);
		return psql;
	}

	/**
	 * 构建更新语句
	 * 
	 * @since
	 * @param table
	 * @param updateColumns
	 * @param updateValues
	 * @param conditionColumns
	 * @param conditionValues
	 * @return
	 */
	public PreparedSQL buildUpdateByColumnsPSQL(String table,
			String[] updateColumns, Object[] updateValues,
			String[] conditionColumns, Object[] conditionValues) {
		UpdateBuilder builder = UpdateBuilder.build()
				.table(getTableName(table));

		// 设置更新列
		setColumns(builder, table, updateColumns, updateValues);

		// 设置条件
		setConditions(builder.condition(true), table, conditionColumns,
				conditionValues);

		PreparedSQL psql = builder.toPreparedSQL();
		log.debug("buildUpdateByColumnsPSQL: " + psql);
		return psql;
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
		DeleteBuilder builder = DeleteBuilder.build()
				.table(getTableName(table));
		setConditions(builder.condition(true), table, columnNames, columnValues);

		PreparedSQL psql = builder.toPreparedSQL();
		log.debug("buildDeleteByColumnsSQL: %s", psql);
		return psql;
	}

	/**
	 * 构造批量插入语句
	 * 
	 * @since
	 * @param entities
	 * @param table
	 * @return
	 * @throws BaseException
	 */
	public <T> BatchPreparedSQL buildBatchInsertPSQL(List<T> entities,
			String table) throws BaseException {
		// AnnotationBatchBuilder builder = new
		// AnnotationBatchBuilder(getTableName(table), entities);

		InsertBuilder builder = BatchInsertBuilder.build().table(
				getTableName(table));

		// 设置插入列

		PreparedSQL psql = builder.toPreparedSQL();
		log.debug("buildInsertPSQL: " + psql);
		for (T entity : entities) {
			setColumns(builder, entity);
			((BatchInsertBuilder) builder).addBatch();
		}

		return (BatchPreparedSQL) builder.toPreparedSQL();
	}

	/**
	 * 添加主键条件
	 * 
	 * @since
	 * @param condition
	 * @param entity
	 */
	private void addKeyConditions(ConditionBuilder condition, Object entity) {
		for (MappingField mf : cfMap.values()) {
			if (mf.isKey()) {
				Object value = getFiledValue(entity, mf);
				addCondition(condition, mf, value);
			}
		}
	}

	/**
	 * 获取字段值
	 * 
	 * @since
	 * @param entity
	 * @param mf
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private Object getFiledValue(Object entity, MappingField mf)
			throws BaseException {
		try {
			return mf.field.get(entity);
		} catch (Exception e) {
			log.error("getFiledValue(entity[%s], mf[%s]) failed", new Object[] {
					entity, mf });
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}
	}

	/**
	 * 设置列条件
	 * 
	 * @since
	 * @param condition
	 * @param table
	 * @param columnNames
	 * @param values
	 */
	private void setConditions(ConditionBuilder condition, String table,
			String[] columnNames, Object[] values) {
		if (columnNames == null || values == null
				|| columnNames.length != values.length) {
			log.error(
					"column/value is empty or length is unmatched, table: %s, entity: %s",
					new Object[] { table, elementType.getSimpleName() });
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		for (int i = 0; i < columnNames.length; i++) {
			MappingField mf = get(columnNames[i]);
			if (mf == null) {
				continue;
			}

			addCondition(condition, mf, values[i]);
		}
	}

	/**
	 * 添加单列条件
	 * 
	 * @since
	 * @param condition
	 * @param mf
	 * @param value
	 */
	private void addCondition(ConditionBuilder condition, MappingField mf,
			Object value) {
		// 值为空，不作为条件
		if (value == null) {
			return;
		}

		String dataType = mf.jc.type();
		Field field = mf.field;
		Class<?> requiredType = field.getType();
		String col = mf.column;
		try {
			final Class<?> clazz = value.getClass();
			if (DataType.isNull(dataType)) {
				if (String.class.equals(requiredType)) {
					if (clazz.isArray() || value instanceof Collection) {
						condition.add(mf.column + " in %s", value);
					} else {
						condition.stringEqual(col, (String) value);
					}
				} else if (int.class.equals(requiredType)
						|| short.class.equals(requiredType)
						|| Integer.class.equals(requiredType)
						|| Short.class.equals(requiredType)) {
					if (clazz.isArray() || value instanceof Collection) {
						condition.add(mf.column + " in %d", value);
					} else {
						condition.intEqual(col, ((Number) value).intValue());
					}
				} else if (long.class.equals(requiredType)
						|| Long.class.equals(requiredType)
						|| BigDecimal.class.equals(requiredType)) {
					if (clazz.isArray() || value instanceof Collection) {
						condition.add(mf.column + " in %d", value);
					} else {
						condition.longEqual(col, ((Number) value).longValue());
					}
				} else if (Timestamp.class.equals(requiredType)) {
					condition.tsEqual(col, (Timestamp) value);
				} else if (byte[].class.equals(requiredType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					condition.binEqual(col, (byte[]) value);
				} else if (float.class.equals(requiredType)
						|| Float.class.equals(requiredType)) {
					condition.floatEqual(col, ((Number) value).floatValue());
				} else if (double.class.equals(requiredType)
						|| Double.class.equals(requiredType)) {
					condition.doubleEqual(col, ((Number) value).doubleValue());
				} else {
					/** 其他类型统一当作string处理 */
					condition.stringEqual(col, value.toString());
				}
			} else {
				if (DataType.STRING.equals(dataType)) {
					if (clazz.isArray() || value instanceof Collection) {
						condition.add(mf.column + " in %s", value);
					} else {
						condition.stringEqual(col, value.toString());
					}
				} else if (DataType.INT.equals(dataType)) {
					if (clazz.isArray() || value instanceof Collection) {
						condition.add(mf.column + " in %d", value);
					} else if (value instanceof Number) {
						condition.intEqual(col, ((Number) value).intValue());
					} else {
						condition.intEqual(col,
								Integer.parseInt(value.toString().trim()));
					}
				} else if (DataType.DECIMAL.equals(dataType)) {
					if (clazz.isArray() || value instanceof Collection) {
						condition.add(mf.column + " in %d", value);
					} else if (value instanceof Number) {
						condition.longEqual(col, ((Number) value).longValue());
					} else {
						condition.longEqual(col,
								Long.parseLong(value.toString().trim()));
					}
				} else if (DataType.TIMESTAMP.equals(dataType)) {
					if (value instanceof Timestamp) {
						condition.tsEqual(col, (Timestamp) value);
					} else {
						condition.tsEqual(col, value.toString());
					}
				} else if (DataType.BINARY.equals(dataType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					if (value instanceof byte[]) {
						condition.binEqual(col, (byte[]) value);
					} else {
						condition.binEqual(col, value.toString());
					}
				} else if (DataType.FLOAT.equals(dataType)) {
					if (value instanceof Number) {
						condition
								.floatEqual(col, ((Number) value).floatValue());
					} else {
						condition.floatEqual(col,
								Float.parseFloat(value.toString().trim()));
					}
				} else {
					condition.stringEqual(col, value.toString());
				}
			}
		} catch (Exception e) {
			String message = "addField failed, value: " + value
					+ ", requiredType: " + requiredType + ", type: "
					+ ", dataType: " + dataType;
			log.error(message, e);
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}
	}

	/**
	 * 设置对象中的待插入或更新的列
	 * 
	 * @since
	 * @param builder
	 * @param entity
	 * @throws BaseException
	 */
	@SuppressWarnings("rawtypes")
	private void setColumns(ColumnBuilder builder, Object entity)
			throws BaseException {
		for (MappingField mf : cfMap.values()) {
			Object value = getFiledValue(entity, mf);
			addColumn(builder, mf, value);
		}
	}

	/**
	 * 设置更新列
	 * 
	 * @since
	 * @param condition
	 * @param table
	 * @param updateColumns
	 * @param updateValues
	 */
	@SuppressWarnings("rawtypes")
	private void setColumns(ColumnBuilder builder, String table,
			String[] updateColumns, Object[] updateValues) {
		if (updateColumns == null || updateValues == null
				|| updateColumns.length == 0 || updateValues.length == 0
				|| updateColumns.length != updateValues.length) {
			log.error("column/value to update is empty or length is unmatched, table: "
					+ table + ", entity: " + elementType.getSimpleName());
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}

		for (int i = 0; i < updateColumns.length; i++) {
			MappingField mf = get(updateColumns[i]);
			if (mf == null) {
				continue;
			}

			addColumn(builder, mf, updateValues[i]);
		}
	}

	/**
	 * 添加待插入或更新的列
	 * 
	 * @since
	 * @param builder
	 * @param mf
	 * @param value
	 */
	@SuppressWarnings({ "rawtypes" })
	private void addColumn(ColumnBuilder builder, MappingField mf, Object value) {
		// 值为空，不作为条件
		if (value == null) {
			return;
		}

		String dataType = mf.jc.type();
		Field field = mf.field;
		Class<?> requiredType = field.getType();
		String col = mf.column;
		try {
			if (DataType.isNull(dataType)) {
				if (String.class.equals(requiredType)) {
					builder.stringCol(col, value.toString());
				} else if (int.class.equals(requiredType)
						|| short.class.equals(requiredType)
						|| Integer.class.equals(requiredType)
						|| Short.class.equals(requiredType)) {
					builder.intCol(col, value);
				} else if (long.class.equals(requiredType)
						|| Long.class.equals(requiredType)
						|| BigDecimal.class.equals(requiredType)) {
					builder.longCol(col, value);
				} else if (Timestamp.class.equals(requiredType)) {
					builder.tsCol(col, (Timestamp) value);
				} else if (byte[].class.equals(requiredType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					builder.binCol(col, (byte[]) value);
				} else if (float.class.equals(requiredType)
						|| Float.class.equals(requiredType)) {
					builder.floatCol(col, ((Number) value).floatValue());
				} else if (double.class.equals(requiredType)
						|| Double.class.equals(requiredType)) {
					builder.doubleCol(col, ((Number) value).doubleValue());
				} else {
					/** 其他类型统一当作string处理 */
					builder.stringCol(col, value.toString());
				}
			} else {
				if (DataType.STRING.equals(dataType)) {
					builder.stringCol(col, value);
				} else if (DataType.INT.equals(dataType)) {
					builder.intCol(col, value);
				} else if (DataType.DECIMAL.equals(dataType)) {
					builder.longCol(col, value);
				} else if (DataType.TIMESTAMP.equals(dataType)) {
					builder.tsCol(col, value);
				} else if (DataType.BINARY.equals(dataType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					builder.binCol(col, value);
				} else if (DataType.FLOAT.equals(dataType)) {
					builder.floatCol(col, value);
				} else {
					builder.stringCol(col, value);
				}
			}
		} catch (Exception e) {
			String message = "addField failed, value: " + value
					+ ", requiredType: " + requiredType + ", type: "
					+ ", dataType: " + dataType;
			log.error(message, e);
			if (e instanceof BaseException) {
				throw (BaseException) e;
			}
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
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
