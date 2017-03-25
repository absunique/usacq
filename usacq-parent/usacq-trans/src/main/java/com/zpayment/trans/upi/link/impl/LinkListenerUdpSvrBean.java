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

import java.net.SocketAddress;
import java.util.LinkedHashMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;

import com.zpayment.trans.upi.link.bean.NettyLinkFactory;
import com.zpayment.trans.upi.link.intfc.LinkListenerUdpSvr;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpSvr;
import com.zpayment.trans.upi.link.unit.LinkAddr;
import com.zpayment.trans.upi.link.unit.LinkCfg;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:38:48
 */
public class LinkListenerUdpSvrBean implements LinkListenerUdpSvr {
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
	public void receive(NettyLinkUdpSvr nlb, Object msg, Channel channel,
			SocketAddress remoteAdress) {
	}

	@Override
	public String getName() {
		return "LinkListenerUdpSvrBean";
	}

	public static void main(String[] args) {
		LinkAddr local = new LinkAddr("172.20.0.12", 35555);
		LinkAddr remote = new LinkAddr("0.0.0.0", 0);
		LinkCfg cfg = LinkCfg.newUdpLineCfg("DDD", 1, "2", local, remote);
		NettyLinkUdpSvr link = NettyLinkFactory.newUdpSvrLink(cfg,
				new LinkListenerUdpSvrBean());
		link.start();
	}
}
