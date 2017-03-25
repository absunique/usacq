/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: NettyLinkForTcpO2OInner.java,v 1.5 2017/02/22 09:51:04 peiwang Exp $
 * 
 * Function:
 * 
 * TODO 功能介绍
 * 
 * Edit History:
 * 
 * 2016-12-20 - Create By peiwang
 */

package com.zpayment.trans.upi.link.intfc;

import org.jboss.netty.channel.Channel;

import com.zpayment.trans.upi.link.unit.AddrManager;
import com.zpayment.trans.upi.link.unit.IdleUnit;
import com.zpayment.trans.upi.link.unit.SynToken;

/**
 * TCP一对一
 * 
 * @author peiwang
 * 
 */
public interface NettyLinkForTcpO2OInner extends NettyLinkO2O {
	public SynToken getToken();

	public boolean isSvr();

	public AddrManager getAddrManager();

	public void closeCurrentChannel();

	public void channelDisconnected(Channel channel);

	public IdleUnit getIdleUnit();

	public void setChannel(Channel channel);

	public void connect();

	public boolean isTimeToConnect();

}
