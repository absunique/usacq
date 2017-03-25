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

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.timer.Timer;
import com.zpayment.cmn.timer.TimerFactory;
import com.zpayment.cmn.timer.TimerTask;
import com.zpayment.cmn.timer.config.CronTimerConfig;
import com.zpayment.cmn.timer.config.FixedDurationTimerConfig;
import com.zpayment.cmn.timer.config.TimerConfig;

public class QuartzTimerFactory extends TimerFactory {

	public Timer newTimer(TimerConfig timerConfig, TimerTask task) {
		QuartzJobManager.init();
		try {
			if (timerConfig instanceof FixedDurationTimerConfig) {
				return new FxiedDurationQaurtzTimer(
						(FixedDurationTimerConfig) timerConfig, task);
			} else if (timerConfig instanceof CronTimerConfig) {
				return new CronQaurtzTimer((CronTimerConfig) timerConfig, task);
			}
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.FAIL, e);
		}
		throw new BaseException(BaseErrorCode.COMN_NOT_SUPPORTED);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zpayment.cmn.util.NamedService#getSerivceName()
	 */
	@Override
	public String getSerivceName() {
		// TODO Auto-generated method stub
		return "quartz";
	}
}
