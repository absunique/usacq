/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: IdleUnit.java,v 1.3 2017/01/13 05:37:05 peiwang Exp $
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

public class IdleUnit {
	private int sndLmt;
	private int rcvLmt;

	public IdleUnit(String idleTp, int sndLmt, int rcvLmt) {
		if (idleTp.equals("0")) {
			this.sndLmt = 0;
			this.rcvLmt = 0;
		} else if (idleTp.equals("1")) {

			this.sndLmt = sndLmt;
			this.rcvLmt = rcvLmt;
		} else if (idleTp.equals("2")) {
			this.sndLmt = sndLmt;
			this.rcvLmt = 0;
		}
	}

	public int getSndLmt() {
		return sndLmt;
	}

	public void setSndLmt(int sndLmt) {
		this.sndLmt = sndLmt;
	}

	public int getRcvLmt() {
		return rcvLmt;
	}

	public void setRcvLmt(int rcvLmt) {
		this.rcvLmt = rcvLmt;
	}

	@Override
	public String toString() {
		return "IdleUnit [sndLmt=" + sndLmt + ", rcvLmt=" + rcvLmt + "]";
	}

}
