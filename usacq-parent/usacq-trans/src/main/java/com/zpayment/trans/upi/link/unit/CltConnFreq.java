/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: CltConnFreq.java,v 1.2 2017/01/13 05:37:05 peiwang Exp $
 * 
 * Function:
 * 
 * TODO 功能介绍
 * 
 * Edit History:
 * 
 * 2016-12-20 - Create By peiwang
 */

package com.zpayment.trans.upi.link.unit;

/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:42:38
 */
public class CltConnFreq {
	private int connIntv = 5 * 1000;
	private int sleepIntv = 30 * 1000;
	private volatile long nextConnTs;

	private int maxTryTime = 5;
	private int connCnt = 0;

	/**
	 * 连接失败，返回下次连接时间
	 * 
	 * @return
	 */
	public void connOnce() {
		connCnt++;
		if (connCnt < maxTryTime) {
			nextConnTs = System.currentTimeMillis() + connIntv;
		} else {
			connCnt = 0;
			nextConnTs = System.currentTimeMillis() + sleepIntv;
		}
	}

	public long getNextConnTs() {
		return nextConnTs;
	}
}
