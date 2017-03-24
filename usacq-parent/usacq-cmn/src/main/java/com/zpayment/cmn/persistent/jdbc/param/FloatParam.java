/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.zpayment.cmn.Const.DataType;

/**
 * 浮点值查询参数
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class FloatParam extends SqlParamAdapter {

    private float value;

    public FloatParam(float value) {
        super(DataType.FLOAT);

        this.value = value;
    }

    
    @Override
    public String toString() {
        return "(Float)" + value;
    }
    
    @Override
    public void setPreparedParamValue(int parameterIndex, PreparedStatement ps) throws SQLException {
        ps.setFloat(parameterIndex, value);
    }
}
