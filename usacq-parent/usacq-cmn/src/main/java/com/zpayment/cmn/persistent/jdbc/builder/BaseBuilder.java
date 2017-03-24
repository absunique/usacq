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

import com.zpayment.cmn.util.StringUtils;

/**
 * PreparedSQL抽象构造器<br>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class BaseBuilder {

	/**
	 * 检查列名正确性
	 * 
	 * @since
	 * @param col
	 */
	protected void checkCol(String col) {
		if (StringUtils.isEmpty(col)) {
			throw new IllegalArgumentException("col is null");
		}
	}
}
