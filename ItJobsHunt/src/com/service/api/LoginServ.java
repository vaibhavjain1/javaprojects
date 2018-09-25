package com.service.api;

/*
 Tutorial
 https://www.journaldev.com/1933/java-servlet-filter-example-tutorial
 */
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.backend.dao.LoginDaoImpl;
import com.backend.model.LoginInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.utilities.ToolLogger;

@Path("/loginServ")
//http://localhost:8080/ItJobsHunt/api/loginServ
public class LoginServ {

	@GET
	@Produces(MediaType.TEXT_XML) // Type of Object returned
	/*To call this: 
	 	http://localhost:8080/ItJobsHunt/api/loginServ
	 */
	public String checkService(){
		return "<Hello>Rest Service is up</Hello>";
	}
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	/*
	 {
	 	"login_UserName":"admin",
	 	"login_Password":"admin"
	 }
	 */
	public Response login(JSONObject login_Data,@Context HttpHeaders httpHeaders, @Context HttpServletRequest request, @Context HttpServletResponse response){
		LoginInfo loginObj = null;
		Map<String, Cookie> temp = httpHeaders.getCookies();
		/*	Not working for some reason
			httpServletRequest.getRequestedSessionId();
		*/
		// TODO
		//System.out.println(request.getRequestedSessionId());
		try {
			loginObj = new Gson().fromJson(login_Data.toJSONString(), LoginInfo.class);
		} catch (JsonSyntaxException e) {
			ToolLogger.logger.error("Error while coverting signup data to object");
			return null;
		}
		try { 
			//response.setHeader("locationSuccess", "signin.html");
			//javax.servlet.http.Cookie cok = new javax.servlet.http.Cookie("Hello", "World");
			//response.addCookie(cok);
			//response.flushBuffer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Response r = new LoginDaoImpl().login(loginObj); 
		return r;
	}
	
	@POST
	@Path("signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	/*
	 {
	 	"login_UserName":"admin",
	 	"login_Password":"admin",
	 	"login_FirstName":"admin",
	 	"login_PhoneNo":"9999999999",
	 	"login_RecoveryEmail":"admin@admin.com"
	 }
	 */
	public JSONObject signup(JSONObject login_Data){
		LoginInfo loginObj = null;
		try {
			loginObj = new Gson().fromJson(login_Data.toJSONString(), LoginInfo.class);
		} catch (JsonSyntaxException e) {
			ToolLogger.logger.error("Error while coverting signup data to object");
			return null;
		}
		new LoginDaoImpl().signUp(loginObj);
		return null;
	}
	
	
}
