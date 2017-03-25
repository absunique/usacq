/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.trans.upi.link.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2OInner;
/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:36:52
 */
public class NettyMessageHandlerO2O extends SimpleChannelUpstreamHandler {

	private NettyLinkForTcpO2OInner relatedBean;
	private LinkListenerO2O linkListener;

	public NettyMessageHandlerO2O(NettyLinkForTcpO2OInner relatedBean,
			LinkListenerO2O linkListener) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!linkListener.isIdlMsg(e.getMessage())) {
			linkListener.receive(relatedBean, e.getMessage());
		}
	}

}
