/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.trans.upi.link.impl;

import java.util.LinkedHashMap;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.zpayment.trans.upi.link.intfc.LinkListenerUdpClt;
/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:38:42
 */
public class LinkListenerUdpCltBean implements LinkListenerUdpClt {
	@Override
	public LinkedHashMap<String, ChannelHandler> getDecoderHandlers() {
		LinkedHashMap<String, ChannelHandler> my = new LinkedHashMap<String, ChannelHandler>();
		my.put("CmnEnc", new StringEncoder());
		return my;
	}

	@Override
	public LinkedHashMap<String, ChannelHandler> getEncoderHandlers() {
		LinkedHashMap<String, ChannelHandler> my = new LinkedHashMap<String, ChannelHandler>();
		my.put("CmnDec", new StringDecoder());
		return my;
	}
	@Override
	public String getName() {
		return "LinkListenerUdpCltBean";
	}
}
