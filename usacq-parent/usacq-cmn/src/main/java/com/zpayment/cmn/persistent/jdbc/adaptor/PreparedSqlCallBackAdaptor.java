/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月23日 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc.adaptor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;

/**
 * spring jdbcTemplate PreparedStatementCreator适配
 * 用于：
 * 插入（批量插入）
 * 更新
 * 删除
 * 
 * @author peiwang
 * @param <T>
 * @since 2017年3月23日
 */
public class PreparedSqlCallBackAdaptor<T> implements
		PreparedStatementCallback<T> {

	private PreparedSQL pSql;

	public PreparedSqlCallBackAdaptor(PreparedSQL pSql) {
		this.pSql = pSql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.jdbc.core.PreparedStatementCallback#doInPreparedStatement
	 * (java.sql.PreparedStatement)
	 */
	@Override
	public T doInPreparedStatement(PreparedStatement ps) throws SQLException,
			DataAccessException {
		pSql.setValues(ps);
		ps.executeUpdate();
		return null;
	}

}
