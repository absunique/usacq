/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zpayment.cmn.persistent.jdbc.JdbcUtils;

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
