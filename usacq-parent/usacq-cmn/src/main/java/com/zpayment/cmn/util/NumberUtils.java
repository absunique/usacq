/*
 * 
 * Copyright 2013, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: NumberUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * 数字类型工具
 * 
 * Edit History:
 * 
 * 2013-5-2 - Create By CUPPC
 */

package com.zpayment.cmn.util;

import java.text.NumberFormat;

/**
 * 数字类型工具
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class NumberUtils {

    /**
     * 生成指定长度的数字字符串，可用于生成记录ID
     * 
     * @since
     * @param number
     * @param length
     * @return
     */
    public static String genNumStr(int number, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        // 设置是否使用分组
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(length);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(length);
        return nf.format(number);
    }

}
