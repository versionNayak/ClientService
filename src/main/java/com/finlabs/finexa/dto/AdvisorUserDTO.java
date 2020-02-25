package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.util.Date;

public class AdvisorUserDTO {
	private static final long serialVersionUID = 1L;


	private int id;
	
	private String activeFlag;
	
	private Date birthDate;

	private String city;

	private String emailID;

	private String employeeCode;

	private String firstName;

	private String gender;

	private Date lastLoginTime;

	private Date lastLogoutTime;

	private String lastName;

	private String loginPassword;

	private String loginUsername;

	private String middleName;

	private String phoneCountryCode;

	private BigInteger phoneNo;

	private String salutation;

	private String state;

	private String budgetManagement;

	private String goalPlanning;

	private String portfolioManagement;

	private String financialPlanning;

	private String bseUsername;
	
	private String branchName;
	
	private String rmName;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public BigInteger getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(BigInteger phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBudgetManagement() {
		return budgetManagement;
	}

	public void setBudgetManagement(String budgetManagement) {
		this.budgetManagement = budgetManagement;
	}

	public String getGoalPlanning() {
		return goalPlanning;
	}

	public void setGoalPlanning(String goalPlanning) {
		this.goalPlanning = goalPlanning;
	}

	public String getPortfolioManagement() {
		return portfolioManagement;
	}

	public void setPortfolioManagement(String portfolioManagement) {
		this.portfolioManagement = portfolioManagement;
	}

	public String getFinancialPlanning() {
		return financialPlanning;
	}

	public void setFinancialPlanning(String financialPlanning) {
		this.financialPlanning = financialPlanning;
	}

	public String getBseUsername() {
		return bseUsername;
	}

	public void setBseUsername(String bseUsername) {
		this.bseUsername = bseUsername;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getRmName() {
		return rmName;
	}

	public void setRmName(String rmName) {
		this.rmName = rmName;
	}

	@Override
	public String toString() {
		return "AdvisorUserDTO [id=" + id + ", activeFlag=" + activeFlag + ", birthDate=" + birthDate + ", city=" + city
				+ ", emailID=" + emailID + ", employeeCode=" + employeeCode + ", firstName=" + firstName + ", gender="
				+ gender + ", lastLoginTime=" + lastLoginTime + ", lastLogoutTime=" + lastLogoutTime + ", lastName="
				+ lastName + ", loginPassword=" + loginPassword + ", loginUsername=" + loginUsername + ", middleName="
				+ middleName + ", phoneCountryCode=" + phoneCountryCode + ", phoneNo=" + phoneNo + ", salutation="
				+ salutation + ", state=" + state + ", budgetManagement=" + budgetManagement + ", goalPlanning="
				+ goalPlanning + ", portfolioManagement=" + portfolioManagement + ", financialPlanning="
				+ financialPlanning + ", bseUsername=" + bseUsername + ", branchName=" + branchName + ", rmName="
				+ rmName + "]";
	}
	
	

}
