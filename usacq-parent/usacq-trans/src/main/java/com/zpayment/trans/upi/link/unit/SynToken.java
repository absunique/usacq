/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.trans.upi.link.unit;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.trans.upi.exp.UpiErrorCode;

/**
 * Syn token
 * 
 * @author peiwang
 * 
 */
public class SynToken {
	private static final Logger logger = Logger.getLogger(SynToken.class);

	private CountDownLatch latch;
	private long timeout;

	public SynToken setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	private volatile Object response;

	public SynToken(long timeout) {
		this.timeout = timeout;
	}

	public void go() {
		latch = new CountDownLatch(1);
	}

	/**
	 * get corresponding response of the request
	 */
	public Object get() {
		boolean ok;
		try {
			// until response message has arrived or time out
			ok = latch.await(this.timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new BaseException(UpiErrorCode.COMN_LINK_TIMEOUT, e);
		}

		if (!ok) {
			logger.debug("time out [%d]", this.timeout);
			throw new BaseException(UpiErrorCode.COMN_LINK_TIMEOUT);
		}

		return response;
	}

	public void back(Object response) {
		this.response = response;
		latch.countDown();
	}

}
