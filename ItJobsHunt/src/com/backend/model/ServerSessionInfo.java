package com.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "serversessioninfo")
public class ServerSessionInfo {
	@Id
	@Column(name = "Session_Id", nullable = false)
	@Getter
	@Setter
	private String session_Id;
	// TODO
	@Getter
	@Setter
	@ManyToOne
    @JoinColumn(name = "Session_UserName", referencedColumnName = "Login_UserName", nullable = false)
	@ForeignKey(name="Fk_ServerSessionInfo_username")
	private LoginInfo session_UserName;

	@Column(name = "Session_Valid", nullable = false)
	@Getter
	@Setter
	private boolean session_Valid;
	
	@Column(name = "Session_LastAccess")
	@Getter
	@Setter
	private Date session_LastAccess;
}
