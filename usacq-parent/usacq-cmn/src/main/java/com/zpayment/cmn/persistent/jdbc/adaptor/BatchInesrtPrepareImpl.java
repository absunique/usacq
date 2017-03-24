///*
// * 
// * Copyright 2017, ZPayment Co., Ltd. All right reserved.
// * 
// * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
// * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
// * PERMISSION OF ZPayment CO., LTD.
// * 
// * 2017年3月23日 - Create By peiwang
// */
//package com.zpayment.cmn.persistent.jdbc.adaptor;
//
//import java.util.Iterator;
///**
// * 
// * @author peiwang
// * @since 2017年3月24日
// * @param <C>
// */
//public class BatchInesrtPrepareImpl<C> implements BatchInsertPrepare<C> {
//
//	private Iterable<C> list;
//	private int count;
//
//	public BatchInesrtPrepareImpl(Iterable<C> list, int count) {
//		this.list = list;
//		this.count = count;
//	}
//
//	@Override
//	public int getBatchCount() {
//		// TODO Auto-generated method stub
//		return count;
//	}
//
//	@Override
//	public Iterator<C> getIterator() {
//		return list.iterator();
//	}
//
//}
