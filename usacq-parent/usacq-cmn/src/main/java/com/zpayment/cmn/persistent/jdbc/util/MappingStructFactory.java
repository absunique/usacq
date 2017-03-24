/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc.util;

import java.util.concurrent.ConcurrentHashMap;

import com.zpayment.cmn.exp.BaseException;

/**
 * mapping struct 工厂
 * 
 * @author peiwang
 * @since 2017年3月23日
 */
public class MappingStructFactory {

	/**
	 * 类与映射机构体的关系
	 */
	private static ConcurrentHashMap<String, MappingStruct> classColumnFiledsMap = new ConcurrentHashMap<String, MappingStruct>();

	/**
	 * 获取列名与字段的映射关系，先从缓存中获取
	 * 
	 * @since
	 * @param entity
	 * @return
	 */
	public static MappingStruct create(Object entity) throws BaseException {
		return create(entity.getClass());
	}

	/**
	 * 获取列名与字段的映射关系，先从缓存中获取
	 * 
	 * @since
	 * @param elementType
	 * @return
	 */
	public static MappingStruct create(Class<?> entityClass)
			throws BaseException {
		String className = entityClass.getName();

		MappingStruct ms = classColumnFiledsMap.get(className);
		if (ms != null) {
			return ms;
		}

		ms = MappingStruct.create(entityClass);

		classColumnFiledsMap.putIfAbsent(className, ms);
		return classColumnFiledsMap.get(className);
	}
}
