package com.finlabs.finexa.dto;

public class UserRoleCreationDTO {

	private int userRoleId;
	private String userName;
	private String userRole;
    private Integer supervisorRoleID;
    private String supervisorRole;
    private String supervisorName;
    private int advisorId;
    private int userId;
    
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public Integer getSupervisorRoleID() {
		return supervisorRoleID;
	}
	public void setSupervisorRoleID(Integer supervisorRoleID) {
		this.supervisorRoleID = supervisorRoleID;
	}
	public String getSupervisorRole() {
		return supervisorRole;
	}
	public void setSupervisorRole(String supervisorRole) {
		this.supervisorRole = supervisorRole;
	}
	public int getAdvisorId() {
		return advisorId;
	}
	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	@Override
	public String toString() {
		return "UserRoleCreationDTO [userRoleId=" + userRoleId + ", userRole=" + userRole + ", supervisorRoleID="
				+ supervisorRoleID + ", supervisorRole=" + supervisorRole + ", advisorId=" + advisorId + ", userId="
				+ userId + "]";
	}
    
    
}
