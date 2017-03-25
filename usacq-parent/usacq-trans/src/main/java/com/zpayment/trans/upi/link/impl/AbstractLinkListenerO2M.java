/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.trans.upi.link.impl;

import java.util.LinkedHashMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;

import com.zpayment.trans.upi.link.intfc.LinkListenerO2M;
import com.zpayment.trans.upi.link.intfc.NettyLinkO2M;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:37:12
 */
public abstract class AbstractLinkListenerO2M implements LinkListenerO2M {

	@Override
	public LinkedHashMap<String, ChannelHandler> getDecoderHandlers() {
		LinkedHashMap<String, ChannelHandler> my = new LinkedHashMap<String, ChannelHandler>();
		return my;
	}

	@Override
	public LinkedHashMap<String, ChannelHandler> getEncoderHandlers() {
		LinkedHashMap<String, ChannelHandler> my = new LinkedHashMap<String, ChannelHandler>();
		return my;
	}

	@Override
	public Object getIdlMsg() {
		return null;
	}

	@Override
	public boolean isIdlMsg(Object msg) {
		return false;
	}

	@Override
	public void receive(String key, NettyLinkO2M nlb, Object msg) {

	}

	@Override
	public void channelConnected(NettyLinkO2M link, String key, Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelDisConnected(NettyLinkO2M link, String key,
			Channel channel) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		return "AbstractLinkListenerO2M";
	}

}
