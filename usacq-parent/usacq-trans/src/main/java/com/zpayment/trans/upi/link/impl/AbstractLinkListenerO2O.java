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

import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.intfc.NettyLinkO2O;

/**
 * 一对一链接基础监听器，派生实现可以只实现部分功能。如不需要空闲包
 * 
 * @author wangpei
 * 
 */
public abstract class AbstractLinkListenerO2O implements LinkListenerO2O {

	@Override
	public void receive(NettyLinkO2O nlb, Object msg) {
	}

	@Override
	public Object getIdlMsg() {
		return null;
	}

	@Override
	public boolean isIdlMsg(Object isIdlMsg) {
		return false;
	}

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
	public void channelConnected(NettyLinkO2O link, Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelDisconnected(NettyLinkO2O link, Channel channel) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getName() {
		return "AbstractLinkListener";
	}
}
