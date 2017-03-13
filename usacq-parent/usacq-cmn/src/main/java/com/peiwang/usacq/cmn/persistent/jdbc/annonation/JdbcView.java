/*
 * 
 *  Copyright 2012, China UnionPay Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF CHINA UNIONPAY CO., LTD.
 *  
 *   $Id: JdbcView.java,v 1.1 2016/08/04 23:15:21 peiwang Exp $
 *
 *  Function:
 *
 *    行数据的注解
 *
 *  Edit History:
 *
 *     2012-11-23 - Create By szwang
 *    
 *    
 */

package com.peiwang.usacq.cmn.persistent.jdbc.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 用于@link RowMapper 映射行数据
 * 
 * @author gys
 * @version 
 * @since  
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcView {
    /**
     * 表名
     * 
     * @since 
     * @return
     */
    String name() default "";
}
