/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc.support;

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
