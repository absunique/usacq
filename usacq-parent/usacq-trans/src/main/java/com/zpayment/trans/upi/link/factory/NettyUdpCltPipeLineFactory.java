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

import com.zpayment.trans.upi.link.intfc.LinkListenerUdpClt;
import com.zpayment.trans.upi.link.intfc.NettyLinkCoder;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpClt;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;

/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:35:56
 */
public class NettyUdpCltPipeLineFactory implements ChannelPipelineFactory {

	private static final boolean hexDump = true;

	protected NettyLinkUdpClt relatedBean;

	protected LinkListenerUdpClt linkListener;

	public NettyUdpCltPipeLineFactory(NettyLinkUdpClt relatedBean,
			LinkListenerUdpClt linkListener) {
		this.relatedBean = relatedBean;
		this.linkListener = linkListener;
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

		return pipeline;
	}
}
