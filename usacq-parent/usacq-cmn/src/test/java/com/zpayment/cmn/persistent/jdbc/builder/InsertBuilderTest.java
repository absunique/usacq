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
import com.zpayment.cmn.persistent.jdbc.builder.InsertBuilder;
import com.zpayment.cmn.persistent.jdbc.param.BinaryParam;
import com.zpayment.cmn.persistent.jdbc.param.IntParam;
import com.zpayment.cmn.persistent.jdbc.param.LongParam;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.StringParam;
import com.zpayment.cmn.persistent.jdbc.param.TimestampParam;
import com.zpayment.cmn.util.StringUtils;



/**
 * 测试插入构造器
 * 
 * @author wangshuzhen
 * @version 
 * @since  
 * 
 */
public class InsertBuilderTest {
    private InsertBuilder ib;

    /**
     * @since 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        ib = InsertBuilder.build();
    }

    @Test
    public void test() {
        try {
            ib.table("TABLE_NAME");

            ib.intCol("c1", 1);
            ib.stringCol("c2", "2");
            ib.tsCol("c3", DefaultValue.CURRENT_TIMESTAMP);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ib.tsCol("c4", ts);
            
            byte[] bb = "123".getBytes();
            ib.binCol("c5", bb);
            
            String hex = StringUtils.convHexByteArrayToStr(bb);
            ib.binCol("c6", hex);
            
            ib.longCol("c7", 3L);

            String sql = "insert into TABLE_NAME(c1,c2,c3,c4,c5,c6,c7) "
                    + "values(?,?," + DefaultValue.CURRENT_TIMESTAMP +",?,?,x'" + hex + "',?)";

            PreparedSQL psql = ib.toPreparedSQL();
            assertEquals(sql, psql.getSql());
            
            assertEquals(5, psql.getParamCount());            
            assertEquals(new IntParam(1), psql.getParams().get(0));
            assertEquals(new StringParam("2"), psql.getParams().get(1));
            assertEquals(new TimestampParam(ts), psql.getParams().get(2));
            assertEquals(new BinaryParam(bb), psql.getParams().get(3));
            assertEquals(new LongParam(3L), psql.getParams().get(4));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
