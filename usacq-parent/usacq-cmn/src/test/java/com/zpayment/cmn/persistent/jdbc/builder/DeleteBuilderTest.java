/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.zpayment.cmn.persistent.jdbc.builder.DeleteBuilder;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;

/**
 * 删除语句构建器测试
 * 
 * @author wangshuzhen
 * @version
 * @since
 * 
 */
public class DeleteBuilderTest {

	private DeleteBuilder db;

	/**
	 * @since
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		db = DeleteBuilder.build();
	}

	@Test
	public void testNoCondition() {
		try {
			db.table("TABLE_NAME");

			String sql = "delete from TABLE_NAME";

			PreparedSQL psql = db.toPreparedSQL();
			assertEquals(sql, psql.getSql());
			assertEquals(0, psql.getParamCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testAnd() {
		try {
			db.table("TABLE_NAME");

			db.condition(true).intEqual("c1", 2).intEqual("c2", 3);

			String sql = "delete from TABLE_NAME where c1=? and c2=?";

			PreparedSQL psql = db.toPreparedSQL();
			assertEquals(sql, psql.getSql());
			assertEquals(2, psql.getParamCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testOr() {
		try {
			db.table("TABLE_NAME");

			db.condition(false).intEqual("c1", 2).intEqual("c2", 3);

			String sql = "delete from TABLE_NAME where c1=? or c2=?";

			PreparedSQL psql = db.toPreparedSQL();
			assertEquals(sql, psql.getSql());
			assertEquals(2, psql.getParamCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
