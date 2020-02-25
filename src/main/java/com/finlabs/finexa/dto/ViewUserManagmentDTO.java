package com.finlabs.finexa.dto;



public class ViewUserManagmentDTO {

	private int id;
	private String firstName;
	private String lastName;
	private String role;
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String userName;
	private String userRole;
	private String userLocation;
	private int userRoleId;
	private int userID;
	private String emailID;
	private String activeFlag;
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	private int supervisorRoleId;
	private String supervisorRoleName; 
	
	public ViewUserManagmentDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	public int getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public int getSupervisorRoleId() {
		return supervisorRoleId;
	}

	public void setSupervisorRoleId(int supervisorRoleId) {
		this.supervisorRoleId = supervisorRoleId;
	}

	public String getSupervisorRoleName() {
		return supervisorRoleName;
	}

	public void setSupervisorRoleName(String supervisorRoleName) {
		this.supervisorRoleName = supervisorRoleName;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	
	
}
