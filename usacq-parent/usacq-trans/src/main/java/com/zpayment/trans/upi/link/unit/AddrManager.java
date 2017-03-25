/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.trans.upi.link.unit;

import java.net.InetSocketAddress;

import com.zpayment.trans.upi.link.LinkUtils;

/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:42:25
 */
public class AddrManager {
	private LinkAddr cfgLocalAddr;
	private LinkAddr cfgRemoteAddr;
	private InetSocketAddress cfgLocalInetAddr;
	private InetSocketAddress cfgRemotIneteAddr;
	private InetSocketAddress localAddr;
	private InetSocketAddress remoteAddr;
	private boolean checkRemoteIp;

	public boolean isCheckRemoteIp() {
		return checkRemoteIp;
	}

	public void setCheckRemoteIp(boolean checkRemoteIp) {
		this.checkRemoteIp = checkRemoteIp;
	}

	public AddrManager(LinkAddr cfgLocalAddr, LinkAddr cfgRemoteAddr,
			boolean checkRemoteIp) {
		this.cfgLocalAddr = cfgLocalAddr;
		this.cfgRemoteAddr = cfgRemoteAddr;
		cfgLocalInetAddr = LinkUtils.convAdress(cfgLocalAddr);
		cfgRemotIneteAddr = LinkUtils.convAdress(cfgRemoteAddr);
		this.checkRemoteIp = checkRemoteIp;
	}

	public boolean checkRemote(LinkAddr remoteAddr) {
		if (checkRemoteIp) {
			if (!remoteAddr.getIp().equals(cfgRemoteAddr.getIp())) {
				return false;
			}
		}
		return true;
	}

	public LinkAddr getCfgLocalAddr() {
		return cfgLocalAddr;
	}

	public void setCfgLocalAddr(LinkAddr cfgLocalAddr) {
		this.cfgLocalAddr = cfgLocalAddr;
	}

	public LinkAddr getCfgRemoteAddr() {
		return cfgRemoteAddr;
	}

	public void setCfgRemoteAddr(LinkAddr cfgRemoteAddr) {
		this.cfgRemoteAddr = cfgRemoteAddr;
	}

	public InetSocketAddress getCfgLocalInetAddr() {
		return cfgLocalInetAddr;
	}

	public void setCfgLocalInetAddr(InetSocketAddress cfgLocalInetAddr) {
		this.cfgLocalInetAddr = cfgLocalInetAddr;
	}

	public InetSocketAddress getCfgRemotIneteAddr() {
		return cfgRemotIneteAddr;
	}

	public void setCfgRemotIneteAddr(InetSocketAddress cfgRemotIneteAddr) {
		this.cfgRemotIneteAddr = cfgRemotIneteAddr;
	}

	public InetSocketAddress getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(InetSocketAddress localAddr) {
		this.localAddr = localAddr;
	}

	public InetSocketAddress getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(InetSocketAddress remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

}
