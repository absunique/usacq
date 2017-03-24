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
 * sql参数统一接口
 * 
 * @author peiwang
 * @since 2017年3月23日
 */
public interface SqlParam {
	String PLACE_HOLDER = "?";

	/**
	 * 获取数据类型
	 * 
	 * @since
	 * @return @see {@link DataType}
	 */
	String getDataType();

	/**
	 * 获取占位符或值
	 * 
	 * @since
	 * @return
	 */
	String getPlaceHolder();

	/**
	 * 判断是否是预定义参数，如果是需要执行 {@link #setPreparedParamValue(int, PreparedStatement)}
	 * 
	 * @since
	 * @return
	 */
	boolean isPreparedParam();

	/**
	 * 设置值
	 * 
	 * @since
	 * @param parameterIndex
	 * @param ps
	 * @throws SQLException
	 */
	void setPreparedParamValue(int parameterIndex, PreparedStatement ps)
			throws SQLException;
}
