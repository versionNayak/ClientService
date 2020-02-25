package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserRoleReMappingDTO {
	@NotNull(message = "roleID cannot be null")
	@Min(value=1,message = "roleID must not be 0")
	private int roleID;
	private String userName;
	private String userRole;
	@NotNull(message = "userID cannot be null")
	@Min(value=1,message = "userID must not be 0")
	private int userID;
	private int advisorId;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	//@NotNull(message = "effectiveFromDate cannot be null")
	private Date effectiveFromDate;
	
	private String ExistingUserRole;
	
	
	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	public UserRoleReMappingDTO() {
		super();
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public String getExistingUserRole() {
		return ExistingUserRole;
	}

	public void setExistingUserRole(String existingUserRole) {
		ExistingUserRole = existingUserRole;
	}
	
	
}
