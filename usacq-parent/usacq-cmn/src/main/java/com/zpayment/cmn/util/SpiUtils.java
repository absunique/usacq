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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author peiwang
 * @since 2017年3月25日
 */
public class SpiUtils {

	private static ConcurrentHashMap<Class<?>, SpiHolder<?>> spiHolderMap = new ConcurrentHashMap<Class<?>, SpiUtils.SpiHolder<?>>();

	@SuppressWarnings("unchecked")
	public static <T> List<T> getSpiList(Class<T> clazz) {
		if (!spiHolderMap.contains(clazz)) {
			synchronized (SpiUtils.class) {
				if (!spiHolderMap.contains(clazz)) {
					SpiHolder<T> newHolder = new SpiHolder<T>(clazz);
					newHolder.init();
					spiHolderMap.putIfAbsent(clazz, newHolder);
				}
			}
		}

		return (List<T>) spiHolderMap.get(clazz).getSpiProviders();
	}

	static class SpiHolder<T> {
		Class<T> clazz;
		volatile List<T> spiProviders;

		public SpiHolder(Class<T> clazz) {
			this.clazz = clazz;
		}

		void init() {
			ServiceLoader<T> sl = ServiceLoader.load(clazz);
			Iterator<T> iter = sl.iterator();
			List<T> result = new ArrayList<T>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			spiProviders = result;
		}

		List<T> getSpiProviders() {
			return spiProviders;
		}

	}
}
