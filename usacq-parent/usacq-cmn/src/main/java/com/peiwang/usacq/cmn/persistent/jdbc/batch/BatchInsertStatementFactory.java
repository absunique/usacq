package com.peiwang.usacq.cmn.persistent.jdbc.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.peiwang.usacq.cmn.exp.BaseErrorCode;
import com.peiwang.usacq.cmn.exp.BaseException;
import com.peiwang.usacq.cmn.persistent.jdbc.param.PreparedSQL;
import com.peiwang.usacq.cmn.persistent.jdbc.support.AbstractBatchStatementFactory;
import com.peiwang.usacq.cmn.persistent.jdbc.util.MappingStructFactory;
import com.peiwang.usacq.cmn.util.ArrayUtils;

/**
 * 批量执行之批量插入
 * 
 * @author wangpei
 * 
 * @param <C>
 */
public class BatchInsertStatementFactory<C> extends
		AbstractBatchStatementFactory<C, Integer> {

	private int batchCount;
	private PreparedStatement ps;
	private int count = 0;
	private BatchInsertPrepare<C> bip;

	// private PreparedSQL pSql;

	public BatchInsertStatementFactory(BatchInsertPrepare<C> bip) {
		super();
		this.batchCount = bip.getBatchCount() < 0 ? 100 : bip.getBatchCount();
		this.bip = bip;
	}

	@Override
	public PreparedStatement createPreparedStatement(Connection con)
			throws SQLException {
		C demoObj;
		try {
			demoObj = entityClass.newInstance();
			PreparedSQL pSql = MappingStructFactory.create(entityClass)
					.buildInsertPSQL(demoObj, tableName);
			String sql = pSql.getSql();
			return con.prepareStatement(sql);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.FAIL, e);
		}
	}

	@Override
	public List<Integer> doInPreparedStatement(PreparedStatement ps)
			throws SQLException, DataAccessException {
		this.ps = ps;
		Iterator<C> it = bip.getIterator();
		while (it.hasNext()) {
			addEntity(it.next());
		}
		if (count != 0) {
			count = 0;
			int[] tmp = ps.executeBatch();
			callBackResult.addAll(ArrayUtils.toIntList(tmp));
		}
		return callBackResult;
	}

	/**
	 * 批处理时，添加单条记录
	 * 
	 * @param entity
	 */
	protected void addEntity(C entity) {
		PreparedSQL pSql = MappingStructFactory.create(entityClass)
				.buildInsertPSQL(entity, tableName);
		try {
			pSql.setValues(ps);
			ps.addBatch();
			count++;
			if (count >= batchCount) {
				count = 0;
				int[] tmp = ps.executeBatch();
				callBackResult.addAll(ArrayUtils.toIntList(tmp));
			}
		} catch (SQLException e) {
			throw new BaseException(BaseErrorCode.FAIL, e);
		}

	}

}
