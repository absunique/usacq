/*
 * 
 * Copyright 2012, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: ClassUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * Class工具类
 * 
 * Edit History:
 * 
 * 2012-11-23 - Create By szwang
 * 
 * 
 */
package com.zpayment.cmn.util;

/**
 * Class工具类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class ClassUtils {

	/**
	 * 根据
	 * 
	 * @since
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object newInstance(String name)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class<?> clazz = forName(name);
		return clazz.newInstance();
	}

	/**
	 * 实例化对象
	 * 
	 * @since
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T newInstance(Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}

	/**
	 * 根据类名加载类
	 * 
	 * @since
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> forName(String name) throws ClassNotFoundException {
		ClassLoader classLoader = getDefaultClassLoader();
		try {
			return classLoader.loadClass(name);
		} catch (ClassNotFoundException ex) {
			int lastDotIndex = name.lastIndexOf('.');
			if (lastDotIndex != -1) {
				// 处理内部类
				String innerClassName = name.substring(0, lastDotIndex) + '$'
						+ name.substring(lastDotIndex + 1);
				try {
					return classLoader.loadClass(innerClassName);
				} catch (ClassNotFoundException ex2) {
				}
			}
			throw ex;
		}
	}

	/**
	 * 获取缺省的ClassLoader<br>
	 * 先从当前线程环境获取，如果取不到，直接取加载当前类的ClassLoader。<br>
	 * 
	 * @since
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			cl = ClassUtils.class.getClassLoader();
		}
		return cl;
	}
}
