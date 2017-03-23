/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: StringParam.java,v 1.1 2016/09/28 04:25:50 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2016年8月15日 - Create By wangshuzhen
 */

package com.zpayment.cmn.persistent.jdbc.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.zpayment.cmn.Const.DataType;

/**
 * TODO 请添加功能描述
 * 
 * @author wangshuzhen
 * @version
 * @since
 * 
 */
public class StringParam extends SqlParamAdapter {

    private String value;

    public StringParam(String value) {
        super(DataType.STRING);
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "(String)'" + value + "'";
    }
    
    @Override
    public void setPreparedParamValue(int parameterIndex, PreparedStatement ps) throws SQLException {
        ps.setString(parameterIndex, value);
    }
}
