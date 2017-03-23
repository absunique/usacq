/*
 * 
 * Copyright 2014, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: AtUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2014-1-6 - Create By CUPPC
 */

package com.zpayment.cmn.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * TODO 请添加功能描述
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class AtUtils {

	/**
	 * 根据小数位数 截断 原数的小数位 Oct.14,2011,by reifu. 重写处理方法 2012-10-19 wangshuzhen
	 * 避免出现科学计数的形式，改为DecimalFormat实现
	 * 
	 * @param digitStr
	 *            小数
	 * @param digit
	 *            小数位数
	 * @return
	 */
	public static String parseDigit2AtStr(double digitStr, int digit) {
		if (digit < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal ds = new BigDecimal(digitStr);
		BigDecimal one = new BigDecimal("1");
		double d = ds.divide(one, digit, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		StringBuilder strBuilder = new StringBuilder("##############0");
		if (digit > 0) {
			strBuilder.append(".");
			for (int i = 0; i < digit; i++) {
				strBuilder.append("0");
			}
		}
		String format = strBuilder.toString();
		DecimalFormat df = new DecimalFormat(format);
		return df.format(d);
	}

	/**
	 * 根据小数位数 截断 原数的小数位 Oct.14,2011,by reifu. 重写处理方法 2012-10-19 wangshuzhen
	 * 避免出现科学计数的形式，改为DecimalFormat实现
	 * 
	 * @param digitStr
	 *            小数
	 * @param digit
	 *            小数位数
	 * @return
	 */
	public static String parseDigit2AtStr(String digitStr, String digit) {
		if (digitStr == null || "".equals(digitStr) || digit.equals("")) {
			return "";
		}

		int scale = Integer.valueOf(digit);
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal ds = new BigDecimal(digitStr);
		BigDecimal one = new BigDecimal("1");
		double d = ds.divide(one, scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		StringBuilder strBuilder = new StringBuilder("##############0");
		if (scale > 0) {
			strBuilder.append(".");
			for (int i = 0; i < scale; i++) {
				strBuilder.append("0");
			}
		}
		String format = strBuilder.toString();
		DecimalFormat df = new DecimalFormat(format);
		return df.format(d);
	}

	/**
	 * 将界面上的小数，转换为string，可存到数据库
	 * 
	 * @param digitStr
	 *            小数
	 * @return
	 */
	public static String parseDigit2AtStr(String digitStr) {
		if (digitStr == null || "".equals(digitStr)) {
			return "";
		}
		// String[] strs = digitStr.split("\\.");
		// if (strs.length == 1) {
		// return strs[0];
		// } else if (strs.length == 2) {
		// if (isAll0(strs[1])) {
		// return strs[0];
		// } else {
		// return deleteLast0(digitStr.replaceAll("\\.", ""));
		// }
		// }
		// return "";
		return digitStr.replaceAll("\\.", "");
	}

	/**
	 * 把数据库中的Decimal的值转换成页面上显示的小数
	 * 
	 * @param valueChar
	 *            例如：25.0
	 * @param atDecimalDigits
	 *            金额小数的位数（比如是某个币种的小数位数）
	 * @return
	 */
	public static String parseAtStr2Digit(String valueChar,
			String atDecimalDigits) {
		// 考虑到科学计数的问题. 2011.12.30
		BigDecimal ds = new BigDecimal(Double.valueOf(valueChar));
		valueChar = String.valueOf(ds);

		String[] strs = valueChar.split("\\.");
		valueChar = strs[0];
		int at = Integer.parseInt(atDecimalDigits);
		if ("0".equals(atDecimalDigits))
			return valueChar;
		if (at == valueChar.length())
			return "0." + valueChar;
		else if (at > valueChar.length()) {
			return new StringBuffer(
					add0(valueChar, at - valueChar.length() + 1))
					.insert(1, ".").toString();

		} else {
			return new StringBuffer(valueChar).insert(valueChar.length() - at,
					".").toString();
		}
	}

	private static String add0(String str, int NumOfZero) {
		String result = str;
		for (int i = 0; i < NumOfZero; i++) {
			result = "0" + result;
		}
		return result;

	}

	public static void main(String[] args) {
		System.out.println(parseAtStr2Digit("0.0", "2"));
		System.out.println(parseDigit2AtStr("250.0"));
		System.out.println(parseDigit2AtStr("0.00", "1"));

	}

}
