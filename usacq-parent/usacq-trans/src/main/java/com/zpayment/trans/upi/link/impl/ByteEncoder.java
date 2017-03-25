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

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.zpayment.cmn.log.Logger;

/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:37:58
 */
public class ByteEncoder extends OneToOneEncoder {
	private static final Logger logger = Logger.getLogger(ByteEncoder.class);

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (msg instanceof byte[]) {
			return copiedBuffer((byte[]) msg);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("encode msg[%s]", msg);
		}

		return msg;
	}

}