/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: SqlParam.java,v 1.1 2016/09/28 04:25:50 peiwang Exp $
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

/**
 * Sql查询参数
 * 
 * @author wangshuzhen
 * @version
 * @since
 * 
 */
public interface SqlParam {
    String PLACE_HOLDER = "?";
    
    /**
     * 获取数据类型
     * 
     * @since
     * @return @see com.cup.ibscmn.Const DataType
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
    void setPreparedParamValue(int parameterIndex, PreparedStatement ps) throws SQLException;
}
