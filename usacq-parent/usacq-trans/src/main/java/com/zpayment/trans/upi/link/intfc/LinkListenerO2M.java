/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: LinkListenerO2M.java,v 1.4 2017/03/02 10:36:44 peiwang Exp $
 * 
 * Function:
 * 
 * TODO 功能介绍
 * 
 * Edit History:
 * 
 * 2016-12-20 - Create By peiwang
 */
package com.zpayment.trans.upi.link.intfc;

import java.util.LinkedHashMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;

/**
 * 通信线路监听器
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public interface LinkListenerO2M extends LinkListener{

	/**
	 * 构造空闲报文
	 * 
	 * @return
	 * @since 2017年1月25日
	 */
	public Object getIdlMsg();

	/**
	 * 判断是否空闲报文
	 * 
	 * @param msg
	 * @return
	 * @since 2017年1月25日
	 */
	public boolean isIdlMsg(Object msg);

	/**
	 * 数据处理
	 * 
	 * @param key
	 * @param nlb
	 * @param msg
	 * @since 2017年1月25日
	 */
	public void receive(String key, NettyLinkO2M nlb, Object msg);

	/***
	 * 根据链路构造KEY
	 * 
	 * @param channel
	 * @return
	 */
	public String buildKeyByChannel(Channel channel);

	/**
	 * 保证顺序的解码器
	 * 
	 * @return
	 */
	public LinkedHashMap<String, ChannelHandler> getDecoderHandlers();

	/**
	 * 保证顺序的编码器
	 * 
	 * @return
	 */
	public LinkedHashMap<String, ChannelHandler> getEncoderHandlers();

	/**
	 * 有连接建立
	 * 
	 * @param link
	 * @param key
	 * @param channel
	 * @since 2017年1月25日
	 */
	public void channelConnected(NettyLinkO2M link, String key, Channel channel);

	/**
	 * 有连接中断
	 * 
	 * @param link
	 * @param key
	 * @param channel
	 * @since 2017年1月25日
	 */
	public void channelDisConnected(NettyLinkO2M link, String key,
			Channel channel);

}
