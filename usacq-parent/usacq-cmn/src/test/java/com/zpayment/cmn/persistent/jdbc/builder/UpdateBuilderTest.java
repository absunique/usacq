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

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import com.zpayment.cmn.Const.DefaultValue;
import com.zpayment.cmn.persistent.jdbc.builder.UpdateBuilder;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.util.StringUtils;

/**
 * 更新语句构建器测试
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class UpdateBuilderTest {

	private UpdateBuilder ub;

	/**
	 * @since
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ub = UpdateBuilder.build();
	}

	@Test
	public void testNoCondition() {
		try {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			byte[] bb = "12345".getBytes();
			String hex = StringUtils.convHexByteArrayToStr(bb);

			ub.table("TABLE_NAME").intCol("c1", 1).stringCol("c2", "02")
					.tsCol("c3", DefaultValue.CURRENT_TIMESTAMP)
					.tsCol("c4", ts).binCol("c5", bb).binCol("c6", hex);

			String sql = "update TABLE_NAME set " + "c1=?,c2=?," + "c3="
					+ DefaultValue.CURRENT_TIMESTAMP + "," + "c4=?," + "c5=?,"
					+ "c6=x'" + hex + "'";

			PreparedSQL psql = ub.toPreparedSQL();
			assertEquals(sql, psql.getSql());
			assertEquals(4, psql.getParamCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testAnd() {
		try {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			byte[] bb = "12345".getBytes();
			String hex = StringUtils.convHexByteArrayToStr(bb);

			ub.table("TABLE_NAME").intCol("c1", 1).stringCol("c2", "02")
					.tsCol("c3", DefaultValue.CURRENT_TIMESTAMP)
					.tsCol("c4", ts).binCol("c5", bb).binCol("c6", hex);

			ub.condition(true).intEqual("c1", 2).intEqual("c2", 3);

			String sql = "update TABLE_NAME set " + "c1=?,c2=?," + "c3="
					+ DefaultValue.CURRENT_TIMESTAMP + "," + "c4=?," + "c5=?,"
					+ "c6=x'" + hex + "' where c1=? and c2=?";

			PreparedSQL psql = ub.toPreparedSQL();
			assertEquals(sql, psql.getSql());
			assertEquals(6, psql.getParamCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testOr() {
		try {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			byte[] bb = "12345".getBytes();
			String hex = StringUtils.convHexByteArrayToStr(bb);

			ub.table("TABLE_NAME").intCol("c1", 1).stringCol("c2", "02")
					.tsCol("c3", DefaultValue.CURRENT_TIMESTAMP)
					.tsCol("c4", ts).binCol("c5", bb).binCol("c6", hex);

			ub.condition(false).intEqual("c1", 2).intEqual("c2", 3);

			String sql = "update TABLE_NAME set " + "c1=?,c2=?," + "c3="
					+ DefaultValue.CURRENT_TIMESTAMP + "," + "c4=?," + "c5=?,"
					+ "c6=x'" + hex + "' where c1=? or c2=?";

			PreparedSQL psql = ub.toPreparedSQL();
			assertEquals(sql, psql.getSql());
			assertEquals(6, psql.getParamCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
