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
//
///**
// * 
// * @author peiwang
// * @since 2017年3月24日
// * @param <C>
// */
//public interface BatchInsertPrepare<C> {
//	/* 达到该记录数，执行一次executeBatch()，以防止SQL语句过长 */
//	public int getBatchCount();
//
//	/* 迭代器，用于批量插入获取数据 */
//	public Iterator<C> getIterator();
//}
