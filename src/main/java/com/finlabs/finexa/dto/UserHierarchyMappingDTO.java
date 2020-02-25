package com.finlabs.finexa.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserHierarchyMappingDTO {

	private int userRoleId;
	private String userRole;
    private Integer supervisorRoleID;
    private String supervisorRole;
    private int advisorId;
    @NotNull(message = "userId cannot be null")
    private int userId;
    private String userName;
    @NotNull(message = "supervisorId cannot be null")
    private int supervisorId;
    private String supervisorName;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
    @NotNull(message = "effectiveDate cannot be null")
    private Date effectiveDate;
    
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
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public int getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}
	@Override
	public String toString() {
		return "UserHierarchyMappingDTO [userRoleId=" + userRoleId + ", userRole=" + userRole + ", supervisorRoleID="
				+ supervisorRoleID + ", supervisorRole=" + supervisorRole + ", advisorId=" + advisorId + ", userId="
				+ userId + ", userName=" + userName + ", supervisorId=" + supervisorId + ", supervisorName="
				+ supervisorName + ", effectiveDate=" + effectiveDate + "]";
	}
	
	
    
    
}
