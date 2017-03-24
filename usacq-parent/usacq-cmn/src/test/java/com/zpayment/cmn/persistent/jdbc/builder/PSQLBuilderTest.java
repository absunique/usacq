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

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.persistent.jdbc.builder.ConditionBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.PSQLBuilder;
import com.zpayment.cmn.persistent.jdbc.param.DecimalParam;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.StringParam;

/**
 * PSQLBuilder基础功能测试
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class PSQLBuilderTest {

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
			PSQLBuilder builder = PSQLBuilder.build().append(
					"select c1,c2,c3 from %S", "TBL_IBMGM_XX");

			ConditionBuilder condition = ConditionBuilder.and()
					.intEqual("c1", 1)
					.stringNotIn("c2", new String[] { "2", "3" });

			if (!condition.isEmpty()) {
				builder.append("where").append(condition);
			}

			builder.append("order by c1 desc");

			String sql = "select c1,c2,c3 from TBL_IBMGM_XX where c1=? and c2 not in (?,?) order by c1 desc";

			PreparedSQL psql = builder.toPreparedSQL();
			assertEquals(sql, psql.getSql());

			assertEquals(3, psql.getParamCount());

			assertEquals(new DecimalParam(1), psql.getParams().get(0));
			assertEquals(new StringParam("2"), psql.getParams().get(1));
			assertEquals(new StringParam("3"), psql.getParams().get(2));

		} catch (BaseException e) {
			e.printStackTrace();
			fail("test failed, " + e.getMessage());
		}
	}

	@Test
	public void testSequence() {
		PSQLBuilder builder = PSQLBuilder
				.build()
				.append("update tbl_ibhcs_bmp_sequence set sequence_val = sequence_val + 1 where")
				.append(ConditionBuilder.and().stringEqual("sequence_id",
						"TTTT"));
		String sql = "update tbl_ibhcs_bmp_sequence set sequence_val = sequence_val + 1 where sequence_id=?";
		assertEquals(sql, builder.toPreparedSQL().getSql());
	}
}
