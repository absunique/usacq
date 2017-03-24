/*
 * 
 * Copyright 2012, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: Const.java,v 1.1 2016/08/30 07:28:19 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2012-11-22 - Create By peiwang
 */
package com.zpayment.cmn.persistent;

import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.zpayment.cmn.persistent.jdbc.support.AbstractBatchStatementFactory;

/**
 * 持久层基础接口，DAO基于此封装。
 * 
 * @author peiwang
 * @since 2017年3月13日
 * 
 */
public interface PersistentService {

	@Deprecated
	/**
	 * 直接执行sql,防止SQL注入，不建议使用
	 */
	public void excute(String sql);

	/**
	 * 清空表
	 * 
	 * @param tableName
	 */
	public void empty(String tableName);

	/*---------------基于非对象查询-----------*/
	public <T> List<T> queryAll(RowMapper<T> rowMapper, String tblName);

	public <T> List<T> queryByColumns(RowMapper<T> rowMapper, String tblName,
			String[] columnNames, Object[] values);

	public <T> List<T> queryUsingResultSet(String sql,
			ResultSetExtractor<List<T>> rse);

	public <C, R> List<R> batchExcute(
			AbstractBatchStatementFactory<C, R> absFactory);

	/*---------------基于对象查询-----------*/

	public <T> T queryOne(T entity, String tblName);// 查单个

	public <T> List<T> queryAll(Class<T> clazz, String tblName);// 查所有

	public <T> T querySingleByColumns(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values);

	public <T> List<T> queryByColumns(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values);

	public <T> Page<T> queryByPageByColumns(Page<T> pg, Class<T> clazz,
			String tblName, String[] columnNames, Object[] values);

	public <T> Page<T> queryAllByPage(Page<T> pg, Class<T> clazz, String tblName);

	public <T> SqlRowSet queryRowSet(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values);

	/*------基于对象插入-------*/
	public <T> void save(T val, String tblName);

	public <T> void save(T val);

	/*------基于对象更新-------*/
	public <T> void merge(T val, String tblName);

	public <T> void merge(T val);

	public int update(Class<?> clazz, String tblName, String[] updateColumns,
			Object[] updateValues, String[] conditionColumns,
			Object[] conditionValues);

	public int update(Class<?> clazz, String[] updateColumns,
			Object[] updateValues, String[] conditionColumns,
			Object[] conditionValues);

	@Deprecated
	public int updateSql(String sql);

}
