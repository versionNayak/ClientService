package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AdvisorDTO {

	private int id;
	private int advisorID;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@Past(message = "Date of Birth should be past date")
	private Date birthDate;
	private String city;
	@Email(message = "Email is not valid")
	private String emailID;
	private String employeeCode;
	@NotNull(message = "First name can not be null")
	private String firstName;
	private String gender;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date lastLoginTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date lastLogoutTime;
	@NotNull(message = "Last name can not be null")
	private String lastName;
	@NotNull(message = "Login Username can not be null")
	private String loginUsername;
	@NotNull(message = "Login Password can not be null")
	private String loginPassword;
	private String middleName;
	@NotNull(message = "Phone number can not be null")
	private BigInteger phoneNo;
	private String salutation;
	private String state;
	private String distributorCode;
	private String orgFlag;
	private String orgName;
	private int lookupCountryId;
	private int roleId;
	private String role;
	private int advisorMasterId;
	private String advisorUserName;
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
	private String bseUsername;
	private String bseAccessMode;
	private String activeFlag;
	private String userName;
	private int userId;
	private UserDTO userDTO;
	private String orgAddressLine1;
	private String orgAddressLine2;
	private String orgAddressLine3;
	private String orgContactDetails;
	private String camsPassword;
	private String karvyPassword;
	private String franklinPassword;
	private String sundaramPassword;
	
	public String getBseUsername() {
		return bseUsername;
	}
	public void setBseUsername(String bseUsername) {
		this.bseUsername = bseUsername;
	}

	public String getBseMemberId() {
		return bseMemberId;
	}

	public void setBseMemberId(String bseMemberId) {
		this.bseMemberId = bseMemberId;
	}

	public String getBsePassword() {
		return bsePassword;
	}

	public void setBsePassword(String bsePassword) {
		this.bsePassword = bsePassword;
	}

	private String bseMemberId;
	private String bsePassword;

	public AdvisorDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

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

	public int getLookupCountryId() {
		return lookupCountryId;
	}

	public void setLookupCountryId(int lookupCountryId) {
		this.lookupCountryId = lookupCountryId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getAdvisorMasterId() {
		return advisorMasterId;
	}

	public void setAdvisorMasterId(int advisorMasterId) {
		this.advisorMasterId = advisorMasterId;
	}

	public String getAdvisorUserName() {
		return advisorUserName;
	}

	public void setAdvisorUserName(String advisorUserName) {
		this.advisorUserName = advisorUserName;
	}

	public String getBseAccessMode1() {
		return bseAccessMode;
	}

	public void setBseAccessMode1(String bseAccessMode) {
		this.bseAccessMode = bseAccessMode;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public int getAdvisorID() {
		return advisorID;
	}

	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getBseAccessMode() {
		return bseAccessMode;
	}

	public void setBseAccessMode(String bseAccessMode) {
		this.bseAccessMode = bseAccessMode;
	}

	public String getOrgAddressLine1() {
		return orgAddressLine1;
	}

	public void setOrgAddressLine1(String orgAddressLine1) {
		this.orgAddressLine1 = orgAddressLine1;
	}

	public String getOrgAddressLine2() {
		return orgAddressLine2;
	}

	public void setOrgAddressLine2(String orgAddressLine2) {
		this.orgAddressLine2 = orgAddressLine2;
	}

	public String getOrgAddressLine3() {
		return orgAddressLine3;
	}

	public void setOrgAddressLine3(String orgAddressLine3) {
		this.orgAddressLine3 = orgAddressLine3;
	}

	public String getOrgContactDetails() {
		return orgContactDetails;
	}

	public void setOrgContactDetails(String orgContactDetails) {
		this.orgContactDetails = orgContactDetails;
	}
	
	public String getCamsPassword() {
		return camsPassword;
	}
	public void setCamsPassword(String camsPassword) {
		this.camsPassword = camsPassword;
	}
	public String getKarvyPassword() {
		return karvyPassword;
	}
	public void setKarvyPassword(String karvyPassword) {
		this.karvyPassword = karvyPassword;
	}
	public String getFranklinPassword() {
		return franklinPassword;
	}
	public void setFranklinPassword(String franklinPassword) {
		this.franklinPassword = franklinPassword;
	}
	public String getSundaramPassword() {
		return sundaramPassword;
	}
	public void setSundaramPassword(String sundaramPassword) {
		this.sundaramPassword = sundaramPassword;
	}
	@Override
	public String toString() {
		return "AdvisorDTO [id=" + id + ", advisorID=" + advisorID + ", birthDate=" + birthDate + ", city=" + city
				+ ", emailID=" + emailID + ", employeeCode=" + employeeCode + ", firstName=" + firstName + ", gender="
				+ gender + ", lastLoginTime=" + lastLoginTime + ", lastLogoutTime=" + lastLogoutTime + ", lastName="
				+ lastName + ", loginUsername=" + loginUsername + ", loginPassword=" + loginPassword + ", middleName="
				+ middleName + ", phoneNo=" + phoneNo + ", salutation=" + salutation + ", state=" + state
				+ ", distributorCode=" + distributorCode + ", orgFlag=" + orgFlag + ", orgName=" + orgName
				+ ", lookupCountryId=" + lookupCountryId + ", roleId=" + roleId + ", role=" + role
				+ ", advisorMasterId=" + advisorMasterId + ", advisorUserName=" + advisorUserName + ", admin=" + admin
				+ ", advisorAdmin=" + advisorAdmin + ", client=" + client + ", clientInfoView=" + clientInfoView
				+ ", clientInfoAddEdit=" + clientInfoAddEdit + ", clientInfoDelete=" + clientInfoDelete
				+ ", budgetManagementView=" + budgetManagementView + ", portfolioManagementView="
				+ portfolioManagementView + ", portfolioManagementAddEdit=" + portfolioManagementAddEdit
				+ ", goalPlanningView=" + goalPlanningView + ", goalPlanningAddEdit=" + goalPlanningAddEdit
				+ ", financialPlanningView=" + financialPlanningView + ", financialPlanningAddEdit="
				+ financialPlanningAddEdit + ", investView=" + investView + ", investAddEdit=" + investAddEdit
				+ ", userManagementView=" + userManagementView + ", userManagementAddEdit=" + userManagementAddEdit
				+ ", userManagementDelete=" + userManagementDelete + ", clientRecordsView=" + clientRecordsView
				+ ", mastersView=" + mastersView + ", mastersAddEdit=" + mastersAddEdit + ", mastersDelete="
				+ mastersDelete + ", mfBackOfficeView=" + mfBackOfficeView + ", mfBackOfficeAddEdit="
				+ mfBackOfficeAddEdit + ", bseUsername=" + bseUsername + ", bseAccessMode=" + bseAccessMode
				+ ", activeFlag=" + activeFlag + ", userName=" + userName + ", userId=" + userId + ", userDTO="
				+ userDTO + ", orgAddressLine1=" + orgAddressLine1 + ", orgAddressLine2=" + orgAddressLine2
				+ ", orgAddressLine3=" + orgAddressLine3 + ", orgContactDetails=" + orgContactDetails
				+ ", camsPassword=" + camsPassword + ", karvyPassword=" + karvyPassword + ", franklinPassword="
				+ franklinPassword + ", sundaramPassword=" + sundaramPassword + ", bseMemberId=" + bseMemberId
				+ ", bsePassword=" + bsePassword + "]";
	}
	
	
	
}
