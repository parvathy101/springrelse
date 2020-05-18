package com.iThingsFoundation.IThingsFramework.Login;

import org.springframework.http.HttpStatus;

import com.iThingsFoundation.IThingsFramework.common.Message;

public class Login {
	

	private String userName;
	private String password;
	private Object uuid;
	private int roleId;
	private String status;
	private String tokenId;
	private Message message;
	
	
	public Login() {
		super();
	}


	public Login(String userName, String password, Object uuid, int roleId, String status, String tokenId,
			Message message) {
		super();
		this.userName = userName;
		this.password = password;
		this.uuid = uuid;
		this.roleId = roleId;
		this.status = status;
		this.tokenId = tokenId;
		this.message = message;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Object getUuid() {
		return uuid;
	}


	public void setUuid(Object uuid) {
		this.uuid = uuid;
	}


	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getTokenId() {
		return tokenId;
	}


	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}


	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}

	
		
	
}