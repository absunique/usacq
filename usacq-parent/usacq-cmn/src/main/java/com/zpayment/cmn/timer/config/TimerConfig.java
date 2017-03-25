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

import java.util.Date;

public interface TimerConfig {
	/**
	 * true当前未执行完，不执行下一个
	 * @return
	 * @since 2017年3月10日
	 */
	public boolean isSyn();

	/**
	 * 定时器名字，组内定时器不能重复
	 * @return
	 * @since 2017年3月10日
	 */
	public String getName();

	/**
	 * 定时器分组，同一分组定时器由一组线程管理
	 * @return
	 * @since 2017年3月10日
	 */
	public String getGroup();
	
	/**
	 * 自动启动时间。null：不启动，手工启动
	 * @return
	 * @since 2017年3月10日
	 */
	public Date getStartDate();
}
