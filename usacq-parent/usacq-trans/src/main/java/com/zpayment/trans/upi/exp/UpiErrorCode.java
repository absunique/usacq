/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月25日 - Create By peiwang
 */
package com.zpayment.trans.upi.exp;

/**
 * upi 错误码，2开头
 * @author peiwang
 * @since 2017年3月25日
 */
public class UpiErrorCode {
	/** 0060 ~ 0069 socket通信异常 */
	public static final String COMN_LINK_UNKOWN_ERROR = "2001"; // CMN - 通信异常
	public static final String COMN_LINK_DIS_CONNECT = "2061"; // CMN - 通信链路未连接异常
	public static final String COMN_LINK_SEND_ERROR = "2062"; // CMN - 发送消息异常
	public static final String COMN_LINK_TO_BYTES_ERROR = "2063"; // CMN - 转换报文错误
	public static final String COMN_LINK_PARSE_BYTES_ERROR = "2064"; // CMN - 解析报文错误
	public static final String COMN_LINK_TIMEOUT = "2065"; // CMN - 响应超时
	public static final String COMN_SEND_QUEUE_OVERLOAD = "2067"; // CMN - 发送队列超限异常
	public static final String COMN_LINK_CONFIG_ERROR = "2068"; // CMN - 通信配置错误
	public static final String COMN_LINK_BIND_ERROR = "2069";	// CMN - 绑定错误
	
}
