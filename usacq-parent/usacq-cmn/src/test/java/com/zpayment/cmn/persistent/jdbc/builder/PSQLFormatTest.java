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
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zpayment.cmn.Const.DefaultValue;
import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.persistent.jdbc.builder.PSQLFormat;
import com.zpayment.cmn.persistent.jdbc.builder.SingleParamsBuilder;

/**
 * 格式化构建器测试
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class PSQLFormatTest {
	/**
	 * @since
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		try {
			// AbstractBuilder psql = null;
			SingleParamsBuilder sb = PSQLFormat.build("c1 in %s",
					(Object) new String[] { "1", "2" });

			assertEquals("c1 in (?,?)", sb.getSql());

			assertEquals(2, sb.getParams().size());
		} catch (BaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test2() {
		try {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			SingleParamsBuilder sb = PSQLFormat
					.build("insert into %S where EVENT_ID=%d and TASK_ST=%s and CRT_TS=%t and UPD_TS=%t",
							"TBL_IBMGM_MGM_TASK", "100", "2",
							DefaultValue.CURRENT_TIMESTAMP, ts);

			String sql = "insert into TBL_IBMGM_MGM_TASK where EVENT_ID=? and TASK_ST=? "
					+ "and CRT_TS="
					+ DefaultValue.CURRENT_TIMESTAMP
					+ " and UPD_TS=?";

			assertEquals(sql, sb.getSql());
			assertEquals(3, sb.getParams().size());
		} catch (BaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testArray() {
		try {
			SingleParamsBuilder sb = PSQLFormat.build(
					"select * from %S where EVENT_ID in %d and TASK_ST in %s",
					"TBL_IBMGM_MGM_TASK", new Object[] { 100, 101 },
					new String[] { "1", "2" });

			String sql = "select * from TBL_IBMGM_MGM_TASK where EVENT_ID in (?,?) and TASK_ST in (?,?)";

			assertEquals(sql, sb.getSql());
			assertEquals(4, sb.getParams().size());
		} catch (BaseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCollection() {
		try {
			List<Object> eventIdList = Arrays
					.asList(new Object[] { 100, "101" });
			List<String> taskStList = Arrays.asList(new String[] { "1", "2" });

			SingleParamsBuilder psql = PSQLFormat.build(
					"select * from %S where EVENT_ID in %d and TASK_ST in %s",
					"TBL_IBMGM_MGM_TASK", eventIdList, taskStList);

			String sql = "select * from TBL_IBMGM_MGM_TASK where EVENT_ID in (?,?) and TASK_ST in (?,?)";

			assertEquals(sql, psql.getSql());
			assertEquals(4, psql.getParams().size());
		} catch (BaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testArgCnt1() {
		try {
			SingleParamsBuilder psql = PSQLFormat.build(
					"select * from %S where EVENT_ID=%d and TASK_ST in %s",
					"TBL_IBMGM_MGM_TASK", new Object[] { 100, "101" });

			System.err.println(psql);
			fail("invalid count");
		} catch (BaseException e) {
			assertEquals(BaseErrorCode.COMN_DATA_INVALID_FOMAT_ARG_CNT,
					e.getErrorCode());
		}
	}

	@Test
	public void testArgCnt2() {
		try {
			SingleParamsBuilder psql = PSQLFormat.build(
					"select * from %S where EVENT_ID=%d and TASK_ST=%s",
					"TBL_IBMGM_MGM_TASK", 100, "101", 1);

			System.err.println(psql);
			fail("invalid count");
		} catch (BaseException e) {
			assertEquals(BaseErrorCode.COMN_DATA_INVALID_FOMAT_ARG_CNT,
					e.getErrorCode());
		}
	}

	@Test
	public void testInvalidType1() {
		try {
			SingleParamsBuilder psql = PSQLFormat.build(
					"select * from %S where EVENT_ID=%i and TASK_ST=%s",
					"TBL_IBMGM_MGM_TASK", 100, "101", 1);

			System.err.println(psql);
			fail("invalid count");
		} catch (BaseException e) {
			Assert.assertEquals(BaseErrorCode.COMN_DATA_INVALID_FOMAT_TYPE,
					e.getErrorCode());
		}
	}

	@Test
	public void testInvalidType2() {
		try {
			SingleParamsBuilder psql = PSQLFormat.build(
					"select * from %S where EVENT_ID=%%d and TASK_ST=%s",
					"TBL_IBMGM_MGM_TASK", 100, "101", 1);

			System.err.println(psql);
			fail("invalid count");
		} catch (BaseException e) {
			Assert.assertEquals(BaseErrorCode.COMN_DATA_INVALID_FOMAT_TYPE,
					e.getErrorCode());
		}
	}

	@Test
	public void testInvalidType3() {
		try {
			SingleParamsBuilder psql = PSQLFormat.build(
					"select * from %S where EVENT_ID=% d and TASK_ST=%s",
					"TBL_IBMGM_MGM_TASK", 100, "101", 1);

			System.err.println(psql);
			fail("invalid count");
		} catch (BaseException e) {
			Assert.assertEquals(BaseErrorCode.COMN_DATA_INVALID_FOMAT_TYPE,
					e.getErrorCode());
		}
	}

	@Test
	public void testInvalidValue() {
		try {
			SingleParamsBuilder psql = PSQLFormat.build(
					"select * from %S where EVENT_ID=%d and CRT_TS=%t",
					"TBL_IBMGM_MGM_TASK", 100, 10000);

			System.err.println(psql);
			fail("invalid count");
		} catch (BaseException e) {
			Assert.assertEquals(BaseErrorCode.COMN_DATA_INVALID_FOMAT_VALUE,
					e.getErrorCode());
		}
	}

}
