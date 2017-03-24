/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */
package com.zpayment.cmn.util;

/**
 * HTTP客户端配置
 * 
 * @author peiwang
 * @since 2017年1月22日
 */
public class HttpCfg {
	private int connectTo;

	private int socketTo;

	private int requestTo;

	public HttpCfg(int connectTo, int socketTo, int requestTo) {
		super();
		this.connectTo = connectTo;
		this.socketTo = socketTo;
		this.requestTo = requestTo;
	}

	public int getConnectTo() {
		return connectTo;
	}

	public void setConnectTo(int connectTo) {
		this.connectTo = connectTo;
	}

	public int getSocketTo() {
		return socketTo;
	}

	public void setSocketTo(int socketTo) {
		this.socketTo = socketTo;
	}

	public int getRequestTo() {
		return requestTo;
	}

	public void setRequestTo(int requestTo) {
		this.requestTo = requestTo;
	}

	@Override
	public String toString() {
		return "HttpCfg [connectTo=" + connectTo + ", socketTo=" + socketTo
				+ ", requestTo=" + requestTo + "]";
	}
}
