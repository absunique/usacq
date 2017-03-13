/*
 * 
 * Copyright 2012, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: ColumnMapRowMapper.java,v 1.1 2016/08/04 23:15:22 peiwang Exp $
 * 
 * Function:
 * 
 * 将行数据转换成按列的Map
 * 
 * Edit History:
 * 
 * 2012-11-22 - Create By szwang
 */

package com.peiwang.usacq.cmn.persistent.jdbc.support;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.peiwang.usacq.cmn.persistent.jdbc.JdbcUtils;

/**
 * 将行数据转换成按列的Map
 * 
 * @author gys
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
