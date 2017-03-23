/*
 * 
 *  Copyright 2014, China UnionPay Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF CHINA UNIONPAY CO., LTD.
 *  
 *   $Id: MappingStructFactory.java,v 1.1 2016/08/04 23:15:22 peiwang Exp $
 *
 *  Function:
 *
 *    //TODO 请添加功能描述
 *
 *  Edit History:
 *
 *     2014-1-8 - Create By szwang
 *    
 *    
 */

package com.zpayment.cmn.persistent.jdbc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zpayment.cmn.exp.BaseException;




/**
 * @link MappingStruct 工厂类，用于创建并缓存映射实体
 * 
 * @author gys
 * @version 
 * @since  
 * 
 */
public class MappingStructFactory {
    
    /**
     * 类与映射机构体的关系
     */
    private static Map<String, MappingStruct> classColumnFiledsMap = new ConcurrentHashMap<String, MappingStruct>();
    
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
    public static MappingStruct create(Class<?> entityClass) throws BaseException {
        String className = entityClass.getName();

        MappingStruct ms = classColumnFiledsMap.get(className);
        if (ms != null) {
            return ms;
        }

        ms = MappingStruct.create(entityClass);

        classColumnFiledsMap.put(className, ms);
        return ms;
    }
}
