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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;

import com.zpayment.cmn.log.Logger;
import com.zpayment.trans.upi.link.intfc.NettyBaseLink;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;
import com.zpayment.trans.upi.link.unit.LinkCfg;

/**
 * 所有连接基类，对应一条线路配置，支持启动，和停止
 * 
 * @author peiwang
 * @since 下午2:32:38
 */
public abstract class AbstractNettyBaseLinkBean implements NettyBaseLink {

	protected static final Logger log = Logger
			.getLogger(NettyLoggingHandler.NEETY_LINK_LOG_NAME);

	/**
	 * 一个JVM中的NettyBaseLink，ID全局唯一
	 */
	private static AtomicInteger GLB_LINK_ID = new AtomicInteger(1);

	private volatile boolean isStart = false;
	private LinkCfg linkCfg;
	private int linkId;
	private String linkKeyDesc;
	protected static Executor bossExecutor = Executors.newCachedThreadPool();
	protected static Executor workerExecutor = Executors.newCachedThreadPool();
	protected static ExecutionHandler executionHandler = new ExecutionHandler(
			new MemoryAwareThreadPoolExecutor(20, 1048576, 1048576, 30,
					TimeUnit.SECONDS));

	/**
	 * 只能通过工厂方法创建link
	 */
	protected AbstractNettyBaseLinkBean(LinkCfg linkCfg) {
		this.linkId = GLB_LINK_ID.getAndIncrement();
		this.linkCfg = linkCfg;
		linkKeyDesc = String.format(
				"id[%d]:addr[%s:%s],dom[%s],no[%d],ins[%s]", linkId,
				linkCfg.getLocalAddr(), linkCfg.getRemoteAddr(),
				linkCfg.getLineDomain(), linkCfg.getLineNo(),
				linkCfg.getInsIdCd());
	}

	/**
	 * 返回绑定的线路配置
	 */
	@Override
	public LinkCfg getLinkCfg() {
		return linkCfg;
	}

	@Override
	public boolean isStarted() {
		return isStart;
	}

	abstract protected boolean doStop();

	@Override
	public boolean stop() {
		synchronized (this) {
			log.debug("this start status[%s],try stop", isStart);
			if (isStart == false) {
				return false;
			}
			boolean ret = doStop();
			isStart = false;
			return ret;
		}
	}

	abstract protected boolean doStart();

	@Override
	public boolean start() {
		NettyLoggingHandler.setCurrMdc(this);
		synchronized (this) {
			log.debug("this start status[%s],try start", isStart);
			if (isStart == true) {
				return false;
			}
			boolean ret = doStart();
			isStart = ret;
			return ret;
		}
	}

	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	@Override
	public String toString() {
		return linkKeyDesc;
	}
}
