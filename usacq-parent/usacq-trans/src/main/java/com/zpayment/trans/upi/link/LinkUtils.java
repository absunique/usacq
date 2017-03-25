/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.trans.upi.link;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.zpayment.trans.upi.link.unit.LinkAddr;

public class LinkUtils {
	public static LinkAddr convAdress(SocketAddress socketAddress) {
		InetSocketAddress addr = (InetSocketAddress) socketAddress;
		// addr.t
		LinkAddr result = new LinkAddr();
		// result.setIp(addr.toString().split("/")[1].split(":")[0]);
		// try {
		// result.setIp(InetAddress.getByName(addr.getHostName()).getHostName());
		// } catch (UnknownHostException e) {
		// result.setIp(addr.toString().split("/")[1].split(":")[0]);
		// }
		// result.setIp(InetAddress.getByName(addr.getHostName()).getHostName());
		// result.setIp(addr.toString());
		result.setIp(addr.getAddress().getHostAddress());
		result.setPort(addr.getPort());

		return result;
	}

	public static InetSocketAddress convAdress(LinkAddr linkAddr) {
		InetSocketAddress result = new InetSocketAddress(linkAddr.getIp(),
				linkAddr.getPort());
		return result;
	}
}
