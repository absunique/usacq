/*
 * 
 *  Copyright 2012, $${COMPANY} Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF $${COMPANY} CO., LTD.
 *  
 *   $Id: StringUtils.java,v 1.4 2016/09/02 03:59:36 peiwang Exp $
 *
 *  Function:
 *
 *    字符串工具类
 *
 *  Edit History:
 *
 *     2012-11-23 - Create By szwang
 *    
 *    
 */

package com.zpayment.cmn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class StringUtils {

	/** 空字符串 */
	private static final String NULL_STRING = "";
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	/**
	 * 判断字符串为null或恐字符串
	 * 
	 * @since
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}

	/**
	 * 获取非null字符串
	 * 
	 * @since
	 * @param str
	 * @return
	 */
	public static String nullSafeGet(String str) {
		return str == null ? NULL_STRING : str;
	}

	/**
	 * 判断有值且相等
	 * 
	 * @since
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean notNullEqual(String str1, String str2) {
		if (isEmpty(str1) || isEmpty(str2)) {
			return false;
		}

		return str1.equals(str2);
	}

	/**
	 * 判断是否包含
	 * 
	 * @since
	 * @param ary
	 * @param s
	 * @return
	 */
	public static boolean contains(String[] ary, String s) {
		if (ary == null || ary.length == 0 || s == null) {
			return false;
		}

		for (String s1 : ary) {
			if (s1.equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取String
	 * 
	 * @since
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj) {
		// TODO 根据类型分别处理
		if (obj == null) {
			return NULL_STRING;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj instanceof Number) {
			return obj.toString();
		}
		if (obj instanceof String[]) {
			String[] tmp = (String[]) obj;
			String result = NULL_STRING;
			for (String i : tmp) {
				result += i;
			}
			return result;
		}
		return obj.toString();
	}

	/**
	 * 判断是否为真
	 * 
	 * @since
	 * @param s
	 * @return
	 */
	public static boolean isTrue(String s) {
		if (s == null) {
			return false;
		}

		s = s.trim();
		return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("t")
				|| s.equals("1");
	}

	/**
	 * 判断是否为真
	 * 
	 * @since
	 * @param ch
	 * @return
	 */
	public static boolean isTrue(final char ch) {
		return ch == '1' || ch == 't' || ch == 'T';
	}

	/**
	 * 字符串转数组
	 * 
	 * @since
	 * @param s
	 * @param seperator
	 * @return
	 */
	public static String[] toArray(String s, String seperator) {
		return s.split(seperator);
	}

	/**
	 * 字符串数组转字符串
	 * 
	 * @since
	 * @param ary
	 * @param seperator
	 * @return
	 */
	public static String toString(String[] ary, String seperator) {
		if (ary == null) {
			return "";
		}

		int l = ary.length;
		if (l == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder(ary[0]);
		for (int i = 1; i < l; i++) {
			sb.append(seperator).append(ary[i]);
		}
		return sb.toString();
	}

	/**
	 * 字符串数组转字符串
	 * 
	 * @since
	 * @param ary
	 * @param seperator
	 * @param wrapStr
	 * @return
	 */
	public static String toString(String[] ary, String seperator, String wrapStr) {
		if (wrapStr == null) {
			return toString(ary, seperator);
		}

		int l = ary.length;
		if (l == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder(wrapStr).append(ary[0]).append(
				wrapStr);
		for (int i = 1; i < l; i++) {
			sb.append(seperator).append(wrapStr).append(ary[i]).append(wrapStr);
		}
		return sb.toString();
	}

	/**
	 * 字符串集合转字符串
	 * 
	 * @since
	 * @param strs
	 * @param seperator
	 */
	public static String toString(Collection<String> strs, String seperator) {
		if (strs == null) {
			return null;
		}

		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			if (first) {
				first = false;
			} else {
				sb.append(seperator);
			}

			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 字符串集合转字符串
	 * 
	 * @since
	 * @param strs
	 * @param seperator
	 * @param wrapStr
	 */
	public static String toString(Collection<String> strs, String seperator,
			String wrapStr) {
		if (wrapStr == null) {
			return toString(strs, seperator);
		}

		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			if (first) {
				first = false;
			} else {
				sb.append(seperator);
			}

			sb.append(wrapStr).append(s).append(wrapStr);
		}
		return sb.toString();
	}

	/**
	 * 字符串数组转列表
	 * 
	 * @since
	 * @param strAry
	 * @return
	 */
	public static List<String> toList(String[] strAry) {
		return Arrays.asList(strAry);
	}

	/**
	 * 字符串比较
	 * 
	 * @since
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int compare(String str1, String str2) {
		if (str1 == null) {
			return str2 == null ? 0 : -1;
		}

		return str2 == null ? 1 : str1.compareTo(str2);
	}

	/**
	 * 分割
	 * 
	 * @since
	 * @param str
	 * @param sepreator
	 * @return
	 */
	public static String[] split(String str, String sepreator) {
		if (str == null) {
			return null;
		}

		return str.split(sepreator);
	}

	/**
	 * 判断是否是正整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPosInt(String str) {
		if (str == null) {
			return false;
		}

		Pattern pattern = Pattern.compile("[0-9]+");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否是正double数字类型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPosDouble(String str) {
		if (str == null) {
			return false;
		}

		Pattern pattern = Pattern.compile("[+]?([1-9]\\d*|0)(\\.\\d+)?");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否是YYYYMMDD格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isYYYYMMDD(String str) {
		if (str == null) {
			return false;
		}

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			df.setLenient(false);
			df.parse(str);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 是否合法的主键
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isValidKey(String key) {
		return key == null ? false : !key.contains("'");
	}

	/**
	 * 编码html字符
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeForHTML(String str) {
		StringBuilder sb = new StringBuilder();
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&#x27;");
					break;
				case '/':
					sb.append("&#x2f;");
					break;
				default:
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 编码带换行的html字符
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeForHTMLWithBR(String str) {
		if (str == null) {
			return "";
		}

		String[] arrMsg = str.split("<br\\s*/?>");
		for (int i = 0; i < arrMsg.length; i++) {
			arrMsg[i] = encodeForHTML(arrMsg[i]);
		}
		return toString(arrMsg, "<br>");
	}

	@SuppressWarnings("rawtypes")
	public static String convMapToString(Map maps) {
		StringBuilder sb = new StringBuilder();
		for (Object ob : maps.keySet()) {
			sb.append("\nkey:" + ob);
		}
		for (Object ob : maps.keySet()) {
			sb.append("[" + maps + "]:" + maps.get(ob) + "\n");
		}

		return sb.toString();
	}

	/**
	 * 将HEX字符串转为四位的0/1字符串
	 * 
	 * @param hex
	 * @return
	 */
	public static String convHexCharToBinStr(char hex) {
		char hexUpper = Character.toUpperCase(hex);
		switch (hexUpper) {
		case '0':
			return "0000";
		case '1':
			return "0001";
		case '2':
			return "0010";
		case '3':
			return "0011";
		case '4':
			return "0100";
		case '5':
			return "0101";
		case '6':
			return "0110";
		case '7':
			return "0111";
		case '8':
			return "1000";
		case '9':
			return "1001";
		case 'A':
			return "1010";
		case 'B':
			return "1011";
		case 'C':
			return "1100";
		case 'D':
			return "1101";
		case 'E':
			return "1110";
		case 'F':
			return "1111";
		default:
			return "0000";
		}
	}

	/**
	 * 将HEX二进制值转为八位的0/1字符串
	 * 
	 * @param val
	 * @return
	 */
	public static String convHexByteToBinStr(byte val) {
		char[] buf = new char[8];
		int charPos = 8;
		int radix = 1 << 1;
		int mask = radix - 1;
		do {
			buf[--charPos] = digits[val & mask];
			val >>>= 1;
		} while (charPos > 0);
		return new String(buf);
	}

	/**
	 * 将HEX字符串转为四倍长的0/1字符串
	 * 
	 * @param hexStr
	 * @return
	 */
	public static String convHexStrToBinStr(String hexStr) {

		StringBuilder sb = new StringBuilder();
		for (char ch : hexStr.toCharArray()) {
			sb.append(convHexCharToBinStr(ch));
		}
		return sb.toString();
	}

	/**
	 * HEX字符转化为一个byte
	 * 
	 * @param c
	 * @return
	 */
	public static byte convHexCharToByte(char leftChar, char rightChar) {
		byte leftByte = (byte) "0123456789ABCDEF".indexOf(leftChar);
		byte rightByte = (byte) "0123456789ABCDEF".indexOf(rightChar);
		return (byte) ((leftByte << 4) | rightByte);
	}

	/**
	 * 将hex字符串转化为byte,长度缩短一倍
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] convHexStrToBytes(String hexStr) {

		if (hexStr == null || hexStr.equals("")) {
			return null;
		}
		hexStr = hexStr.toUpperCase();
		int length = hexStr.length() / 2;
		char[] hexChars = hexStr.toCharArray();
		byte[] retBytes = new byte[length];

		for (int i = 0; i < length; i++) {
			int pos = i * 2;

			retBytes[i] = convHexCharToByte(hexChars[pos], hexChars[pos + 1]);
		}

		return retBytes;
	}

	/**
	 * 将HEX byte[]转为四倍长的0/1字符串
	 * 
	 * @param hexStr
	 * @return
	 */
	public static String convHexByteArrayToBinStr(byte[] hexArray) {

		StringBuilder sb = new StringBuilder();
		for (byte ch : hexArray) {
			sb.append(convHexByteToBinStr(ch));
		}
		return sb.toString();
	}

	/**
	 * 将HEX byte[]转为两倍长的字符串
	 * 
	 * @param hexStr
	 * @return
	 */
	public static String convHexByteArrayToStr(byte[] b) // byte转16进制字符串
	{
		String retVal = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF).toUpperCase();
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			retVal += hex;
		}
		return retVal;
	}

	/***
	 * 将0/1字符串转换为LIST，LIST中为该位为1的位值，从1开始。
	 * 
	 * @param binStr
	 * @return
	 */
	public static List<Integer> convBinToIndexList(String binStr) {
		List<Integer> buf = new LinkedList<Integer>();
		char[] charSet = binStr.toCharArray();
		for (int index = 0; index < charSet.length; index++) {
			if (charSet[index] == '1') {
				buf.add(index + 1);
			}
		}

		return buf;
	}

	/***
	 * 将HEX字符串转换为LIST，LIST中为该位为1的位值，从1开始。
	 * 
	 * @param hexStr
	 * @return
	 */
	public static List<Integer> convHexStrToIndexList(String hexStr) {

		return convBinToIndexList(convHexStrToBinStr(hexStr));
	}

	/***
	 * 将HEX字符串转换为LIST，LIST中为该位为1的位值，从1开始。
	 * 
	 * @param hexStr
	 * @return
	 */
	public static List<Integer> convHexByteArrayToIndexList(byte[] hexArray) {

		return convBinToIndexList(convHexByteArrayToBinStr(hexArray));
	}

	/***
	 * 输入整数，获取字长串长度
	 * 
	 * @since
	 * @param value
	 * @return
	 */
	public static int calIntegerLen(int value) {
		return (value < 10 ? 1 : (value < 100 ? 2 : (value < 1000 ? 3
				: (value < 10000 ? 4 : (value < 100000 ? 5
						: (value < 1000000 ? 6 : (value < 10000000 ? 7
								: (value < 100000000 ? 8 : (9)))))))));
	}

	/****
	 * 格式化整数为STRING
	 * 
	 * @since
	 * @param value
	 * @param len
	 * @return
	 */
	public static String formatInteger(int value, int len) {
		int valueLen = calIntegerLen(value);
		if (valueLen > len) {
			return null;
		}
		int zeroNum = len - valueLen;
		char[] zeros = new char[zeroNum];
		for (int i = 0; i < zeroNum; i++) {
			zeros[i] = '0';
		}
		String zeroString = new String(zeros);
		return zeroString + len;
	}

	/****
	 * 格式化整数为BYTE[]
	 * 
	 * @since
	 * @param value
	 * @param len
	 * @return
	 */
	public static byte[] formatIntegerToByte(int value, int len) {
		int valueLen = calIntegerLen(value);
		if (valueLen > len) {
			return null;
		}
		int zeroNum = len - valueLen;
		byte[] zeros = new byte[zeroNum + valueLen];
		for (int i = 0; i < zeroNum; i++) {
			zeros[i] = '0';
		}
		char[] intChar = (value + "").toString().toCharArray();
		for (int i = 0; i < valueLen; i++) {
			zeros[i + zeroNum] = (byte) intChar[i];
		}
		return zeros;

	}

	/**
	 * 截取字符串首位空格
	 * 
	 * @since
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? "" : str.trim();
	}

	/**
	 * 判断对象为空
	 * 
	 * @since
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object msg) {
		if (msg instanceof String)
			return isEmpty((String) msg);
		return msg == null;
	}

	public static String concat(Object... pieces) {
		StringBuilder sb = new StringBuilder();
		for (Object piece : pieces) {
			sb.append(isEmpty(piece) ? "" : piece);
		}
		return sb.toString();
	}

	private static final char SEPARATOR = '_';

	/**
	 * 驼峰转下划线
	 * 
	 * @param s
	 * @return
	 */
	public static String toUnderlineName(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i >= 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					if (i > 0)
						sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 下划线转驼峰
	 * 
	 * @param s
	 * @return
	 */
	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static String toCapitalizeCamelCase(String s) {
		if (s == null) {
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/** short 类型转化为byte数组函数 */
	public static byte[] shortToByteArray(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) (n >> 8 & 0xff);
		return b;
	}

	public static byte byteAscToBcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	public static byte[] bytesAscToBcd(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = byteAscToBcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : byteAscToBcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static void main(String[] args) {
		byte[] a = { 0x01, 0x02 };
		System.out.println(convHexByteArrayToStr(a));
	}
}
