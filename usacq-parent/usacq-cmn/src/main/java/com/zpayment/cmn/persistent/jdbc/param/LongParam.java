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
 * Long参数
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class LongParam extends SqlParamAdapter {

    private long value;

    public LongParam(long value) {
        super(DataType.DECIMAL);
        this.value = value;
    }

    @Override
    public String toString() {
        return "(Long)" + value;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.cup.ibscmn.dao.param.SqlParam#setPreparedParamValue(int, java.sql.PreparedStatement)
     */
    @Override
    public void setPreparedParamValue(int parameterIndex, PreparedStatement ps) throws SQLException {
        ps.setLong(parameterIndex, value);
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LongParam other = (LongParam) obj;
        if (value != other.value)
            return false;
        return true;
    }

}
