/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: NettyLinkO2M.java,v 1.4 2017/02/22 07:00:25 peiwang Exp $
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

import java.util.Map;

import com.zpayment.trans.upi.link.bean.NettyChannel;

/**
 * 多连接服务器
 * 
 * @author peiwang
 * 
 */
public interface NettyLinkO2M extends NettyBaseLink {

	public boolean isConnected(String key);

//	public void send(String key, final byte[] buff);

	public void send(String key, Object message);
	
	public Map<String, NettyChannel> getAllChannels();

	public Object synSend(String key, Object message, long timeoutMillsec);

//	public byte[] synSend(String key, byte[] message, long timeoutMillsec);
}
