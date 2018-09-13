package com.backend.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.backend.HibernateSessionFactory;
import com.backend.model.LoginInfo;
import com.utilities.ToolLogger;

public class LoginDaoImpl implements LoginDao {
	
	@Override
	public void signUp(LoginInfo p) {
		try {
			Session session = HibernateSessionFactory.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			session.persist(p);
			tx.commit();
			session.close();
		} catch (Exception e) {
			ToolLogger.logger.error("Error while inserting signup data"+e);
		}
	}

	@Override
	public List<LoginInfo> listOfUsers() {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		List<LoginInfo> userslist = session.createQuery("from logininfo").list();
		session.close();
		return userslist;
	}

	@Override
	public boolean login(LoginInfo p) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		List<LoginInfo> userslist = session.createQuery("select * from logininfo").list();
		session.close();
		return false;
	}

}
