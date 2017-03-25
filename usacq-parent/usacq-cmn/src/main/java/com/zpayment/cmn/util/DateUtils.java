/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */

package com.zpayment.cmn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.Integer;
import java.sql.Timestamp;

/**
 * 时间格式化工具类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class DateUtils {

	/** 时间格式，到秒 */
	private static final String PATTERN_TIME = "yyyyMMdd HH:mm:ss";

	/** 日期格式 */
	private static final String PATTERN_DATE = "yyyyMMdd";

	/** 星期字符串 */
	private static final String[] weekDays = { "SUN", "MON", "TUES", "WED",
			"THUR", "FRI", "SATS" };

	/**
	 * 转换成时间格式
	 * 
	 * @since
	 * @param date
	 * @return
	 */
	public static String formatTime(Date time) {
		return format(PATTERN_TIME, time);
	}

	/**
	 * 解析时间
	 * 
	 * @since
	 * @param time
	 * @return
	 */
	public static Date parseTime(String time) {
		return parse(PATTERN_TIME, time);
	}

	/**
	 * 转换成日期格式
	 * 
	 * @since
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return format(PATTERN_DATE, date);
	}

	/**
	 * 解析日期
	 * 
	 * @since
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date) {
		return parse(PATTERN_DATE, date);
	}

	/**
	 * date to time
	 * 
	 * @since
	 * @param date
	 * @return
	 */
	public static String format(String pattern, Date time) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(time);
	}

	/**
	 * string to date
	 * 
	 * @since
	 * @param date
	 * @return
	 */
	public static Date parse(String pattern, String time) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(time);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取月份
	 * 
	 * @since
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static String getMonth(String yyyymmddStr) {
		return yyyymmddStr.substring(4, 6);
	}

	public static String getDay(String yyyymmddStr) {
		return yyyymmddStr.substring(6, 8);
	}

	public static String getYYMMDD(String yyyymmddStr) {
		return yyyymmddStr.substring(2, 8);
	}

	/**
	 * 生成国业平台常用当前时间字符串，格式为yyyyMMdd
	 * 
	 * 
	 * @since
	 * @return
	 */
	public static String getCurrentDateStr() {
		return format(PATTERN_DATE, new Date());
	}

	/**
	 * 下一天时间字符串
	 * 
	 * 
	 * @since
	 * @param dateStr
	 * @return
	 */
	public static String nextDateStr(String dateStr) {

		return addDateStr(dateStr, 1);

	}

	/**
	 * 日期增减
	 * 
	 * 
	 * @since
	 * @param dateStr
	 * @return
	 */
	public static String addDateStr(String dateStr, Integer days) {

		Date preDate = parse("yyyyMMdd", dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(preDate);
		calendar.add(Calendar.DATE, days);
		Date curDate = calendar.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(curDate);

	}

	/**
	 * 两日期相差的天数 dateStr1 > dateStr2 返回正数，dateStr1 < dateStr2 返回负数，相等返回0
	 * 
	 * @since
	 * @param dateStr
	 * @return
	 */
	public static Integer getDateDiff(String dateStr1, String dateStr2) {

		Date d1 = DateUtils.parse("yyyyMMdd", dateStr1);
		Date d2 = DateUtils.parse("yyyyMMdd", dateStr2);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d1);
		long d1Millis = calendar.getTimeInMillis();
		calendar.setTime(d2);
		long d2Millis = calendar.getTimeInMillis();
		Integer days = new Integer(
				(int) ((d1Millis - d2Millis) / (1000 * 60 * 60 * 24)));
		return days;

	}

	/**
	 * 获取月份的天数
	 * 
	 * @since
	 * @param dt
	 * @return
	 */
	public static Integer getDaysOfMonth(String dateStr) {
		Date dt = parse("yyyyMMdd", dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		Integer days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	/**
	 * 获取当前星期字符串，取值分别为：
	 * 
	 * @since
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 获取当前星期索引：
	 * 
	 * @since
	 * @param dt
	 * @return
	 */
	public static int getWeekdayIdx(String dateStr) {
		Date dt = parse(PATTERN_DATE, dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}

	/**
	 * 获取当前时间的毫秒数：
	 * 
	 * @since
	 * @param dt
	 * @return
	 */
	public static long getTimeMillis() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		long l = cal.getTimeInMillis();
		return l;
	}

	/**
	 * 获取当前时间戳
	 * 
	 * @since
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 修改时间
	 * 
	 * @param toModTime
	 * @param tu
	 *            时间单元
	 * @param amount
	 *            时间值
	 * @return
	 */
	public static Timestamp modTime(Timestamp toModTime, int tu, int amount) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(toModTime);
		cl.add(tu, amount);
		return new Timestamp(cl.getTimeInMillis());
	}

	/**
	 * 转换timestamp到string
	 * 
	 * @since
	 * @param dt
	 * @return
	 */
	public static String formatTs(Timestamp ts) {
		return format(PATTERN_TIME, ts);
	}
}
