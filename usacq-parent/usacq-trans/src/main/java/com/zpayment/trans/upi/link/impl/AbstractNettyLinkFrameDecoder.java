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

import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.zpayment.trans.upi.link.intfc.NettyBaseLink;
import com.zpayment.trans.upi.link.intfc.NettyLinkCoder;
/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:37:20
 */
public abstract class AbstractNettyLinkFrameDecoder extends FrameDecoder
		implements NettyLinkCoder {
	NettyBaseLink relatedBean;

	@Override
	public void setRelatedLink(NettyBaseLink nettyLink) {
		relatedBean = nettyLink;
	}

	@Override
	public NettyBaseLink getRelatedLink() {
		return relatedBean;
	}
}
