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

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.trans.upi.exp.UpiErrorCode;
import com.zpayment.trans.upi.link.factory.NettyTcpO2MPipeLineFactory;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2M;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2MInner;
import com.zpayment.trans.upi.link.unit.AddrManager;
import com.zpayment.trans.upi.link.unit.IdleUnit;
import com.zpayment.trans.upi.link.unit.LinkCfg;
import com.zpayment.trans.upi.link.unit.SvrConnFreq;
import com.zpayment.trans.upi.link.unit.SynToken;

/**
 * TCP一对多服务器
 * 
 * 具体实现中，需要提供根据连接构造key的方法，同样的key重复连接会替换之前的连接， 发送基于Key进行。
 * 
 * @author peiwang
 * 
 */
public class NettyLinkBeanO2M extends AbstractNettyBaseLinkBean implements
		NettyLinkForTcpO2MInner {

	protected AddrManager addrManager;
	private NettyChannels nettyChannles = new NettyChannels();

	private IdleUnit idleUnit;

	protected final Lock lock = new ReentrantLock();

	protected LinkListenerO2M linkListener;

	private ServerBootstrap bootstrap;
	protected static ChannelFactory channelFactory;
	private SvrConnFreq connFreq;

	protected NettyLinkBeanO2M(LinkCfg linkCfg, LinkListenerO2M linkListener) {
		super(linkCfg);
		this.linkListener = linkListener;

		if (!linkCfg.getLineTp().equals(LinkCfg.LINK_SVR_O2M)) {
			log.error("invalid " + LinkCfg.LINK_SVR_O2M);
			throw new BaseException(UpiErrorCode.COMN_LINK_CONFIG_ERROR);
		}
		addrManager = new AddrManager(linkCfg.getLocalAddr(),
				linkCfg.getRemoteAddr(), linkCfg.isRemoteChkIn());

		idleUnit = new IdleUnit(linkCfg.getIdlTstTp(), linkCfg.getSndIdlLmt(),
				linkCfg.getRcvIdlLmt());
	}

	private NettyChannel getChannelByKey(String key) {
		return nettyChannles.getChannelByKey(key);
	}

	public SvrConnFreq getConnFreq() {
		return connFreq;
	}

	protected ChannelPipelineFactory getPipeLineFactory() {
		return new NettyTcpO2MPipeLineFactory(this, linkListener,
				executionHandler);
	}

	@Override
	public boolean doStart() {
		try {
			log.debug("start o2m svr");
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
			throw new BaseException(UpiErrorCode.COMN_LINK_UNKOWN_ERROR, e);
		}
		return true;
	}

	@Override
	public boolean doStop() {
		nettyChannles.stopAll();
		bootstrap.shutdown();
		return true;
	}

	@Override
	public boolean isConnected(String key) {
		NettyChannel nChannel = getChannelByKey(key);
		return nChannel == null ? false : nChannel.isConnected();
	}

	@Override
	public SynToken getToken(String key) {
		NettyChannel nChannel = getChannelByKey(key);
		return nChannel == null ? null : nChannel.getToken();
	}

	@Override
	public AddrManager getAddrManager() {
		return addrManager;
	}

	@Override
	public void send(String key, Object message) {

		NettyChannel channel = getChannelByKey(key);
		if (channel == null) {
			throw new BaseException(UpiErrorCode.COMN_LINK_DIS_CONNECT);
		}
		channel.send(message);
	}

	@Override
	public Object synSend(String key, Object message, long timeoutMillsec) {
		NettyChannel channel = getChannelByKey(key);
		if (channel == null) {
			throw new BaseException(UpiErrorCode.COMN_LINK_DIS_CONNECT);
		}
		return channel.synSend(message, timeoutMillsec);
	}

	/**
	 * <pre>
	 * <li>空闲报文超时</li>
	 * <li>异常</li>
	 * </pre>
	 */
	@Override
	public void closeCurrentChannel(String key) {
		nettyChannles.closeCurrentChannel(key);
	}

	/**
	 * 被动断链
	 * 
	 */
	@Override
	public void channelDisconnected(String key, Channel channel) {
		nettyChannles.channelDisconnected(key, channel);
	}

	@Override
	public IdleUnit getIdleUnit() {
		return idleUnit;
	}

	@Override
	public void channelConnected(String key, Channel channel) {
		synchronized (this) {
			nettyChannles.setChannel(key, channel);
		}
	}

	@Override
	public Map<String, NettyChannel> getAllChannels() {
		return this.nettyChannles.getAllChannels();

	}

	@Override
	public NettyChannel getChannel(int channelId) {
		return this.nettyChannles.getChannelById(channelId);
	}

}
