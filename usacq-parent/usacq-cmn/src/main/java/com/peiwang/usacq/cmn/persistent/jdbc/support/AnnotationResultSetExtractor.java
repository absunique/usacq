/*
 * 
 *  Copyright 2012, China UnionPay Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF CHINA UNIONPAY CO., LTD.
 *  
 *   $Id: AnnotationResultSetExtractor.java,v 1.1 2016/08/04 23:15:22 peiwang Exp $
 *
 *  Function:
 *
 *    //TODO 请添加功能描述
 *
 *  Edit History:
 *
 *     2012-11-23 - Create By szwang
 *    
 *    
 */

package com.peiwang.usacq.cmn.persistent.jdbc.support;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;

/**
 * 字段转换成
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public class AnnotationResultSetExtractor<T> extends
		RowMapperResultSetExtractor<T> {

	public AnnotationResultSetExtractor(Class<T> elementType) {
		super(new AnnotationRowMapper<T>(elementType));
	}
}
