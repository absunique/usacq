/*
 * 
 * Copyright 2016, China UnionPay Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF CHINA UNIONPAY CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF CHINA UNIONPAY CO., LTD.
 * 
 * $Id: HttpCfg.java,v 1.1 2017/01/22 01:51:23 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2016-12-14 - Create By peiwang
 */
package com.peiwang.usacq.cmn.util;

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
