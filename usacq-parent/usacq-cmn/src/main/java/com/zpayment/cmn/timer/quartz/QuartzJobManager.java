/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.timer.quartz;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.util.PropertyUtils;

public class QuartzJobManager {
	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(QuartzJobManager.class);
	private static boolean init = false;
	private static Map<String, SchedulerFactory> sf;
	private static Properties quartzProperties;
	private static boolean insulatedByGroup;

	// private static Map<String, Scheduler> schedulers;

	public static synchronized void init() {
		if (!init) {
			sf = new HashMap<String, SchedulerFactory>();
			try {
				quartzProperties = PropertyUtils.getProps("quartz.properties");
				if (quartzProperties != null) {
					try {
						insulatedByGroup = Boolean
								.parseBoolean(quartzProperties
										.getProperty("com.cup.ibs.quartz.insulatedByGroup"));
					} catch (Exception e) {
						insulatedByGroup = false;
					}
				}
			} catch (Exception e) {
				// TODO 找不到do nothing
			}
		}
	}

	public static synchronized void uinit() {
		if (init) {
			for (SchedulerFactory s : sf.values()) {
				try {
					for (Scheduler _s : s.getAllSchedulers()) {
						try {
							_s.shutdown();
						} catch (SchedulerException e) {
							log.error(e);
						}
					}
				} catch (SchedulerException e) {
					log.error(e);
				}
			}
			sf.clear();
			// schedulers.clear();
		}
	}

	private static SchedulerFactory newSchedulerFactory(String groupName) {
		if (quartzProperties != null && insulatedByGroup) {
			try {
				Properties tmpP = (Properties) quartzProperties.clone();
				tmpP.setProperty("org.quartz.scheduler.instanceName",
						"DefaultQuartzScheduler" + groupName);

				return new StdSchedulerFactory(tmpP);
			} catch (SchedulerException e) {
				throw new BaseException(BaseErrorCode.FAIL, e);
			}
		} else {
			return new StdSchedulerFactory();
		}
	}

	private static synchronized SchedulerFactory getSchedulerFactory(
			String groupName) throws SchedulerException {
		SchedulerFactory s = sf.get(groupName);
		if (s == null) {
			s = newSchedulerFactory(groupName);
			sf.put(groupName, s);
		}
		return s;
	}

	private static synchronized Scheduler getScheduler(String groupName)
			throws SchedulerException {
		// Scheduler sched = schedulers.get(groupName);
		// if (sched == null) {
		return getSchedulerFactory(groupName).getScheduler();
		// schedulers.put(groupName, sched);
		// }
		// return sched;
	}

	public static <T extends Job> void addJobUsingTrigger(String groupName,
			JobKey jobKey, Class<T> jobClazz, JobDataMap jdm, Trigger trigger)
			throws SchedulerException, ParseException {
		Scheduler sched = getScheduler(groupName);
		JobDetail jobDetail = JobBuilder.newJob(jobClazz).withIdentity(jobKey)
				.usingJobData(jdm).build();
		sched.scheduleJob(jobDetail, trigger);
		// 启动
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @throws SchedulerException
	 */
	public static void removeJob(String groupName, JobKey jobKey,
			TriggerKey triggerKey) throws SchedulerException {
		Scheduler sched = getScheduler(groupName);
		sched.pauseTrigger(triggerKey);// 停止触发器
		sched.unscheduleJob(triggerKey);// 移除触发器
		sched.deleteJob(jobKey);// 删除任务
	}
	//
	// /** */
	// /**
	// * 移除一个任务
	// *
	// * @param jobName
	// * @param jobGroupName
	// * @param triggerName
	// * @param triggerGroupName
	// * @throws SchedulerException
	// */
	// public static void removeJob(String jobName, String jobGroupName,
	// String triggerName, String triggerGroupName)
	// throws SchedulerException {
	// Scheduler sched = sf.getScheduler();
	// sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
	// sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
	// sched.deleteJob(jobName, jobGroupName);// 删除任务
	// }
}
