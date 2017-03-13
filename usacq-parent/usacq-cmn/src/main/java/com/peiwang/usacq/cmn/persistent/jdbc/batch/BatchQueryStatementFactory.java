package com.peiwang.usacq.cmn.persistent.jdbc.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.peiwang.usacq.cmn.persistent.jdbc.support.AbstractBatchStatementFactory;
import com.peiwang.usacq.cmn.persistent.jdbc.util.MappingStruct;
import com.peiwang.usacq.cmn.persistent.jdbc.util.MappingStructFactory;

/**
 * 批量执行之批量查询
 * 
 * @author wangpei
 * 
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
