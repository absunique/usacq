/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.timer.config;

import java.util.Calendar;
import java.util.Date;

import com.zpayment.cmn.util.DateUtils;

public class FixedDurationTimerConfig extends AbstractTimerConfig {
	private int millIntvl;

	public FixedDurationTimerConfig(boolean syn, String name, String group,
			int millIntvl, int delay) {
		this(syn, name, group, millIntvl, DateUtils.modTime(
				DateUtils.getCurrentTimestamp(), Calendar.MILLISECOND, delay));
	}

	public FixedDurationTimerConfig(boolean syn, String name, String group,
			int millIntvl, Date startTm) {
		super(syn, name, group, startTm);
		this.millIntvl = millIntvl;
	}

	public int getMillIntvl() {
		return millIntvl;
	}

	public void setMillIntvl(int millIntvl) {
		this.millIntvl = millIntvl;
	}

}
