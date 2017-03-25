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

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.timer.Timer;
import com.zpayment.cmn.timer.TimerTask;
import com.zpayment.cmn.timer.config.TimerConfig;

public abstract class BaseQaurtzTimer implements Timer {
	private static String JOB_DETAIL_PREFIX = "JOB_GRP_";
	private static String TRIGGER_DETAIL_PREFIX = "TRIGGER_GRP_";

	private static JobKey buildJobKey(String jobName, String groupName) {
		return new JobKey(jobName, JOB_DETAIL_PREFIX + groupName);
	}

	private static TriggerKey buildTriggerKey(String jobName, String groupName) {
		return new TriggerKey(jobName, TRIGGER_DETAIL_PREFIX + groupName);
	}

	protected TimerConfig timerConfig;
	protected TimerTask timerTask;
	protected String jobName;
	protected String groupName = "DEFAULT_GRP";

	protected JobDataMap jdm;
	protected volatile boolean isStart = false;

	private JobKey jobKey;
	private TriggerKey triggerKey;

	public BaseQaurtzTimer(TimerConfig timerConfig, TimerTask task)
			throws Exception {
		this.timerConfig = timerConfig;
		this.timerTask = task;
		this.jobName = timerConfig.getName();
		this.jdm = QuartzContext.newJobDataMap(task);
		if (timerConfig.getGroup() != null) {
			this.groupName = timerConfig.getGroup();
		}
		jobKey = buildJobKey(jobName, groupName);
		triggerKey = buildTriggerKey(jobName, groupName);
	}

	abstract protected Trigger getTrigger(TriggerKey triggerKey)
			throws Exception;

	@Override
	public void start() {
		try {
			if (timerConfig.isSyn()) {
				QuartzJobManager.addJobUsingTrigger(groupName, jobKey,
						StatefulQuartzTimerTaskAdaptor.class, jdm,
						getTrigger(triggerKey));
			} else {
				QuartzJobManager.addJobUsingTrigger(groupName, jobKey,
						StandardQuartzTimerTaskAdaptor.class, jdm,
						getTrigger(triggerKey));
			}
			isStart = true;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.FAIL, e);
		}
	}

	@Override
	public void stop() {
		try {
			QuartzJobManager.removeJob(groupName, jobKey, triggerKey);
			isStart = false;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.FAIL, e);
		}
	}

	@Override
	public boolean isStarted() {
		return isStart;
	}

	@Override
	public String getName() {
		return jobName;
	}

}
