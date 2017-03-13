/*
 * 
 * Copyright 2012, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: SingleColumnRowMapper.java,v 1.1 2016/08/04 23:15:22 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2012-11-22 - Create By szwang
 */

package com.peiwang.usacq.cmn.persistent.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.peiwang.usacq.cmn.persistent.jdbc.JdbcUtils;

/**
 * 一行中只有一列的转换
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public class SingleColumnRowMapper<T> implements RowMapper<T> {

    private Class<T> requiredType;

    public SingleColumnRowMapper(Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.cup.ibscmn.dao.jdbc.support.RowMapper#mapRow(java.sql.ResultSet)
     */
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object result = JdbcUtils.getResultSetValue(rs, 1, requiredType);
        return requiredType.cast(result);
    }
}
