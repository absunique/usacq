/*
 * 
 * Copyright 2013, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: Config.java,v 1.1 2016/08/30 07:28:19 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2013-1-14 - Create By peiwang
 */

package com.zpayment.cmn;

import com.zpayment.cmn.log.Level;
import com.zpayment.cmn.util.PropertyUtils;

/**
 * 公共功能所需配置
 * 
 * @author gys
 * @version
 * @since
 * 
 */
public final class Config {
	/**
	 * 判断是否upjas2.x
	 * 
	 * @return
	 */
	public static final boolean isUpjas2() {
		String upjasVersion = getUpjasVersion();
		return upjasVersion != null && upjasVersion.trim().startsWith("2");
	}

	/**
	 * 获取upjas版本号
	 * 
	 * @return
	 */
	public static final String getUpjasVersion() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"upjas.version");
	}

	/**
	 * 获取EAR名
	 * 
	 * @since
	 * @return
	 */
	public static final String getEarName() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE, "ear.name");
	}

	/**
	 * 获取EJB名
	 * 
	 * @since
	 * @return
	 */
	public static final String getEjbName() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE, "ejb.name");
	}

	/**
	 * 获取数据库产品名
	 * 
	 * @since
	 * @return
	 */
	public static String getDbType() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE, "db.type",
				"db2");
	}

	/**
	 * 获取管理库的数据源Jndi名
	 * 
	 * @since
	 * @return
	 */
	public static String getMgmDsJndi() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE, "ds.jndi");
	}

	/**
	 * 获取缓存表名
	 * 
	 * @since
	 * @return
	 */
	public static String getPojoViewDefTable() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"view.def.table");
	}

	/**
	 * 获取对象缓存表名
	 * 
	 * @since
	 * @return
	 */
	public static String getDbViewDefTable() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"view.pojo.def.table");
	}

	/**
	 * 获取参数维护任务的表名
	 * 
	 * @since
	 * @return
	 */
	public static String getMainTaskTable() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"param.task.table");
	}

	/**
	 * 获取系统参数表的表名
	 * 
	 * @since
	 * @return
	 */
	public static String getSysParamTable() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"sys.param.table");
	}

	/**
	 * 获取环境配置文件，用于中心标识等内容的获取
	 * 
	 * @since
	 * @return
	 */
	public static String getEnvPropFile() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"env.prop.file");
	}

	/**
	 * 获取错误信息文件定义，多个用','分隔
	 * 
	 * @since
	 * @return
	 */
	public static String getErrorMessageFile() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"error.message.file");
	}

	/**
	 * 获取扩展操作的package
	 * 
	 * @since
	 * @return
	 */
	public static String getExtPackage() {
		String extPkg = PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"param.ext.package");
		if (extPkg == null) {
			return extPkg;
		}

		if (!extPkg.endsWith(".")) {
			return extPkg + ".";
		} else {
			return extPkg;
		}
	}

	/**
	 * 获取审计日志表名
	 * 
	 * @since
	 * @return
	 */
	public static String getAuditLogTable() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"audit.log.table");
	}

	/**
	 * 获取审计日志的SEQUENCE
	 * 
	 * @since
	 * @return
	 */
	public static String getAuditLogSqn() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"audit.log.sqn");
	}

	/**
	 * 获取锁记录表名
	 * 
	 * @since
	 * @return
	 */
	public static String getLockTable() {
		return PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"lock.cfg.table");
	}

	/**
	 * 获取缺省的锁超时时间，单位：秒
	 * 
	 * @since
	 * @return
	 */
	public static int getLockTimeOut() {
		return PropertyUtils.getInt(Const.COMMON_CONFIG_FILE, "lock.timeout",
				30);
	}

	/**
	 * 获取方法调用入口参数日志打印级别 none/trace/debug/info/warn/error
	 * 
	 * @since
	 * @return
	 */
	public static Level getInvokeLogLevel() {
		String level = PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"invoke.log.level", "none");
		try {
			return Level.valueOf(level.trim().toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取关联事件操作的package
	 * 
	 * @since
	 * @return
	 */
	public static String getEvtPackage() {
		String evtPkg = PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"param.evt.package");
		if (evtPkg == null) {
			return evtPkg;
		}

		if (!evtPkg.endsWith(".")) {
			return evtPkg + ".";
		} else {
			return evtPkg;
		}
	}

	/**
	 * 获取验证器操作的package
	 * 
	 * @since
	 * @return
	 */
	public static String getValidatorPackage() {
		String evtPkg = PropertyUtils.getString(Const.COMMON_CONFIG_FILE,
				"param.val.package");
		if (evtPkg == null) {
			return evtPkg;
		}

		if (!evtPkg.endsWith(".")) {
			return evtPkg + ".";
		} else {
			return evtPkg;
		}
	}
}
