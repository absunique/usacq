/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: LinkListenerUdpSvr.java,v 1.3 2017/03/02 10:36:44 peiwang Exp $
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

import java.net.SocketAddress;
import java.util.LinkedHashMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;

/**
 * UDP 服务器事件监听
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public interface LinkListenerUdpSvr  extends LinkListener{

	public void receive(NettyLinkUdpSvr nlb, Object msg, Channel channel,
			SocketAddress remoteAdress);

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

}
