package com.backend.model;

import java.util.Base64;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.utilities.ToolLogger;

@Entity
@Table(name = "logininfo")
public class LoginInfo {

	@Id
	@Column(name = "Login_Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private int login_Id;
	
	@Column(name = "Login_UserName", nullable = false, unique = true, length = 50)
	@Getter
	@Setter
	private String login_UserName;
	
	@Column(name = "Login_Password", nullable = false)
	private String login_Password;

	@Column(name = "Login_FirstName", nullable = false)
	@Getter
	@Setter
	private String login_FirstName;
	
	@Column(name = "Login_LastName")
	@Getter
	@Setter
	private String login_LastName;
	
	@Column(name = "Login_PhoneNo", nullable = false)
	@Getter
	@Setter
	private String login_PhoneNo;
	
	@Column(name = "Login_RecoveryEmail", nullable = false)
	@Getter
	@Setter
	private String login_RecoveryEmail;
	
	@Column(name = "Login_Dp")
	@Lob
	@Getter
	@Setter
	private byte[] login_db;
	
	@Column(name = "Login_Active", nullable = false)
	@Getter
	@Setter
	private boolean login_active = true;
	
	/*@Column(name = "Login_last", nullable = false)
	@Getter
	@Setter
	private Date login_last;*/
	
	public String getLogin_Password() {
		byte[] decodedBytes;
		try {
			decodedBytes = Base64.getDecoder().decode(login_Password);
			return decodedBytes.toString();
		} catch (Exception e) {
			ToolLogger.logger.debug("password is not Base64 encoded");
			return login_Password;
		}
	}

	public void setLogin_Password(String login_Password) {
		String encodedBytes = Base64.getEncoder().encodeToString(login_Password.getBytes());
		this.login_Password = encodedBytes;
	}
	
	public String getEncodedPassword(){
		return Base64.getEncoder().encodeToString(login_Password.getBytes());
	}
}
