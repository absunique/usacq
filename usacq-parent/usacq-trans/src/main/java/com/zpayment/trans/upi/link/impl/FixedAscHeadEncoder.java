/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: FixedAscHeadEncoder.java,v 1.2 2016/12/28 02:38:24 peiwang Exp $
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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * 固定ASCII码编码器
 * 
 * @author peiwang
 * 
 */
public class FixedAscHeadEncoder extends OneToOneEncoder {

	private int fixedHeadLen;

	public FixedAscHeadEncoder(int fixedHeadLen) {
		super();
		this.fixedHeadLen = fixedHeadLen;
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof ChannelBuffer)) {
			return null;
		}
		ChannelBuffer srcBuffer = (ChannelBuffer) msg;
		int len = srcBuffer.readableBytes();
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(fixedHeadLen + len);
		buffer.writeBytes(String.format("%0" + fixedHeadLen + "d", len)
				.getBytes());
		byte[] result = new byte[srcBuffer.readableBytes()];
		srcBuffer.readBytes(result);
		buffer.writeBytes(result);
		return buffer;
	}

}