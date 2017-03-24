/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月23日 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc.adaptor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementSetter;

import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;

/**
 * @author peiwang
 * @since 2017年3月23日
 */
public class PreparedSqlSetterAdaptor implements PreparedStatementSetter {
	private PreparedSQL pSql;

	public PreparedSqlSetterAdaptor(PreparedSQL pSql) {
		this.pSql = pSql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.jdbc.core.PreparedStatementSetter#setValues(java.
	 * sql.PreparedStatement)
	 */
	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
		pSql.setValues(ps);
	}

}
