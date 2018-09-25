package com.utilities;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ToolLogger {

	public static Logger logger;

	public static void initLogger() {
		Properties log4jProperties = new Properties();
		logger = Logger.getLogger("ToolLogger");
		
		try {
			log4jProperties.load(ToolLogger.class.getResourceAsStream("toollogger.properties"));
			PropertyConfigurator.configure(log4jProperties);
		} catch (Exception e) {
			System.out.println("Error while reading ToolLogger config file."+e);
		}
		logger.info("Logger Initialized");
	}
}
