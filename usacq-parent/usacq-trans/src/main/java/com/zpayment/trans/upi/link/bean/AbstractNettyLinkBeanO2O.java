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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.channel.Channel;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.trans.upi.exp.UpiErrorCode;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2OInner;
import com.zpayment.trans.upi.link.unit.AddrManager;
import com.zpayment.trans.upi.link.unit.IdleUnit;
import com.zpayment.trans.upi.link.unit.LinkCfg;
import com.zpayment.trans.upi.link.unit.SynToken;

/**
 * AbstractNettyLinkBeanO2O TCP一对一连接实现，包括客户端，服务器 支持： 空闲报文 指定超时连接 IP检查 异步、同步发送
 * 
 * @author peiwang
 * @since 下午2:33:25
 */
public abstract class AbstractNettyLinkBeanO2O extends
		AbstractNettyBaseLinkBean implements NettyLinkForTcpO2OInner {

	protected AddrManager addrManager;

	// 一对一连接，只有一条NettyChannel
	protected NettyChannel channel = new NettyChannel();
	private boolean isSvr;

	private IdleUnit idleUnit;

	protected final Lock lock = new ReentrantLock();

	protected LinkListenerO2O linkListener;

	protected AbstractNettyLinkBeanO2O(LinkCfg linkCfg,
			LinkListenerO2O linkListener) {
		super(linkCfg);
		if (linkListener == null) {
			log.error("linkListner == null");
			throw new BaseException(UpiErrorCode.COMN_LINK_CONFIG_ERROR);
		}
		this.linkListener = linkListener;

		if (linkCfg.getLineTp().equals(LinkCfg.LINK_CLT)) {

			isSvr = false;

		} else if (linkCfg.getLineTp().equals(LinkCfg.LINK_SVR)) {

			isSvr = true;
		} else {
			throw new BaseException(UpiErrorCode.COMN_LINK_CONFIG_ERROR,
					new Object[] { linkCfg.getLineTp() });
		}
		addrManager = new AddrManager(linkCfg.getLocalAddr(),
				linkCfg.getRemoteAddr(), linkCfg.isRemoteChkIn());

		idleUnit = new IdleUnit(linkCfg.getIdlTstTp(), linkCfg.getSndIdlLmt(),
				linkCfg.getRcvIdlLmt());
	}

	/**
	 * 主要接口功能
	 */
	@Override
	public boolean isConnected() {
		return channel.isConnected();
	}

	@Override
	public void send(Object message) {
		getChannel().send(message);
	}

	@Override
	public Object synSend(Object message, long timeoutMillsec) {
		try {
			return getChannel().synSend(message, timeoutMillsec);
		} catch (BaseException e) {
			closeCurrentChannel();
			throw e;
		}
	}

	@Override
	public SynToken getToken() {
		return channel.getToken();
	};

	@Override
	public boolean isSvr() {
		return isSvr;
	}

	@Override
	public AddrManager getAddrManager() {
		return addrManager;
	}

	@Override
	public void closeCurrentChannel() {
		this.channel.closeCurrentChannel();
	}

	@Override
	public void channelDisconnected(Channel channel) {
		this.channel.channelDisconnected(channel);
	}

	@Override
	public IdleUnit getIdleUnit() {
		return idleUnit;
	}

	@Override
	public void setChannel(Channel channel) {
		this.channel.setChannel(channel);
	}

	@Override
	public void connect() {
		throw new BaseException(BaseErrorCode.COMN_NOT_SUPPORTED);
	}

	@Override
	public boolean isTimeToConnect() {
		throw new BaseException(BaseErrorCode.COMN_NOT_SUPPORTED);
	}

	public NettyChannel getChannel() {
		return channel;
	}

}
