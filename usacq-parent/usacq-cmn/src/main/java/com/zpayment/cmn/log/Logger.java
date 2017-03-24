/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.log;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.LoggerFactory;

/**
 * 基于slf4j封装
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public final class Logger {

	private static final Locale DEFAULT_LOCALE = Locale.getDefault();

	private static ResourceBundle resource;

	/** 日志缓存 */
	private static Map<String, Logger> logMap = new ConcurrentHashMap<String, Logger>();

	/** SLF4J 日志 */
	private org.slf4j.Logger log;

	/**
	 * 获取类的日志记录
	 * 
	 * @since
	 * @param clazz
	 * @return
	 */
	public static Logger getLogger(Class<?> clazz) {
		String name = clazz.getName();
		return getLogger(name);
	}

	/**
	 * 获取指定名称的日志记录
	 * 
	 * @since
	 * @param name
	 * @return
	 */
	public static Logger getLogger(String name) {
		Logger logger = logMap.get(name);
		if (logger == null) {
			logger = new Logger(name);
			logMap.put(name, logger);
		}
		return logger;
	}

	public void trace(String message, Object... params) {
		doFileLog(Level.TRACE, null, message, params);
	}

	public void debug(String message, Object... params) {
		doFileLog(Level.DEBUG, null, message, params);
	}

	public void info(String message, Object... params) {
		doFileLog(Level.INFO, null, message, params);
	}

	public void warn(String message) {
		warn(message, null, null);
	}

	public void warn(String message, Object[] params) {
		warn(message, params, null);
	}

	public void warn(Throwable e) {
		warn(null, null, e);
	}

	public void warn(String message, Throwable e) {
		warn(message, null, e);
	}

	public void warn(String message, Object[] params, Throwable e) {
		doFileLog(Level.WARN, e, message, params);
	}

	public void error(String message) {
		error(message, null, null);
	}

	public void error(String message, Object[] params) {
		error(message, params, null);
	}

	public void error(Throwable e) {
		error(null, null, e);
	}

	public void error(String message, Throwable e) {
		error(message, null, e);
	}

	public void error(String message, Object[] params, Throwable e) {
		doFileLog(Level.ERROR, e, message, params);
	}

	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	public boolean isWarnEnabled() {
		return log.isInfoEnabled();
	}

	public boolean isErrorEnabled() {
		return log.isInfoEnabled();
	}

	/**
	 * 记录日志到文件
	 * 
	 * @since
	 * @param t
	 * @param level
	 * @param message
	 * @param params
	 */
	public void doFileLog(Level level, Throwable t, String message,
			Object... params) {
		if (!level.isLoggerEnable(this.log)) {
			return;
		}

		String msg = null;
		if (params != null && params.length > 0) {
			try {
				msg = String.format(message, params);
			} catch (Exception e) {
				this.error("invalid log message format: " + message);
				msg = message;
			}
		} else {
			msg = message;
		}
		switch (level) {
		case ERROR:
			log.error(msg, t);
			break;
		case WARN:
			log.warn(msg, t);
			break;
		case INFO:
			log.info(msg);
			break;
		case DEBUG:
			log.debug(msg);
			break;
		case TRACE:
			log.trace(msg);
			break;
		}
	}

	private Logger(Class<?> clazz) {
		this.log = LoggerFactory.getLogger(clazz);
	}

	private Logger(String name) {
		this.log = LoggerFactory.getLogger(name);
	}

	/**
	 * 获取错误码对应的错误信息
	 * 
	 * @since
	 * @param errorCode
	 * @param params
	 * @return
	 */
	public String formatMessage(int errorCode, Object... params) {
		String message = null;
		try {
			message = getResource().getString("" + errorCode);

			if (params == null || params.length == 0) {
				return message;
			}

			MessageFormat mf = new MessageFormat(message, DEFAULT_LOCALE);
			return mf.format(params);
		} catch (Exception e) {
			this.error(String
					.format("formatMessage(errorCode[%s], params) failed, message[%s])",
							errorCode, message));

			if (message != null) {
				return message;
			} else {
				return String.format("Unkown ErrorCode[%s]", errorCode);
			}
		}
	}

	private static synchronized ResourceBundle getResource() {
		if (resource != null) {
			return resource;
		}

		resource = ResourceBundle.getBundle("errormsg", DEFAULT_LOCALE);
		return resource;
	}
}