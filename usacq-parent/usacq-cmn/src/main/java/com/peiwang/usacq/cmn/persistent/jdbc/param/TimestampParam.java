/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: TimestampParam.java,v 1.1 2016/09/28 04:25:50 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2016年9月2日 - Create By wangshuzhen
 */

package com.peiwang.usacq.cmn.persistent.jdbc.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.peiwang.usacq.cmn.Const.DataType;
import com.peiwang.usacq.cmn.Const.DefaultValue;
import com.peiwang.usacq.cmn.persistent.jdbc.JdbcUtils;

/**
 * TimestampParam
 * 
 * @author wangshuzhen
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
}
