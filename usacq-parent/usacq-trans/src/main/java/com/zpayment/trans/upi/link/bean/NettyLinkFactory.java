/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.trans.upi.link.bean;

import org.jboss.netty.logging.InternalLoggerFactory;

import com.zpayment.trans.upi.link.impl.LinkListenerO2MSynWrapper;
import com.zpayment.trans.upi.link.impl.LinkListenerSynWrapper;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2M;
import com.zpayment.trans.upi.link.intfc.LinkListenerO2O;
import com.zpayment.trans.upi.link.intfc.LinkListenerUdpClt;
import com.zpayment.trans.upi.link.intfc.LinkListenerUdpSvr;
import com.zpayment.trans.upi.link.intfc.NettyLinkO2M;
import com.zpayment.trans.upi.link.intfc.NettyLinkO2O;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpClt;
import com.zpayment.trans.upi.link.intfc.NettyLinkUdpSvr;
import com.zpayment.trans.upi.link.log.UpLoggerFactory;
import com.zpayment.trans.upi.link.unit.LinkCfg;
/**
 * 
 * Link工厂
 *
 * @author peiwang
 * @since  下午2:34:34
 */
public class NettyLinkFactory {

	static {
		InternalLoggerFactory.setDefaultFactory(new UpLoggerFactory());
	}
	public static NettyLinkO2O newO2OLink(boolean isSyn, LinkCfg linkCfg,
			LinkListenerO2O linkListener) {
		if (isSyn) {
			linkListener = new LinkListenerSynWrapper(linkListener);
		}

		if (linkCfg.getLineTp().equals(LinkCfg.LINK_SVR)) {

			return new NettyLinkO2OSvrBean(linkCfg, linkListener);
		} else {
			return new NettyLinkO2OCltBean(linkCfg, linkListener);
		}
	}

	public static NettyLinkO2M newO2MLink(boolean isSyn, LinkCfg linkCfg,
			LinkListenerO2M linkListener) {
		if (isSyn) {
			LinkListenerO2M synLinkListener = new LinkListenerO2MSynWrapper(
					linkListener);
			return new NettyLinkBeanO2M(linkCfg, synLinkListener);
		} else {
			return new NettyLinkBeanO2M(linkCfg, linkListener);
		}

	}

	public static NettyLinkUdpSvr newUdpSvrLink(LinkCfg linkCfg,
			LinkListenerUdpSvr linkListener) {

		return new NettyLinkUdpSvrBean(linkCfg, linkListener);

	}

	public static NettyLinkUdpClt newUdpCltLink(LinkCfg linkCfg,
			LinkListenerUdpClt linkListener) {

		return new NettyLinkUdpCltBean(linkCfg, linkListener);

	}

}
