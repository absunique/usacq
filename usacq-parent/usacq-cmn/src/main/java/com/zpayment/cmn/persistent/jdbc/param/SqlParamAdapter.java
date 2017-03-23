/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: SqlParamAdapter.java,v 1.1 2016/09/28 04:25:50 peiwang Exp $
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

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;

/**
 * SqlParam适配器，屏蔽部分操作
 * 
 * @author peiwang
 * @version
 * @since
 * 
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
	 * @see com.cup.ibscmn.dao.param.SqlParam#getDataType()
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
	 * @see com.cup.ibscmn.dao.param.SqlParam#isPreparedParam()
	 */
	@Override
	public boolean isPreparedParam() {
		return true;
	}
}
