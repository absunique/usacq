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
import com.zpayment.trans.upi.link.bean.NettyLinkO2OCltBean;
import com.zpayment.trans.upi.link.bean.NettyLinkO2OSvrBean;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2OInner;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:36:23
 */
public class NettyConnctionHandlerO2O extends SimpleChannelHandler {
	private static final Logger logger = Logger
			.getLogger(NettyLoggingHandler.NEETY_LINK_LOG_NAME);
	NettyLinkForTcpO2OInner relatedBean;
	LinkListenerO2O linkListener;

	public NettyConnctionHandlerO2O(NettyLinkForTcpO2OInner relatedBean,
			LinkListenerO2O linkListener) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		NettyLoggingHandler.setCurrMdc(relatedBean);
		logger.error("catch ExceptionEvent local[%s]remote[%s]", new Object[] {
				e.getChannel().getLocalAddress(),
				e.getChannel().getRemoteAddress() }, e.getCause());
		relatedBean.closeCurrentChannel();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		synchronized (relatedBean) {
			NettyLoggingHandler.setCurrMdc(relatedBean);
			Channel newChannel = ctx.getChannel();
			if (relatedBean instanceof NettyLinkO2OSvrBean) {
				NettyLinkO2OSvrBean svrBean = (NettyLinkO2OSvrBean) relatedBean;
				logger.error("recieve remote[%s] in [%s]",
						new Object[] { newChannel.getRemoteAddress(),
								newChannel.getLocalAddress() });
				if (!svrBean.getConnFreq().recvConn()) {
					logger.error("conn too frequent!");
					Channels.close(newChannel);
					return;
				}
				if (svrBean.isConnected()) {
					svrBean.closeCurrentChannel();
				}
			} else if (relatedBean instanceof NettyLinkO2OCltBean) {
				NettyLinkO2OCltBean cltBean = (NettyLinkO2OCltBean) relatedBean;
				logger.error("connect remote[%s] in [%s]",
						new Object[] { cltBean.getLinkCfg().getRemoteAddr(),
								newChannel.getLocalAddress() });
				if (cltBean.isConnected()) {
					cltBean.closeCurrentChannel();
				}
			}
			this.relatedBean.setChannel(newChannel);
			linkListener.channelConnected(relatedBean, newChannel);
		}

	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		NettyLoggingHandler.setCurrMdc(relatedBean);
		logger.error("disconnect remote[%s] in [%s]", new Object[] {
				ctx.getChannel().getRemoteAddress(),
				ctx.getChannel().getLocalAddress() });
		this.relatedBean.channelDisconnected(ctx.getChannel());
		linkListener.channelDisconnected(relatedBean, ctx.getChannel());
	}

}
