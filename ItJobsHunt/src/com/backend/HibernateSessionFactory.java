package com.backend;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.backend.model.LoginInfo;

public class HibernateSessionFactory {

	public static volatile SessionFactory sessionFactory;
	
	private HibernateSessionFactory() throws Exception {
		throw new Exception("Not Allowed to instantiate HibernateSessionFactory");
	}
	
	public static SessionFactory getSessionFactory(){
		if(sessionFactory==null){
			synchronized (HibernateSessionFactory.class) {
				// create Configuration class
				// Configuration object parses and reads .cfg.xml file
				Configuration c = new Configuration();
				c.configure("com/backend/resources/hibernate.cfg.xml");
				sessionFactory = c.buildSessionFactory();
			}
		}
		return sessionFactory;
	}
	
	public static void main(String[] args) {
		LoginInfo obj = new LoginInfo();
		obj.setLogin_UserName("Ganesh1");
		obj.setLogin_Password("Ganesh");
		obj.setLogin_FirstName("Ganesh");
		obj.setLogin_SecondName("Mahadev");
		obj.setLogin_PhoneNo("9742442667");
		obj.setLogin_RecoveryEmail("abc@gmail.com");
		
		Session session = getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		session.save(obj);
		tx.commit();
		session.close();
		getSessionFactory().close();
	}
}
