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
            throw new BaseException(BaseErrorCode.COMN_DATA_TYPE_MIS_MATCH, new Object[] { "null" });
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
