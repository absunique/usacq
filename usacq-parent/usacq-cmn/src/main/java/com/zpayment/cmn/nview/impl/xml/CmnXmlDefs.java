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
package com.zpayment.cmn.nview.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:47:16
 */
@XStreamAlias("viewdefs")
public class CmnXmlDefs {
	@XStreamImplicit
	private List<CmnXmlDef> viewDefs;

	@Override
	public String toString() {
		return "viewDefs [viewDefs=" + viewDefs + "]";
	}

	public List<CmnXmlDef> getViewDefs() {
		return viewDefs;
	}

}
