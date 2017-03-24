/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc.adaptor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;

/**
 * 单条PreparedSql 设置 ps通用基类
 * 
 * @author peiwang
 * @since 2017年3月24日
 */
public abstract class AbstractSinglePreparedSqlSetter {
	protected void setValuesByPreparedSql(PreparedSQL pSql, PreparedStatement ps)
			throws SQLException {
		if (pSql.getParams() != null) {
			int parameterIndex = 1;
			for (SqlParam sp : pSql.getParams()) {
				sp.setPreparedParamValue(parameterIndex++, ps);
			}
		}
	}
}
