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
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2MInner;
import com.zpayment.trans.upi.link.intfc.NettyLinkO2M;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:38:28
 */
public class LinkListenerO2MSynWrapper implements LinkListenerO2M {
	private LinkListenerO2M linkListener;

	public LinkListenerO2MSynWrapper(LinkListenerO2M linkListener) {
		this.linkListener = linkListener;
	}

	@Override
	public Object getIdlMsg() {
		return linkListener.getIdlMsg();
	}

	@Override
	public boolean isIdlMsg(Object msg) {
		return linkListener.isIdlMsg(msg);
	}

	@Override
	public void receive(String key, NettyLinkO2M nlb, Object msg) {
		NettyLinkForTcpO2MInner inerLink = (NettyLinkForTcpO2MInner) nlb;
		// 由于 key 是链路建立时分配的，断开时不删除。因此始终能找到。
		inerLink.getToken(key).back(msg);
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
	public String buildKeyByChannel(Channel channel) {
		return linkListener.buildKeyByChannel(channel);
	}

	@Override
	public void channelConnected(NettyLinkO2M link, String key, Channel channel) {
		linkListener.channelConnected(link, key, channel);

	}

	@Override
	public void channelDisConnected(NettyLinkO2M link, String key,
			Channel channel) {
		linkListener.channelDisConnected(link, key, channel);

	}

	@Override
	public String getName() {
		return "LinkListenerO2MSynWrapper";
	}

}
