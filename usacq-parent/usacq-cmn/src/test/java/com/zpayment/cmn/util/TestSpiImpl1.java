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
package com.zpayment.cmn.util;

/**
 * @author peiwang
 * @since 2017年3月25日
 */
public class TestSpiImpl1 implements TestSpi{

	/* (non-Javadoc)
	 * @see com.zpayment.cmn.util.TestSpi#echoName()
	 */
	@Override
	public String echoName() {
		// TODO Auto-generated method stub
		return "this is TestSpiImpl1";
	}
	
	/* (non-Javadoc)
	 * @see com.zpayment.cmn.util.NamedService#getSerivceName()
	 */
	@Override
	public String getSerivceName() {
		// TODO Auto-generated method stub
		return "TestSpiImpl1";
	}

}
