/*
 * 
 * Copyright 2012, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: SqlParameter.java,v 1.1 2016/08/04 23:15:21 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2012-12-3 - Create By szwang
 * 
 * 
 */

package com.zpayment.cmn.persistent.jdbc;

/**
 * TODO 请添加功能描述
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public class SqlParameter {
    /** 未知类型 */
    public static final int SQL_TYPE_UNKOWN = Integer.MIN_VALUE;

    /** SQL type <code>java.sql.Types</code> */
    private int    sqlType = SQL_TYPE_UNKOWN;

    /** value */
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

}
