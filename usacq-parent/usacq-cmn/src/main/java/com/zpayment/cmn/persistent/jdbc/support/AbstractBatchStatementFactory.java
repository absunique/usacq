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

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;

public abstract class AbstractBatchStatementFactory<C, R> implements
		PreparedStatementCreator, PreparedStatementCallback<List<R>> {

	protected Class<C> entityClass;
	protected String tableName;
	protected List<Integer> callBackResult;

	public AbstractBatchStatementFactory() {
		callBackResult = new ArrayList<Integer>();
	}

	public void setEntityClass(Class<C> entityClass) {
		this.entityClass = entityClass;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
