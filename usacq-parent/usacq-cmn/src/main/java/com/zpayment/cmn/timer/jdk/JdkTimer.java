/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.cmn.timer.jdk;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.timer.FixedDurationTimer;
import com.zpayment.cmn.timer.TimerTask;
import com.zpayment.cmn.timer.config.FixedDurationTimerConfig;
import com.zpayment.cmn.timer.config.TimerConfig;

/**
 * 
 * 基于JDK ScheduledExecutorService实现 只支持按固定间隔的定时器
 * 
 * @author peiwang
 * @since 2017年2月16日
 */
public class JdkTimer implements FixedDurationTimer {

	private static final Logger log = Logger.getLogger(JdkTimer.class);
	// private BlockingQueue<BaseCmd> workQueue;
	private volatile boolean isStart = false;
	private FixedDurationTimerConfig timerConfig;
	private volatile int duration;
	// private boolean syn;
	ScheduledExecutorService service;
	private Runnable runTask;

	public JdkTimer(TimerConfig timerConfig, final TimerTask task) {
		if (!(timerConfig instanceof FixedDurationTimerConfig)) {
			log.error("JdkTimer need FixedDurationTimerConfig!Not "
					+ timerConfig.getClass().getName());
			throw new BaseException(BaseErrorCode.COMN_NOT_SUPPORTED);
		}
		this.timerConfig = (FixedDurationTimerConfig) timerConfig;
		this.duration = this.timerConfig.getMillIntvl();
		this.runTask = new Runnable() {
			@Override
			public void run() {
				try {
					if (!task.execute()) {
						JdkTimer.this.stop();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		};
		// this.syn = syn;
		if (null != this.timerConfig.getStartDate()) {
			start();
		}
	}

	@Override
	public synchronized void start() {
		if (isStart) {
			return;
		}
		if (service != null) {
			service.shutdown();
		}
		service = Executors.newScheduledThreadPool(5);
		service.scheduleAtFixedRate(runTask, 0, duration, TimeUnit.MILLISECONDS);
		isStart = true;
	}

	@Override
	public synchronized void stop() {
		if (isStart) {
			if (service != null) {
				service.shutdown();
				service = null;
			}
			isStart = false;
		}
	}

	@Override
	public boolean isStarted() {
		return isStart;
	}

	@Override
	public String getName() {
		return timerConfig.getName();
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
