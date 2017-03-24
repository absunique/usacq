/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.persistent.Page;
import com.zpayment.cmn.persistent.PersistentService;
import com.zpayment.cmn.persistent.jdbc.adaptor.PreparedSqlCallBackAdaptor;
import com.zpayment.cmn.persistent.jdbc.adaptor.PreparedSqlSetterAdaptor;
import com.zpayment.cmn.persistent.jdbc.annonation.JdbcView;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.persistent.jdbc.support.AbstractBatchStatementFactory;
import com.zpayment.cmn.persistent.jdbc.support.AnnotationResultSetExtractor;
import com.zpayment.cmn.persistent.jdbc.util.MappingStructFactory;

/**
 * 持久化接口-spring JdbcTemplate适配器
 * 
 * @author peiwang
 * @since 2017年3月23日
 */
public class PersistentServiceImplJDBC implements PersistentService {

	private static final Logger log = Logger
			.getLogger(PersistentServiceImplJDBC.class);
	JdbcTemplate jdbcTemplate;

	// abstract public JdbcTemplate getJdbcTemplate();

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// private PreparedSQL buildPSByTblName(String tableName,
	// String[] columnNames, Object[] values) {
	// PreparedSQL ps = new PreparedSQL();
	// StringBuilder sql = new StringBuilder("select * from " + tableName
	// + " where ");
	// for (int i = 0; i < columnNames.length; i++) {
	// sql.append(columnNames[i] + " ");
	//
	// if (i < columnNames.length - 1) {
	// sql.append(" and ");
	// }
	// }
	// }

	@Override
	public void excute(String sql) {
		jdbcTemplate.execute(sql);
	}

	@Override
	public void empty(String tableName) {
		String sql = "truncate table " + tableName;
		excute(sql);
	}

	/**
	 * 根据原生SQL查询，并根据rse映射成各类对象、MAP、LIST等
	 * 
	 * @see JdbcView @see JdbcColumn
	 * 
	 * @since
	 * @param <R>
	 *            结果集
	 * @param sql
	 * @param elementType
	 * @deprecated
	 * @return
	 */
	private <R> R nativeQuery(String sql, ResultSetExtractor<R> rse) {
		return getJdbcTemplate().query(sql, rse);
	}

	/**
	 * 执行原生的SQL
	 * 
	 * @see JdbcView @see JdbcColumn
	 * 
	 * @since
	 * @param <T>
	 * @param sql
	 * @param elementType
	 * @return
	 */
	private void nativeExcute(String sql) {
		// jt.ex
		try {
			getJdbcTemplate().execute(sql);
		} catch (DuplicateKeyException e) {
			throw new BaseException(BaseErrorCode.COMN_DB_RECORD_EXISTED,
					new Object[] { sql }, e);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_DB_SQL_EXCEPTION,
					new Object[] { sql }, e);
		}
	}

	/**
	 * 执行原生的SQL
	 * 
	 * @see JdbcView @see JdbcColumn
	 * 
	 * @since
	 * @param <T>
	 * @param sql
	 * @param elementType
	 * @return
	 */
	private void nativeExcute(PreparedSQL pSql) {
		// jt.ex
		try {
			getJdbcTemplate().execute(pSql.getSql(),
					new PreparedSqlCallBackAdaptor<Integer>(pSql));
		} catch (DuplicateKeyException e) {
			throw new BaseException(BaseErrorCode.COMN_DB_RECORD_EXISTED,
					new Object[] { pSql }, e);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_DB_SQL_EXCEPTION,
					new Object[] { pSql }, e);
		}
	}

	/**
	 * 执行原生的SQL
	 * 
	 * @see JdbcView @see JdbcColumn
	 * 
	 * @since
	 * @param <T>
	 * @param sql
	 * @param elementType
	 * @return
	 */
	private int nativeUpdate(String sql) {
		// jt.ex
		if (log.isDebugEnabled()) {
			log.debug("[" + sql + "]");
		}
		try {
			return getJdbcTemplate().update(sql);
		} catch (DuplicateKeyException e) {
			throw new BaseException(BaseErrorCode.COMN_DB_RECORD_EXISTED,
					new Object[] { sql }, e);
		}
	}

	/**
	 * 获取单个记录
	 * 
	 * @since
	 * @param <T>
	 * @param list
	 * @return
	 */
	private static <T> T singleResult(List<T> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		return list.get(0);
	}

	/**
	 * 按数据库类型构建查询语句
	 * 
	 * @param sql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	private String nativePageQuerySql(String sql, int pageNum, int pageSize) {
		String querySql = sql;

		// if (dbType.equalsIgnoreCase(DbType.DB2)) {
		// int start = (pageNum - 1) * pageSize + 1;
		// int end = start + pageSize - 1;
		//
		// querySql =
		// "select * from (select rownumber() over() as row_num,s.* from (" +
		// sql
		// + ") as s) as t  where row_num between " + start + " and " + end;
		//
		// } else if (dbType.equalsIgnoreCase(DbType.MYSQL)) {
		int start = (pageNum - 1) * pageSize;
		// int end = start + pageSize - 1;
		int end = pageSize;

		querySql = "select * from (" + sql + ") as t limit " + start + ","
				+ end + ";";

		// } else {
		// //
		// log.error("invalid db type: " + dbType);
		// }
		return querySql;
	}

	/**
	 * 按数据库类型构建查询语句
	 * 
	 * @param sql
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	private PreparedSQL nativePageQuerySql(PreparedSQL pSql, int pageNum,
			int pageSize) {
		String querySql = pSql.getSql();

		// if (dbType.equalsIgnoreCase(DbType.DB2)) {
		// int start = (pageNum - 1) * pageSize + 1;
		// int end = start + pageSize - 1;
		//
		// querySql =
		// "select * from (select rownumber() over() as row_num,s.* from (" +
		// sql
		// + ") as s) as t  where row_num between " + start + " and " + end;
		//
		// } else if (dbType.equalsIgnoreCase(DbType.MYSQL)) {
		int start = (pageNum - 1) * pageSize;
		// int end = start + pageSize - 1;
		int end = pageSize;

		querySql = "select * from (" + querySql + ") as t limit " + start + ","
				+ end + ";";
		pSql.setSql(querySql);
		// } else {
		// //
		// log.error("invalid db type: " + dbType);
		// }
		return pSql;
	}

	/**
	 * 最通用的查询，占位符查询
	 * 
	 * @since
	 * @param <T>
	 * @param psql
	 * @param res
	 * @param values
	 * @return
	 */
	public <T> T query(final PreparedSQL pSql, final ResultSetExtractor<T> res) {
		if (log.isDebugEnabled()) {
			log.debug("query psql: " + pSql);
		}
		return getJdbcTemplate().query(pSql.getSql(),
				new PreparedSqlSetterAdaptor(pSql), res);
	}

	public <T> List<T> query(PreparedSQL psql, Class<T> elementType) {
		ResultSetExtractor<List<T>> rse = new AnnotationResultSetExtractor<T>(
				elementType);
		return query(psql, rse);
	}

	/*-------基于语句查询-------*/
	public <T> List<T> queryAll(RowMapper<T> rowMapper, String tblName) {
		PreparedSQL pSql = new PreparedSQL();
		pSql.setSql(String.format("select * from (%s)", tblName));
		return query(pSql, new RowMapperResultSetExtractor<T>(rowMapper));
	}

	@Override
	//TODO 待完善
	public <T> List<T> queryByColumns(RowMapper<T> rowMapper, String tblName,
			String[] columnNames, Object[] values) throws BaseException {
		ResultSetExtractor<List<T>> rse = new RowMapperResultSetExtractor<T>(
				rowMapper);
		PreparedSQL pSql = new PreparedSQL();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from (").append(tblName).append(")");
		return query(pSql, rse);
	}

	/*-------基于对象查询-------*/

	@Override
	@SuppressWarnings("unchecked")
	public <T> T queryOne(T entity, String tblName) throws BaseException {
		PreparedSQL pSql = MappingStructFactory.create(entity).buildQueryPSQL(
				entity, tblName);
		List<T> list = (List<T>) query(pSql, entity.getClass());
		return singleResult(list);
	}

	@Override
	public <T> List<T> queryAll(Class<T> clazz, String tblName) {
		PreparedSQL ntvSql = MappingStructFactory.create(clazz)
				.buildQueryAllPSSQL(tblName);
		return query(ntvSql, clazz);
	}

	@Override
	public <T> T querySingleByColumns(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values) throws BaseException {
		return singleResult(queryByColumns(clazz, tblName, columnNames, values));
	}

	@Override
	public <T> List<T> queryByColumns(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values) throws BaseException {
		PreparedSQL ps = MappingStructFactory.create(clazz)
				.buildQueryByColumnsPSQL(tblName, columnNames, values);
		List<T> list = (List<T>) query(ps, clazz);
		return list;
	}

	@Override
	public <T> Page<T> queryByPageByColumns(Page<T> pg, Class<T> clazz,
			String tblName, String[] columnNames, Object[] values) {
		PreparedSQL pSql = MappingStructFactory.create(clazz)
				.buildQueryByColumnsPSQL(tblName, columnNames, values);
		initPage(pg, pSql);

		return this.innerQueryPage(pg, pSql, clazz);
	}

	@Override
	public <T> Page<T> queryAllByPage(Page<T> pg, Class<T> clazz, String tblName) {
		PreparedSQL pSql = MappingStructFactory.create(clazz)
				.buildQueryAllPSSQL(tblName);
		initPage(pg, pSql);
		return this.innerQueryPage(pg, pSql, clazz);
	}

	/**
	 * 查询某整数列的第一个值，常用于count
	 * 
	 * @since
	 * @param sql
	 * @return
	 */
	public Integer queryForInt(String sql) {
		return getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	/**
	 * 查询某整数列的第一个值，常用于count
	 * 
	 * @since
	 * @param sql
	 * @return
	 */
	public Integer queryForInt(PreparedSQL pSql) {
		List<Integer> result = getJdbcTemplate().query(pSql.getCountSql(),
				new PreparedSqlSetterAdaptor(pSql),
				new SingleColumnRowMapper<Integer>(Integer.class));
		return singleResult(result);
	}

	@Override
	public <T> SqlRowSet queryRowSet(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values) {
		PreparedSQL sql = MappingStructFactory.create(clazz)
				.buildQueryByColumnsPSQL(tblName, columnNames, values);
		return query(sql, new SqlRowSetResultSetExtractor());
	}

	@Override
	public <T> List<T> queryUsingResultSet(String sql,
			ResultSetExtractor<List<T>> rse) {
		return getJdbcTemplate().query(sql, rse);
	}

	/**
	 * 初始化页信息
	 * 
	 * @since
	 * @param pageNum
	 * @param pageSize
	 * @param countSql
	 * @return
	 */
	private <T> void initPage(Page<T> page, String countSql) {
		// Page page = new Page().setPageSize(pageSize).setCurrentPage(pageNum);
		if (!page.isFirst()) {
			return;
		}
		Integer count = queryForInt(countSql);
		if (count == null || count == 0) {
			return;
			// return page.setTotalPage(0).setTotalCount(0);
		}
		int totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
		page.setTotalPage(totalPage).setTotalCount(count);
		page.setFirst(false);
		// return page;
	}

	/**
	 * 初始化页信息
	 * 
	 * @since
	 * @param pageNum
	 * @param pageSize
	 * @param countSql
	 * @return
	 */
	private <T> void initPage(Page<T> page, PreparedSQL pSql) {
		// Page page = new Page().setPageSize(pageSize).setCurrentPage(pageNum);
		if (!page.isFirst()) {
			return;
		}
		Integer count = queryForInt(pSql);
		if (count == null || count == 0) {
			return;
			// return page.setTotalPage(0).setTotalCount(0);
		}
		int totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
		page.setTotalPage(totalPage).setTotalCount(count);
		page.setFirst(false);
		// return page;
	}

	/**
	 * 
	 * @param pg
	 * @param sql
	 * @param clazz
	 * @deprecated
	 * @return
	 */
	private <T> Page<T> innerQueryPage(Page<T> pg, String sql, Class<T> clazz) {
		ResultSetExtractor<List<T>> rse = new AnnotationResultSetExtractor<T>(
				clazz);
		int pageNum = pg.getCurrentPage();
		int pageSize = pg.getPageSize();
		if (pg.getTotalCount() == 0 || pageNum > pg.getTotalPage()) {
			pg.setResults(new LinkedList<T>());
		} else {
			String querySql = nativePageQuerySql(sql, pageNum, pageSize);
			List<T> results = nativeQuery(querySql, rse);
			pg.setResults(results);
		}
		return pg;
	}

	private <T> Page<T> innerQueryPage(Page<T> pg, PreparedSQL pSql,
			Class<T> clazz) {
		ResultSetExtractor<List<T>> rse = new AnnotationResultSetExtractor<T>(
				clazz);
		int pageNum = pg.getCurrentPage();
		int pageSize = pg.getPageSize();
		if (pg.getTotalCount() == 0 || pageNum > pg.getTotalPage()) {
			pg.setResults(new LinkedList<T>());
		} else {
			pSql = nativePageQuerySql(pSql, pageNum, pageSize);
			List<T> results = query(pSql, rse);
			pg.setResults(results);
		}
		return pg;
	}

	@Override
	public int updateSql(String sql) {
		// TODO Auto-generated method stub
		return nativeUpdate(sql);
	}

	/**
	 * 按主键更新指定列
	 * 
	 * @see com.cup.ibscmn.persistent.PersistentService#updateByPriKey(java.lang.Class,
	 *      java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	// @Override
	// public int updateByPriKey(Object entity, String tblName,
	// String[] updateColumns, Object[] updateValues) {
	// // TODO Auto-generated method stub
	// String sql = MappingStructFactory.create(entity)
	// .buildUpdateByColumnsSQL(tblName, updateColumns, updateValues);
	// return nativeUpdate(sql);
	// // return 1;
	// }

	/**
	 * 指定列更新
	 * 
	 * @since
	 * @param clazz
	 * @param table
	 * @param updateColumns
	 * @param updateValues
	 * @param conditionColumns
	 * @param conditionValues
	 * @return
	 */
	@Override
	public int update(Class<?> clazz, String tblName, String[] updateColumns,
			Object[] updateValues, String[] conditionColumns,
			Object[] conditionValues) {
		String sql = MappingStructFactory.create(clazz)
				.buildUpdateByColumnsSQL(tblName, updateColumns, updateValues,
						conditionColumns, conditionValues);
		return nativeUpdate(sql);
	}

	@Override
	public int update(Class<?> clazz, String[] updateColumns,
			Object[] updateValues, String[] conditionColumns,
			Object[] conditionValues) {
		return this.update(clazz, null, updateColumns, updateValues,
				conditionColumns, conditionValues);

	}

	@Override
	public <C, R> List<R> batchExcute(
			AbstractBatchStatementFactory<C, R> absFactory) {
		return getJdbcTemplate().execute(absFactory, absFactory);
	}

	@Override
	public <T> void save(T val, String tblName) {
		PreparedSQL sql = MappingStructFactory.create(val).buildInsertPSQL(val,
				tblName);
		nativeExcute(sql);
	}

	@Override
	public <T> void save(T val) throws BaseException {
		// TODO Auto-generated method stub
		save(val, null);
	}

	@Override
	public <T> void merge(T val, String tblName) throws BaseException {
		String ntvSql = MappingStructFactory.create(val).buildUpdateSQL(val,
				tblName);
		nativeExcute(ntvSql);
	}

	@Override
	public <T> void merge(T val) throws BaseException {
		// TODO Auto-generated method stub
		merge(val, null);
	}
}
