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
import java.sql.Timestamp;

import com.zpayment.cmn.Const.DataType;
import com.zpayment.cmn.Const.DefaultValue;
import com.zpayment.cmn.persistent.jdbc.JdbcUtils;

/**
 * TimestampParam
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class TimestampParam extends SqlParamAdapter {

	private boolean tsValue;

	private Timestamp ts;

	private String tsStr;

	public TimestampParam(Timestamp ts) {
		super(DataType.TIMESTAMP);

		if (ts != null) {
			this.tsValue = true;
			this.ts = ts;
		} else {
			this.tsValue = false;
			this.tsStr = JdbcUtils.tsForSql(null);
		}
	}

	public TimestampParam(String tsStr) {
		super(DataType.TIMESTAMP);
		this.tsValue = false;
		this.tsStr = JdbcUtils.tsForSql(tsStr);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.cup.ibscmn.dao.param.SqlParam#setPreparedParamValue(int,
	 *      java.sql.PreparedStatement)
	 */
	@Override
	public void setPreparedParamValue(int parameterIndex, PreparedStatement ps)
			throws SQLException {
		if (tsValue) {
			ps.setTimestamp(parameterIndex, ts);
		}
	}

	@Override
	public String getPlaceHolder() {
		return tsValue ? PLACE_HOLDER : tsStr;
	}

	@Override
	public boolean isPreparedParam() {
		return tsValue;
	}

	@Override
	public String toString() {
		if (tsValue) {
			return "(Timestamp)'" + ts + "'";
		}

		if (tsStr.equals(DefaultValue.CURRENT_TIMESTAMP)) {
			return DefaultValue.CURRENT_TIMESTAMP;
		} else {
			return "(Timestamp)'" + ts + "'";
		}
	}
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ts == null) ? 0 : ts.hashCode());
        result = prime * result + ((tsStr == null) ? 0 : tsStr.hashCode());
        result = prime * result + (tsValue ? 1231 : 1237);
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
        TimestampParam other = (TimestampParam) obj;
        if (ts == null) {
            if (other.ts != null)
                return false;
        } else if (!ts.equals(other.ts))
            return false;
        if (tsStr == null) {
            if (other.tsStr != null)
                return false;
        } else if (!tsStr.equals(other.tsStr))
            return false;
        if (tsValue != other.tsValue)
            return false;
        return true;
    }
}
