/*
 * 
 * Copyright 2015, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: ByteArrayUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2015年3月18日 - Create By wangshuzhen
 */

package com.peiwang.usacq.cmn.util;

import java.nio.charset.Charset;


/**
 * 字节数组工具类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class ByteArrayUtils {

    private static final int                      MASK =  0xFF;
    
    private static final Charset DEFAULT_CHARSET  = Charset.forName("ISO-8859-1");
    
    /**
     * byte[]转String
     * 
     * @since 
     * @param bytes
     * @param offset
     * @param len
     * @return
     */
    public static String byteArrayToString(byte[] bytes, int offset, int len) {
        return new String(bytes, offset, len, DEFAULT_CHARSET);
    }
    
    /**
     * String转byte[]
     * 
     * @since 
     * @param str
     * @return
     */
    public static byte[] stringToByteArray(String str) {
        byte[] bb = str.getBytes(DEFAULT_CHARSET);
        return bb;
    }

    /**
     * int转byte[]
     * 
     * @since 
     * @param i
     * @param len
     * @return
     */
    public static byte[] intToByteArray(int i, int len) {
        byte[] bb = new byte[len];
        
        for (int j = 0, c = Math.min(4, len); j < c; j++) {
            bb[j] = (byte) ((i >> ((c - j - 1) * 8)) & MASK);
        }
        return bb;
    }
    
    /**
     * long转byte[]
     * 
     * @since 
     * @param l
     * @param len
     * @return
     */
    public static byte[] longToByteArray(long l, int len) {
        byte[] bb = new byte[len];
        
        for (int j = 0, c = Math.min(8, len); j < c; j++) {
            bb[j] = (byte) ((l >> ((c - j - 1) * 8)) & MASK);
        }
        return bb;
    }

    /**
     * byte[]转int
     * 
     * @since
     * @param bb
     * @param offset
     * @return
     */
    public static int byteArrayToInt(byte[] bb, int offset, int len) {
        int v = 0;
        for (int i = 0, c = Math.min(4, len); i < c; i++) {
            v <<= 8;
            v |= (bb[offset + i] & MASK);
        }
        return v;
    }

    /**
     * byte[]转long
     * 
     * @since
     * @param bb
     * @param offset
     * @param len
     * @return
     */
    public static long byteArrayToLong(byte[] bb, int offset, int len) {        
        long v = 0;
        for (int i = 0, c = Math.min(8, len); i < c; i++) {
            v <<= 8;
            v |= (bb[offset + i] & MASK);
        }
        return v;
    }
    
    /**
     * byte[] 转 16进制表示的字符串
     * 
     * @since 
     * @param bb
     * @param offset
     * @param len
     * @return
     */
    public static String byteArrayToHexString(byte[] bb, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < len; i++) {
            byte b = bb[offset + i];
            sb.append(Integer.toHexString(b & MASK).toUpperCase());
        }
        
        return sb.toString();
    }
    
    /**
     * byte[] 转 二进制表示的字符串
     * 
     * @since 
     * @param bb
     * @param offset
     * @param len
     * @return
     */
    public static String byteArrayToBinaryString(byte[] bb, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < len; i++) {
            byte b = bb[offset + i];
            for (int j = 0; j < 8; j++) {
                if (((b >> (8 - j - 1)) & 1) == 1) {
                    sb.append('1');
                } else {
                    sb.append('0');
                }
            }
        }
        
        return sb.toString();
    }
          
    /**
     * 二进制格式字符串转byte[]
     * 
     * @since 
     * @param binStr
     * @return
     */
    public static byte[] binaryStringToByteArray(String binStr) {
        int l = 0;
        if (!StringUtils.isEmpty(binStr)) {
            l = binStr.length();
        }
        
        if (l % 8 != 0) {
            throw new IllegalArgumentException("invalid binary string[" + binStr +"]");
        }
        
        int c = l / 8;
        int idx = 0;
        byte[] bb = new byte[c];
        for (int i = 0; i < c; i++) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                char ch = binStr.charAt(idx++);
                if(ch == '1') {
                    b += 1 << (8 - j - 1);
                }                
            }
            bb[i] = b;
        }
        return bb;
    }
    
    /**
     * 16进制格式字符串转byte[]
     * 
     * @since 
     * @param hexStr
     * @return
     */
    public static byte[] hexStringToByteArray(String hexStr) {
        int l = 0;
        if (!StringUtils.isEmpty(hexStr)) {
            l = hexStr.length();
        }
        
        if (l % 2 != 0) {
            throw new IllegalArgumentException("invalid hex string[" + hexStr +"]");
        }
        
        int c = l / 2;
        int idx = 0;
        byte[] bb = new byte[c];
        for (int i = 0; i < c; i++) {
            byte b = 0;
            for (int j = 0; j < 2; j++) {
                b <<= 4;
                
                char ch = hexStr.charAt(idx++);
                if (ch <= '9' && ch >= '0') {
                    b |= (byte)(ch - '0');
                } else if (ch <= 'F' && ch >= 'A') {
                    b |= (byte)(ch - 'A' + 10);
                } else if (ch <= 'f' && ch >= 'a') {
                    b |= (byte)(ch - 'a' + 10);
                } else {
                    throw new IllegalArgumentException("invalid hex string[" + hexStr +"]");
                }                
            }
            bb[i] = b;
        }
        return bb;
    }
    
    /**
     * 判断二进制指定位是否为真
     * 
     * @since
     * @param bytes
     * @param offset
     * @param binIdx 0-base
     * @return
     */
    public static boolean isBinaryIn(byte[] bytes, int offset, int binIdx) {
        if (binIdx < 0) {
            return false;
        }

        int i = binIdx / 8;
        int j = binIdx % 8;
        if (offset + i >= bytes.length) {
            return false;
        }

        byte b = bytes[offset + i];
        if (((b >> (8 - j - 1)) & 1) == 1) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        byte[] bb = new byte[] { (byte) (0x70), (byte) (0xFF), (byte) (0xFF), (byte) (0xFF) };
        print(bb);
        
        String binStr = byteArrayToHexString(bb, 0, 4);
        System.out.println("hex string: " + binStr);
        
        byte[] bb3 = hexStringToByteArray(binStr);
        print(bb3);
        

//        
//        int i = byteArrayToInt(bb, 0, 2);
//        System.out.println(i);
//        
//        byte[] bb1 = intToByteArray(i, 2);
//        print(bb1);
//        
//        long l = byteArrayToLong(bb, 0, 4);
//        System.out.println(l);
//        
//        byte[] bb2 = longToByteArray(l, 4);
//        print(bb2);
    }
    
    private static void print(byte[] bb) {
        for (int i = 0; i < bb.length; i++) {
            System.out.print(Integer.toHexString((bb[i] & MASK)) + " ");
        }
        System.out.println();
    }
}
