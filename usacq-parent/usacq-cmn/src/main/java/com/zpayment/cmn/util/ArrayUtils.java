/*
 * 
 * Copyright 2014, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: ArrayUtils.java,v 1.1 2016/08/04 23:15:22 peiwang Exp $
 * 
 * Function:
 * 
 * 数组操作工具
 * 
 * Edit History:
 * 
 * 2014-1-26 - Create By CUPPC
 */

package com.zpayment.cmn.util;

import java.util.ArrayList;
import java.util.List;

import com.zpayment.cmn.log.Logger;

/**
 * 数组操作工具
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public class ArrayUtils {

    /** 用于日志记录的Logger */
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(ArrayUtils.class);

    /**
     * 打印列表字符串
     * 
     * @since
     * @param list
     * @return
     */
    public static <T> String toString(List<T> list) {
        if (list == null || list.size() == 0) {
            return "";
        } else {
            return list.toString();
        }
    }

    /**
     * 打印数组字符串
     * 
     * @since
     * @param array
     * @return
     */
    public static <T> String toString(T[] array) {
        if (array == null || array.length == 0) {
            return "none";
        } else {
            StringBuilder rslt = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                rslt.append(array[i].toString() + " ");
            }
            return rslt.toString();
        }
    }

    /**
     * 打印数组字符串
     * 
     * @since
     * @param array
     * @return
     */
    public static String toString(byte[] array) {
        if (array == null || array.length == 0) {
            return "none";
        } else {
            StringBuilder rslt = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                rslt.append(new Byte(array[i]).toString() + " ");
            }
            return rslt.toString();
        }
    }

    /***
     * 生成指char[]数组，每个char值指定为一样
     */
    public static char[] getCharArray(int len, char ch) {
        char[] ret = new char[len];
        for (int i = 0; i < len; i++) {
            ret[i] = ch;
        }
        return ret;
    }

    /***
     * 生成指char[]数组，每个char值指定为一样
     */
    public static byte[] getByteArray(int len, byte ch) {
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = ch;
        }
        return ret;
    }

    /**
     * 将数组转换为LIST
     * 
     * @since
     * @param val
     * @return
     */
    public static List<Integer> toIntList(int[] val) {
        if (null == val || val.length == 0) {
            return new ArrayList<Integer>();
        }
        List<Integer> ls = new ArrayList<Integer>();
        for (int v : val) {
            ls.add(new Integer(v));
        }

        return ls;
    }
}
