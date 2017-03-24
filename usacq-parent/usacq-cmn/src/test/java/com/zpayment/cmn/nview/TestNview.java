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
package com.zpayment.cmn.nview;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.zpayment.cmn.nview.intf.NviewManager;
import com.zpayment.cmn.persistent.DBUser;
import com.zpayment.cmn.persistent.PersistentService;
import com.zpayment.cmn.persistent.TestPersistent;

/**
 * @author peiwang
 * @since 2017年3月24日
 */
@Component
public class TestNview {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"com/zpayment/cmn/nview/testNview.xml");
		TestNview tp = ac.getBean(TestNview.class);
		tp.test(ac);
	}

	public void test(ApplicationContext ac) {
		NviewManager nm = ac.getBean(NviewManager.class);
		PersistentService ps = ac.getBean("PersistentService_JDBC_swtDb",
				PersistentService.class);
		nm.init(ps, "test_zpayment_view_def", null);
		System.out.println(Arrays.toString(nm.getList(DBUser.class).toArray()));
	}
}
