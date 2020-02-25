package com.finlabs.finexa.dto;



public class RoleDTO {
	
	private int id;
	private int advisorID;
    private String roleDescription;
    private int supervisorRoleID;
    
    public RoleDTO() {
		super();
	}

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

	public int getSupervisorRoleID() {
		return supervisorRoleID;
	}

	public void setSupervisorRoleID(int supervisorRoleID) {
		this.supervisorRoleID = supervisorRoleID;
	}
	
	

	public int getAdvisorID() {
		return advisorID;
	}

	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}

	@Override
	public String toString() {
		return "RoleDTO [id=" + id + ", advisorID=" + advisorID + ", roleDescription=" + roleDescription
				+ ", supervisorRoleID=" + supervisorRoleID + "]";
	}

	

	

}
