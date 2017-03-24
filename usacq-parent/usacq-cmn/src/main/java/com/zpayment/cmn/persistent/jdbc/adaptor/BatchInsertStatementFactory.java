///*
// * 
// * Copyright 2017, ZPayment Co., Ltd. All right reserved.
// * 
// * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
// * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
// * PERMISSION OF ZPayment CO., LTD.
// * 
// * 2017年3月23日 - Create By peiwang
// */
//package com.zpayment.cmn.persistent.jdbc.adaptor;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.Iterator;
//import java.util.List;
//
//import org.springframework.dao.DataAccessException;
//
//import com.zpayment.cmn.exp.BaseErrorCode;
//import com.zpayment.cmn.exp.BaseException;
//import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
//import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
//import com.zpayment.cmn.persistent.jdbc.support.AbstractBatchStatementFactory;
//import com.zpayment.cmn.persistent.jdbc.util.MappingStructFactory;
//import com.zpayment.cmn.util.ArrayUtils;
//
///**
// * 
// * @author peiwang
// * @since 2017年3月24日
// * @param <C>
// */
//public class BatchInsertStatementFactory<C> extends
//		AbstractBatchStatementFactory<C, Integer> {
//
//	private int batchCount;
//	private PreparedStatement ps;
//	private int count = 0;
//	private BatchInsertPrepare<C> bip;
//
//	// private PreparedSQL pSql;
//
//	public BatchInsertStatementFactory(BatchInsertPrepare<C> bip) {
//		super();
//		this.batchCount = bip.getBatchCount() < 0 ? 100 : bip.getBatchCount();
//		this.bip = bip;
//	}
//
//	@Override
//	public PreparedStatement createPreparedStatement(Connection con)
//			throws SQLException {
//		C demoObj;
//		try {
//			demoObj = entityClass.newInstance();
//			PreparedSQL pSql = MappingStructFactory.create(entityClass)
//					.buildInsertPSQL(demoObj, tableName);
//			String sql = pSql.getSql();
//			return con.prepareStatement(sql);
//		} catch (Exception e) {
//			throw new BaseException(BaseErrorCode.FAIL, e);
//		}
//	}
//
//	@Override
//	public List<Integer> doInPreparedStatement(PreparedStatement ps)
//			throws SQLException, DataAccessException {
//		this.ps = ps;
//		Iterator<C> it = bip.getIterator();
//		while (it.hasNext()) {
//			addEntity(it.next());
//		}
//		if (count != 0) {
//			count = 0;
//			int[] tmp = ps.executeBatch();
//			callBackResult.addAll(ArrayUtils.toIntList(tmp));
//		}
//		return callBackResult;
//	}
//
//	/**
//	 * 批处理时，添加单条记录
//	 * 
//	 * @param entity
//	 */
//	protected void addEntity(C entity) {
//		PreparedSQL pSql = MappingStructFactory.create(entityClass)
//				.buildInsertPSQL(entity, tableName);
//		try {
//			int parameterIndex = 1;
//			for (SqlParam sp : pSql.getParams()) {
//				sp.setPreparedParamValue(parameterIndex++, ps);
//			}
//			ps.addBatch();
//			count++;
//			if (count >= batchCount) {
//				count = 0;
//				int[] tmp = ps.executeBatch();
//				callBackResult.addAll(ArrayUtils.toIntList(tmp));
//			}
//		} catch (SQLException e) {
//			throw new BaseException(BaseErrorCode.FAIL, e);
//		}
//
//	}
//
//}
