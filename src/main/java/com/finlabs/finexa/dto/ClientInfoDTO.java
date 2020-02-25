package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientInfoDTO {
	private int id;
	private int userID;
	private int advisorMasterID;
	private String salutation;
	private String name;
	private String firstName;
	private String middleName;
	private String lastName;
	private String emailId;
	private BigInteger mobile;
	private String gender;
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String state;
	private String country;
	private String disableFlag;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Timestamp createdOn;
	private int age;
	private String birthDate;
	private String retirementStatus;
	private Date lastLoginTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date lastLogoutTime;
	private String clientInfoAddEdit;
	
	private String clientInfoDelete;
	
	private String clientInfoView;
	
	private String budgetManagementView;
	
	private String financialPlanningAddEdit;
	
	private String financialPlanningView;

	private String goalPlanningAddEdit;

	private String goalPlanningView;

	private String investAddEdit;

	private String investView;

	private String mfBackOfficeView;

	private String portfolioManagementAddEdit;

	private String portfolioManagementView;
	
	private String maritalStatus;
	
	private String alreadyRetired;
	
	private String organization;
	
	private String user;
	
	private String userLocation;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getAdvisorMasterID() {
		return advisorMasterID;
	}

	public void setAdvisorMasterID(int advisorMasterID) {
		this.advisorMasterID = advisorMasterID;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public BigInteger getMobile() {
		return mobile;
	}

	public void setMobile(BigInteger mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getRetirementStatus() {
		return retirementStatus;
	}

	public void setRetirementStatus(String retirementStatus) {
		this.retirementStatus = retirementStatus;
	}

	public String getDisableFlag() {
		return disableFlag;
	}

	public void setDisableFlag(String disableFlag) {
		this.disableFlag = disableFlag;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLogoutTime() {
		return lastLogoutTime;
	}

	public void setLastLogoutTime(Date lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	public String getFinancialPlanningAddEdit() {
		return financialPlanningAddEdit;
	}

	public void setFinancialPlanningAddEdit(String financialPlanningAddEdit) {
		this.financialPlanningAddEdit = financialPlanningAddEdit;
	}

	public String getFinancialPlanningView() {
		return financialPlanningView;
	}

	public void setFinancialPlanningView(String financialPlanningView) {
		this.financialPlanningView = financialPlanningView;
	}

	public String getGoalPlanningAddEdit() {
		return goalPlanningAddEdit;
	}

	public void setGoalPlanningAddEdit(String goalPlanningAddEdit) {
		this.goalPlanningAddEdit = goalPlanningAddEdit;
	}

	public String getGoalPlanningView() {
		return goalPlanningView;
	}

	public void setGoalPlanningView(String goalPlanningView) {
		this.goalPlanningView = goalPlanningView;
	}

	public String getInvestAddEdit() {
		return investAddEdit;
	}

	public void setInvestAddEdit(String investAddEdit) {
		this.investAddEdit = investAddEdit;
	}

	public String getInvestView() {
		return investView;
	}

	public void setInvestView(String investView) {
		this.investView = investView;
	}

	public String getMfBackOfficeView() {
		return mfBackOfficeView;
	}

	public void setMfBackOfficeView(String mfBackOfficeView) {
		this.mfBackOfficeView = mfBackOfficeView;
	}

	public String getPortfolioManagementAddEdit() {
		return portfolioManagementAddEdit;
	}

	public void setPortfolioManagementAddEdit(String portfolioManagementAddEdit) {
		this.portfolioManagementAddEdit = portfolioManagementAddEdit;
	}

	public String getPortfolioManagementView() {
		return portfolioManagementView;
	}

	public void setPortfolioManagementView(String portfolioManagementView) {
		this.portfolioManagementView = portfolioManagementView;
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

	public String getClientInfoView() {
		return clientInfoView;
	}

	public void setClientInfoView(String clientInfoView) {
		this.clientInfoView = clientInfoView;
	}

	public String getBudgetManagementView() {
		return budgetManagementView;
	}

	public void setBudgetManagementView(String budgetManagementView) {
		this.budgetManagementView = budgetManagementView;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getAlreadyRetired() {
		return alreadyRetired;
	}

	public void setAlreadyRetired(String alreadyRetired) {
		this.alreadyRetired = alreadyRetired;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	
}
