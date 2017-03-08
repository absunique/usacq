/*
 * 
 *  Copyright 2013, $${COMPANY} Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF $${COMPANY} CO., LTD.
 *  
 *   $Id: PropertyUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 *
 *  Function:
 *
 *    //TODO 请添加功能描述
 *
 *  Edit History:
 *
 *     2013-12-31 - Create By szwang
 *    
 *    
 */

package com.peiwang.usacq.cmn.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.peiwang.usacq.cmn.exp.BaseErrorCode;
import com.peiwang.usacq.cmn.exp.BaseException;
import com.peiwang.usacq.cmn.log.Logger;

/**
 * 配置文件.properties读取，封装公共组件室的 @link PropertyUtil 实现，提供基础类型转换
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class PropertyUtils {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(PropertyUtils.class);

	/**
	 * 获取所有配置
	 * 
	 * @param propFile
	 * @return
	 */
	public static Map<String, String> getProperties(String propFile) {
		Properties p = getProps(propFile);

		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Iterator<Map.Entry<Object, Object>> itr = p.entrySet().iterator(); itr
				.hasNext();) {
			Map.Entry<Object, Object> entry = itr.next();
			Object key = entry.getKey();
			if (key == null) {
				continue;
			}
			Object value = entry.getValue();
			map.put(key.toString().trim(), (value == null ? "" : value
					.toString().trim()));
		}

		return map;
	}

	/**
	 * 获取指定文件的属性值
	 * 
	 * @since
	 * @param propFile
	 *            形如com/cup/**.properties
	 * @param key
	 * @return
	 */
	public static String getString(String propFile, String key) {
		Properties p = getProps(propFile);
		String v = p.getProperty(key);
		return v == null ? null : v.trim();
	}

	/**
	 * 获取指定文件的属性
	 * 
	 * @since
	 * @param propFile
	 *            形如com/cup/**.properties
	 * @param key
	 * @param defValue
	 *            缺省值
	 * @return
	 */
	public static String getString(String propFile, String key, String defValue) {
		String v = null;
		try {
			v = getString(propFile, key);
		} catch (Exception e) {
			// do nothing
		}
		return v == null ? defValue : v;
	}

	/**
	 * 获取指定文件的属性值，整型值返回
	 * 
	 * @since
	 * @param propFile
	 *            形如com/cup/**.properties
	 * @param key
	 * @return
	 */
	public static int getInt(String propFile, String key) {
		try {
			String v = getString(propFile, key);
			return Integer.parseInt(v);
		} catch (Exception e) {
			log.error("property not found or invalid value, file: " + propFile
					+ ", key: " + key);
			throw new BaseException(BaseErrorCode.COMN_INVALID_PROPERTY);
		}
	}

	/**
	 * 获取指定文件的属性值，整型值返回
	 * 
	 * @since
	 * @param propFile
	 *            形如com/cup/**.properties
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(String propFile, String key, int defValue) {
		try {
			return getInt(propFile, key);
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 获取指定文件的属性值，整型值返回
	 * 
	 * @since
	 * @param propFile
	 *            形如com/cup/**.properties
	 * @param key
	 * @return
	 */
	public static long getLong(String propFile, String key) {
		try {
			String v = getString(propFile, key);
			return Long.parseLong(v);
		} catch (Exception e) {
			log.error("property not found or invalid value, file: " + propFile
					+ ", key: " + key);
			throw new BaseException(BaseErrorCode.COMN_INVALID_PROPERTY);
		}
	}

	/**
	 * 获取指定文件的属性值，整型值返回
	 * 
	 * @since
	 * @param propFile
	 *            形如com/cup/**.properties
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(String propFile, String key, long defValue) {
		try {
			return getLong(propFile, key);
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 按propFile获取资源
	 * 
	 * @since
	 * @param propFile
	 *            形如com/cup/**.properties
	 * @return
	 */
	public static Properties getProps(String propFile) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(propFile);
			Properties p = new Properties();
			p.load(is);
			return p;
		} catch (IOException e) {
			log.error("file not found, file: " + propFile, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NOT_EXIST);
		} catch (Exception e) {
			log.error("file not found, file: " + propFile, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NOT_EXIST);
		}
	}
}
