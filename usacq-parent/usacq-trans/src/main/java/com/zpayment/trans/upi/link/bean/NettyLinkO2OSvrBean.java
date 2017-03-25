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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.trans.upi.exp.UpiErrorCode;
import com.zpayment.trans.upi.link.factory.NettyTcpO2OPipeLineFactory;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.unit.LinkCfg;
import com.zpayment.trans.upi.link.unit.SvrConnFreq;

/**
 * NettyClient
 * 
 * @author peiwang
 * 
 */
public class NettyLinkO2OSvrBean extends AbstractNettyLinkBeanO2O {

	// private static final Logger logger = Logger
	// .getLogger(NettyLinkO2OSvrBean.class);

	private ServerBootstrap bootstrap;
	private ChannelFactory channelFactory;

	private static Executor bossExecutor = Executors.newCachedThreadPool();
	private static Executor workerExecutor = Executors.newCachedThreadPool();

	private SvrConnFreq connFreq;

	protected NettyLinkO2OSvrBean(LinkCfg linkCfg, LinkListenerO2O linkListener) {
		super(linkCfg, linkListener);
		connFreq = new SvrConnFreq(5000);
	}

	protected ChannelPipelineFactory getPipeLineFactory() {
		return new NettyTcpO2OPipeLineFactory(this, linkListener,executionHandler);
	}

	public SvrConnFreq getConnFreq() {
		return connFreq;
	}

	@Override
	public boolean doStart() {
		try {
			log.debug("start o2o Server");
			channelFactory = new NioServerSocketChannelFactory(bossExecutor,
					workerExecutor);
			bootstrap = new ServerBootstrap(channelFactory);
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.reuseAddress", true);
			bootstrap.setOption("child.keepAlive", true);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("reuseAddress", true);

			bootstrap.setPipelineFactory(getPipeLineFactory());
			bootstrap.bind(addrManager.getCfgLocalInetAddr());
		} catch (ChannelException e) {
			throw new BaseException(UpiErrorCode.COMN_LINK_BIND_ERROR,
					new Object[] { addrManager.getCfgLocalInetAddr() }, e);
		}
		return true;
	}

	@Override
	public boolean doStop() {
		closeCurrentChannel();
		bootstrap.shutdown();
		return true;
	}
}
