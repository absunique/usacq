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

import com.zpayment.cmn.timer.TimerTask;

public class QuartzContext {
	public static JobDataMap newJobDataMap(TimerTask timerTask) {
		JobDataMap jdm = new JobDataMap();
		jdm.put("timerTask", timerTask);
		return jdm;
	}

	public static TimerTask readTimerTask(JobDataMap jdm) {
		return (TimerTask) jdm.get("timerTask");
	}
}
