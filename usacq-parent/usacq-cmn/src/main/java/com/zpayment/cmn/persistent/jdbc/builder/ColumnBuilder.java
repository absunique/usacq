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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.persistent.jdbc.param.BinaryParam;
import com.zpayment.cmn.persistent.jdbc.param.DoubleParam;
import com.zpayment.cmn.persistent.jdbc.param.FloatParam;
import com.zpayment.cmn.persistent.jdbc.param.IntParam;
import com.zpayment.cmn.persistent.jdbc.param.LongParam;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.persistent.jdbc.param.StringParam;
import com.zpayment.cmn.persistent.jdbc.param.TimestampParam;
import com.zpayment.cmn.util.StringUtils;

/**
 * 插入与更新的基础类，用于设置待插入列与更新列
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
@SuppressWarnings("unchecked")
public class ColumnBuilder<T extends ColumnBuilder<T>> extends AbstractBuilder {
	private static final Logger log = Logger.getLogger(ColumnBuilder.class);

	/**
	 * 待插入或更新的列
	 */
	protected List<ColParam> colParams = new LinkedList<ColParam>();

	/**
	 * 返回当前的参数列List，用于添加列
	 * @return
	 */
	protected List<ColParam> getCurrentColParams() {
		return colParams;
	}

	protected void setColParams(List<ColParam> colParams) {
		this.colParams = colParams;
	}
	
	/**
	 * 获取参照的参数列，用于生成sql
	 * @return
	 */
	protected List<ColParam> getFlagshipColParams() {
		return colParams;
	} 

	/**
	 * 设置整型列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T intCol(String col, int value) {
		SqlParam param = new IntParam(value);
		add(col, param);
		return (T) this;
	}

	/**
	 * 添加整型列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T intCol(String col, Object value) {
		do {
			if (value == null) {
				break;
			}

			Class<?> clazz = value.getClass();

			if (int.class.equals(clazz) || short.class.equals(clazz)
					|| long.class.equals(clazz)
					|| Number.class.isAssignableFrom(clazz)) {
				intCol(col, ((Number) value).intValue());
			} else {
				try {
					intCol(col, Integer.parseInt(value.toString().trim()));
				} catch (Exception e) {
					log.error("invalid int: " + value);
					throw new BaseException(
							BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
				}
			}
		} while (false);

		return (T) this;
	}

	/**
	 * 设置长整型列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T longCol(String col, long value) {
		SqlParam param = new LongParam(value);
		add(col, param);
		return (T) this;
	}

	/**
	 * 添加长整型列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T longCol(String col, Object value) {
		do {
			if (value == null) {
				break;
			}

			Class<?> clazz = value.getClass();

			if (int.class.equals(clazz) || short.class.equals(clazz)
					|| long.class.equals(clazz)
					|| Number.class.isAssignableFrom(clazz)) {
				longCol(col, ((Number) value).longValue());
			} else {
				try {
					longCol(col, Long.parseLong(value.toString().trim()));
				} catch (Exception e) {
					log.error("invalid long: " + value);
					throw new BaseException(
							BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
				}
			}
		} while (false);

		return (T) this;
	}

	/**
	 * 设置字符串型列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T stringCol(String col, String value) {
		if (value != null) {
			SqlParam param = new StringParam(value);
			add(col, param);
		}
		return (T) this;
	}

	/**
	 * 设置字符串型列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T stringCol(String col, Object value) {
		if (value != null) {
			SqlParam param = new StringParam(value.toString());
			add(col, param);
		}

		return (T) this;
	}

	/**
	 * 设置时间戳列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T tsCol(String col, Timestamp value) {
		if (value != null) {
			SqlParam param = new TimestampParam(value);
			add(col, param);
		}

		return (T) this;
	}

	/**
	 * 设置时间戳列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T tsCol(String col, String value) {
		if (value != null) {
			SqlParam param = new TimestampParam(value);
			add(col, param);
		}

		return (T) this;
	}

	/**
	 * 设置时间戳列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T tsCol(String col, Object value) {
		if (value != null) {
			if (value instanceof Timestamp) {
				this.tsCol(col, (Timestamp) value);
			} else {
				this.tsCol(col, value.toString());
			}
		}

		return (T) this;
	}

	/**
	 * 设置二进制戳列
	 * 
	 * @since
	 * @param col
	 * @param value
	 *            十六进制字符串
	 * @return
	 */
	public T binCol(String col, String value) {
		if (value != null) {
			SqlParam param = new BinaryParam(value);
			add(col, param);
		}
		return (T) this;
	}

	/**
	 * 设置二进制戳列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T binCol(String col, byte[] value) {
		if (value != null) {
			SqlParam param = new BinaryParam(value);
			add(col, param);
		}
		return (T) this;
	}

	/**
	 * 设置二进制戳列
	 * 
	 * @since
	 * @param col
	 * @param value
	 *            十六进制字符串
	 * @return
	 */
	public T binCol(String col, Object value) {
		if (value != null) {
			if (value.getClass().equals(byte[].class)) {
				this.binCol(col, (byte[]) value);
			} else {
				this.binCol(col, value.toString());
			}
		}
		return (T) this;
	}

	/**
	 * 设置浮点列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T floatCol(String col, float value) {
		SqlParam param = new FloatParam(value);
		add(col, param);
		return (T) this;
	}

	/**
	 * 设置浮点列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T floatCol(String col, Object value) {
		do {
			if (value == null) {
				break;
			}

			Class<?> clazz = value.getClass();

			if (float.class.equals(value) || double.class.equals(value)
					|| int.class.equals(clazz) || short.class.equals(clazz)
					|| long.class.equals(clazz)
					|| Number.class.isAssignableFrom(clazz)) {
				floatCol(col, ((Number) value).floatValue());
			} else {
				try {
					floatCol(col, Float.parseFloat(value.toString().trim()));
				} catch (Exception e) {
					log.error("invalid float: " + value);
					throw new BaseException(
							BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
				}
			}
		} while (false);

		return (T) this;
	}

	/**
	 * 设置双精度浮点列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T doubleCol(String col, double value) {
		SqlParam param = new DoubleParam(value);
		add(col, param);
		return (T) this;
	}

	/**
	 * 设置双精度浮点列
	 * 
	 * @since
	 * @param col
	 * @param value
	 * @return
	 */
	public T doubleCol(String col, Object value) {
		do {
			if (value == null) {
				break;
			}

			Class<?> clazz = value.getClass();

			if (double.class.equals(value) || float.class.equals(value)
					|| int.class.equals(clazz) || short.class.equals(clazz)
					|| long.class.equals(clazz)
					|| Number.class.isAssignableFrom(clazz)) {
				doubleCol(col, ((Number) value).doubleValue());
			} else {
				try {
					doubleCol(col, Double.parseDouble(value.toString().trim()));
				} catch (Exception e) {
					log.error("invalid double: " + value);
					throw new BaseException(
							BaseErrorCode.COMN_DATA_MAPPING_EXCEPTOIN);
				}
			}
		} while (false);

		return (T) this;
	}

	/**
	 * 添加一列
	 * 
	 * @since
	 * @param col
	 * @param param
	 * @return
	 */
	public T add(String col, SqlParam param) {
		this.getCurrentColParams().add(new ColParam(col, param));
		return (T) this;
	}

	@Override
	public String getSql() {
		return null;
	}

	@Override
	public List<SqlParam> getParams() {
		List<SqlParam> params = new ArrayList<SqlParam>(getCurrentColParams().size());

		for (ColParam colParam : colParams) {
			SqlParam sqlParam = colParam.getParam();
			if (sqlParam != null && sqlParam.isPreparedParam()) {
				params.add(sqlParam);
			}
		}

		return params;
	}

	/**
	 * 列名与对应参数
	 * 
	 * @author wangshuzhen
	 * @version
	 * @since
	 * 
	 */
	protected static class ColParam {

		private String col;
		private SqlParam param;

		public ColParam(String col, SqlParam param) {
			if (StringUtils.isEmpty(col)) {
				throw new IllegalArgumentException("col is null");
			}

			this.col = col;
			this.param = param;
		}

		public String getCol() {
			return col;
		}

		public SqlParam getParam() {
			return param;
		}
	}

}
