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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.zpayment.cmn.persistent.jdbc.support.AbstractBatchStatementFactory;
import com.zpayment.cmn.persistent.jdbc.util.MappingStruct;
import com.zpayment.cmn.persistent.jdbc.util.MappingStructFactory;

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 * @param <C>
 */
public class BatchQueryStatementFactory<C> extends
		AbstractBatchStatementFactory<C, Integer> {

	private MappingStruct ms;
	private BatchQueryGenSql bqgs;
	private BatchQueryProcRec<C> bqpr;

	public BatchQueryStatementFactory(BatchQueryGenSql bqgs,
			BatchQueryProcRec<C> bqpr) {
		this.bqgs = bqgs;
		this.bqpr = bqpr;
	}

	@Override
	public PreparedStatement createPreparedStatement(Connection con)
			throws SQLException {
		ms = MappingStructFactory.create(entityClass);
		String sql = bqgs.getQuerySql(ms, tableName);
		return con.prepareStatement(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> doInPreparedStatement(PreparedStatement ps)
			throws SQLException, DataAccessException {
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			C entity = (C) ms.toObject(rs);
			callBackResult.add(bqpr.procOneRec(entity));
		}
		rs.close();
		return callBackResult;
	}

}
