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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.timer.Timer;
import com.zpayment.cmn.timer.TimerFactory;
import com.zpayment.cmn.timer.TimerTask;
import com.zpayment.cmn.timer.config.FixedDurationTimerConfig;
import com.zpayment.trans.upi.link.intfc.NettyLinkForTcpO2OInner;

/**
 * NettyLinki保活器,只保活NettyLinkO2O的客户端
 * 
 * @author wangpei
 * 
 */
public class NettyLinkKeepAlive implements Serializable, TimerTask {
	private static final Logger log = Logger
			.getLogger(NettyLinkKeepAlive.class);

	private static Timer tmr;
	private static final long serialVersionUID = 1L;
	private Map<Integer, NettyLinkForTcpO2OInner> linkCache;
	private List<NettyLinkForTcpO2OInner> newLinkCache;

	private NettyLinkKeepAlive() {
		// linkCache = new ConcurrentHashMap<String, Map<Integer,
		// NettyLinkForTcpO2OInner>>();
		linkCache = new HashMap<Integer, NettyLinkForTcpO2OInner>();
		// newLinkCache = new CopyOnWriteArrayList<NettyLinkForTcpO2OInner>();
		newLinkCache = new LinkedList<NettyLinkForTcpO2OInner>();
	}

	public static NettyLinkKeepAlive getInstance() {
		return LazeDefaultLinkManager.dlm;
	}

	static class LazeDefaultLinkManager {
		private final static NettyLinkKeepAlive dlm = new NettyLinkKeepAlive();
	}

	public void register(NettyLinkForTcpO2OInner link) {
		int id = link.getLinkId();
		linkCache.put(id, link);
		newLinkCache.add(link);
	}

	public void unRegister(NettyLinkForTcpO2OInner link) {
		int id = link.getLinkId();
		linkCache.put(id, null);
		newLinkCache.remove(link);
	}

	public void keepAlive() {
		Iterator<NettyLinkForTcpO2OInner> linkItr = newLinkCache.iterator();
		while (linkItr.hasNext()) {
			NettyLinkForTcpO2OInner link = linkItr.next();
			if (link.isStarted() && !link.isSvr() && !link.isConnected()
					&& link.isTimeToConnect()) {
				log.info("total[%d],need connect[%s]", newLinkCache.size(),
						link.getLinkCfg());
				link.connect();
			}
		}
	}

	@Override
	public boolean execute() {
		log.info("begin keepAlive");
		keepAlive();
		log.info("end keepAlive");
		return true;
	}

	public void start() {
		tmr = TimerFactory.getInstance("quartz").newTimer(new FixedDurationTimerConfig(true,
				NettyLinkKeepAlive.class.getSimpleName(),
				NettyLinkKeepAlive.class.getSimpleName(), 1000, null), this);
		tmr.start();
	}

	public void stop() {
		tmr.stop();
	}
}
