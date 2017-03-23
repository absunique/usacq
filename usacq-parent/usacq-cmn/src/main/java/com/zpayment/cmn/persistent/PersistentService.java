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
import org.springframework.jdbc.core.RowCallbackHandler;
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

	/*---------------基于非对象操作-----------*/
	public <T> List<T> queryByColumns(RowMapper<T> rowMapper, String tblName,
			String[] columnNames, Object[] values);

	public <T> List<T> queryUsingResultSet(String sql,
			ResultSetExtractor<List<T>> rse);

	public <C, R> List<R> batchExcute(
			AbstractBatchStatementFactory<C, R> absFactory);

	/*---------------基于对象操作-----------*/
	/**
	 * 直接缓存所有查询结果并映射平面对象。在数据量小时使用
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public <T> List<T> query(String sql, Class<T> clazz);

	/**
	 * 按类名或表名查询所有结果
	 * 
	 * @param tblName
	 * @param clazz
	 * @return
	 */
	public <T> List<T> queryAll(String tblName, Class<T> clazz);

	/**
	 * 按类名、或表名，根据输入条件查询所有记录
	 * 
	 * @param clazz
	 * @param tblName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public <T> List<T> queryByColumns(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values);

	/**
	 * 按类名、或表名，根据输入条件查询单条记录
	 * 
	 * @param clazz
	 * @param tblName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public <T> T querySingleByColumns(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values);

	/**
	 * spring SqlRowSet也会缓存所有结果，谨慎使用于少量数据的游标遍历
	 * 
	 * @param sql
	 * @return
	 */
	public SqlRowSet queryRowSet(String sql);

	/**
	 * 根据输入条件，查询游标
	 * 
	 * @param clazz
	 * @param tblName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public <T> SqlRowSet queryRowSet(Class<T> clazz, String tblName,
			String[] columnNames, Object[] values);

	public void queryUsingCallback(String sql, RowCallbackHandler rch);

	public <T> T get(T entity, String tblName);

	public <T> T get(T entity);

	public <T> void save(T val, String tblName);

	public <T> void save(T val);

	/***
	 * 更新相关操作
	 */
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

	/**
	 * 分页相关操作
	 * 
	 */
	public <T> Page<T> queryByPageBySql(Page<T> pg, String qrySql,
			Class<T> clazz);

	public <T> Page<T> queryAllByPage(Page<T> pg, Class<T> clazz, String tblName);

	public <T> Page<T> queryByPageByColumns(Page<T> pg, Class<T> clazz,
			String tblName, String[] columnNames, Object[] values);

}
