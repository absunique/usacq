/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: DoubleParam.java,v 1.1 2016/09/28 04:25:50 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2016年9月5日 - Create By wangshuzhen
 */

package com.zpayment.cmn.persistent.jdbc.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.zpayment.cmn.Const.DataType;

/**
 * 双精度参数
 * 
 * @author wangshuzhen
 * @version
 * @since
 * 
 */
public class DoubleParam extends SqlParamAdapter {

	private double value;

	public DoubleParam(double value) {
		super(DataType.FLOAT);
		this.value = value;
	}

	@Override
	public String toString() {
		return "(Double)" + value;
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
		ps.setDouble(parameterIndex, value);
	}
}
