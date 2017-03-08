package com.peiwang.usacq.cmn.log;

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
