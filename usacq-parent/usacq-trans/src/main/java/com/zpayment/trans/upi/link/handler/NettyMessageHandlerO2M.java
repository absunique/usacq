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

import com.zpayment.trans.upi.link.bean.NettyChannel;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2M;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2MInner;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:36:46
 */
public class NettyMessageHandlerO2M extends SimpleChannelUpstreamHandler {

	private NettyLinkForTcpO2MInner relatedBean;
	private LinkListenerO2M linkListener;

	public NettyMessageHandlerO2M(NettyLinkForTcpO2MInner relatedBean,
			LinkListenerO2M linkListener) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!linkListener.isIdlMsg(e.getMessage())) {
			// String key = linkListener.buildKeyByChannel(ctx.getChannel());
			// if (key == null) {
			// return;
			// }

			NettyChannel nettyChannel = relatedBean.getChannel(e.getChannel()
					.getId());
			if (nettyChannel == null) {
				return;
			}
			String key = nettyChannel.getKey();
			linkListener.receive(key, relatedBean, e.getMessage());
		}
	}

}
