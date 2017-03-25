/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.trans.upi.link.log;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.slf4j.MDC;

import com.zpayment.trans.upi.link.intfc.NettyBaseLink;

/**
 * 
 * NETTY日志处理器增强实现：增加MDC，用于查找线路报错和报文
 * 
 * @author peiwang
 * @since 2017年1月25日
 */
public class NettyLoggingHandler extends LoggingHandler {

	public static final String NEETY_LINK_LOG_NAME = "link";

	public final static String NETTY_LOG_KEY_INS = "ins";
	public final static String NETTY_LOG_KEY_LINE = "key";

	NettyBaseLink link;

	public NettyLoggingHandler(boolean hexDump, NettyBaseLink link) {
		super(NEETY_LINK_LOG_NAME, hexDump);
		this.link = link;
	}

	/**
	 * 静态生成MDC，根据链路配置，生成ins，生成key(线路domain+线路号) 主要用于一对一连接，以及UPD
	 * 
	 * @param link
	 * @since 2017年1月25日
	 */
	public static void setCurrMdc(NettyBaseLink link) {
		MDC.put(NETTY_LOG_KEY_INS, link.getLinkCfg().getInsIdCd() == null ? ""
				: link.getLinkCfg().getInsIdCd());
		String domain = link.getLinkCfg().getLineDomain();
		domain = domain == null ? "DEFAULT_DOMAIN" : domain;
		MDC.put(NETTY_LOG_KEY_LINE, domain + ":"
				+ link.getLinkCfg().getLineNo());
	}

	/**
	 * 提供动态生成MDC，根据实际链路注入ins，根据链路配置，生成key。 主要用于一对多服务器
	 * 
	 * @param link
	 * @param insIdCd
	 * @since 2017年1月25日
	 */
	public static void setDynMdc(NettyBaseLink link, String insIdCd) {
		String mdcIns = (insIdCd != null) ? insIdCd : ((link.getLinkCfg()
				.getInsIdCd() != null) ? link.getLinkCfg().getInsIdCd() : "");

		MDC.put(NETTY_LOG_KEY_INS, mdcIns == null ? "" : mdcIns);
		String domain = link.getLinkCfg().getLineDomain();
		domain = domain == null ? "DEFAULT_DOMAIN" : domain;
		MDC.put(NETTY_LOG_KEY_LINE, domain + ":"
				+ link.getLinkCfg().getLineNo());
	}

	@Override
	public void log(ChannelEvent e) {
//		setCurrMdc(this.link);
		super.log(e);
	}

	public static void main(String[] args) {
		String a = null;
		String b = a + "asdfsadf";
		System.out.println(b);
	}
}
