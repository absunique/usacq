/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: JsonDecoder.java,v 1.2 2017/03/24 06:18:30 peiwang Exp $
 * 
 * Function:
 * 
 * 固定ASCII长度头的报文拆包
 * 
 * Edit History:
 * 
 * 2016年12月20日 - Create By peiwang
 */
package com.zpayment.trans.upi.link.impl;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.zpayment.cmn.util.JSONUtils;

/**
 * JSON解码器
 * 
 * @author peiwang
 * 
 */
public class JsonDecoder<T> extends OneToOneDecoder {
	private Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
	private Class<T> clazz;
	private String charset;

	public JsonDecoder(Class<T> clazz, Map<String, Class<?>> classMap) {
		this(clazz, classMap, "UTF-8");
	}

	public JsonDecoder(Class<T> clazz, Map<String, Class<?>> classMap,
			String charset) {
		super();
		this.classMap = classMap;
		this.clazz = clazz;
		this.charset = charset;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof ChannelBuffer)) {
			return null;
		}
		ChannelBuffer buffer = (ChannelBuffer) msg;

		byte[] bytes = buffer.array();
		if(bytes.length == 0){
			return ChannelBuffers.EMPTY_BUFFER;
		}
		String jsonStr = new String(bytes, charset);
		return JSONUtils.jsonToObj(jsonStr, clazz, classMap);
	}
}