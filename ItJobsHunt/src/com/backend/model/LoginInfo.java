package com.backend.model;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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
	
	@Column(name = "Login_SecondName")
	@Getter
	@Setter
	private String login_SecondName;
	
	@Column(name = "Login_PhoneNo", nullable = false)
	@Getter
	@Setter
	private String login_PhoneNo;
	
	@Column(name = "Login_RecoveryEmail", nullable = false)
	@Getter
	@Setter
	private String login_RecoveryEmail;
	
	public String getLogin_Password() {
		byte[] decodedBytes = Base64.getDecoder().decode(login_Password);
		return decodedBytes.toString();
	}

	public void setLogin_Password(String login_Password) {
		byte[] encodedBytes = Base64.getEncoder().encode(login_Password.getBytes());
		this.login_Password = encodedBytes.toString();
	}
}
