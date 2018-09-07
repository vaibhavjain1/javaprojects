package com.backend.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.backend.HibernateSessionFactory;
import com.backend.model.LoginInfo;

public class LoginDaoImpl implements LoginDao {
	
	@Override
	public void signUp(LoginInfo p) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.persist(p);
		tx.commit();
		session.close();
	}

	@Override
	public List<LoginInfo> listOfUsers() {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		List<LoginInfo> userslist = session.createQuery("from logininfo").list();
		session.close();
		return userslist;
	}

}
