package com.finlabs.finexa.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientRecordDTO {

private String remarks;
private int userId;
private String userName;
private String location;
private int countryId;
private int roleId;
private String roleName;
private int remappedUserId;
private String remappedUserName;
private  int clientId;
private int advisorID;
private String firstName;
private String clientName;
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
private Date remappingDate;

public String getRemarks() {
	return remarks;
}
public void setRemarks(String remarks) {
	this.remarks = remarks;
}
public int getUserId() {
	return userId;
}
public void setUserId(int userId) {
	this.userId = userId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}

public int getCountryId() {
	return countryId;
}
public void setCountryId(int countryId) {
	this.countryId = countryId;
}
public int getRoleId() {
	return roleId;
}
public void setRoleId(int roleId) {
	this.roleId = roleId;
}
public String getRoleName() {
	return roleName;
}
public void setRoleName(String roleName) {
	this.roleName = roleName;
}
public int getRemappedUserId() {
	return remappedUserId;
}
public void setRemappedUserId(int remappedUserId) {
	this.remappedUserId = remappedUserId;
}
public String getRemappedUserName() {
	return remappedUserName;
}
public void setRemappedUserName(String remappedUserName) {
	this.remappedUserName = remappedUserName;
}
public int getClientId() {
	return clientId;
}
public void setClientId(int clientId) {
	this.clientId = clientId;
}
public String getClientName() {
	return clientName;
}
public void setClientName(String clientName) {
	this.clientName = clientName;
}
public Date getRemappingDate() {
	return remappingDate;
}
public void setRemappingDate(Date remappingDate) {
	this.remappingDate = remappingDate;
}
public int getAdvisorID() {
	return advisorID;
}
public void setAdvisorID(int advisorID) {
	this.advisorID = advisorID;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
	
}
