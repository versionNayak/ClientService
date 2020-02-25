package com.finlabs.finexa.dto;

import javax.validation.constraints.NotNull;

public class AdvisorRoleSubmoduleMappingDTO {
	
	private int id;
	@NotNull(message = "subModuleID cannot be null")
	private int subModuleID;
	@NotNull(message = "roleID cannot be null")
	private int roleID;
	private String viewAllowedFlag;
	private String addAllowedFlag;
	private String editAllowedFlag;
	private String deleteAllowedFlag;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSubModuleID() {
		return subModuleID;
	}
	public void setSubModuleID(int subModuleID) {
		this.subModuleID = subModuleID;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public String getViewAllowedFlag() {
		return viewAllowedFlag;
	}
	public void setViewAllowedFlag(String viewAllowedFlag) {
		this.viewAllowedFlag = viewAllowedFlag;
	}
	public String getAddAllowedFlag() {
		return addAllowedFlag;
	}
	public void setAddAllowedFlag(String addAllowedFlag) {
		this.addAllowedFlag = addAllowedFlag;
	}
	public String getEditAllowedFlag() {
		return editAllowedFlag;
	}
	public void setEditAllowedFlag(String editAllowedFlag) {
		this.editAllowedFlag = editAllowedFlag;
	}
	public String getDeleteAllowedFlag() {
		return deleteAllowedFlag;
	}
	public void setDeleteAllowedFlag(String deleteAllowedFlag) {
		this.deleteAllowedFlag = deleteAllowedFlag;
	}
}
