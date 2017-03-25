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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.Channels;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.trans.upi.exp.UpiErrorCode;
import com.zpayment.trans.upi.link.LinkUtils;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;
import com.zpayment.trans.upi.link.unit.LinkAddr;
import com.zpayment.trans.upi.link.unit.SynToken;

/**
 * 
 * Netty Channel封装，包含地址，连接级同步发送接收等功能
 * 
 * @author peiwang
 * @since 下午2:33:52
 */
public class NettyChannel {
	private static final Logger logger = Logger
			.getLogger(NettyLoggingHandler.NEETY_LINK_LOG_NAME);
	private Channel channel;
	private volatile boolean isConnected;
	protected final Lock lock = new ReentrantLock();
	protected SynToken token = new SynToken(1000);

	private InetSocketAddress localAddr;
	private InetSocketAddress remoteAddr;

	private LinkAddr friendLocalAddr;
	private LinkAddr friendRemoteAddr;
	private String key;

	public NettyChannel() {
		// TODO Auto-generated constructor stub
	}

	public NettyChannel(String key) {
		this.key = key;
	}

	public InetSocketAddress getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(InetSocketAddress localAddr) {
		this.localAddr = localAddr;
	}

	public InetSocketAddress getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(InetSocketAddress remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * 判断netty链路是否同一条链路
	 * 
	 * @param channel1
	 * @param channel2
	 * @return
	 */
	private boolean isChannelEqual(Channel channel1, Channel channel2) {
		if (channel1 == null || channel2 == null) {
			return false;
		}
		if (channel1.getId() != channel2.getId()) {
			return false;
		}
		if (!channel1.getRemoteAddress().equals(channel2.getRemoteAddress())) {
			return false;
		}
		return true;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setChannel(Channel channel) {
		localAddr = (InetSocketAddress) channel.getLocalAddress();
		remoteAddr = (InetSocketAddress) channel.getRemoteAddress();
		friendLocalAddr = LinkUtils.convAdress(localAddr);
		friendRemoteAddr = LinkUtils.convAdress(remoteAddr);
		this.channel = channel;
		setConnected(true);
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 */
	public void send(Object message) {
		if (channel == null) {
			throw new BaseException(UpiErrorCode.COMN_LINK_DIS_CONNECT);
		}
		channel.write(message);
	}

	/**
	 * 同步发送消息 支持多线程使用同一个CHANNEL同步发送，有序等待
	 * 
	 * @param message
	 */
	public Object synSend(Object message, long timeoutMillsec) {
		lock.lock();
		try {
			token.setTimeout(timeoutMillsec).go();
			send(message);
			return token.get();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * <pre>
	 * 主动关闭当前的连接。
	 * </pre>
	 * 
	 * @return 关闭的连接id。没有可关闭的返回null。
	 * @since 2017年2月22日
	 */
	public Integer closeCurrentChannel() {
		Integer id = null;
		if (channel != null) {
			id = channel.getId();
			logger.info(
					"thread[%s],close current channel local[%s] ,remote[%s]",
					Thread.currentThread().getId(), channel.getLocalAddress(),
					channel.getRemoteAddress());
			setConnected(false);
			Channels.close(channel);
			channel = null;
		}
		return id;
	}

	/**
	 * 连接被动中断，由于NETTY是异步，因此有可能出现主动断1-连2-被动断1的事件发生
	 * 
	 * @param channel
	 * @since 2017年2月22日
	 */
	public void channelDisconnected(Channel channel) {
		if (isChannelEqual(this.channel, channel)) {
			setConnected(false);
		}
	}

	public SynToken getToken() {
		return token;
	}

	public LinkAddr getFriendLocalAddr() {
		return friendLocalAddr;
	}

	public LinkAddr getFriendRemoteAddr() {
		return friendRemoteAddr;
	}

	public String getKey() {
		return key;
	}
}
