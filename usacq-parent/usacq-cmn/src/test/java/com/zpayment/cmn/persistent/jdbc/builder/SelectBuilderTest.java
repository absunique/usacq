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

import com.zpayment.cmn.persistent.jdbc.builder.SelectBuilder;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;

/**
 * 查询语句构建器测试
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class SelectBuilderTest {

	private SelectBuilder selectBuilder;

	/**
	 * @since
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		selectBuilder = SelectBuilder.build();
	}

	@Test
	public void testNoCondition() {
		try {
			selectBuilder.table("TABLE_NAME");

			selectBuilder.addCol("c1", "c2,c3");

			selectBuilder.setOrderBy("c1 desc");

			String sql = "select c1,c2,c3 from TABLE_NAME order by c1 desc";

			PreparedSQL psql = selectBuilder.toPreparedSQL();
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
			selectBuilder.table("TABLE_NAME");

			selectBuilder.addCol("c1", "c2,c3");

			selectBuilder.condition(true).intEqual("c1", 2).intEqual("c2", 3);

			selectBuilder.setOrderBy("c1 desc");

			String sql = "select c1,c2,c3 from TABLE_NAME where c1=? and c2=? order by c1 desc";

			PreparedSQL psql = selectBuilder.toPreparedSQL();
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
			selectBuilder.table("TABLE_NAME");

			selectBuilder.addCol("c1", "c2,c3");

			selectBuilder.condition(false).intEqual("c1", 2).intEqual("c2", 3);

			selectBuilder.setOrderBy("c1 desc");

			String sql = "select c1,c2,c3 from TABLE_NAME where c1=? or c2=? order by c1 desc";

			PreparedSQL psql = selectBuilder.toPreparedSQL();
			assertEquals(sql, psql.getSql());
			assertEquals(2, psql.getParamCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
