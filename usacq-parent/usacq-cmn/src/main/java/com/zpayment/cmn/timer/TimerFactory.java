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

import com.zpayment.cmn.timer.config.TimerConfig;
import com.zpayment.cmn.util.NamedService;
import com.zpayment.cmn.util.SpiUtils;

/**
 * 
 * Timer工厂
 * 
 * @author peiwang
 * @since 2017年2月16日
 */
public abstract class TimerFactory implements NamedService {
	public static TimerFactory proxyedFactory = null;

	public abstract Timer newTimer(TimerConfig timerConfig,
			TimerTask task);

	public static TimerFactory getInstance(String name) {
		return SpiUtils.getNamedSpi(TimerFactory.class, name);
	}
}
