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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.zpayment.cmn.exp.BaseException;
import com.zpayment.trans.upi.exp.UpiErrorCode;
import com.zpayment.trans.upi.link.factory.NettyTcpO2OPipeLineFactory;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;
import com.zpayment.trans.upi.link.unit.CltConnFreq;
import com.zpayment.trans.upi.link.unit.LinkCfg;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:34:59
 */
public class NettyLinkO2OCltBean extends AbstractNettyLinkBeanO2O {

	protected static ChannelFactory channelFactory = new NioClientSocketChannelFactory(
			bossExecutor, workerExecutor);
	private ClientBootstrap bootstrap;

	protected final Lock lock = new ReentrantLock();

	private int connectTo = 30000;

	private CltConnFreq cltConnFreq;

	public CltConnFreq getCltConnFreq() {
		return cltConnFreq;
	}

	@Override
	public boolean isTimeToConnect() {
		if (System.currentTimeMillis() > getCltConnFreq().getNextConnTs()) {
			return true;
		}
		return false;
	}

	protected NettyLinkO2OCltBean(LinkCfg lc, LinkListenerO2O linkListener) {
		super(lc, linkListener);
		bootstrap = new ClientBootstrap(channelFactory);
		cltConnFreq = new CltConnFreq();
	}

	private boolean isConnecting = false;

	public boolean isConnecting() {
		return isConnecting;
	}

	public void setConnecting(boolean isConnecting) {
		log.debug("setConnecting " + isConnecting);
		this.isConnecting = isConnecting;
		if (isConnecting) {
			cltConnFreq.connOnce();
		}
	}

	protected ChannelPipelineFactory getPipeLineFactory() {
		return new NettyTcpO2OPipeLineFactory(this, linkListener,
				executionHandler);
	}

	public void _connect() {
		if (!isStarted()) {
			log.error("not started,cannot connect");
			return;
		}
		if (!lock.tryLock()) {
			log.warn("tryLock fail");
			return;
		}
		try {
			if (!isConnected() && !isConnecting()) {
				final CountDownLatch channelLatch = new CountDownLatch(1);
				setConnecting(true);
				// TODO这里的异常不属于连接异常，暂时不捕获
				ChannelFuture future = bootstrap.connect(addrManager
						.getCfgRemotIneteAddr());

				future.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture cf)
							throws Exception {
						channelLatch.countDown();
					}
				});

				boolean finished;

				try {
					finished = channelLatch.await(connectTo,
							TimeUnit.MILLISECONDS);
				} catch (InterruptedException ex) {
					throw new BaseException(
							UpiErrorCode.COMN_LINK_UNKOWN_ERROR, ex);
				}

				Throwable cause = future.getCause();
				if (cause != null) {
					throw new BaseException(
							UpiErrorCode.COMN_LINK_UNKOWN_ERROR, cause);
				}

				// 连接超时
				if (!finished) {
					throw new BaseException(
							UpiErrorCode.COMN_LINK_UNKOWN_ERROR);
				}
				log.info("connect finished");
			}
		} catch (BaseException e) {
			throw e;
		} finally {
			setConnecting(false);
			lock.unlock();
		}
	}

	@Override
	public void connect() {
		/**
		 * 连接成功与否不影响启动，因此单开线程做
		 */
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					NettyLoggingHandler.setCurrMdc(NettyLinkO2OCltBean.this);
					_connect();
				} catch (Exception e) {
					log.error(e);
				}
			}

		};
		Thread t = new Thread(r);
		t.start();
	}

	@Override
	public boolean doStart() {
		bootstrap = new ClientBootstrap(channelFactory);

		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("connectTimeoutMillis", connectTo);

		bootstrap.setPipelineFactory(getPipeLineFactory());
		NettyLinkKeepAlive.getInstance().register(this);
		connect();
		return true;
	}

	@Override
	public boolean doStop() {
		closeCurrentChannel();
		return true;
	}

}
