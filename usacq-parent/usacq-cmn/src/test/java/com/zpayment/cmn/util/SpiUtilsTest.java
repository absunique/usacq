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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

/**
 * @author peiwang
 * @since 2017年3月25日
 */
public class SpiUtilsTest {

	@Test
	public void test() throws IOException {
		List<TestSpi> list = SpiUtils.getSpiList(TestSpi.class);
		assertEquals(2, list.size());
		for (TestSpi ts : list) {
			System.out.println(ts);
		}
		list = SpiUtils.getSpiList(TestSpi.class);
		// assertEquals(2, list.size());
		for (TestSpi ts : list) {
			System.out.println(ts);
		}
	}

}
