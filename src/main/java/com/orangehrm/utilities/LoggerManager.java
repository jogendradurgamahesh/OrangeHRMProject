package com.orangehrm.utilities;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LoggerManager {

	//return a logger instance for provider class
	public static Logger getLogger(Class<?> clazz) {
	 return	LogManager.getLogger();
	}
}
