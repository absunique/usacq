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

import com.zpayment.cmn.log.Logger;
import com.zpayment.trans.upi.link.bean.NettyLinkFactory;
import com.zpayment.trans.upi.link.intfc.NettyLinkO2O;
import com.zpayment.trans.upi.link.unit.LinkAddr;
import com.zpayment.trans.upi.link.unit.LinkCfg;
/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:38:13
 */
public class LinkListenerO2OBase extends AbstractLinkListenerO2O {

	@Override
	public void receive(NettyLinkO2O nlb, Object msg) {
	}

	@Override
	public Object getIdlMsg() {
		return "0000";
	}

	@Override
	public LinkedHashMap<String, ChannelHandler> getDecoderHandlers() {
		LinkedHashMap<String, ChannelHandler> my = new LinkedHashMap<String, ChannelHandler>();
		// my.put("Frame", new FixedAscHeadDecoder(4));
		my.put("CmnDec", new StringDecoder());
		return my;
	}

	@Override
	public LinkedHashMap<String, ChannelHandler> getEncoderHandlers() {
		LinkedHashMap<String, ChannelHandler> my = new LinkedHashMap<String, ChannelHandler>();
		my.put("CmnEnc", new StringEncoder());
		return my;
	}

	public static void main(String[] args) throws InterruptedException {
		Logger log = Logger.getLogger(LinkListenerO2OBase.class);
		LinkCfg cfg = LinkCfg.newO2OLineCfg("SSS", 1, "00010000", "3",
				new LinkAddr("0.0.0.0", 0), new LinkAddr("172.20.0.12", 60151),
				"0", 10000, 20000, false);
		NettyLinkO2O link = NettyLinkFactory.newO2OLink(true, cfg,
				new LinkListenerO2OBase());
		link.start();

		while (true) {
			try {
				System.out.println("SEND");
				link.synSend("0000", 3000);
			} catch (Exception e) {
				log.error(e);
			} finally {
				Thread.sleep(1000);
			}
		}
	}

}
