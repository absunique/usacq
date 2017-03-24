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

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.persistent.jdbc.param.BinaryParam;
import com.zpayment.cmn.persistent.jdbc.param.DecimalParam;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;
import com.zpayment.cmn.persistent.jdbc.param.StringParam;
import com.zpayment.cmn.persistent.jdbc.param.TimestampParam;

/**
 * format方式构造器
 * <p>
 * 格式化参数说明：<br>
 * %S 普通字符串，用于替换非值部分；<br>
 * %s 字符串值，数组与集合配合in/notin使用<br>
 * %d 整型值，数组与集合配合in/notin使用 <br>
 * %t 时间戳 <br>
 * %b 二进制，仅支持十六进制字符串形式<br>
 * 
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public final class PSQLFormat extends BaseBuilder implements
		SingleParamsBuilder {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(PSQLFormat.class);

	/** 关键字匹配 */
	private static final String FORMAT = "%([S|s|d|t|b])?";

	// /**
	// * 格式化SQL语句
	// *
	// * @since
	// * @param format
	// * @param args
	// * @return
	// * @throws BaseException
	// */
	// public static PreparedSQL format(String format, Object... args)
	// throws BaseException {
	// PSQLFormat sqlFormat = build(format, args);
	// return sqlFormat.toPreparedSQL();
	// }

	public static PSQLFormat build(String format, Object... args) throws BaseException {
		PSQLFormat sqlFormat = new PSQLFormat(format, args);
		return sqlFormat;
	}

	private List<ArgFormat> formats = new LinkedList<ArgFormat>();

	private StringBuilder sqlBuilder = new StringBuilder();

	private List<SqlParam> params = new LinkedList<SqlParam>();

	/**
	 * 构造函数，按样式解析并替换
	 * 
	 * @param format
	 * @param args
	 * @throws BaseException
	 */
	PSQLFormat(String format, Object... args) throws BaseException {
		// format解析
		Pattern p = Pattern.compile(FORMAT);
		Matcher m = p.matcher(format);
		int i = 0;
		while (i < format.length()) {
			if (m.find(i)) {
				if (m.start() != i) {
					String s = format.substring(i, m.start());
					formats.add(new FixedString(s));
				}

				String type = m.group(1);
				if (type == null) {
					log.error("invliad type[unkown], fomrat[%s]",
							new Object[] { format });
					throw new BaseException(
							BaseErrorCode.COMN_DATA_INVALID_FOMAT_TYPE,
							new Object[] { type });
				}

				char tpCh = type.charAt(0);
				switch (tpCh) {
				case 'S':
					formats.add(new StringFormat());
					break;
				case 's':
					formats.add(new ValueFormat(type, new StringValueFormat()));
					break;
				case 'd':
					formats.add(new ValueFormat(type, new DecimalValueFormat()));
					break;
				case 't':
					formats.add(new ValueFormat(type,
							new TimestampValueFormat()));
					break;
				case 'b':
					formats.add(new ValueFormat(type, new BinaryValueFormat()));
					break;
				default:
					log.error("invliad type[%s], fomrat[%s]", new Object[] {
							type, format });
					throw new BaseException(
							BaseErrorCode.COMN_DATA_INVALID_FOMAT_TYPE,
							new Object[] { type });
				}
			} else {
				String s = format.substring(i);
				formats.add(new FixedString(s));
				break;
			}

			i = m.end();
		}

		// 参数个数检查
		int argCnt = args == null ? 0 : args.length;
		int fArgCnt = 0;
		for (ArgFormat f : formats) {
			if (f.isArg()) {
				fArgCnt++;
			}
		}
		if (fArgCnt != argCnt) {
			log.error(
					"invalid arg count[%s], format arg count[%s], format[%s]",
					new Object[] { argCnt, fArgCnt, format });
			throw new BaseException(
					BaseErrorCode.COMN_DATA_INVALID_FOMAT_ARG_CNT);
		}

		this.params.clear();

		// 参数值替换
		int index = 0;
		for (ArgFormat f : formats) {
			if (f.isArg()) {
				f.format(args[index++]);
			} else {
				f.format(null);
			}
		}
	}

	@Override
	public String getSql() {
		return sqlBuilder.toString();
	}

	@Override
	public List<SqlParam> getParams() {
		List<SqlParam> list = new LinkedList<SqlParam>();
		if (params != null) {
			for (SqlParam sp : params) {
				if (sp.isPreparedParam()) {
					list.add(sp);
				}
			}
		}
		return list;
	}

	private interface ArgFormat {
		/**
		 * 是否是参数
		 * 
		 * @since
		 * @return
		 */
		boolean isArg();

		/**
		 * 参数替换
		 * 
		 * @since
		 * @param arg
		 * @throws BaseException
		 */
		void format(Object arg) throws BaseException;
	}

	/** 固定字符串，不需要替换 */
	private class FixedString implements ArgFormat {

		private String s;

		FixedString(String s) {
			this.s = s;
		}

		@Override
		public boolean isArg() {
			return false;
		}

		@Override
		public void format(Object arg) throws BaseException {
			sqlBuilder.append(s);
		}
	}

	/** %S 字符串替换，不涉及值 */
	private class StringFormat implements ArgFormat {
		@Override
		public boolean isArg() {
			return true;
		}

		@Override
		public void format(Object arg) throws BaseException {
			if (arg == null) {
				throw new BaseException(
						BaseErrorCode.COMN_DATA_INVALID_FOMAT_VALUE,
						new Object[] { "%S", arg });
			}

			sqlBuilder.append(arg.toString());
		}
	}

	/** 值解析与替换，用于统一处理列表与数据参数 */
	private class ValueFormat implements ArgFormat {

		private String type;
		private ArgFormat format;

		ValueFormat() {
		}

		ValueFormat(String type, ArgFormat format) {
			this.type = type;
			this.format = format;
		}

		@Override
		public boolean isArg() {
			return true;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void format(Object arg) throws BaseException {
			if (arg == null) {
				throw new BaseException(
						BaseErrorCode.COMN_DATA_INVALID_FOMAT_VALUE,
						new Object[] { "%" + type, arg });
			}

			Class clazz = arg.getClass();
			if (clazz.isArray()) { // 数组处理
				sqlBuilder.append("(");

				int length = Array.getLength(arg);
				for (int i = 0; i < length; i++) {
					if (i != 0) {
						sqlBuilder.append(",");
					}

					Object obj = Array.get(arg, i);
					doFormat(obj);
				}

				sqlBuilder.append(")");
			} else if (arg instanceof Collection) {// 集合处理
				Collection collection = (Collection) arg;
				sqlBuilder.append("(");

				boolean first = true;
				for (Object obj : collection) {
					if (first) {
						first = false;
					} else {
						sqlBuilder.append(",");
					}

					doFormat(obj);
				}

				sqlBuilder.append(")");
			} else {
				// 普通对象
				doFormat(arg);
			}
		}

		private void doFormat(Object arg) throws BaseException {
			if (arg == null) {
				throw new BaseException(
						BaseErrorCode.COMN_DATA_INVALID_FOMAT_VALUE,
						new Object[] { "%" + type, arg });
			}

			format.format(arg);
		}
	}

	/** %s 字符串值替换 */
	private class StringValueFormat extends ValueFormat {

		@Override
		public void format(Object arg) throws BaseException {
			SqlParam param = new StringParam(arg.toString());
			params.add(param);

			sqlBuilder.append(param.getPlaceHolder());
		}
	}

	/** %d 整型值替换 */
	private class DecimalValueFormat extends ValueFormat {

		@Override
		public void format(Object arg) throws BaseException {
			try {
				long v = Long.parseLong(arg.toString());
				SqlParam param = new DecimalParam(v);
				params.add(param);

				sqlBuilder.append(param.getPlaceHolder());
			} catch (Exception e) {
				throw new BaseException(
						BaseErrorCode.COMN_DATA_INVALID_FOMAT_VALUE,
						new Object[] { "%d", arg });
			}
		}
	}

	/** %t 时间替换，支持字符串与时间戳 */
	private class TimestampValueFormat extends ValueFormat {

		@Override
		public void format(Object arg) throws BaseException {
			if (arg instanceof Timestamp) {
				SqlParam param = new TimestampParam((Timestamp) arg);
				params.add(param);
				sqlBuilder.append(param.getPlaceHolder());
			} else if (arg instanceof String) {
				SqlParam param = new TimestampParam((String) arg);
				sqlBuilder.append(param.getPlaceHolder());
			} else {
				throw new BaseException(
						BaseErrorCode.COMN_DATA_INVALID_FOMAT_VALUE,
						new Object[] { "%t", arg });
			}
		}
	}

	/** %b 时间替换，二进制（十六进制字符串） */
	private class BinaryValueFormat extends ValueFormat {

		@Override
		public void format(Object arg) throws BaseException {
			if (arg instanceof String) {
				BinaryParam param = new BinaryParam((String) arg);
				params.add(param);
				sqlBuilder.append(param.getPlaceHolder());
			} else {
				throw new BaseException(
						BaseErrorCode.COMN_DATA_INVALID_FOMAT_VALUE,
						new Object[] { "%t", arg });
			}
		}
	}
}
