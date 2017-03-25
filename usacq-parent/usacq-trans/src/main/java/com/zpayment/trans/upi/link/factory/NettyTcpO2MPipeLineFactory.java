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

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.ipfilter.IpFilterRuleHandler;
import org.jboss.netty.handler.ipfilter.IpFilterRuleList;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.zpayment.trans.upi.link.handler.NettyConnctionHandlerO2M;
import com.zpayment.trans.upi.link.handler.NettyIdleChannelHandlerO2M;
import com.zpayment.trans.upi.link.handler.NettyMessageHandlerO2M;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2M;
import com.zpayment.trans.upi.link.intfc.NettyLinkCoder;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2MInner;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;

/**
 * O2M一对多pipeline工厂
 * 
 * @author peiwang
 */
public class NettyTcpO2MPipeLineFactory implements ChannelPipelineFactory {
	private static final boolean hexDump = true;

	private Timer timer = new HashedWheelTimer();

	private NettyLinkForTcpO2MInner relatedBean;

	private LinkListenerO2M linkListener;

	private ExecutionHandler executionHandler;

	public NettyTcpO2MPipeLineFactory(NettyLinkForTcpO2MInner relatedBean,
			LinkListenerO2M linkListener, ExecutionHandler executionHandler) {
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
		if (relatedBean.getAddrManager().isCheckRemoteIp()) {
			IpFilterRuleHandler iprule = new IpFilterRuleHandler();
			iprule.addAll(new IpFilterRuleList("+i:"
					+ relatedBean.getAddrManager().getCfgRemoteAddr().getIp()
					+ ", -i:*"));
			pipeline.addLast("ipFilterRuleHandler", iprule);
		}
		pipeline.addLast("connectionHandler", new NettyConnctionHandlerO2M(
				relatedBean, linkListener));
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
		if (linkListener.getIdlMsg() != null) {
			pipeline.addLast("idleStateHandler", new IdleStateHandler(timer,
					relatedBean.getIdleUnit().getRcvLmt(), relatedBean
							.getIdleUnit().getSndLmt(), 0,
					TimeUnit.MILLISECONDS));
			pipeline.addLast("idleHandler", new NettyIdleChannelHandlerO2M(
					relatedBean, linkListener));
		}
		pipeline.addLast("executionHandler", executionHandler);
		pipeline.addLast("messageHandler", new NettyMessageHandlerO2M(
				relatedBean, linkListener));

		return pipeline;
	}
}
