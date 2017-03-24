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
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zpayment.cmn.Const.DefaultValue;
import com.zpayment.cmn.persistent.jdbc.builder.ConditionBuilder;
import com.zpayment.cmn.persistent.jdbc.param.BinaryParam;
import com.zpayment.cmn.persistent.jdbc.param.DecimalParam;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.persistent.jdbc.param.StringParam;
import com.zpayment.cmn.persistent.jdbc.param.TimestampParam;
import com.zpayment.cmn.util.DateUtils;
import com.zpayment.cmn.util.StringUtils;




/**
 * 条件构建测试
 * 
 * @author wangshuzhen
 * @version 
 * @since  
 * 
 */
public class ConditionBuilderTest {
    private ConditionBuilder cb;
    
    /**
     * @since 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        cb = ConditionBuilder.and();
    }
    
    @Test
    public void testMultiCondtion() {
        ConditionBuilder cb1 = ConditionBuilder.and()
                .intEqual("c1", 1);
        assertEquals("c1=?", cb1.getSql());
        assertEquals(1, cb1.getParams().size());
        
        ConditionBuilder cb2 = ConditionBuilder.and()
                .intEqual("c1", 1)
                .add(ConditionBuilder.or().stringEqual("c2", "2").stringEqual("c2", "3"));        
        
        assertEquals("c1=? and (c2=? or c2=?)", 
                cb2.getSql());
        assertEquals(3, cb2.getParams().size());

        ConditionBuilder cb3 = ConditionBuilder.and().intEqual("c1", 1).add(
                ConditionBuilder.or()
                    .add(ConditionBuilder.and().stringEqual("c2", "2").stringEqual("c3", "3"))
                    .add(ConditionBuilder.and().stringNotEqual("c2", "2").stringEqual("c4", "3"))
               );        
               
        assertEquals("c1=? and ((c2=? and c3=?) or (c2<>? and c4=?))", 
                cb3.getSql());
        assertEquals(5, cb3.getParams().size());
        assertEquals(new StringParam("3"), cb3.getParams().get(4));
        
        ConditionBuilder cb4 = ConditionBuilder.and().intEqual("c1", 1).add(
                ConditionBuilder.or()
                    .add(ConditionBuilder.and().stringEqual("c2", "2").stringEqual("c3", "3"))
                    .add(ConditionBuilder.and().stringNotEqual("c2", "2").stringEqual("c4", "3"))
               ).add("c5 in %d", new int[] { 4,5,6}); 
        
        assertEquals("c1=? and ((c2=? and c3=?) or (c2<>? and c4=?)) and c5 in (?,?,?)", 
                cb4.getSql());
        assertEquals(8, cb4.getParams().size());
        assertEquals(new DecimalParam(4), cb4.getParams().get(5));
    } 

    @Test
    public void testIntCondition()  {
        try {
            cb.intEqual("c1", 1).intNotEqual("c2", 2).intGreaterThan("c3", 3).intGreaterEqual("c4", 4)
                .intLessThan("c5", 5).intLessEqual("c6", 6).intBetween("c7", 7, 8)
                .intIn("c8", new int[] { 9, 10, 11 }).intNotIn("c9", new int[] { 12 })
                .intIn("c10", new String[] { "13", "14" }).intNotIn("c11", new String[] { "15", "16"})
                .intIn("c11", Arrays.asList(new String[] { "17", "18" }))
                .intNotIn("c12", Arrays.asList(new Integer[] { 19 }));
            
            String condition = "c1=? and c2<>? and c3>? and c4>=?"
                    + " and c5<? and c6<=? and c7 between ? and ?"
                    + " and c8 in (?,?,?) and c9 not in (?)"
                    + " and c10 in (?,?) and c11 not in (?,?)"
                    + " and c11 in (?,?)"
                    + " and c12 not in (?)";
            
            String psql = cb.getSql();
            assertEquals(condition, psql);
            
            List<SqlParam> params = cb.getParams();
            int i = 0;
            for (SqlParam param : params) {
                assertEquals(new DecimalParam(++i), param);
            }
            
        } catch(Exception e) {
            fail("testIntCondition failed, " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testStringCondition()  {
        try {
            cb.stringEqual("c1", "1").stringNotEqual("c2", "2").stringGreaterThan("c3", "3").stringGreaterEqual("c4", "4")
                .stringLessThan("c5", "5").stringLessEqual("c6", "6").stringBetween("c7", "7", "8")
                .stringIn("c8", new String[] { "9", "10", "11" }).stringNotIn("c9", new String[] { "12" })
                .stringIn("c10", Arrays.asList(new String[] { "13", "14" }))
                .stringNotIn("c11", Arrays.asList(new String[] { "15" }));
            
            String condition = "c1=? and c2<>? and c3>? and c4>=?"
                    + " and c5<? and c6<=? and c7 between ? and ?"
                    + " and c8 in (?,?,?) and c9 not in (?)"
                    + " and c10 in (?,?)"
                    + " and c11 not in (?)";
            
            String psql = cb.getSql();
            assertEquals(condition, psql);
            
            List<SqlParam> params = cb.getParams();
            int i = 0;
            for (SqlParam param : params) {
                assertEquals(new StringParam(++i + ""), param);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
            fail("testStringCondition failed, " + e.getMessage());
        }
    }
    
    @Test
    public void testTsCondition()  {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            
            long time = cal.getTime().getTime();
            
            final long hour = 60 * 60 * 1000L;
            final int count = 8;
            Timestamp[] tss = new Timestamp[count];
            String[] ss = new String[count];
            for (int i = 0; i < count; i++, time += hour) {
                tss[i] = new Timestamp(time);
                ss[i] = DateUtils.format("yyyyMMdd HH:mm:ss",tss[i]);
            }
            
            // Timestamp形式
            cb.tsEqual("c1", tss[0]).tsNotEqual("c2", tss[1])
                .tsGreaterThan("c3", tss[2]).tsGreaterEqual("c4", tss[3])
                .tsLessThan("c5", tss[4]).tsLessEqual("c6", tss[5])
                .tsBetween("c7", tss[6], tss[7]);
            
            
            String condition = "c1=? and c2<>?"
                    + " and c3>? and c4>=?"
                    + " and c5<? and c6<=?"
                    + " and c7 between ? and ?";            
           
            assertEquals(condition, cb.getSql());

            int i = 0;
            for (SqlParam param : cb.getParams()) {
                assertEquals(new TimestampParam(tss[i++]).toString(), param.toString());
            }

            // 字符串形式
            ConditionBuilder cb1 = ConditionBuilder.and();
            cb1.tsEqual("c1", ss[0]).tsNotEqual("c2", ss[1])
                .tsGreaterThan("c3", ss[2]).tsGreaterEqual("c4", ss[3])
                .tsLessThan("c5", ss[4]).tsLessEqual("c6", ss[5])
                .tsBetween("c7", ss[6], ss[7]);
            
            String condition1 = String.format("c1='%s' and c2<>'%s'"
                    + " and c3>'%s' and c4>='%s'"
                    + " and c5<'%s' and c6<='%s'"
                    + " and c7 between '%s' and '%s'", (Object[]) ss);

            assertEquals(condition1, cb1.getSql());
            assertEquals(0, cb1.getParams().size());
            
            // 关键字CURRENT TIMESTAMP
            ConditionBuilder cb2 = ConditionBuilder.and().tsEqual("c1", DefaultValue.CURRENT_TIMESTAMP);
            assertEquals("c1=" + DefaultValue.CURRENT_TIMESTAMP, cb2.getSql());
        } catch(Exception e) {
            e.printStackTrace();
            fail("testTsCondition failed, " + e.getMessage());
        }
    }
    
    @Test
    public void testBinCondition()  {
        try {
           String s = "12345";
           byte[] bb = s.getBytes();
           String hex = StringUtils.convHexByteArrayToStr(bb);
           
           cb.binEqual("c1", bb).binEqual("c2", hex);
           String condition = String.format("c1=? and c2=x'%s'", hex);
           
           Assert.assertEquals(condition, cb.getSql());
           Assert.assertEquals(1, cb.getParams().size());
           Assert.assertEquals(new BinaryParam(bb), cb.getParams().get(0));
           
        } catch(Exception e) {
            e.printStackTrace();
            fail("testBinCondition failed, " + e.getMessage());
        }
    }
}
