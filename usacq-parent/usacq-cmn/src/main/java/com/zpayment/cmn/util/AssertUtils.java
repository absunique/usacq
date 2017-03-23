/*
 * 
 *  Copyright 2013, $${COMPANY} Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF $${COMPANY} CO., LTD.
 *  
 *   $Id: AssertUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 *
 *  Function:
 *
 *    //TODO 请添加功能描述
 *
 *  Edit History:
 *
 *     2013-1-5 - Create By szwang
 *    
 *    
 */

package com.zpayment.cmn.util;

import java.lang.reflect.Array;
import java.util.Collection;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;

/**
 * 判定并抛出异常
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class AssertUtils {

	/**
	 * 判断是否为null
	 * 
	 * @since
	 * @param obj
	 */
	public static void notNull(Object obj) {
		if (obj == null) {
			throw new BaseException(BaseErrorCode.COMN_NULL_EXCEPTION);
		}
	}

	/**
	 * 判断是否为空
	 * 
	 * @since
	 * @param obj
	 */
	public static void notEmpty(Object obj) {
		if (isEmpty(obj)) {
			throw new BaseException(BaseErrorCode.COMN_NULL_EXCEPTION);
		}
	}

	/**
	 * 判断是否为空
	 * 
	 * @since
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj instanceof String) {
			return StringUtils.isEmpty((String) obj);
		} else if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) <= 0;
		} else {
			return false;
		}
	}
}
