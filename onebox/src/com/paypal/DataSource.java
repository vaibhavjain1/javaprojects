package com.paypal;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

class CloseResource extends Thread{
	DataSource instance;
	
	public CloseResource(DataSource instance) {
		this.instance = instance;
	}
	
	@Override
	public void run() {
		//instance.closeDataSource();
		System.out.println("\n---------------------All resources closed---------------------");
	}
}

public class DataSource {
	
    private static DataSource datasource;
    private BasicDataSource ds;
    
	 private DataSource() throws IOException, SQLException, PropertyVetoException {
	        ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setUsername(Resources.SPOTLIGHT_USERNAME);
	        ds.setPassword(Resources.SPOTLIGHT_PASSWORD);
	        ds.setUrl(Resources.SPOTLIGH_URL);
	        ds.setMaxTotal(1);
	        ds.setMinIdle(5);
	        ds.setMaxIdle(20);
	        ds.setMaxOpenPreparedStatements(180);
	    }
	
	 public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
	        if (datasource == null) {
	            datasource = new DataSource();
	            return datasource;
	        } else {
	            return datasource;
	        }
	    }

	    public Connection getConnection() throws SQLException {
	        return this.ds.getConnection();
	    }
	    
	
}
