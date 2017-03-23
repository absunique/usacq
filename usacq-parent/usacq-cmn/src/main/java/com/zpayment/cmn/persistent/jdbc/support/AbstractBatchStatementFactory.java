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
