package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.User;

public class UserDTO {
	private int id;
	private int userID;
	//@NotNull(message = "firstName cannot be null")
	private String firstName;
	//@NotNull(message = "lastName cannot be null")
	private String lastName;
	//@NotNull(message = "emailID cannot be null")
	private String emailID; 
	//@NotNull(message = "phoneNo cannot be null")
	private BigInteger phoneNo;
	private Integer countryId;
	private String countryName;
	private String state ;
	private String city;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date birthDate;
	private String organizationName; 
	private String disributorCode; // Distributor Code*
	private String employeeCode; // Employee Code*
	private Integer roleId;  //Role*
	private long supervisorId;  // suoervisor
	private Date supervisorFrom;
	private String password;
	private boolean isFirstLogin;
	private int advisorID;
	private int masterID;
	private String salutation;
	private String gender;
	private String phoneCountryCode;
	private String loginUsername;
	private String organizationFlag;
	private String admin;
	private String advisorAdmin;
	private String  client;
	private String  clientInfoView;
	private String  clientInfoAddEdit;
	private String  clientInfoDelete;
	private String  budgetManagementView;
	private String  portfolioManagementView;
	private String  portfolioManagementAddEdit;
	private String  goalPlanningView;
	private String  goalPlanningAddEdit;
	private String  financialPlanningView;
	private String  financialPlanningAddEdit;
	private String  investView;
	private String  investAddEdit;
	private String  userManagementView;
	private String  userManagementAddEdit;
	private String  userManagementDelete;
	private String  clientRecordsView;
	private String  mastersView;
	private String  mastersAddEdit;
	private String  mastersDelete;
	private String  mfBackOfficeView;
	private String  mfBackOfficeAddEdit;
	private String orgFlag;
	private String orgName;
	private List<String> roles;
	private List<String> users;
	private String userRole;
	private String userName;
	private String roleDescription;
	private int riskProfileCreation;
	private int productRecoCreation;
	private int clientCreation;
	private String supervisorName;
	private String supervisorRole;
	private String finlabsAdminRole;
	private int supervisorRoleId;
	private int supervisorUserId;
	private int clientID;
	
	public String getOrgFlag() {
		return orgFlag;
	}
	public void setOrgFlag(String orgFlag) {
		this.orgFlag = orgFlag;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	private String activeFlag;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date createdOn; 
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
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
	
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOrganisationName() {
		return organizationName;
	}
	public void setOrganisationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getDisributorCode() {
		return disributorCode;
	}
	public void setDisributorCode(String disributorCode) {
		this.disributorCode = disributorCode;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public long getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(long supervisorId) {
		this.supervisorId = supervisorId;
	}
	public Date getSupervisorFrom() {
		return supervisorFrom;
	}
	public void setSupervisorFrom(Date supervisorFrom) {
		this.supervisorFrom = supervisorFrom;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isFirstLogin() {
		return isFirstLogin;
	}
	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public BigInteger getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(BigInteger phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}
	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}
	public String getLoginUsername() {
		return loginUsername;
	}
	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}
	public int getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}
	public String getOrganizationFlag() {
		return organizationFlag;
	}
	public void setOrganizationFlag(String organizationFlag) {
		this.organizationFlag = organizationFlag;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	public int getMasterID() {
		return masterID;
	}
	public void setMasterID(int masterID) {
		this.masterID = masterID;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roleList) {
		this.roles = roleList;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}

	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getAdvisorAdmin() {
		return advisorAdmin;
	}
	public void setAdvisorAdmin(String advisorAdmin) {
		this.advisorAdmin = advisorAdmin;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public int getRiskProfileCreation() {
		return riskProfileCreation;
	}
	public void setRiskProfileCreation(int riskProfileCreation) {
		this.riskProfileCreation = riskProfileCreation;
	}
	public int getProductRecoCreation() {
		return productRecoCreation;
	}
	public void setProductRecoCreation(int productRecoCreation) {
		this.productRecoCreation = productRecoCreation;
	}
	public int getClientCreation() {
		return clientCreation;
	}
	public void setClientCreation(int clientCreation) {
		this.clientCreation = clientCreation;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFinlabsAdminRole() {
		return finlabsAdminRole;
	}
	public void setFinlabsAdminRole(String finlabsAdminRole) {
		this.finlabsAdminRole = finlabsAdminRole;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	public String getSupervisorRole() {
		return supervisorRole;
	}
	public void setSupervisorRole(String supervisorRole) {
		this.supervisorRole = supervisorRole;
	}
	public int getSupervisorRoleId() {
		return supervisorRoleId;
	}
	public void setSupervisorRoleId(int supervisorRoleId) {
		this.supervisorRoleId = supervisorRoleId;
	}
	public int getSupervisorUserId() {
		return supervisorUserId;
	}
	public void setSupervisorUserId(int supervisorUserId) {
		this.supervisorUserId = supervisorUserId;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getClientInfoView() {
		return clientInfoView;
	}
	public void setClientInfoView(String clientInfoView) {
		this.clientInfoView = clientInfoView;
	}
	public String getClientInfoAddEdit() {
		return clientInfoAddEdit;
	}
	public void setClientInfoAddEdit(String clientInfoAddEdit) {
		this.clientInfoAddEdit = clientInfoAddEdit;
	}
	public String getClientInfoDelete() {
		return clientInfoDelete;
	}
	public void setClientInfoDelete(String clientInfoDelete) {
		this.clientInfoDelete = clientInfoDelete;
	}
	public String getBudgetManagementView() {
		return budgetManagementView;
	}
	public void setBudgetManagementView(String budgetManagementView) {
		this.budgetManagementView = budgetManagementView;
	}
	public String getPortfolioManagementView() {
		return portfolioManagementView;
	}
	public void setPortfolioManagementView(String portfolioManagementView) {
		this.portfolioManagementView = portfolioManagementView;
	}
	public String getPortfolioManagementAddEdit() {
		return portfolioManagementAddEdit;
	}
	public void setPortfolioManagementAddEdit(String portfolioManagementAddEdit) {
		this.portfolioManagementAddEdit = portfolioManagementAddEdit;
	}
	public String getGoalPlanningView() {
		return goalPlanningView;
	}
	public void setGoalPlanningView(String goalPlanningView) {
		this.goalPlanningView = goalPlanningView;
	}
	public String getGoalPlanningAddEdit() {
		return goalPlanningAddEdit;
	}
	public void setGoalPlanningAddEdit(String goalPlanningAddEdit) {
		this.goalPlanningAddEdit = goalPlanningAddEdit;
	}
	public String getFinancialPlanningView() {
		return financialPlanningView;
	}
	public void setFinancialPlanningView(String financialPlanningView) {
		this.financialPlanningView = financialPlanningView;
	}
	public String getFinancialPlanningAddEdit() {
		return financialPlanningAddEdit;
	}
	public void setFinancialPlanningAddEdit(String financialPlanningAddEdit) {
		this.financialPlanningAddEdit = financialPlanningAddEdit;
	}
	public String getInvestView() {
		return investView;
	}
	public void setInvestView(String investView) {
		this.investView = investView;
	}
	public String getInvestAddEdit() {
		return investAddEdit;
	}
	public void setInvestAddEdit(String investAddEdit) {
		this.investAddEdit = investAddEdit;
	}
	public String getUserManagementView() {
		return userManagementView;
	}
	public void setUserManagementView(String userManagementView) {
		this.userManagementView = userManagementView;
	}
	public String getUserManagementAddEdit() {
		return userManagementAddEdit;
	}
	public void setUserManagementAddEdit(String userManagementAddEdit) {
		this.userManagementAddEdit = userManagementAddEdit;
	}
	public String getUserManagementDelete() {
		return userManagementDelete;
	}
	public void setUserManagementDelete(String userManagementDelete) {
		this.userManagementDelete = userManagementDelete;
	}
	public String getClientRecordsView() {
		return clientRecordsView;
	}
	public void setClientRecordsView(String clientRecordsView) {
		this.clientRecordsView = clientRecordsView;
	}
	public String getMastersView() {
		return mastersView;
	}
	public void setMastersView(String mastersView) {
		this.mastersView = mastersView;
	}
	public String getMastersAddEdit() {
		return mastersAddEdit;
	}
	public void setMastersAddEdit(String mastersAddEdit) {
		this.mastersAddEdit = mastersAddEdit;
	}
	public String getMastersDelete() {
		return mastersDelete;
	}
	public void setMastersDelete(String mastersDelete) {
		this.mastersDelete = mastersDelete;
	}
	public String getMfBackOfficeView() {
		return mfBackOfficeView;
	}
	public void setMfBackOfficeView(String mfBackOfficeView) {
		this.mfBackOfficeView = mfBackOfficeView;
	}
	public String getMfBackOfficeAddEdit() {
		return mfBackOfficeAddEdit;
	}
	public void setMfBackOfficeAddEdit(String mfBackOfficeAddEdit) {
		this.mfBackOfficeAddEdit = mfBackOfficeAddEdit;
	}

}
