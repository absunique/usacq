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

import com.zpayment.cmn.util.StringUtils;

/**
 * 线路配置
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class LinkCfg {
	public static final String LINK_SVR = "2";
	public static final String LINK_CLT = "3";
	public static final String LINK_SVR_O2M = "M2";
	public static final String LINK_UDP_SVR = "U2";
	public static final String LINK_UDP_CLT = "U3";

	/**
	 * 需要根据链路类型，使用工厂方法初始化链路配置
	 */
	private LinkCfg() {

	}

	public static boolean isValidTp(String tp) {
		return ((!StringUtils.isEmpty(tp)) && tp.equals(LINK_SVR)
				|| tp.equals(LINK_CLT) || tp.equals(LINK_SVR_O2M)
				|| tp.equals(LINK_UDP_SVR) || tp.equals(LINK_UDP_CLT));
	}

	public static LinkCfg newO2OLineCfg(String lineDomain, int lineNo,
			String insIdCd, String lineTp, LinkAddr localAddr,
			LinkAddr remoteAddr, String idlTstTp, int sndLmt, int rcvLmt,
			boolean chkIp) {
		LinkCfg cfg = new LinkCfg();
		cfg.lineDomain = lineDomain;
		cfg.lineNo = lineNo;
		cfg.lineTp = lineTp;
		cfg.insIdCd = insIdCd;
		cfg.localAddr = localAddr;
		cfg.remoteAddr = remoteAddr;
		cfg.idlTstTp = idlTstTp;
		cfg.sndIdlLmt = sndLmt;
		cfg.rcvIdlLmt = rcvLmt;
		cfg.remoteChkIn = chkIp;
		return cfg;
	}

	public static LinkCfg newO2MLineCfg(String lineDomain, int lineNo,
			String insIdCd, String lineTp, LinkAddr localAddr,
			LinkAddr remoteAddr, String idlTstTp, int sndLmt, int rcvLmt) {
		LinkCfg cfg = new LinkCfg();
		cfg.lineDomain = lineDomain;
		cfg.lineNo = lineNo;
		cfg.insIdCd = insIdCd;
		cfg.lineTp = lineTp;
		cfg.localAddr = localAddr;
		cfg.remoteAddr = remoteAddr;
		cfg.idlTstTp = idlTstTp;
		cfg.sndIdlLmt = sndLmt;
		cfg.rcvIdlLmt = rcvLmt;
		return cfg;
	}

	public static LinkCfg newUdpLineCfg(String lineDomain, int lineNo,
			String lineTp, LinkAddr localAddr, LinkAddr remoteAddr) {
		LinkCfg cfg = new LinkCfg();
		cfg.lineDomain = lineDomain;
		cfg.lineNo = lineNo;
		cfg.lineTp = lineTp;
		cfg.localAddr = localAddr;
		cfg.remoteAddr = remoteAddr;
		return cfg;
	}

	/** < 线路组，用于区别不同组的线路 */
	private String lineDomain;

	/** < 线路号 */
	private int lineNo;

	/** < 连接的机构代码或主机代码 */
	private String insIdCd;

	/** < SERVER/CLIENT */
	private String lineTp;

	/** < 本地地址和端口 */
	private LinkAddr localAddr;

	/** < 对方地址和端口 */
	private LinkAddr remoteAddr;

	/** < 接收通道空闲时间(s) */
	private int rcvIdlLmt;

	/** < 发送通道空闲时间(s) */
	private int sndIdlLmt;

	/** < 发送tps */
	private int sndTps;

	/** < 接收tps */
	private int rcvTps;

	/** < 记录报文标志 */
	private String logMsgIn;

	/** < 是否发送空闲报文 */
	private String idlTstTp;

	/** < 对方地址检查标志 */
	private boolean remoteChkIn;

	public String getLineDomain() {
		return lineDomain;
	}

	public int getLineNo() {
		return lineNo;
	}

	public String getInsIdCd() {
		return insIdCd;
	}

	public String getLineTp() {
		return lineTp;
	}

	public LinkAddr getLocalAddr() {
		return localAddr;
	}

	public LinkAddr getRemoteAddr() {
		return remoteAddr;
	}

	public int getRcvIdlLmt() {
		return rcvIdlLmt;
	}

	public int getSndIdlLmt() {
		return sndIdlLmt;
	}

	public int getSndTps() {
		return sndTps;
	}

	public int getRcvTps() {
		return rcvTps;
	}

	public String getLogMsgIn() {
		return logMsgIn;
	}

	public String getIdlTstTp() {
		return idlTstTp;
	}

	public boolean isRemoteChkIn() {
		return remoteChkIn;
	}

	@Override
	public String toString() {
		return "LinkCfg [lineKey=" + lineDomain + ", lineNo=" + lineNo
				+ ", insIdCd=" + insIdCd + ", lineTp=" + lineTp
				+ ", localAddr=" + localAddr + ", remoteAddr=" + remoteAddr
				+ ", rcvIdlLmt=" + rcvIdlLmt + ", sndIdlLmt=" + sndIdlLmt
				+ ", sndTps=" + sndTps + ", rcvTps=" + rcvTps + ", logMsgIn="
				+ logMsgIn + ", idlTstTp=" + idlTstTp + ", remoteChkIn="
				+ remoteChkIn + "]";
	}

}
