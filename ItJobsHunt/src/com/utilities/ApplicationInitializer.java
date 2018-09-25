package com.utilities;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.backend.HibernateSessionFactory;
import com.utilities.ToolLogger;

// This listener initializes and closes Hibernate on deployment and undeployment,
// instead of the first user request hitting the application:
public class ApplicationInitializer implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			ToolLogger.initLogger();
		} catch (Exception e) {
			System.out.println("Error while initializing logger "+e);
		}
		try {
			HibernateSessionFactory.getSessionFactory();
			ToolLogger.logger.info("Initialized context");
		} catch (Exception e) {
			ToolLogger.logger.fatal("Error while initializing Hibernate session "+ e);
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		HibernateSessionFactory.getSessionFactory().close();
		ToolLogger.logger.info("Destroying context");
	}

}
