/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: NettyLinkUdpSvrInnter.java,v 1.2 2017/01/13 05:37:05 peiwang Exp $
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

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;

/**
 * UPD客户端
 * 
 * @author peiwang
 * 
 */
public interface NettyLinkUdpSvrInnter extends NettyLinkUdpSvr {
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e);

}
