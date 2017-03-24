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
package com.zpayment.cmn.persistent.jdbc.builder;

import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;

/**
 * PreparedSQL抽象构造器<br>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class AbstractBuilder extends BaseBuilder implements Builder,
		SingleParamsBuilder {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(AbstractBuilder.class);

	/**
	 * 构造预定义查询对象
	 * 
	 * @since
	 * @return
	 */
	@Override
	public PreparedSQL toPreparedSQL() {
		PreparedSQL psql = new PreparedSQL();
		psql.setSql(getSql());
		psql.setParams(getParams());

		log.debug("psql: %s", psql);
		return psql;
	}

}
