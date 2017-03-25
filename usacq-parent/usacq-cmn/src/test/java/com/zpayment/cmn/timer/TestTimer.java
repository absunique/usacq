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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.zpayment.cmn.timer.config.FixedDurationTimerConfig;
import com.zpayment.cmn.timer.quartz.QuartzTimerFactory;

class CountTimerTask implements TimerTask {
	private volatile int count = 0;
	private int sleepTime = 1;

	CountTimerTask() {

	}

	CountTimerTask(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Override
	public boolean execute() {
		try {
			count++;
			System.out.println(Thread.currentThread().getName()+new Date());
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	public int getCount() {
		return count;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
}

public class TestTimer {

	@Before
	public void init() {
		TimerFactory.setFactory(new QuartzTimerFactory());
	}

	/**
	 * 普通定时器，异步
	 * 
	 * @throws Exception
	 * @since 2017年3月21日
	 */
	@Test
	public void testTimerAsy() throws Exception {
		CountTimerTask ctt = new CountTimerTask(10000);
		Timer timer1 = TimerFactory.newTimer(new FixedDurationTimerConfig(
				false, "AAA", "testTimerAsy", 1000, null), ctt);
		timer1.start();
		Thread.sleep(5000);
		assertEquals(6, ctt.getCount());
		timer1.stop();
	}

	/**
	 * 普通定时器，同步
	 * 
	 * @throws Exception
	 * @since 2017年3月21日
	 */
	@Test
	public void testTimerSyn() throws Exception {
		CountTimerTask ctt = new CountTimerTask(10000);
		Timer timer1 = TimerFactory.newTimer(new FixedDurationTimerConfig(true,
				"AAA", "testTimerSyn", 1000, null), ctt);
		timer1.start();
		Thread.sleep(5000);
		assertEquals(1, ctt.getCount());
		timer1.stop();
	}

	/**
	 * 两个定时器，同组。两个异步，一个占用过多线程。
	 * 
	 * @throws Exception
	 * @since 2017年3月21日
	 */
	@Test
	public void testTwoTimer() throws Exception {
		CountTimerTask ctt1 = new CountTimerTask(20000);
		Timer timer1 = TimerFactory.newTimer(new FixedDurationTimerConfig(
				false, "AAA1", "testTwoTimer", 1000, null), ctt1);
		timer1.start();

		CountTimerTask ctt2 = new CountTimerTask();
		Timer timer2 = TimerFactory.newTimer(new FixedDurationTimerConfig(
				false, "AAA2", "testTwoTimer", 1000, null), ctt2);
		timer2.start();
		Thread.sleep(15000);
		assertTrue(16 > ctt2.getCount());
		timer1.stop();
		timer2.stop();
	}

	/**
	 * 两个定时器，不同组。两个异步，一个占用过多线程。
	 * 
	 * @throws Exception
	 * @since 2017年3月21日
	 */
	@Test
	public void test2Timer2Grp() throws Exception {
		CountTimerTask ctt1 = new CountTimerTask(20000);
		Timer timer1 = TimerFactory.newTimer(new FixedDurationTimerConfig(
				false, "AAA1", "testTwoTimer1", 1000, null), ctt1);
		timer1.start();

		CountTimerTask ctt2 = new CountTimerTask();
		Timer timer2 = TimerFactory.newTimer(new FixedDurationTimerConfig(
				false, "AAA2", "testTwoTimer2", 1000, null), ctt2);
		timer2.start();
		Thread.sleep(15000);
		assertTrue(15 <= ctt2.getCount());
		timer1.stop();
		timer2.stop();
	}
}
