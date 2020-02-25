package com.finlabs.finexa.dto;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientLoginInfoDTO {

	private String userId;
	private String loggedInUserName;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss", timezone="IST")
	private Date loginTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss", timezone="IST")
	private Date logoutTime;
	private String loggedInflag;
	private String token;

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getLoggedInUserName() {
		return loggedInUserName;
	}

	public void setLoggedInUserName(String loggedInUserName) {
		this.loggedInUserName = loggedInUserName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "AdvisorUserLoginInfoDTO [userId=" + userId + ", loggedInUserName=" + loggedInUserName + ", loginTime="
				+ loginTime + ", logoutTime=" + logoutTime + "]";
	}

	public String getLoggedInflag() {
		return loggedInflag;
	}

	public void setLoggedInflag(String loggedInflag) {
		this.loggedInflag = loggedInflag;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

	

}
