package log;

import java.util.Arrays;
import java.util.List;

import com.peiwang.usacq.cmn.log.Logger;

public class LoggerTest {
	public static void main(String[] args) {
		Logger logError = Logger.getLogger("error");
		Logger logWarn = Logger.getLogger("warn");
		Logger logInfo = Logger.getLogger("info");
		Logger logDebug = Logger.getLogger("debug");
		Logger logTrace = Logger.getLogger("trace");

		List<Logger> loggerList = Arrays.asList(logError, logWarn, logInfo,
				logDebug, logTrace);
		for (Logger logger : loggerList) {
			System.out.println("-----------");
			logger.error("E");
			logger.warn("W");
			logger.info("I");
			logger.debug("D");
			logger.trace("T");
			System.out.println("-----------");
		}

	}
}
