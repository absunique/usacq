/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.exp;

/**
 * 管理异常基类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 异常码
	 */
	private String errorCode;

	/**
	 * 异常内容
	 */
	private Object[] args;

	protected BaseException() {

	}

	public BaseException(String errorCode) {
		this(errorCode, null, null);
	}

	public BaseException(String errorCode, Object[] args) {
		this(errorCode, args, null);
	}

	public BaseException(String errorCode, Throwable cause) {
		this(errorCode, null, cause);
	}

	public BaseException(String errorCode, Object[] args, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.args = args;
	}

	public String getMessage() {
		return ErrorMessage.formatMessage(errorCode, args);
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}

}
