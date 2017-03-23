package com.zpayment.cmn.persistent.jdbc.batch;

import java.util.Iterator;

/**
 * 批量插入用户实现接口
 * 
 * @author wangpei
 * 
 * @param <C>
 */
public interface BatchInsertPrepare<C> {
	/* 达到该记录数，执行一次executeBatch()，以防止SQL语句过长 */
	public int getBatchCount();

	/* 迭代器，用于批量插入获取数据 */
	public Iterator<C> getIterator();
}
