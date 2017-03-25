/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.trans.upi.link.bean;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.trans.upi.exp.UpiErrorCode;
import com.zpayment.trans.upi.link.factory.NettyUdpSvrPipeLineFactory;
import com.zpayment.trans.upi.link.intfc.LinkListenerUdpSvr;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpSvrInnter;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;
import com.zpayment.trans.upi.link.unit.AddrManager;
import com.zpayment.trans.upi.link.unit.LinkCfg;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:35:24
 */
public class NettyLinkUdpSvrBean extends AbstractNettyBaseLinkBean implements
		NettyLinkUdpSvrInnter {
	private ConnectionlessBootstrap bootStrap;
	protected static ChannelFactory channelFactory = new NioDatagramChannelFactory(
			workerExecutor);
	protected LinkListenerUdpSvr linkLinstener;

	protected AddrManager addrManager;

	protected NettyLinkUdpSvrBean(LinkCfg linkCfg,
			LinkListenerUdpSvr linkLinstener) {

		super(linkCfg);
		bootStrap = new ConnectionlessBootstrap(channelFactory);
		this.linkLinstener = linkLinstener;
		addrManager = new AddrManager(linkCfg.getLocalAddr(),
				linkCfg.getRemoteAddr(), linkCfg.isRemoteChkIn());
	}

	@Override
	protected boolean doStop() {
		bootStrap.shutdown();
		return true;
	}

	@Override
	protected boolean doStart() {
		try {
			bootStrap.setPipelineFactory(new NettyUdpSvrPipeLineFactory(this,
					linkLinstener, executionHandler));
			bootStrap.bind(addrManager.getCfgLocalInetAddr());
			return true;
		} catch (Exception e) {
			throw new BaseException(UpiErrorCode.COMN_LINK_UNKOWN_ERROR, e);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		NettyLoggingHandler.setCurrMdc(this);
		log.error("catch ExceptionEvent local[%s]remote[%s]", new Object[] {
				e.getChannel().getLocalAddress(),
				e.getChannel().getRemoteAddress() }, e.getCause());
	}

}
