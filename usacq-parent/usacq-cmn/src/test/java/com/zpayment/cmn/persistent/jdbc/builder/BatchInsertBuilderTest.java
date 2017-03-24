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

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zpayment.cmn.persistent.jdbc.builder.batch.BatchInsertBuilder;
import com.zpayment.cmn.persistent.jdbc.param.BatchPreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.IntParam;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;

/**
 * TODO 请添加功能描述
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class BatchInsertBuilderTest {

	private BatchInsertBuilder builder;

	/**
	 * @since
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		builder = BatchInsertBuilder.build();
	}

	@Test
	public void test() {
		builder.table("TBL_IBMGM_XXX");
		// builder.table("TBL_IBMGM_XXX");
		for (int i = 0; i < 10; i++) {
			builder.stringCol("c2", "1").intCol("c3", i).intCol("c1", 1);
			builder.addBatch();
		}

		BatchPreparedSQL psql = (BatchPreparedSQL) builder.toPreparedSQL();
		System.out.println(builder.getSql());
		assertEquals(builder.getSql(),
				"insert into TBL_IBMGM_XXX(c2,c3,c1) values(?,?,?)");
		assertEquals(10, psql.getBatchCount());
		assertEquals(3, psql.getParamCount());

		List<List<SqlParam>> paramsList = psql.getParamsList();
		assertEquals(new IntParam(9), paramsList.get(9).get(1));
	}

	/**
	 * @since
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

}
