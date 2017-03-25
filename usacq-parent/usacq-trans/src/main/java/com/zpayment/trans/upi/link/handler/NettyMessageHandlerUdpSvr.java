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
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.zpayment.trans.upi.link.intfc.LinkListenerUdpSvr;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpSvrInnter;
/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:36:59
 */
public class NettyMessageHandlerUdpSvr extends SimpleChannelUpstreamHandler {

	private NettyLinkUdpSvrInnter relatedBean;
	private LinkListenerUdpSvr linkListener;

	public NettyMessageHandlerUdpSvr(NettyLinkUdpSvrInnter relatedBean,
			LinkListenerUdpSvr linkListener) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		linkListener.receive(relatedBean, e.getMessage(), e.getChannel(),
				e.getRemoteAddress());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		relatedBean.exceptionCaught(ctx, e);
	}

}
