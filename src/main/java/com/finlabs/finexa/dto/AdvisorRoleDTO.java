package com.finlabs.finexa.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class AdvisorRoleDTO {
	
	private int id;
	@NotEmpty
	private String roleDescription;
	@NotNull
	private Integer supervisorRoleID;
	@NotNull
	private Integer advisorMasterId;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public Integer getSupervisorRoleID() {
		return supervisorRoleID;
	}
	public void setSupervisorRoleID(Integer supervisorRoleID) {
		this.supervisorRoleID = supervisorRoleID;
	}
	public Integer getAdvisorMasterId() {
		return advisorMasterId;
	}
	public void setAdvisorMasterId(Integer advisorMasterId) {
		this.advisorMasterId = advisorMasterId;
	}
	
	@Override
	public String toString() {
		return "AdvisorRoleDTO [id=" + id + ", roleDescription=" + roleDescription + ", supervisorRoleID="
				+ supervisorRoleID + ", advisorMasterId=" + advisorMasterId + "]";
	}


	
	

}
