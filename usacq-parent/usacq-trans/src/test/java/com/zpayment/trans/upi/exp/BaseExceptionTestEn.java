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

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;

/**
 * @author peiwang
 * @since 2017年3月25日
 */
public class BaseExceptionTestEn {
	@Before
	public void init(){
		Locale.setDefault(Locale.ENGLISH);
	}
	@Test
	public void test() {
		BaseException be = new BaseException(BaseErrorCode.COMN_NULL_EXCEPTION,
				new Object[] { "name" });

		assertEquals("0002-NULL Exception,var is name", be.getMessage());
		be = new BaseException(UpiErrorCode.COMN_LINK_UNKOWN_ERROR);

		assertEquals("2001-UNKNOW LINK EXCEPTION", be.getMessage());
	}

}
