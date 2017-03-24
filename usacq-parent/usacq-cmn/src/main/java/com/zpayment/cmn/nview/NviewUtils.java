/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */
package com.zpayment.cmn.nview;

import java.sql.Timestamp;

import com.zpayment.cmn.Const.DataType;
import com.zpayment.cmn.Const.DefaultValue;
import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.util.DateUtils;
import com.zpayment.cmn.util.StringUtils;

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 */
public class NviewUtils {
	/**
	 * 整型数据检查
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static int intForNview(String value) {
		if (StringUtils.isEmpty(value)) {
			return 0;
		}

		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_DATA_TYPE_INVALID);
		}
	}

	/**
	 * 浮点型数据检查
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static float floatForNview(String value) {
		if (StringUtils.isEmpty(value)) {
			return 0.0f;
		}

		try {
			return Float.parseFloat(value);

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
	public static double decimalForSql(String value) {
		return floatForNview(value);
	}

	/**
	 * 时间戳字段转换成入库值
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static Timestamp tsForNview(String value) {
		if (StringUtils.isEmpty(value)
				|| value.toUpperCase().equals(
						DefaultValue.CURRENT_TIMESTAMP.toUpperCase())) {
			return DateUtils.getCurrentTimestamp();
		} else {
			return new Timestamp(DateUtils.parseTime(value).getTime());
		}
	}

	/**
	 * 获取入NVIEW转换后的值
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static Object valueForNview(String value, String dataType) {
		if (dataType.equals(DataType.STRING)) {
			// STRING原样返回
			return value;
		} else if (DataType.INT.equals(dataType)) {
			return intForNview(value);
		} else if (DataType.FLOAT.equals(dataType)) {
			return floatForNview(value);
		} else if (DataType.TIMESTAMP.equals(dataType)) {
			return tsForNview(value);
		} else if (DataType.DECIMAL.equals(dataType)) {
			return decimalForSql(value);
		} else {
			return null;
		}
	}
}
