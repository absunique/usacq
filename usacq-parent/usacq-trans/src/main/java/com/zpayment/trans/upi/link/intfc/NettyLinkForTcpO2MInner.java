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

import org.jboss.netty.channel.Channel;

import com.zpayment.trans.upi.link.bean.NettyChannel;
import com.zpayment.trans.upi.link.unit.AddrManager;
import com.zpayment.trans.upi.link.unit.IdleUnit;
import com.zpayment.trans.upi.link.unit.SynToken;

/**
 * TCP一对一
 * 
 * @author peiwang
 * 
 */
public interface NettyLinkForTcpO2MInner extends NettyLinkO2M {
	public SynToken getToken(String key);

	public AddrManager getAddrManager();

	public void closeCurrentChannel(String key);

	public void channelDisconnected(String key, Channel channel);

	public void channelConnected(String key, Channel channel);

	public IdleUnit getIdleUnit();

	public NettyChannel getChannel(int channelId);
}
