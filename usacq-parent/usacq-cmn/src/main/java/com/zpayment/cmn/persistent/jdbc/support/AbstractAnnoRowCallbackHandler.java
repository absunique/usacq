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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

abstract public class AbstractAnnoRowCallbackHandler<T> implements
		RowCallbackHandler {

	private Class<T> elementType;

	public AbstractAnnoRowCallbackHandler(Class<T> elementType) {
		this.elementType = elementType;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		T entity = new AnnotationRowMapper<T>(elementType).mapRow(rs, 1);
		processEntity(entity);
	}

	protected abstract void processEntity(T entity);
}
