/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.cmn.timer;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.timer.config.TimerConfig;

/**
 * 
 * Timer工厂
 * 
 * @author peiwang
 * @since 2017年2月16日
 */
public abstract class TimerFactory {
	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(TimerFactory.class);
	public static TimerFactory proxyedFactory = null;

	protected abstract Timer prxoyNewTimer(TimerConfig timerConfig,
			TimerTask task);

	protected abstract void init();

	public static void setFactory(TimerFactory proxyedFactory) {
		TimerFactory.proxyedFactory = proxyedFactory;
		TimerFactory.proxyedFactory.init();
	}

	/**
	 * 定时器工厂方法
	 * 
	 * @param timerConfig
	 *            定时器配置
	 * @param task
	 *            定时器任务
	 * @return
	 * @since 2017年3月10日
	 */
	public static Timer newTimer(TimerConfig timerConfig, TimerTask task) {
		if (proxyedFactory == null) {
			log.error("please setFactory");
			throw new BaseException(BaseErrorCode.FAIL);
		}
		try {
			return TimerFactory.proxyedFactory.prxoyNewTimer(timerConfig, task);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.FAIL, e);
		}
	}
}
