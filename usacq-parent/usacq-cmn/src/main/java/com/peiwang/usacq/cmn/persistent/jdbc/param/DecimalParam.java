/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: DecimalParam.java,v 1.1 2016/09/28 04:25:50 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2016年8月15日 - Create By wangshuzhen
 */

package com.peiwang.usacq.cmn.persistent.jdbc.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.peiwang.usacq.cmn.Const.DataType;

/**
 * Decimal查询参数
 * 
 * @author wangshuzhen
 * @version
 * @since
 * 
 */
public class DecimalParam extends SqlParamAdapter {

    private long value;

    public DecimalParam(long value) {
        super(DataType.DECIMAL);
        this.value = value;
    }

    @Override
    public String toString() {
        return "(Decimal)" + value;
    }

    @Override
    public void setPreparedParamValue(int parameterIndex, PreparedStatement ps) throws SQLException {
        ps.setLong(parameterIndex, value);
    }
}
