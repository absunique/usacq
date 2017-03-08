/*
 * 
 * Copyright 2013, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: ErrorMessage.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2013-12-31 - Create By peiwang
 */

package com.peiwang.usacq.cmn.exp;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.peiwang.usacq.cmn.Config;
import com.peiwang.usacq.cmn.log.Logger;
import com.peiwang.usacq.cmn.util.StringUtils;

/**
 * 错误消息
 * 
 * @author peiwang
 * 
 * @version
 * @since
 * 
 */
public final class ErrorMessage {

	private static Logger log = Logger.getLogger(ErrorMessage.class);

	/**
	 * 配置文件中错误消息文件的分隔符
	 */
	private static final String ERROR_MESSAGE_FILE_SEPREATOR = ",";

	/**
	 * 错误消息文件列表
	 */
	private List<ResourceBundle> messages = new LinkedList<ResourceBundle>();

	/**
	 * 获取错误信息
	 * 
	 * @since
	 * @param errorCode
	 * @param args
	 * @return
	 */
	public static String formatMessage(String errorCode, Object... args) {
		return LazyErrorCodeUtils.instance.getErrorMessage(errorCode, args);
	}

	/**
	 * 格式化错误信息
	 * 
	 * @since
	 * @param errorCode
	 * @param args
	 * @return
	 */
	private String getErrorMessage(String errorCode, Object... args) {
		String messageFormatStr = getFormat(errorCode);
		if (messageFormatStr == null) {
			return "ErrorCode-" + errorCode;
		}

		if (args == null || args.length == 0) {
			return messageFormatStr;
		}

		MessageFormat mf = new MessageFormat(messageFormatStr);
		return mf.format(args);
	}

	/**
	 * 获取错误码原始格式化消息
	 * 
	 * @since
	 * @param errorCode
	 * @return
	 */
	private String getFormat(String errorCode) {
		for (ResourceBundle message : messages) {
			String msg = null;
			try {
				msg = message.getString(errorCode + "");
			} catch (MissingResourceException e) {
			}

			if (msg != null) {
				return errorCode + "-" + msg;
			}
		}
		return null;
	}

	/**
	 * 初始化加载各错误文件
	 */
	private ErrorMessage() {
		String errorFileStr = null;
		try {
			errorFileStr = Config.getErrorMessageFile();
			if (StringUtils.isEmpty(errorFileStr)) {
				return;
			}

			String[] ary = StringUtils.split(errorFileStr,
					ERROR_MESSAGE_FILE_SEPREATOR);
			for (String msgFile : ary) {
				ResourceBundle message = ResourceBundle.getBundle(msgFile);
				messages.add(message);
			}
		} catch (Exception e) {
			log.error("load error message failed: " + errorFileStr, e);
		}
	}

	private static class LazyErrorCodeUtils {

		private static ErrorMessage instance = new ErrorMessage();
	}
}
