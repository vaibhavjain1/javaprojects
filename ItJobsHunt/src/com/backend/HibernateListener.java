package com.backend;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

// This listener initializes and closes Hibernate on deployment and undeployment,
// instead of the first user request hitting the application:
public class HibernateListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		HibernateSessionFactory.getSessionFactory();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		HibernateSessionFactory.getSessionFactory().close();
	}

}
