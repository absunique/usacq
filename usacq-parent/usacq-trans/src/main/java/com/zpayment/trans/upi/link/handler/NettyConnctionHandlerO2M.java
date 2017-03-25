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

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.zpayment.cmn.log.Logger;
import com.zpayment.trans.upi.link.bean.NettyChannel;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2M;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2MInner;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:36:16
 */
public class NettyConnctionHandlerO2M extends SimpleChannelHandler {
	private static final Logger logger = Logger
			.getLogger(NettyLoggingHandler.NEETY_LINK_LOG_NAME);
	NettyLinkForTcpO2MInner relatedBean;
	LinkListenerO2M linkListener;

	public NettyConnctionHandlerO2M(NettyLinkForTcpO2MInner relatedBean,
			LinkListenerO2M linkListener) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		NettyChannel nettyChannel = relatedBean.getChannel(e.getChannel()
				.getId());
		if (nettyChannel == null) {
			return;
		}
		String key = nettyChannel.getKey();
		NettyLoggingHandler.setDynMdc(relatedBean, key);
		logger.error("catch ExceptionEvent local[%s]remote[%s]key[%s]",
				new Object[] { e.getChannel().getLocalAddress(),
						e.getChannel().getRemoteAddress(), key }, e.getCause());
		relatedBean.closeCurrentChannel(key);
		if (logger.isDebugEnabled()) {
			logger.debug("closeCurrentChannel key[%s][%s]", key,
					ctx.getChannel());
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {

		Channel newChannel = ctx.getChannel();
		String key = linkListener.buildKeyByChannel(newChannel);
		if (key == null) {
			Channels.close(newChannel);
			return;
		}
		NettyLoggingHandler.setDynMdc(relatedBean, key);
		relatedBean.channelConnected(key, newChannel);
		logger.error("[%s] recieve remote[%s] in [%s],key[%s]", new Object[] {
				Thread.currentThread().getId(), newChannel.getRemoteAddress(),
				newChannel.getLocalAddress(), key });
		linkListener.channelConnected(relatedBean, key, newChannel);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		Channel oldChannel = ctx.getChannel();
		NettyChannel nettyChannel = relatedBean.getChannel(oldChannel.getId());
		if (nettyChannel == null) {
			return;
		}
		String key = nettyChannel.getKey();
		NettyLoggingHandler.setDynMdc(relatedBean, key);
		relatedBean.channelDisconnected(key, ctx.getChannel());
		logger.error("disconnect remote[%s] in [%s],key[%s]", new Object[] {
				ctx.getChannel().getRemoteAddress(),
				ctx.getChannel().getLocalAddress(), key });
		linkListener.channelDisConnected(relatedBean, key, oldChannel);
	}

}
