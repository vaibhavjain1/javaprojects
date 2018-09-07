package com.backend.dao;

import java.util.List;

import com.backend.model.LoginInfo;

public interface LoginDao {
	
	public void signUp(LoginInfo p);
	
	public List<LoginInfo> listOfUsers();

}
