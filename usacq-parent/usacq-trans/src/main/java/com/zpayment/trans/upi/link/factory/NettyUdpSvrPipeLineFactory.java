/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.trans.upi.link.factory;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;

import com.zpayment.trans.upi.link.handler.NettyMessageHandlerUdpSvr;
import com.zpayment.trans.upi.link.intfc.LinkListenerUdpSvr;
import com.zpayment.trans.upi.link.intfc.NettyLinkCoder;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpSvrInnter;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:36:07
 */
public class NettyUdpSvrPipeLineFactory implements ChannelPipelineFactory {

	private static final boolean hexDump = true;

	private NettyLinkUdpSvrInnter relatedBean;

	private LinkListenerUdpSvr linkListener;

	private ExecutionHandler executionHandler;

	public NettyUdpSvrPipeLineFactory(NettyLinkUdpSvrInnter relatedBean,
			LinkListenerUdpSvr linkListener, ExecutionHandler executionHandler) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
		this.executionHandler = executionHandler;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {

		ChannelPipeline pipeline = Channels.pipeline();
		// pipeline.addLast("loggingHandler", new LoggingHandler(hexDump));
		pipeline.addLast("loggingHandler", new NettyLoggingHandler(hexDump,
				relatedBean));
		for (String handlerName : linkListener.getDecoderHandlers().keySet()) {
			ChannelHandler coder = linkListener.getDecoderHandlers().get(
					handlerName);
			if (coder instanceof NettyLinkCoder) {
				NettyLinkCoder nettyLinkCoder = (NettyLinkCoder) coder;
				nettyLinkCoder.setRelatedLink(relatedBean);
			}
			pipeline.addLast("decodeHandler-" + handlerName, coder);
		}
		for (String handlerName : linkListener.getEncoderHandlers().keySet()) {
			ChannelHandler coder = linkListener.getEncoderHandlers().get(
					handlerName);
			if (coder instanceof NettyLinkCoder) {
				NettyLinkCoder nettyLinkCoder = (NettyLinkCoder) coder;
				nettyLinkCoder.setRelatedLink(relatedBean);
			}
			pipeline.addLast("encodeHandler-" + handlerName, coder);
		}
		pipeline.addLast("executionHandler", executionHandler);
		pipeline.addLast("messageHandler", new NettyMessageHandlerUdpSvr(
				relatedBean, linkListener));

		return pipeline;
	}
}
