/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: TpsCtrlUnit.java,v 1.2 2017/01/13 05:37:05 peiwang Exp $
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
 * @since  下午2:44:39
 */
public class TpsCtrlUnit {
	public enum TSP_CTRL_UNIT_TYPE {
		CTRL_BYTE, CTRL_PACKAGE
	};

	private TSP_CTRL_UNIT_TYPE type;

	private int sndTps;
	private int rcvTps;
	public TSP_CTRL_UNIT_TYPE getType() {
		return type;
	}
	public void setType(TSP_CTRL_UNIT_TYPE type) {
		this.type = type;
	}
	public int getSndTps() {
		return sndTps;
	}
	public void setSndTps(int sndTps) {
		this.sndTps = sndTps;
	}
	public int getRcvTps() {
		return rcvTps;
	}
	public void setRcvTps(int rcvTps) {
		this.rcvTps = rcvTps;
	}
	public TpsCtrlUnit(TSP_CTRL_UNIT_TYPE type, int sndTps, int rcvTps) {
		super();
		this.type = type;
		this.sndTps = sndTps;
		this.rcvTps = rcvTps;
	}

	@Override
	public String toString() {
		return "TpsCtrlUnit [type=" + type + ", sndTps=" + sndTps + ", rcvTps="
				+ rcvTps + "]";
	}
}
