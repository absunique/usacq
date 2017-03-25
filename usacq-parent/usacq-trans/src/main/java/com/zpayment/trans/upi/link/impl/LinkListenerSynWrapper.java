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
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2OInner;
import com.zpayment.trans.upi.link.intfc.NettyLinkO2O;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:38:35
 */
public class LinkListenerSynWrapper implements LinkListenerO2O {
	private LinkListenerO2O linkListener;

	public LinkListenerSynWrapper(LinkListenerO2O linkListener) {
		this.linkListener = linkListener;
	}

	@Override
	public void receive(NettyLinkO2O nlb, Object msg) {
		NettyLinkForTcpO2OInner inerLink = (NettyLinkForTcpO2OInner) nlb;
		inerLink.getToken().back(msg);
	}

	// @Override
	// public boolean exceptionCaught(LinkException linkException) {
	// return linkListener.exceptionCaught(linkException);
	// }

	@Override
	public Object getIdlMsg() {
		return linkListener.getIdlMsg();
	}

	@Override
	public boolean isIdlMsg(Object isIdlMsg) {
		return linkListener.isIdlMsg(isIdlMsg);
	}

	@Override
	public LinkedHashMap<String, ChannelHandler> getDecoderHandlers() {
		return linkListener.getDecoderHandlers();
	}

	@Override
	public LinkedHashMap<String, ChannelHandler> getEncoderHandlers() {
		return linkListener.getEncoderHandlers();
	}

	@Override
	public void channelConnected(NettyLinkO2O link, Channel channel) {
		linkListener.channelConnected(link, channel);
	}

	@Override
	public void channelDisconnected(NettyLinkO2O link, Channel channel) {
		linkListener.channelDisconnected(link, channel);
	}
	
	@Override
	public String getName() {
		return "LinkListenerSynWrapper";
	}
}
