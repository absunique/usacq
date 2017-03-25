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
import org.junit.Test;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;

/**
 * @author peiwang
 * @since 2017年3月25日
 */
public class BaseExceptionTestZh {
	@Test
	public void test() {
		BaseException be = new BaseException(BaseErrorCode.COMN_NULL_EXCEPTION,
				new Object[] { "name" });

		assertEquals("0002-空指针异常,变量名:name", be.getMessage());
		be = new BaseException(UpiErrorCode.COMN_LINK_UNKOWN_ERROR);

		assertEquals("2001-未知通信异常", be.getMessage());
	}

}
