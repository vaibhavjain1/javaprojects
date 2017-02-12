package taskerSrc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	public static Connection conn;
	static final String DB_URL = "jdbc:mysql://localhost/TASKER_DB";
	static final String DB_USER = "taskeradmin";
	static final String DB_PASSWORD = "taskeradmin";
	
	private ConnectionUtil(){
		
	}
	
	public static Connection getConnection(){
		if(conn==null){
			try {
				Class.forName("com.mysql.jdbc.Driver");  
				conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			return conn;
		}
		return conn;
	}
	}
