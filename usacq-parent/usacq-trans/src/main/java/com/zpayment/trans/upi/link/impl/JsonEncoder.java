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

import static org.jboss.netty.buffer.ChannelBuffers.copiedBuffer;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.zpayment.cmn.util.JSONUtils;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:37:58
 */
public class JsonEncoder<T> extends OneToOneEncoder {
	private Class<T> clazz;
	private String charset;

	public JsonEncoder(Class<T> clazz) {
		this(clazz, "UTF-8");
	}

	public JsonEncoder(Class<T> clazz, String charset) {
		super();
		this.clazz = clazz;
		this.charset = charset;
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {

		if (msg == ChannelBuffers.EMPTY_BUFFER) {
			return msg;
		}
		if (clazz.isInstance(msg)) {
			String jsonStr = JSONUtils.toJson(msg);
			byte[] jsonStrBytes = jsonStr.getBytes(charset);
			return copiedBuffer(jsonStrBytes);
		}

		return null;

	}

}