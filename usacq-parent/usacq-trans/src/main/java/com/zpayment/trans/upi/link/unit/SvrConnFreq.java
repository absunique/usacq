/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: SvrConnFreq.java,v 1.4 2017/01/13 05:37:05 peiwang Exp $
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
 * @since  下午2:44:07
 */
public class SvrConnFreq {
	private int minIntvlMillSec = 0;
	private long lastTs = 0;

	public SvrConnFreq(int minIntvlMillSec) {
		this.minIntvlMillSec = minIntvlMillSec;
	}

	public boolean recvConn() {
		long currTs = System.currentTimeMillis();
		long timeDiff = currTs - lastTs;
		if (timeDiff < minIntvlMillSec) {
			return false;
		}
		lastTs = currTs;
		return true;
	}
}
