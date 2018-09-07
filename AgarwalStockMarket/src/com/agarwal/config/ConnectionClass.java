package com.agarwal.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionClass {

	static BasicDataSource dataSource = null;
	public static BasicDataSource getDataSource(){
		  if(dataSource==null){
			  dataSource = new BasicDataSource();
		      dataSource.setDriverClassName("org.h2.Driver");
		      dataSource.setUrl("jdbc:h2:tcp://localhost/~/test");
		      dataSource.setUsername("sa");
		      dataSource.setPassword("");
		      dataSource.setMaxTotal(10);
		      dataSource.setMaxConnLifetimeMillis(10000);
		      dataSource.setMaxIdle(10);
		  }
	      return dataSource;
	}
	
	public static void main(String[] args) {
	
		try {
			Connection con = getDataSource().getConnection();
			Connection con1 = getDataSource().getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from INFORMATION_SCHEMA.taBLES ;");
			while(rs.next()){
				System.out.println(rs.getObject(1));
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
