/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: NettyLinkO2O.java,v 1.3 2017/01/13 05:37:05 peiwang Exp $
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

/**
 * 应用一对一双向收发，TCP支持客户端和服务器
 * 
 * @author peiwang
 * 
 */
public interface NettyLinkO2O extends NettyBaseLink {

	/**
	 * 是否连通，瞬时状态。后续发送可能报断链错误
	 * @return
	 */
	public boolean isConnected();

	/**
	 * 发送。抛出断链异常
	 * @param message
	 * 
	 */
	public void send(Object message);

	/**
	 * 同步发送。抛出断链异常，或超时异常。无异常时，返回值不为null
	 * @param message
	 * @param timeoutMillsec
	 * @return 
	 */
	public Object synSend(Object message, long timeoutMillsec);

}
