/*
 * 
 * Copyright 2012, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: MessageUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * 错误码解析工具
 * 
 * Edit History:
 * 
 * 2012-12-21 - Create By CUPPC
 */

package com.peiwang.usacq.cmn.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 错误码解析工具
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class MessageUtils {

	private static ResourceBundle myResources = ResourceBundle
			.getBundle("message/apperrmessage");

	/**
	 * 生成debug信息
	 * 
	 * @since
	 * @param position
	 * @param funcName
	 * @return
	 */
	public static String genDebugMsg(String position, String funcName) {
		String debugMsg1 = "{0} {1}!";
		return MessageFormat.format(debugMsg1, position, funcName);
	}

	/**
	 * 根据错误代码，取出其对应的错误信息。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return 错误代码对应的字符串
	 */
	public static String getMessageString(String errorCode) {
		return "ErrorCode[" + errorCode + "]"
				+ myResources.getString(errorCode);
	}

	/**
	 * 根据错误代码，取出其对应的本地错误信息。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param locale
	 *            本地信息
	 * @return 错误代码对应的字符串
	 */
	public static String getMessageString(String errorCode, Locale locale) {
		ResourceBundle myLocaleRes = ResourceBundle.getBundle(
				"ErrorMsgForParaResources", locale);
		return "ErrorCode[" + errorCode + "]"
				+ myLocaleRes.getString(errorCode);
	}

	/**
	 * 根据错误代码，取出错误信息并将其格式化为字符串。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return 格式化后的字符串
	 */
	public static String formatMessage(String errorCode) {
		MessageFormat mf = new MessageFormat(getMessageString(errorCode));
		return mf.format(new Object[0]);
	}

	/**
	 * 根据错误代码，取出错误信息并将其格式化为字符串。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return 格式化后的字符串
	 */
	public static String formatMessage(String errorCode, Object arg0) {
		MessageFormat mf = new MessageFormat(getMessageString(errorCode));
		Object[] args = new Object[1];
		args[0] = arg0;
		return mf.format(args);
	}

	/**
	 * 根据错误代码，取出错误信息并将其格式化为字符串。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return 格式化后的字符串
	 */
	public static String formatMessage(String errorCode, Object... args) {
		MessageFormat mf = new MessageFormat(getMessageString(errorCode));
		return mf.format(args);
	}

	/**
	 * 根据错误代码，取出错误信息并将其格式化为本地化字符串。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param locale
	 *            本地化信息
	 * @return 格式化后的字符串
	 */
	public static String formatMessage(String errorCode, Locale locale) {
		MessageFormat mf = new MessageFormat(
				getMessageString(errorCode, locale));
		return mf.format(new Object[0]);
	}

	/**
	 * 根据错误代码，取出错误信息并将其格式化为字符串。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return 格式化后的字符串
	 */
	public static String formatMessage(String errorCode, Object arg0,
			Locale locale) {
		MessageFormat mf = new MessageFormat(
				getMessageString(errorCode, locale));
		Object[] args = new Object[1];
		args[0] = arg0;
		return mf.format(args);
	}

	/**
	 * 根据错误代码，取出错误信息并将其格式化为字符串。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return 格式化后的字符串
	 */
	public static String formatMessage(String errorCode, Object arg0,
			Object arg1, Locale locale) {
		MessageFormat mf = new MessageFormat(
				getMessageString(errorCode, locale));
		Object[] args = new Object[2];
		args[0] = arg0;
		args[1] = arg1;
		return mf.format(args);
	}

	/**
	 * 根据错误代码，取出错误信息并将其格式化为字符串。
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return 格式化后的字符串
	 */
	public static String formatMessage(String errorCode, Object[] args,
			Locale locale) {
		MessageFormat mf = new MessageFormat(
				getMessageString(errorCode, locale));
		return mf.format(args);
	}
}
