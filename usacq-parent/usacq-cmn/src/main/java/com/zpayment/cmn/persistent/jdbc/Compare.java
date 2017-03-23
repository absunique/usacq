/*
 * 
 * Copyright 2013, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: Compare.java,v 1.1 2016/08/04 23:15:21 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2013-3-13 - Create By szwang
 */

package com.zpayment.cmn.persistent.jdbc;

/**
 * 比较类型
 * 
 * @author gys
 * @version
 * @since
 * 
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
