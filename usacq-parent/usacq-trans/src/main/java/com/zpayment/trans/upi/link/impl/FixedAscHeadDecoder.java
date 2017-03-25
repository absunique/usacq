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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.zpayment.cmn.log.Logger;

/**
 * 固定拆包器，拆包后会带头
 * 
 * @author peiwang
 * 
 */
public class FixedAscHeadDecoder extends FrameDecoder {
	private static final Logger logger = Logger
			.getLogger(FixedAscHeadDecoder.class);

	private int fixedHeadLen;
	private boolean offerHead;

	public FixedAscHeadDecoder(int fixedHeadLen) {
		this(fixedHeadLen, false);
	}

	public FixedAscHeadDecoder(int fixedHeadLen, boolean offerHead) {
		this.fixedHeadLen = fixedHeadLen;
		this.offerHead = offerHead;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		logger.debug("begin decoding");
		int readIndex = buffer.readerIndex();

		int readable = buffer.readableBytes();
		if (readable < fixedHeadLen) {
			logger.debug("readable[%d]<fixedHeadLen[%d]", readable,
					fixedHeadLen);
			return null;
		}
		buffer.markReaderIndex();

		byte[] head = new byte[fixedHeadLen];
		buffer.readBytes(head, 0, fixedHeadLen);
		int bodyLen = 0;
		bodyLen = calBodyLen(head);
		readable = buffer.readableBytes();
		if (readable < bodyLen) {
			buffer.resetReaderIndex();
			return null;
		}
		ChannelBuffer cb;
		if (offerHead) {
			cb = extractFrame(buffer, readIndex, fixedHeadLen + bodyLen);
		} else {
			cb = extractFrame(buffer, readIndex + fixedHeadLen, bodyLen);
		}
		buffer.skipBytes(bodyLen);
		return cb;
	}

	public int calBodyLen(byte[] head) {
		return Integer.parseInt(new String(head));
	}

}