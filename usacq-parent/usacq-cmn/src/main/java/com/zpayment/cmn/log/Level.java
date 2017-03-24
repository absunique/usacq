/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */
package com.zpayment.cmn.log;

public enum Level {
	ERROR(0), WARN(1), INFO(2), DEBUG(3), TRACE(4);
	private int level;

	public int getLevel() {
		return level;
	}

	Level(int level) {
		this.level = level;
	}

	public boolean isLoggerEnable(org.slf4j.Logger logger) {
		switch (level) {
		case 0:
			return logger.isErrorEnabled();
		case 1:
			return logger.isWarnEnabled();
		case 2:
			return logger.isInfoEnabled();
		case 3:
			return logger.isDebugEnabled();
		case 4:
			return logger.isTraceEnabled();
		}
		return false;
	}
}
