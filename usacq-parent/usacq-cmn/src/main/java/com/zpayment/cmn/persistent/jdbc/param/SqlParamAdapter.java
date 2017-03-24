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

import com.zpayment.cmn.Const.DataType;
import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;

/**
 * 统一适配器
 * 
 * @author peiwang
 * @since 2017年3月23日
 */
public abstract class SqlParamAdapter implements SqlParam {

	protected String dataType = null;

	public SqlParamAdapter(String dataType) {
		this.dataType = dataType;

		if (dataType == null) {
			throw new BaseException(BaseErrorCode.FAIL, new Object[] { "null" });
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see {@link DataType}
	 */
	@Override
	public String getDataType() {
		return dataType;
	}

	/**
	 * 获取占位符或值
	 * 
	 * @since
	 * @return
	 */
	@Override
	public String getPlaceHolder() {
		return PLACE_HOLDER;
	}

	/**
	 * 缺省是占位符，需要设置预定义值
	 * 
	 */
	@Override
	public boolean isPreparedParam() {
		return true;
	}
}
