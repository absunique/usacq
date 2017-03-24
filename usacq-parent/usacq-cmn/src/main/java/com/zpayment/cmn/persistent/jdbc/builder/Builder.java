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

import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;

/**
 * PSQL构造器接口
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public interface Builder {
	/**
	 * 构造预定义查询对象
	 * 
	 * @since
	 * @return
	 */
	public PreparedSQL toPreparedSQL();
}
