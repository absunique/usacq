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
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import com.zpayment.cmn.log.Logger;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2OInner;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:36:39
 */
public class NettyIdleChannelHandlerO2O extends IdleStateAwareChannelHandler {
	private static final Logger logger = Logger
			.getLogger(NettyLoggingHandler.NEETY_LINK_LOG_NAME);
	NettyLinkForTcpO2OInner relatedBean;
	LinkListenerO2O linkListener;
	private Object idleMsg;

	public NettyIdleChannelHandlerO2O(NettyLinkForTcpO2OInner relatedBean,
			LinkListenerO2O linkListener) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
		idleMsg = linkListener.getIdlMsg();
	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
			throws Exception {
		Channel nettyChannel = e.getChannel();
		NettyLoggingHandler.setCurrMdc(relatedBean);
		if (e.getState() == IdleState.READER_IDLE) {
			logger.error("read idle ,disconnect the link");
			relatedBean.closeCurrentChannel();
		} else if (e.getState() == IdleState.WRITER_IDLE) {
			logger.info("write idle for [%s]", relatedBean);
			nettyChannel.write(idleMsg);
		}
	}
}
