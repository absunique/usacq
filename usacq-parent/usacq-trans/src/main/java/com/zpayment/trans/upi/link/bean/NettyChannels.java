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

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;

import com.zpayment.cmn.log.Logger;
import com.zpayment.trans.upi.link.log.NettyLoggingHandler;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 2017年2月22日
 */
public class NettyChannels {
	private static final Logger logger = Logger
			.getLogger(NettyLoggingHandler.NEETY_LINK_LOG_NAME);

	protected Map<String, NettyChannel> channels = new HashMap<String, NettyChannel>();
	protected Map<Integer, String> channelsById = new HashMap<Integer, String>();

	/**
	 * 停止所有NettyChannel
	 * 
	 * @since 2017年2月22日
	 */
	public void stopAll() {
		logger.debug("begin stopAll");
		for (NettyChannel nc : channels.values()) {
			nc.closeCurrentChannel();
		}
	}

	public Map<String, NettyChannel> getAllChannels() {
		return channels;
	}

	public NettyChannel getChannelByKey(String key) {
		return channels.get(key);
	}

	public NettyChannel getChannelById(int id) {
		String key = channelsById.get(id);
		return key == null ? null : channels.get(key);
	}

	/**
	 * 主动关闭一条连接
	 * 
	 * @param key
	 * @since 2017年2月22日
	 */
	public void closeCurrentChannel(String key) {
		NettyChannel nc = getChannelByKey(key);
		if (nc != null) {
			Integer id = nc.closeCurrentChannel();
			if (id != null) {
				channelsById.remove(id);
			}
		}
	}

	/**
	 * 连接被动中断
	 * @param key
	 * @param channel
	 * @since 2017年2月22日
	 */
	public void channelDisconnected(String key, Channel channel) {
		NettyChannel nc = getChannelByKey(key);
		if (nc != null) {
			nc.channelDisconnected(channel);
		}
	}

	/**
	 * 建立新的连接
	 * 
	 * @param key
	 * @param channel
	 * @since 2017年2月22日
	 */
	public void setChannel(String key, Channel channel) {
		NettyChannel nc = getChannelByKey(key);
		if (nc == null) {
			nc = new NettyChannel(key);
			channels.put(key, nc);
		}
		// 如果该Key有连接，要先断开
		Integer id = nc.closeCurrentChannel();
		if (id != null) {
			channelsById.remove(id);
		}
		nc.setChannel(channel);
		channelsById.put(channel.getId(), key);
	}

}
