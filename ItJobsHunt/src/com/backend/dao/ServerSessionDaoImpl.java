package com.backend.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.backend.HibernateSessionFactory;
import com.utilities.ToolLogger;

public class ServerSessionDaoImpl {
	public String createSession(String userName){
		Session session = null;
		try {
			UUID JSESSIONID = UUID.randomUUID();
			session = HibernateSessionFactory.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			String sessionQuery = "INSERT INTO serversessioninfo(session_Id, session_UserName, session_Valid)VALUES('"+JSESSIONID+"','"+userName+"',TRUE) ON DUPLICATE KEY UPDATE session_Id='"+JSESSIONID+"', session_Valid = TRUE";
			session.createNativeQuery(sessionQuery);
			tx.commit();
			return JSESSIONID.toString();
		} catch (Exception e) {
			ToolLogger.logger.error("Error while inserting Session "+e);
			return null;
		} finally {
			if(session!=null) session.close();
		}
	}
	
	public Cookie createLoginCookie(String userName){
		//String name, String value, String path, String domain, int version, String comment, int maxAge, Date expiry, boolean secure, boolean httpOnly
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		String JSESSIONID = createSession(userName);
		Cookie newCookie = null;
		if(JSESSIONID!=null){
			newCookie = new Cookie("JSESSIONIDTEMP", JSESSIONID);
			newCookie.setDomain("ItJobsHunt.com");
			newCookie.setMaxAge(60000);
			newCookie.setVersion(1);
			//null, , 1, userName, , , false, true
			ToolLogger.logger.debug("User Login successful for User:"+userName);
		}
		return newCookie;
	}
	
	public void validateSession(){
		
	}
	
	public void removeSession(){
		
	}
	
	public void updateSession(){
		
	}
	
	public void getSession(){
		
	}
}
