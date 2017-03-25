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

import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.zpayment.cmn.timer.FixedDurationTimer;
import com.zpayment.cmn.timer.TimerTask;
import com.zpayment.cmn.timer.config.FixedDurationTimerConfig;

public class FxiedDurationQaurtzTimer extends BaseQaurtzTimer implements
		FixedDurationTimer {

	private int duration;

	public FxiedDurationQaurtzTimer(FixedDurationTimerConfig timerConfig,
			TimerTask task) throws Exception {
		super(timerConfig, task);
		duration = timerConfig.getMillIntvl();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Trigger getTrigger(TriggerKey triggerKey) throws Exception {
		TriggerBuilder tb = TriggerBuilder
				.newTrigger()
				.withIdentity(triggerKey)
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMilliseconds(duration)
								.repeatForever());
		return tb.build();
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public void setDuration(int duration) {
		this.duration = duration;
		stop();
		start();
	}
}
