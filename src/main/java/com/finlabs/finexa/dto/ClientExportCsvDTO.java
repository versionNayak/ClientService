package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientExportCsvDTO {

	private String userName;
	private String salutation;
	private String firstName;
	private String middleName;
	private String lastName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date birthDate;
	private String gender;
	private String pan;
	private long aadhar;
	private String maritalStatus;
	private String otherMaritalStatus;
	private String educationalQualification;
	private String otherEducationalQualification;
	private String employmentType;
	private String otherEmploymentType;
	private String organisationName;
	private String currentDesignation;
	private String residentType;
	private String otherResidentType;
	private String countryOfResidence;
	private String retiredFlag;
	private Integer retirementAge;

	private String emailAddress;
	private String alternateEmailAddress;
	private BigInteger mobileNumber;
	private BigInteger emergencyContact;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String state;
	private int pinCode;
	private String country;
	private String addressType;
	private double annualIncome;
	private String isTobaccoUser;
	private String isNormalBMI;
	private String historyOfCriticalIllness;
	private String isNormalBP;

	public ClientExportCsvDTO() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public long getAadhar() {
		return aadhar;
	}

	public void setAadhar(long aadhar) {
		this.aadhar = aadhar;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getOtherMaritalStatus() {
		return otherMaritalStatus;
	}

	public void setOtherMaritalStatus(String otherMaritalStatus) {
		this.otherMaritalStatus = otherMaritalStatus;
	}

	public String getEducationalQualification() {
		return educationalQualification;
	}

	public void setEducationalQualification(String educationalQualification) {
		this.educationalQualification = educationalQualification;
	}

	public String getOtherEducationalQualification() {
		return otherEducationalQualification;
	}

	public void setOtherEducationalQualification(String otherEducationalQualification) {
		this.otherEducationalQualification = otherEducationalQualification;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getOtherEmploymentType() {
		return otherEmploymentType;
	}

	public void setOtherEmploymentType(String otherEmploymentType) {
		this.otherEmploymentType = otherEmploymentType;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getCurrentDesignation() {
		return currentDesignation;
	}

	public void setCurrentDesignation(String currentDesignation) {
		this.currentDesignation = currentDesignation;
	}

	public String getResidentType() {
		return residentType;
	}

	public void setResidentType(String residentType) {
		this.residentType = residentType;
	}

	public String getOtherResidentType() {
		return otherResidentType;
	}

	public void setOtherResidentType(String otherResidentType) {
		this.otherResidentType = otherResidentType;
	}

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public String getRetiredFlag() {
		return retiredFlag;
	}

	public void setRetiredFlag(String retiredFlag) {
		this.retiredFlag = retiredFlag;
	}

	public Integer getRetirementAge() {
		return retirementAge;
	}

	public void setRetirementAge(Integer retirementAge) {
		this.retirementAge = retirementAge;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getAlternateEmailAddress() {
		return alternateEmailAddress;
	}

	public void setAlternateEmailAddress(String alternateEmailAddress) {
		this.alternateEmailAddress = alternateEmailAddress;
	}

	public BigInteger getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(BigInteger mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public BigInteger getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(BigInteger emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
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

	public int getPinCode() {
		return pinCode;
	}

	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	@Override
	public String toString() {
		return "ClientExportCsvDTO [userName=" + userName + ", salutation=" + salutation + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName + ", birthDate=" + birthDate + ", gender="
				+ gender + ", pan=" + pan + ", aadhar=" + aadhar + ", maritalStatus=" + maritalStatus
				+ ", otherMaritalStatus=" + otherMaritalStatus + ", educationalQualification="
				+ educationalQualification + ", otherEducationalQualification=" + otherEducationalQualification
				+ ", employmentType=" + employmentType + ", otherEmploymentType=" + otherEmploymentType
				+ ", organisationName=" + organisationName + ", currentDesignation=" + currentDesignation
				+ ", residentType=" + residentType + ", otherResidentType=" + otherResidentType
				+ ", countryOfResidence=" + countryOfResidence + ", retiredFlag=" + retiredFlag + ", retirementAge="
				+ retirementAge + ", emailAddress=" + emailAddress + ", alternateEmailAddress=" + alternateEmailAddress
				+ ", mobileNumber=" + mobileNumber + ", emergencyContact=" + emergencyContact + ", addressLine1="
				+ addressLine1 + ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3 + ", city=" + city
				+ ", state=" + state + ", pinCode=" + pinCode + ", country=" + country + ", addressType=" + addressType
				+ "]";
	}

	public double getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(double annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getIsTobaccoUser() {
		return isTobaccoUser;
	}

	public void setIsTobaccoUser(String isTobaccoUser) {
		this.isTobaccoUser = isTobaccoUser;
	}

	public String getIsNormalBMI() {
		return isNormalBMI;
	}

	public void setIsNormalBMI(String isNormalBMI) {
		this.isNormalBMI = isNormalBMI;
	}

	public String getHistoryOfCriticalIllness() {
		return historyOfCriticalIllness;
	}

	public void setHistoryOfCriticalIllness(String historyOfCriticalIllness) {
		this.historyOfCriticalIllness = historyOfCriticalIllness;
	}

	public String getIsNormalBP() {
		return isNormalBP;
	}

	public void setIsNormalBP(String isNormalBP) {
		this.isNormalBP = isNormalBP;
	}

}
