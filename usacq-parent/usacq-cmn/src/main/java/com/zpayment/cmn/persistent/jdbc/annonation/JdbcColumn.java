/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.zpayment.cmn.Const;

/**
 * @link RowMapper 用于映射列数据
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcColumn {

	/**
	 * 映射到列的名称，缺省是用字段名
	 * 
	 * @since
	 * @return
	 */
	String name() default "";

	/**
	 * 是否为数据库主键
	 * 
	 * @since
	 * @return
	 */
	boolean key() default false;

	/**
	 * 数据类型，如果没有设置，则根据字段的数据类型判断。<br>
	 * 取值与参数管理中的定义相同 @see com.cup.ibscmn.Const.DataType<br>
	 * 
	 * @since
	 * @return
	 */
	String type() default Const.DataType.NONE;

	/**
	 * 是否为可以为空，用于校验。TODO 暂时没有用到
	 * 
	 * @since
	 * @return
	 */
	boolean nullable() default true;

	/**
	 * 长度，用于校验，0表示不限制。TODO 暂时没有用到
	 * 
	 * @since
	 * @return
	 */
	int length() default 0;

	/**
	 * 精度，用于校验，0表示不限制。TODO 暂时没有用到
	 */
	int precision() default 0;
}
