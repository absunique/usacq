/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */


package com.zpayment.trans.upi.link.intfc;

import com.zpayment.trans.upi.link.unit.LinkAddr;

/**
 * UPD客户端
 * 
 * @author peiwang
 * 
 */
public interface NettyLinkUdpClt extends NettyBaseLink {

	public void send(Object message);

	public void send(Object message, LinkAddr addr);
}
