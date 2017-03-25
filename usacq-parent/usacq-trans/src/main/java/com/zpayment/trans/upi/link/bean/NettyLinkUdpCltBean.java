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

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.trans.upi.exp.UpiErrorCode;
import com.zpayment.trans.upi.link.LinkUtils;
import com.zpayment.trans.upi.link.factory.NettyUdpCltPipeLineFactory;
import com.zpayment.trans.upi.link.intfc.LinkListenerUdpClt;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpClt;
import com.zpayment.trans.upi.link.unit.AddrManager;
import com.zpayment.trans.upi.link.unit.LinkAddr;
import com.zpayment.trans.upi.link.unit.LinkCfg;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:35:15
 */
public class NettyLinkUdpCltBean extends AbstractNettyBaseLinkBean implements
		NettyLinkUdpClt {
	// private static final Logger logger = Logger
	// .getLogger(AbstractNettyBaseLinkBean.class);
	private ConnectionlessBootstrap bootStrap;
	protected static ChannelFactory channelFactory = new NioDatagramChannelFactory(
			workerExecutor);
	protected LinkListenerUdpClt linkLinstener;

	protected AddrManager addrManager;

	private Channel udpChannel;

	private boolean isDone;

	protected NettyLinkUdpCltBean(LinkCfg linkCfg,
			LinkListenerUdpClt linkLinstener) {
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
			if (addrManager.getCfgLocalInetAddr().getAddress()
					.isAnyLocalAddress()) {
				log.debug("any adreess");
			}
			bootStrap.setPipelineFactory(new NettyUdpCltPipeLineFactory(this,
					linkLinstener));
			// udpChannel = bootStrap.bind(addrManager.getCfgLocalInetAddr());
			InetSocketAddress remoteAddr = LinkUtils.convAdress(addrManager
					.getCfgRemoteAddr());
			ChannelFuture future = bootStrap.connect(remoteAddr);
			isDone = future.await(3000);
			if (isDone) {
				udpChannel = future.getChannel();
			} else {
				udpChannel = null;
			}
			return true;
		} catch (Exception e) {
			throw new BaseException(UpiErrorCode.COMN_LINK_UNKOWN_ERROR, e);
		}
	}

	@Override
	public void send(Object message) {
		send(message, addrManager.getCfgRemoteAddr());
	}

	@Override
	public void send(Object message, LinkAddr addr) {
		if (!isDone || null == udpChannel) {
			throw new BaseException(UpiErrorCode.COMN_LINK_DIS_CONNECT);
		}
		InetSocketAddress remoteAddr = LinkUtils.convAdress(addr);
		udpChannel.write(message, remoteAddr);
	}
}
