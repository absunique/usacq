/*
 * 
 *  Copyright 2012, $${COMPANY} Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF $${COMPANY} CO., LTD.
 *  
 *   $Id: ExceptionUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 *
 *  Function:
 *
 *    异常工具类，用于统一以异常
 *
 *    
 *    
 */

package com.zpayment.cmn.exp;

import javax.ejb.EJBException;

import com.zpayment.cmn.log.Logger;

/**
 * 异常工具类，用于解析异常
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class ExceptionUtils {
	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(ExceptionUtils.class);

	/**
	 * 解析异常
	 * 
	 * @since
	 * @param e
	 * @return
	 */
	public static BaseException parse(Throwable e) {
		if (e instanceof BaseException) {
			return (BaseException) e;
		}

		if (e instanceof EJBException) {
			Exception ex = ((EJBException) e).getCausedByException();
			return parse(ex);
		}

		log.error("unkown exception", e);
		return new BaseException(BaseErrorCode.COMN_UNKOWN_EXCEPTION, e);
	}

}
