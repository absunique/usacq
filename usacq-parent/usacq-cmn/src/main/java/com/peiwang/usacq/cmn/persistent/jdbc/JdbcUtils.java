/*
 * 
 * Copyright 2012, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: JdbcUtils.java,v 1.1 2016/08/04 23:15:21 peiwang Exp $
 * 
 * Function:
 * 
 * JDBC工具类
 * 
 * Edit History:
 * 
 * 2012-11-22 - Create By szwang
 */

package com.peiwang.usacq.cmn.persistent.jdbc;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.peiwang.usacq.cmn.Const.DataType;
import com.peiwang.usacq.cmn.Const.DefaultValue;
import com.peiwang.usacq.cmn.exp.BaseErrorCode;
import com.peiwang.usacq.cmn.exp.BaseException;
import com.peiwang.usacq.cmn.log.Logger;
import com.peiwang.usacq.cmn.util.DateUtils;
import com.peiwang.usacq.cmn.util.StringUtils;

/**
 * JDBC工具类
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public class JdbcUtils {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(JdbcUtils.class);

	/**
	 * 关闭数据库资源
	 * 
	 * @since
	 * @param con
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection con, Statement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
		close(con);
	}

	/**
	 * 关闭Connection
	 * 
	 * @since
	 * @param con
	 */
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				log.debug("Could not close JDBC Connection", ex);
			} catch (Throwable ex) {
				log.debug("Unexpected exception on closing JDBC Connection", ex);
			}
		}
	}

	/**
	 * 关闭statement
	 * 
	 * @since
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				// log.trace("Could not close JDBC Statement", ex);
			} catch (Throwable ex) {
				// log.trace("Unexpected exception on closing JDBC Statement",
				// ex);
			}
		}
	}

	/**
	 * 关闭result set
	 * 
	 * @since
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				// log.trace("Could not close JDBC ResultSet", ex);
			} catch (Throwable ex) {
				// log.trace("Unexpected exception on closing JDBC ResultSet",
				// ex);
			}
		}
	}

	/**
	 * 获取ResultSet的值
	 * 
	 * @since
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, int index)
			throws SQLException {
		Object obj = rs.getObject(index);

		if (obj instanceof String) {
			obj = ((String) obj).trim();
		} else if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		} else if (obj instanceof Clob) {
			obj = rs.getString(index);
		} else if (obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData()
					.getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
	}

	/**
	 * 获取ResultSet的值
	 * 
	 * @since
	 * @param rs
	 * @param index
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, int index,
			Class<?> requiredType) throws SQLException {
		if (requiredType == null) {
			return getResultSetValue(rs, index);
		}

		Object value = null;
		boolean wasNullCheck = false;

		if (String.class.equals(requiredType)) {
			value = rs.getString(index);
		} else if (int.class.equals(requiredType)
				|| Integer.class.equals(requiredType)) {
			value = rs.getInt(index);
			wasNullCheck = true;
		} else if (long.class.equals(requiredType)
				|| Long.class.equals(requiredType)) {
			value = rs.getLong(index);
			wasNullCheck = true;
		} else if (java.sql.Timestamp.class.equals(requiredType)
				|| java.util.Date.class.equals(requiredType)) {
			value = rs.getTimestamp(index);
		} else if (boolean.class.equals(requiredType)
				|| Boolean.class.equals(requiredType)) {
			value = rs.getBoolean(index);
			wasNullCheck = true;
		} else if (byte.class.equals(requiredType)
				|| Byte.class.equals(requiredType)) {
			value = rs.getByte(index);
			wasNullCheck = true;
		} else if (short.class.equals(requiredType)
				|| Short.class.equals(requiredType)) {
			value = rs.getShort(index);
			wasNullCheck = true;
		} else if (float.class.equals(requiredType)
				|| Float.class.equals(requiredType)) {
			value = rs.getFloat(index);
			wasNullCheck = true;
		} else if (double.class.equals(requiredType)
				|| Double.class.equals(requiredType)
				|| Number.class.equals(requiredType)) {
			value = rs.getDouble(index);
			wasNullCheck = true;
		} else if (byte[].class.equals(requiredType)) {
			value = rs.getBytes(index);
		} else if (java.sql.Date.class.equals(requiredType)) {
			value = rs.getDate(index);
		} else if (java.sql.Time.class.equals(requiredType)) {
			value = rs.getTime(index);
		} else if (BigDecimal.class.equals(requiredType)) {
			value = rs.getBigDecimal(index);
		} else if (Blob.class.equals(requiredType)) {
			value = rs.getBlob(index);
		} else if (Clob.class.equals(requiredType)) {
			value = rs.getClob(index);
		} else {
			value = getResultSetValue(rs, index);
		}

		if (wasNullCheck && value != null && rs.wasNull()) {
			value = null;
		}
		return value;
	}

	/**
	 * 获取ResultSet的值
	 * 
	 * @since
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, String columnName)
			throws SQLException {
		Object obj = rs.getObject(columnName);

		if (obj instanceof Blob) {
			obj = rs.getBytes(columnName);
		} else if (obj instanceof Clob) {
			obj = rs.getString(columnName);
		} else if (obj != null && obj instanceof java.sql.Date) {
			obj = rs.getTimestamp(columnName);
		}
		return obj;
	}

	/**
	 * 获取ResultSet的值
	 * 
	 * @since
	 * @param rs
	 * @param columnName
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static Object getResultSetValue(ResultSet rs, String columnName,
			Class requiredType) throws SQLException {
		if (requiredType == null) {
			return getResultSetValue(rs, columnName);
		}

		Object value = null;
		boolean wasNullCheck = false;

		if (String.class.equals(requiredType)) {
			value = rs.getString(columnName);
			if (value != null) {
				value = value.toString().trim();
			}
		} else if (int.class.equals(requiredType)
				|| Integer.class.equals(requiredType)) {
			value = rs.getInt(columnName);
			wasNullCheck = true;
		} else if (long.class.equals(requiredType)
				|| Long.class.equals(requiredType)) {
			value = rs.getLong(columnName);
			wasNullCheck = true;
		} else if (boolean.class.equals(requiredType)
				|| Boolean.class.equals(requiredType)) {
			value = rs.getBoolean(columnName);
			wasNullCheck = true;
		} else if (byte.class.equals(requiredType)
				|| Byte.class.equals(requiredType)) {
			value = rs.getByte(columnName);
			wasNullCheck = true;
		} else if (short.class.equals(requiredType)
				|| Short.class.equals(requiredType)) {
			value = rs.getShort(columnName);
			wasNullCheck = true;
		} else if (float.class.equals(requiredType)
				|| Float.class.equals(requiredType)) {
			value = rs.getFloat(columnName);
			wasNullCheck = true;
		} else if (double.class.equals(requiredType)
				|| Double.class.equals(requiredType)
				|| Number.class.equals(requiredType)) {
			value = rs.getDouble(columnName);
			wasNullCheck = true;
		} else if (byte[].class.equals(requiredType)) {
			value = rs.getBytes(columnName);
		} else if (java.sql.Date.class.equals(requiredType)) {
			value = rs.getDate(columnName);
		} else if (java.sql.Time.class.equals(requiredType)) {
			value = rs.getTime(columnName);
		} else if (java.sql.Timestamp.class.equals(requiredType)
				|| java.util.Date.class.equals(requiredType)) {
			value = rs.getTimestamp(columnName);
		} else if (BigDecimal.class.equals(requiredType)) {
			value = rs.getBigDecimal(columnName);
		} else if (Blob.class.equals(requiredType)) {
			value = rs.getBlob(columnName);
		} else if (Clob.class.equals(requiredType)) {
			value = rs.getClob(columnName);
		} else {
			value = getResultSetValue(rs, columnName);
		}

		if (wasNullCheck && value != null && rs.wasNull()) {
			value = null;
		}
		return value;
	}

	/**
	 * 获取ResultSet的值
	 * 
	 * @since
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(SqlRowSet rs, String columnName) {
		Object obj = rs.getObject(columnName);

		if (obj instanceof Blob) {
			obj = rs.getByte(columnName);
		} else if (obj instanceof Clob) {
			obj = rs.getString(columnName);
		} else if (obj != null && obj instanceof java.sql.Date) {
			obj = rs.getTimestamp(columnName);
		}
		return obj;
	}

	/**
	 * 获取sqlRowSet的值
	 * 
	 * @since
	 * @param rs
	 * @param columnName
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static Object getResultSetValue(SqlRowSet rs, String columnName,
			Class requiredType) {
		if (requiredType == null) {
			return getResultSetValue(rs, columnName);
		}

		Object value = null;
		boolean wasNullCheck = false;

		if (String.class.equals(requiredType)) {
			value = rs.getString(columnName);
			if (value != null) {
				value = value.toString().trim();
			}
		} else if (int.class.equals(requiredType)
				|| Integer.class.equals(requiredType)) {
			value = rs.getInt(columnName);
			wasNullCheck = true;
		} else if (long.class.equals(requiredType)
				|| Long.class.equals(requiredType)) {
			value = rs.getLong(columnName);
			wasNullCheck = true;
		} else if (boolean.class.equals(requiredType)
				|| Boolean.class.equals(requiredType)) {
			value = rs.getBoolean(columnName);
			wasNullCheck = true;
		} else if (byte.class.equals(requiredType)
				|| Byte.class.equals(requiredType)) {
			value = rs.getByte(columnName);
			wasNullCheck = true;
		} else if (short.class.equals(requiredType)
				|| Short.class.equals(requiredType)) {
			value = rs.getShort(columnName);
			wasNullCheck = true;
		} else if (float.class.equals(requiredType)
				|| Float.class.equals(requiredType)) {
			value = rs.getFloat(columnName);
			wasNullCheck = true;
		} else if (double.class.equals(requiredType)
				|| Double.class.equals(requiredType)
				|| Number.class.equals(requiredType)) {
			value = rs.getDouble(columnName);
			wasNullCheck = true;
		} else if (byte[].class.equals(requiredType)) {
			value = rs.getByte(columnName);
		} else if (java.sql.Date.class.equals(requiredType)) {
			value = rs.getDate(columnName);
		} else if (java.sql.Time.class.equals(requiredType)) {
			value = rs.getTime(columnName);
		} else if (java.sql.Timestamp.class.equals(requiredType)
				|| java.util.Date.class.equals(requiredType)) {
			value = rs.getTimestamp(columnName);
		} else if (BigDecimal.class.equals(requiredType)) {
			value = rs.getBigDecimal(columnName);
		} else {
			value = getResultSetValue(rs, columnName);
		}

		if (wasNullCheck && value != null && rs.wasNull()) {
			value = null;
		}
		return value;
	}

	/**
	 * 获取列名
	 * 
	 * @since
	 * @param resultSetMetaData
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public static String getColumnName(ResultSetMetaData resultSetMetaData,
			int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1) {
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}

	/**
	 * 判断是否为字符串值
	 * 
	 * @since
	 * @param inValueType
	 * @return
	 */
	public static boolean isStringValue(Class<?> inValueType) {
		// Consider any CharSequence (including StringBuffer and StringBuilder)
		// as a String.
		return (CharSequence.class.isAssignableFrom(inValueType) || StringWriter.class
				.isAssignableFrom(inValueType));
	}

	/**
	 * 判断是否为时间值
	 * 
	 * @since
	 * @param inValueType
	 * @return
	 */
	public static boolean isDateValue(Class<?> inValueType) {
		return (java.util.Date.class.isAssignableFrom(inValueType) && !(java.sql.Date.class
				.isAssignableFrom(inValueType)
				|| java.sql.Time.class.isAssignableFrom(inValueType) || java.sql.Timestamp.class
					.isAssignableFrom(inValueType)));
	}

	/**
	 * 改单引号为双引号，用于数据库值输入
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static String stringForSQL(String value) {
		if (value == null) {
			return "''";
		}

		value = value.replace("'", "''");
		value = "'" + value + "'";
		return value;
	}

	/**
	 * 整型数据检查
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static String intForSql(String value) {
		if (StringUtils.isEmpty(value)) {
			value = DefaultValue.ZERO;
		}

		try {
			Long.parseLong(value);
			return value;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_DATA_TYPE_INVALID);
		}
	}

	/**
	 * 数据检查
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static String decimalForSql(String value) {
		return floatForSql(value);
	}

	/**
	 * 浮点型数据检查
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static String floatForSql(String value) {
		if (StringUtils.isEmpty(value)) {
			value = DefaultValue.ZERO;
		}

		try {
			Double.parseDouble(value);
			return value;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_DATA_TYPE_INVALID);
		}
	}

	/**
	 * 时间戳字段转换成入库值
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static String tsForSql(String value) {
		if (StringUtils.isEmpty(value)
				|| value.toUpperCase().equals(
						DefaultValue.CURRENT_TIMESTAMP.toUpperCase())) {
			return DefaultValue.CURRENT_TIMESTAMP;
		} else {
			return "'" + value + "'";
		}
	}

	/**
	 * 二进制字符串处理
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static String binaryForSql(String value) {
		return "x'" + value + "'";
	}

	/**
	 * 值转换成sql
	 * 
	 * @since
	 * @param value
	 * @param requiredType
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String valueForDB(String value, Class<?> requiredType) {
		if (String.class.equals(requiredType)) {
			value = stringForSQL(value);
		} else if (int.class.equals(requiredType)
				|| Integer.class.equals(requiredType)
				|| long.class.equals(requiredType)
				|| Long.class.equals(requiredType)
				|| byte.class.equals(requiredType)
				|| Byte.class.equals(requiredType)
				|| short.class.equals(requiredType)
				|| Short.class.equals(requiredType)
				|| float.class.equals(requiredType)
				|| Float.class.equals(requiredType)
				|| double.class.equals(requiredType)
				|| Double.class.equals(requiredType)) {
			if (value == null) {
				value = DefaultValue.ZERO;
			}
		} else if (java.sql.Timestamp.class.equals(requiredType)
				|| java.util.Date.class.equals(requiredType)
				|| java.sql.Date.class.equals(requiredType)
				|| java.sql.Time.class.equals(requiredType)) {
			value = tsForSql(value);
		} else {
			// not support
		}

		return value;
	}

	/**
	 * 值转换成SET对象
	 * 
	 * @since
	 * @param value
	 * @param requiredType
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object valueForDBForSet(Object value, Class<?> requiredType) {

		if (value != null) {
			return value;
		}
		if (String.class.equals(requiredType)) {
			value = "";
		} else if (int.class.equals(requiredType)
				|| Integer.class.equals(requiredType)
				|| long.class.equals(requiredType)
				|| Long.class.equals(requiredType)
				|| byte.class.equals(requiredType)
				|| Byte.class.equals(requiredType)
				|| short.class.equals(requiredType)
				|| Short.class.equals(requiredType)
				|| float.class.equals(requiredType)
				|| Float.class.equals(requiredType)
				|| double.class.equals(requiredType)
				|| Double.class.equals(requiredType)) {
			if (value == null) {
				value = 0;
			}
		} else if (java.sql.Timestamp.class.equals(requiredType)
				|| java.util.Date.class.equals(requiredType)
				|| java.sql.Date.class.equals(requiredType)
				|| java.sql.Time.class.equals(requiredType)) {
			value = DateUtils.getCurrentTimestamp();
		} else {
			// not support
		}

		return value;
	}

	/**
	 * 获取入库转换后的值
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static String valueForDB(String value, String dataType) {
		if (dataType.equals(DataType.STRING)) {
			value = stringForSQL(value);
		} else if (DataType.INT.equals(dataType)) {
			value = intForSql(value);
		} else if (DataType.FLOAT.equals(dataType)) {
			value = floatForSql(value);
		} else if (DataType.TIMESTAMP.equals(dataType)) {
			value = tsForSql(value);
		} else if (DataType.BINARY.equals(dataType)) {
			return binaryForSql(value);
		} else if (DataType.DECIMAL.equals(dataType)) {
			value = decimalForSql(value);
		} else {
			// do nothing
		}
		return value;
	}

	/**
	 * 获取入库转换后的值
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static Object valueForDBForSet(Object value, String dataType) {
		if (value != null) {
			return value;
		}
		if (dataType.equals(DataType.STRING)) {
			value = "";
		} else if (DataType.INT.equals(dataType)) {
			value = 0;
		} else if (DataType.FLOAT.equals(dataType)) {
			value = 0.0f;
		} else if (DataType.TIMESTAMP.equals(dataType)) {
			value = DateUtils.getCurrentTimestamp();
		} else if (DataType.BINARY.equals(dataType)) {
			return "";
		} else if (DataType.DECIMAL.equals(dataType)) {
			value = new BigDecimal(0);
		} else {
			// do nothing
		}
		return value;
	}

	/**
	 * 获取值
	 * 
	 * @since
	 * @param rs
	 * @param name
	 * @param dataType
	 * @return
	 * @throws SQLException
	 */
	public static String getResultSetValue(ResultSet rs, String name,
			String dataType) throws SQLException {
		Object obj = rs.getObject(name);
		if (obj == null) {
			return "";
		}

		String v = obj.toString().trim();
		if (dataType.equals(DataType.BINARY)) {
			byte[] bb = rs.getBytes(name);
			v = new String(bb);
		} else if (dataType.equals(DataType.DECIMAL)) {
			try {
				v = DecimalFormat.getInstance().parse(v).toString();
			} catch (ParseException e) {
				log.error("parse field failed, " + name + ", value: " + obj);
			}
		} else {// TODO 其他类型处理
			// do nothing
		}

		return v;
	}

	/**
	 * 获取数据库模板
	 * 
	 * @since
	 * @param jndiName
	 * @return
	 */
	/*
	 * public static JdbcTemplate getJdbcTemplate(String jndiName) {
	 * 
	 * DataSource ds = ServicesUtil.getDataSource(jndiName); JdbcTemplate jdbcT
	 * = new JdbcTemplate(ds); return jdbcT;
	 * 
	 * }
	 */
}
