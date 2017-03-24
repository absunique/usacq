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
import com.zpayment.cmn.persistent.jdbc.JdbcUtils;
import com.zpayment.cmn.util.StringUtils;

/**
 * 二进制参数 VARCHAR(CHAR) FOR BIT DATA
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class BinaryParam extends SqlParamAdapter {

	private byte[] bytes;

	private String hex;

	private boolean binary;

	public BinaryParam(byte[] bytes) {
		super(DataType.BINARY);
		this.binary = true;
		this.bytes = bytes == null ? new byte[0] : bytes;
	}

	public BinaryParam(String hex) {
		super(DataType.BINARY);
		this.binary = false;
		this.hex = hex == null ? "" : hex;
		this.hex = JdbcUtils.binaryForSql(this.hex);
	}

	@Override
	public String getPlaceHolder() {
		if (binary) {
			return PLACE_HOLDER;
		} else {
			return hex;
		}
	}

	/**
	 * 缺省是占位符，需要设置预定义值
	 * 
	 * @see com.cup.ibscmn.dao.param.SqlParam#isPreparedParam()
	 */
	@Override
	public boolean isPreparedParam() {
		return binary;
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
		if (binary) {
			ps.setBytes(parameterIndex, bytes);
		}
	}

	@Override
	public String toString() {
		if (binary) {
			return "(Bytes)" + StringUtils.convHexByteArrayToStr(bytes);
		} else {
			return "(Hex)" + hex;
		}
	}

	@Override
	public int hashCode() {
		String s = toString();
		return s.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		return this.toString().equals(obj.toString());
	}
}
