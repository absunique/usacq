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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.Const.DataType;
import com.zpayment.cmn.Const.DefaultValue;
import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.util.StringUtils;

/**
 * hold sql以及参数列表
 * 
 * @author peiwang
 * @since 2017年3月23日
 */
public class PreparedSQL {

	private static final Logger log = Logger.getLogger(PreparedSQL.class);

	private List<SqlParam> params = new LinkedList<SqlParam>();

	private String sql;

	private String countSql;

	public PreparedSQL() {

	}

	public PreparedSQL(String sql) {
		this.sql = sql;
	}

	// @Override
	// public Iterator<SqlParam> iterator() {
	// return params.iterator();
	// }

	/**
	 * 设置查询值
	 * 
	 * @since
	 * @param ps
	 * @param psql
	 */
	public void setValues(PreparedStatement ps) throws SQLException {
		int i = 1;
		for (SqlParam p : this.getParams()) {
			if (p.isPreparedParam()) {
				p.setPreparedParamValue(i++, ps);
			}
		}
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

	public String getCountSql() {
		if (StringUtils.isEmpty(countSql)) {
			countSql = String.format("select count(*) from (%s) as __newName", sql);
		}
		return countSql;
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

	public void setParams(List<SqlParam> params) {
		this.params = params;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	// public SqlParam addField(Object obj) {
	// if (obj.getClass().isArray()) {
	// // TODO 参数列表暂时不支持数组
	// throw new BaseException(BaseErrorCode.FAIL);
	// } else if (obj instanceof Collection) {
	// int cnt = ((Collection) obj).size();
	// for (int j = 0; j < cnt - 1; j++) {
	// sql.append("?,");
	// }
	// sql.append("?) ");
	// } else if (obj instanceof String) {
	// return addString((String) obj);
	// } else if (obj instanceof Integer) {
	// return addInt((Integer) obj);
	// } else if (obj instanceof BigInteger) {
	// return addLong(((BigInteger) obj).intValue());
	// }
	// }

	/**
	 * 添加实体对象指定字段
	 * 
	 * @since
	 * @param entity
	 *            实体对象
	 * @param field
	 *            指定字段
	 * @param dataType
	 *            数据类型，如果为空，则按字段类型处理
	 * @return
	 */
	public SqlParam addField(Object entity, Field field, String dataType) {
		try {
			Class<?> requiredType = field.getType();
			// dataType未定义，根据object的实际类型处理
			if (DataType.isNull(dataType)) {
				if (String.class.equals(requiredType)) {
					String v = (String) field.get(entity);
					if (v == null) {
						v = DefaultValue.NULL;
					}
					return addString(v);
				} else if (int.class.equals(requiredType)
						|| short.class.equals(requiredType)) {
					int v = field.getInt(entity);
					return addInt(v);
				} else if (Integer.class.equals(requiredType)
						|| Short.class.equals(requiredType)) {
					Number obj = (Number) field.get(entity);
					int i = DefaultValue.INT_ZERO;
					if (obj != null) {
						i = obj.intValue();
					}
					return addInt(i);
				} else if (long.class.equals(requiredType)) {
					long l = field.getLong(entity);
					return addLong(l);
				} else if (Long.class.equals(requiredType)
						|| BigDecimal.class.equals(requiredType)) {
					Object obj = field.get(entity);
					long l = DefaultValue.INT_ZERO;
					if (obj != null) {
						l = ((Number) obj).longValue();
					}
					return addLong(l);
				} else if (java.sql.Timestamp.class.equals(requiredType)) {
					Object obj = field.get(entity);
					return addTimestamp((Timestamp) obj);
				} else if (byte[].class.equals(requiredType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					byte[] v = (byte[]) field.get(entity);
					return addBinary(v);
				} else if (float.class.equals(requiredType)) {
					float f = field.getFloat(entity);
					return addFloat(f);
				} else if (Float.class.equals(requiredType)) {
					Object obj = field.get(entity);
					float f = DefaultValue.INT_ZERO;
					if (obj != null) {
						f = ((Float) obj).floatValue();
					}
					return addFloat(f);
				} else if (double.class.equals(requiredType)) {
					double d = field.getDouble(entity);
					return addDouble(d);
				} else if (Double.class.equals(requiredType)) {
					Object obj = field.get(entity);
					double d = DefaultValue.INT_ZERO;
					if (obj != null) {
						d = ((Double) obj).doubleValue();
					}
					return addDouble(d);
				} else {
					/** 其他类型统一当作string处理 */
					Object obj = field.get(entity);
					String s = obj == null ? DefaultValue.NULL : obj.toString();
					return addString(s);
				}
			} else {// 定义了dataType，以dataType为准处理
				if (DataType.STRING.equals(dataType)) {
					Object obj = field.get(entity);
					String s = obj == null ? DefaultValue.NULL : obj.toString();
					return addString(s);
				} else if (DataType.INT.equals(dataType)
						|| DataType.DECIMAL.equals(dataType)) {
					if (int.class.equals(requiredType)
							|| short.class.equals(requiredType)) {
						int v = field.getInt(entity);
						return addInt(v);
					} else if (Integer.class.equals(requiredType)
							|| Short.class.equals(requiredType)) {
						Number obj = (Number) field.get(entity);
						int i = DefaultValue.INT_ZERO;
						if (obj != null) {
							i = obj.intValue();
						}
						return addInt(i);
					} else if (long.class.equals(requiredType)) {
						long l = field.getLong(entity);
						return addLong(l);
					} else if (Long.class.equals(requiredType)
							|| BigDecimal.class.equals(requiredType)) {
						Object obj = field.get(entity);
						long l = DefaultValue.INT_ZERO;
						if (obj != null) {
							l = ((Number) obj).longValue();
						}
						return addLong(l);
					} else {
						/** 其他情况当作string处理 */
						Object obj = field.get(entity);
						String s = obj == null ? null : obj.toString();
						int i = DefaultValue.INT_ZERO;
						if (s != null) {
							i = Integer.parseInt(s.trim());
						}
						return addInt(i);
					}
				} else if (DataType.TIMESTAMP.equals(dataType)) {
					Object obj = field.get(entity);
					if (java.sql.Timestamp.class.equals(requiredType)) {
						return addTimestamp((Timestamp) obj);
					} else {
						String tsString = obj == null ? null : obj.toString();
						return addTimestamp(tsString);
					}
				} else if (DataType.FLOAT.equals(dataType)) {
					if (float.class.equals(requiredType)) {
						float f = field.getFloat(entity);
						return addFloat(f);
					} else if (Float.class.equals(requiredType)) {
						Object obj = field.get(entity);
						float f = DefaultValue.INT_ZERO;
						if (obj != null) {
							f = ((Float) obj).floatValue();
						}
						return addFloat(f);
					} else if (double.class.equals(requiredType)) {
						double d = field.getDouble(entity);
						return addDouble(d);
					} else if (Double.class.equals(requiredType)) {
						Object obj = field.get(entity);
						double d = DefaultValue.INT_ZERO;
						if (obj != null) {
							d = ((Double) obj).doubleValue();
						}
						return addDouble(d);
					} else {
						Object obj = field.get(entity);
						float f = DefaultValue.INT_ZERO;
						if (obj != null) {
							f = Float.parseFloat(obj.toString());
						}
						return addFloat(f);
					}
				} else if (DataType.BINARY.equals(dataType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					if (byte[].class.equals(requiredType)) {
						byte[] v = (byte[]) field.get(entity);
						return addBinary(v);
					} else {
						Object obj = field.get(entity);
						String v = obj == null ? DefaultValue.NULL : obj
								.toString();
						return addBinary(v);
					}
				} else {
					Object obj = field.get(entity);
					String s = obj == null ? DefaultValue.NULL : obj.toString();
					return addString(s);
				}
			}
		} catch (Exception e) {
			String message = "addSqlParam failed, entityClass: "
					+ entity.getClass().getSimpleName() + ", field: "
					+ field.getName() + ", type: " + field.getType().getName()
					+ ", dataType: " + dataType;
			log.error(message, e);
			throw new BaseException(BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
		}
	}

	/**
	 * 添加指定类型的参数
	 * 
	 * @since
	 * @param requiredType
	 *            对象类型
	 * @param dataType
	 *            数据类型
	 * @param value
	 *            参数值
	 * @return
	 */
	public SqlParam addParam(Class<?> requiredType, String dataType,
			Object value) {
		try {
			if (DataType.isNull(dataType)) {
				if (String.class.equals(requiredType)) {
					String v = value == null ? DefaultValue.NULL : value
							.toString();
					return addString(v);
				} else if (int.class.equals(requiredType)
						|| short.class.equals(requiredType)
						|| Integer.class.equals(requiredType)
						|| Short.class.equals(requiredType)) {
					if (value == null) {
						return addInt(DefaultValue.INT_ZERO);
					} else {
						return addInt(((Number) value).intValue());
					}
				} else if (long.class.equals(requiredType)
						|| Long.class.equals(requiredType)
						|| BigDecimal.class.equals(requiredType)) {
					if (value == null) {
						return addLong(DefaultValue.INT_ZERO);
					} else {
						return addLong(((Number) value).longValue());
					}
				} else if (Timestamp.class.equals(requiredType)) {
					return addTimestamp((Timestamp) value);
				} else if (byte[].class.equals(requiredType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					byte[] v = (byte[]) value;
					return addBinary(v);
				} else if (float.class.equals(requiredType)
						|| Float.class.equals(requiredType)) {
					if (value == null) {
						return addFloat(DefaultValue.INT_ZERO);
					} else {
						return addFloat(((Number) value).floatValue());
					}
				} else if (double.class.equals(requiredType)
						|| Double.class.equals(requiredType)) {
					if (value == null) {
						return addDouble(DefaultValue.INT_ZERO);
					} else {
						return addDouble(((Number) value).doubleValue());
					}
				} else {
					/** 其他类型统一当作string处理 */
					String v = value == null ? DefaultValue.NULL : value
							.toString();
					return addString(v);
				}
			} else {
				if (DataType.STRING.equals(dataType)) {
					String v = value == null ? DefaultValue.NULL : value
							.toString();
					return addString(v);
				} else if (DataType.INT.equals(dataType)) {
					if (value == null) {
						return addInt(DefaultValue.INT_ZERO);
					} else if (value instanceof Number) {
						return addInt(((Number) value).intValue());
					} else {
						return addInt(Integer.parseInt(value.toString().trim()));
					}
				} else if (DataType.DECIMAL.equals(dataType)) {
					if (value == null) {
						return addLong(DefaultValue.INT_ZERO);
					} else if (value instanceof Number) {
						return addLong(((Number) value).longValue());
					} else {
						return addLong(Long.parseLong(value.toString().trim()));
					}
				} else if (DataType.TIMESTAMP.equals(dataType)) {
					if (value instanceof Timestamp) {
						return addTimestamp((Timestamp) value);
					} else {
						String ts = value == null ? null : value.toString();
						return addTimestamp(ts);
					}
				} else if (DataType.BINARY.equals(dataType)) {
					/** 对应于DB2的VARCHAR(CHAR) FOR BIT DATA */
					if (value instanceof byte[]) {
						byte[] v = (byte[]) value;
						return addBinary(v);
					} else {
						String v = value == null ? DefaultValue.NULL : value
								.toString();
						return addBinary(v);
					}
				} else if (DataType.FLOAT.equals(dataType)) {
					if (value == null) {
						return addFloat(DefaultValue.INT_ZERO);
					} else if (value instanceof Number) {
						return addFloat(((Number) value).floatValue());
					} else {
						return addFloat(Float.parseFloat(value.toString()
								.trim()));
					}
				} else {
					String v = value == null ? DefaultValue.NULL : value
							.toString();
					return addString(v);
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

	public SqlParam addParam(String dataType, String value) {
		if (dataType == null) {
			throw new BaseException(BaseErrorCode.FAIL, new Object[] { "null" });
		}

		if (value == null) {
			throw new BaseException(BaseErrorCode.COMN_NULL_EXCEPTION);
		}

		try {
			if (DataType.STRING.equals(dataType)) {
				return addString(value);
			} else if (DataType.INT.equals(dataType)) {
				return addInt(Integer.parseInt(value.trim()));
			} else if (DataType.DECIMAL.equals(dataType)) {
				return addDecimal(Long.parseLong(value.trim()));
			} else if (DataType.TIMESTAMP.equals(dataType)) {
				return addTimestamp(value);
			} else if (DataType.BINARY.equals(dataType)) {
				return addBinary(value);// 仅支持16进制字符串
			} else if (DataType.FLOAT.equals(dataType)) {
				return addFloat(Float.parseFloat(value.trim()));
			} else {
				// 其他类型暂时不支持
				throw new BaseException(BaseErrorCode.FAIL,
						new Object[] { dataType });
			}
		} catch (Exception e) {
			String message = "addParam failed, value: " + value
					+ ", dataType: " + dataType;
			log.error(message, e);
			throw new BaseException(BaseErrorCode.FAIL, new Object[] {
					dataType, value });
		}
	}

	@Override
	public String toString() {
		List<String> ps = new LinkedList<String>();
		for (SqlParam p : this.getParams()) {
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
