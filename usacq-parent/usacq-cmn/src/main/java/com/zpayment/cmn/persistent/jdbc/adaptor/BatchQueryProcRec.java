/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月23日 - Create By peiwang
 */

package com.zpayment.cmn.persistent.jdbc.adaptor;

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 * @param <C>
 */
public interface BatchQueryProcRec<C> {
	public Integer procOneRec(C rec);
}
