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

import com.zpayment.cmn.persistent.jdbc.util.MappingStructFactory;

/**
 * 根据注解将行记录反射成对象
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class AnnotationRowMapper<T> implements RowMapper<T> {

    /** 对象类型 */
    protected Class<T> elementType;

    public AnnotationRowMapper(Class<T> elementType) {
        this.elementType = elementType;
    }

    /**
     * 将行记录反射成对象
     * 
     * @see com.cup.ibscmn.dao.jdbc.support.RowMapper#mapRow(java.sql.ResultSet)
     */
    @SuppressWarnings("unchecked")
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T result = (T) MappingStructFactory.create(elementType).toObject(rs);
        return result;
    }
}
