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

package com.zpayment.cmn;

import java.sql.Timestamp;

public abstract class Const {

	/** 序列化版本号 */
	public static final long VERSION_ID = 1L;

	/** 公共模块配置文件 */
	public static final String COMMON_CONFIG_FILE = "config/common-config.properties";

	/** 列名分隔符号 */
	public static final String COLUMN_SEPARATOR = ",";

	/** 表-列连接符 */
	public static final String COLUMN_POINT = ".";

	/** *通配符 */
	public static final String COMN_MATCH = "*";

	/** 主键标识的Key */
	public static final String KEY_STRING = "KEY_STRING";

	public static final String CONN_FACTRY_NAME = "JmsXA";

	/** BOOLEAN STRING */
	public static final String BOOL_TRUE = "1";
	public static final String BOOL_FALSE = "0";

	/**
	 * 数据库类型
	 * 
	 * @author gys
	 * 
	 */
	public static abstract class DbType {

		public static final String DB2 = "db2";
		public static final String MYSQL = "mysql";
		public static final String ORACLE = "oracle";
		public static final String DEfAULT = DB2;
	}

	/**
	 * bool类型
	 */
	public static abstract class BoolType {

		public static final String FALSE = "0";
		public static final String TRUE = "1";

		public static final boolean getBoolean(String boolStr) {
			return boolStr != null && boolStr.equals(TRUE);
		}
	}

	/**
	 * 字段控件类型
	 */
	public static abstract class WidgetType {

		public static final String TEXT = "00"; // 文本输入框
		public static final String DROP_DOWN = "01"; // 下拉框
		public static final String MULTI_SELECT = "02"; // 单选
		public static final String SINGLE_SELECT = "03"; // 多选
		public static final String DATE = "04"; // 日期
		public static final String AUTO_COMPLETE = "08"; // 自动完成
		public static final String BITMAP_BIN = "61"; // 二进制位图
		public static final String BITMAP_HEX = "64"; // 十六进制位图
	}

	/**
	 * 字段数据类型
	 */
	public static abstract class DataType {

		public static final String NONE = ""; // 忽略，按 实际类型操作
		public static final String STRING = "00";
		public static final String INT = "01";
		public static final String TIMESTAMP = "02";
		public static final String FLOAT = "03";
		public static final String DECIMAL = "04";
		public static final String BINARY = "05";

		/**
		 * 判断是否未定义数据类型
		 * 
		 * @since
		 * @param dataType
		 * @return
		 */
		public static boolean isNull(String dataType) {
			return dataType == null || DataType.NONE.equals(dataType);
		}

		/**
		 * nullSafeGet
		 * 
		 * @since
		 * @param dataType
		 * @return
		 */
		public static String nullSafeGet(String dataType) {
			return dataType == null ? NONE : dataType;
		}
	}

	/**
	 * 字段缺省值定义
	 */
	public static abstract class DefaultValue {

		public final static String ZERO = "0";
		public final static String NULL = "";
		public final static String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
		public final static Timestamp CURRENT_TS = null;
		public final static Timestamp EMPTY_TS = null;
		public final static int INT_ZERO = 0;
	}

	/**
	 * 字段显示类型
	 */
	public static abstract class ShowType {

		public static final String HIDDEN = "0"; // 不显示
		public static final String READONLY = "1"; // 只读
		public static final String EDITABLE = "2"; // 可读可写
	}

	/**
	 * 下拉框类型
	 */
	public static abstract class DrdlType {

		public static final String STATIC = "1"; // 纯静态下拉框，数据从菜单项配置表获取
		public static final String DYNAMIC = "2"; // 纯动态下拉框，根据SQL查询获取
		public static final String COMBINE = "3"; // 静态与动态组合下拉框，同时从两部分获取数据
		public static final String LOGIC = "4"; // 逻辑下拉框，根据制定代码动态获取
		public static final String REALTIME = "5"; // 实时下拉菜单
	}

	/**
	 * 校验类型
	 */
	public static abstract class VerifyType {

		public static final String TEXT = "0"; // 文本
		public static final String DIGIT = "1"; // 数字
		public static final String TIME = "2"; // 日期,时间
		public static final String EXPRESSION = "3"; // 表达式，目前为正则表达式
		public static final String VRF_CLASS = "4"; // 指定校验类
		public static final String AT = "5"; // 金额
	}

	/**
	 * 校验类型
	 */
	public static abstract class EventType {

		public static final String VALUE_CHANGE = "01"; // 值变化
	}

	/**
	 * 下拉框项排序类型
	 */
	public static abstract class DrdlSortType {

		public static final String KEY_STRING = "1"; // 按key的字符排序
		public static final String KEY_INT = "2"; // 按key的整形值排序
		public static final String VALUE_STRING = "3"; // 按值的字符串排序
	}

	/**
	 * 金额类显示特殊字符
	 */
	public static abstract class AtPreciChara {

		public static final String POINT = ".";
		public static final String COMMA = ",";
		public static final String ZERO = "0";

	}

	/** 文件后缀 */
	public static abstract class FileSuffix {

		public static final String TXT = ".txt";
		public static final String EXCEL_03 = ".xls";
		public static final String EXCEL_07 = ".xlsx";
		public static final String ZIP = ".zip";
		public static final String TMP = ".tmp";

	}

	/** 缓存类型 */

	public static abstract class CacheTp {

		public static final int DATA_TYPE_LIST = 0; // 平铺数据
		public static final int DATA_TYPE_MAP = 1; // 按键值对建议索引，支持多套索引
		public static final int DATA_TYPE_TREE = 2; // 树结构
	}

	/** 缓存SQL查询方式 */

	public static abstract class CacheQueryTp {

		public static final int JDBC_SQL_ALL = 0; // 使用JDBC原生查询
		public static final int JPA_HQL_ALL = 1; // 使用JPA-HQL语言查询
		public static final int QUERY_FREE = 2; // 使用自定义SQL查询
	}
}
