package com.backend.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import com.backend.HibernateSessionFactory;
import com.backend.model.LoginInfo;
import com.utilities.ToolLogger;

public class LoginDaoImpl {
	
	public boolean signUp(LoginInfo p) {
		if (p == null)
			return false;
		Session session = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			// Will remove this once we move towards Jackson
			p.setLogin_Password(p.getLogin_Password());
			session.persist(p);
			tx.commit();
			return true;
		} catch (Exception e) {
			ToolLogger.logger.error("Error while inserting signup data"+e);
			return false;
		} finally {
			session.close();
		}
	}

	public List<LoginInfo> listOfUsers() {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		List<LoginInfo> userslist = session.createQuery("from logininfo").list();
		session.close();
		return userslist;
	}
	
	public Response logout(LoginInfo p) {
		if (p == null)
			return Response.status(401).build();
		
		return null;
	}

	public Response login(LoginInfo p) {
		if (p == null)
			return Response.status(401).build();
		
		Session session = null;
		try {
			session = HibernateSessionFactory.getSessionFactory().openSession();
			String str = "SELECT login_UserName As Login_UserName, login_FirstName As Login_FirstName, (CASE WHEN COUNT(*) = 1 THEN 'TRUE' ELSE 'FALSE' END) AS Login_Result"
					+ " from LoginInfo where Login_UserName = '"+p.getLogin_UserName()+"'"
					+ " and Login_Password = '"+p.getEncodedPassword()+"'";

			Query query = session.createQuery(str);
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String,Object>> loginMap=query.list();

			if (loginMap.get(0).get("Login_Result").equals("TRUE")){
				
				//String name, String value, String path, String domain, int version, String comment, int maxAge, Date expiry, boolean secure, boolean httpOnly
				Date today = new Date();
				Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
				String JSESSIONID = new ServerSessionDaoImpl().createSession(p.getLogin_UserName());
				if(JSESSIONID!=null){
					NewCookie newCookie = new NewCookie("JSESSIONIDTEMP", JSESSIONID, null, "ItJobsHunt.com", 1, p.getLogin_UserName(), 60000, tomorrow, false, true);
					ToolLogger.logger.debug("User Login successful for User:"+p.getLogin_UserName());
					return Response.ok().entity(loginMap).cookie(newCookie).build();
				} else {
					return Response.status(500, "Internal Server Error").build();
				}
			}
			else
				return Response.status(401, "Invalid Login").build();
		} catch (HibernateException e) {
			ToolLogger.logger.error("Error while checking login " + e);
			return Response.status(401, "Invalid Login").build();
		} catch (Exception e) {
			ToolLogger.logger.error("Error while checking login " + e);
			return Response.status(401, "Invalid Login").build();
		} finally {
			session.close();
		}
	}
}
