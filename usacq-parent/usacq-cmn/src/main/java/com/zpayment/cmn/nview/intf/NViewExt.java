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
package com.zpayment.cmn.nview.intf;

import java.util.List;

/**
 * 
 * 缓存扩展操作，装载前，装载后，重载前，重载后
 * 
 * @author peiwang
 * 
 * @param <T>
 */
public interface NViewExt<T> {

	/**
	 * 构造缓存之前的回调（重载也会调用）
	 * @param view
	 * @param loadData
	 * @since 2017年2月10日
	 */
	public void beforeLoad(NView<T> view,List<T> loadData);

	/**
	 * 重载前调用
	 * @param view
	 * @param loadData
	 * @since 2017年2月10日
	 */
	public void beforeReload(NView<T> view,List<T> loadData);

	/**
	 * 构造完缓存后的回调（重载也会调用）
	 * 
	 * @param view
	 */
	public void afterLoad(NView<T> view);

	/**
	 * 重载时调用
	 * 
	 * @param view
	 */
	public void afterReload(NView<T> view);
}
