package taskerSrc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DBOperations {

	public boolean login(String UserName, String password){
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionUtil.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT USER_NAME FROM User_Details WHERE USER_NAME = '"+UserName+"' AND PASSWORD = '"+password+"';");
			if(rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	public boolean addUserTask(String UserName, String Task_Name, String Task_Description, String Start_Time, String End_Time, String Status, String Comment){
		Statement stmt = null;
		ResultSet rs = null;
		int userId =0 ,taskId;
		try {
			stmt = ConnectionUtil.getConnection().createStatement();
			String TaskSql = "INSERT INTO TASK_DETAILS(Task_Name,Task_Description,Start_Time,End_Time,Task_Status) VALUES ('"+Task_Name+"','"+Task_Description+"','"+new Timestamp(Timestamp.parse(Start_Time))+"','"+new Timestamp(Timestamp.parse(End_Time))+"','"+Status+"')";
			stmt.executeUpdate(TaskSql, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			rs.next();
				taskId = rs.getInt(1);
			rs = stmt.executeQuery("SELECT USER_ID FROM User_Details WHERE User_Name='"+UserName+"'");
			if(rs.next())
				userId = rs.getInt(1);
			
			String TaskDetailsSql = "INSERT INTO User_Task(User_Id,Task_Id,Comment) VALUES ("+userId+","+taskId+",'"+Comment+"')";
			stmt.execute(TaskDetailsSql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public boolean addUser(String UserName, String password){
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionUtil.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT USER_NAME FROM User_Details WHERE USER_NAME = '"+UserName+"';");
			if(rs.next()){
				System.out.println("Already Exists");
				return false;
			}else{
				stmt.execute("INSERT INTO User_Details(User_Name,Password) Values('"+UserName+"','"+password+"');");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public List<String> getUser(){
		List<String> userList =  new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionUtil.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT USER_NAME FROM User_Details;");
			while(rs.next()){
				userList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return userList;
	}
	
	
	public String getUserTasks(String userName){
		Statement stmt = null;
		ResultSet rs = null;
		String listResponse = "";
		try {
			stmt = ConnectionUtil.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM TASK_DETAILS WHERE TASK_ID IN (SELECT TASK_ID FROM USER_TASK WHERE USER_ID IN (SELECT USER_ID FROM USER_DETAILS WHERE USER_NAME = '"+userName+"'));");
			while(rs.next()){
				listResponse+="<li><a href=\"EditTask.html?"+rs.getInt(1)+"\"><span class=\"feature\">"+rs.getString(2)+"</span> <small class=\"description\">"+"StartTime: "+rs.getTimestamp(4)+" EndTime: "+rs.getTimestamp(5)+"</small></a></li>";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(listResponse=="")
			listResponse+="<li><span class=\"feature\">No Task Available</span><small class=\"description\"></small></li>";
		return listResponse;
	}
	
	public String getTaskDetails(String TaskId){
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionUtil.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM Task_Details WHERE Task_Id = "+TaskId+";");
			if(rs.next())
				return rs.getInt(1)+"#"+rs.getString(2)+"#"+rs.getString(3)+"#"+rs.getTimestamp(4)+"#"+rs.getTimestamp(5)+"#"+rs.getString(6);
			else
				return "false";
		} catch (SQLException e) {
			e.printStackTrace();
			return "false";
		} finally{
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String updateTask(String taskId, String TaskName, String taskDescription, String Start_Time, String End_Time, String Status, String Comment){
		PreparedStatement pstmt = null;
		try {
			pstmt = ConnectionUtil.getConnection().prepareStatement("UPDATE TASK_DETAILS SET TASK_NAME = ?, TASK_DESCRIPTION = ?, START_TIME = ?, END_TIME = ?, TASK_STATUS = ? WHERE TASK_ID = ?");
			pstmt.setString(1, TaskName);
			pstmt.setString(2, taskDescription);
			pstmt.setTimestamp(3, Timestamp.valueOf(Start_Time));
			pstmt.setTimestamp(4, Timestamp.valueOf(End_Time));
			pstmt.setString(5, Status);
			pstmt.setInt(6, Integer.parseInt(taskId));
			pstmt.execute();
			return "true";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		} finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
