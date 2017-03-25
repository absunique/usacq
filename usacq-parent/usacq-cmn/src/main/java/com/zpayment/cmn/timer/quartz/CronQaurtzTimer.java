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

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.zpayment.cmn.timer.CronTimer;
import com.zpayment.cmn.timer.TimerTask;
import com.zpayment.cmn.timer.config.CronTimerConfig;

public class CronQaurtzTimer extends BaseQaurtzTimer implements CronTimer {
	private String cronExpression;

	public CronQaurtzTimer(CronTimerConfig timerConfig, TimerTask task)
			throws Exception {
		super(timerConfig, task);
		this.cronExpression = timerConfig.getCronExpression();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Trigger getTrigger(TriggerKey triggerKey) throws Exception {
		TriggerBuilder tb = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
		return tb.build();
	}

	public String getCronExpression() {
		return cronExpression;
	}

}
