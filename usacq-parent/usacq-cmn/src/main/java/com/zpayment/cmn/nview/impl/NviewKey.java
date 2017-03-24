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
package com.zpayment.cmn.nview.impl;

import java.util.Arrays;

/**
 * 缓存索引对象
 * 
 * @author peiwang
 * 
 */
public class NviewKey {
	/**
	 * 不需要匹配的KEY
	 */
	public static Object NVIEWKEY_NO_NEED_MATCH = new Object();
	private Object[] keyValues;

	// TODO 为了增强整数、字符串之间的匹配，需要将OBJECT进行转换。统一得到字符串。
	public NviewKey(Object[] keyValues) {
		this.keyValues = keyValues;
	}

	public boolean match(NviewKey obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (obj.keyValues == null && this.keyValues != null) {
			return false;
		}
		if (obj.keyValues != null && this.keyValues == null) {
			return false;
		}
		if (obj.keyValues.length != this.keyValues.length) {
			return false;
		}
		for (int index = 0; index < keyValues.length; index++) {
			if (obj.keyValues[index] == NVIEWKEY_NO_NEED_MATCH) {
				continue;
			}
			if (!this.keyValues[index].equals(obj.keyValues[index])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(keyValues);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NviewKey other = (NviewKey) obj;
		if (!Arrays.equals(keyValues, other.keyValues))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (this == NVIEWKEY_NO_NEED_MATCH) {
			return "NVIEWKEY_NO_NEED_MATCH";
		}
		return "NviewKey [keyValues=" + Arrays.toString(keyValues) + "]";
	}

}
