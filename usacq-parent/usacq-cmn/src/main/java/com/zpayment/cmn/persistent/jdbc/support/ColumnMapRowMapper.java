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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.zpayment.cmn.persistent.jdbc.JdbcUtils;

/**
 * 将行数据转换成按列的Map
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class ColumnMapRowMapper implements RowMapper<Map<String, Object>> {

    /**
     * 将一行记录转换成Map，key为列名，value为值
     * 
     * @see com.cup.ibscmn.dao.jdbc.support.RowMapper#mapRow(java.sql.ResultSet)
     */
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (int i = 1, colCount = rsmd.getColumnCount(); i <= colCount; i++) {
            String name = JdbcUtils.getColumnName(rsmd, i);
            Object value = JdbcUtils.getResultSetValue(rs, i);
            map.put(name, value);
        }
        return map;
    }
}
