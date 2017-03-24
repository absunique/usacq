/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc;

/**
 * 比较类型
 * 
 * @author peiwang
 * @since 2017年3月23日
 */
public enum Compare {
	/**
	 * 相等
	 */
	eq,

	/**
	 * 不相等
	 */
	neq,

	/**
	 * 大于
	 */
	gt,
	/**
	 * 大于或等于
	 */
	ge,

	/**
	 * 小于
	 */
	lt,
	/**
	 * 小于或等于
	 */
	le,

	/**
	 * contains 包含所给内容 like %s%
	 */
	ct,

	/**
	 * startWith 以所给内容开头 like s%
	 */
	sw,

	/**
	 * endWith 已所给内容结尾 like %s
	 */
	ew,

	/**
	 * in
	 */
	in,

	/**
	 * not in
	 */
	notin
}
