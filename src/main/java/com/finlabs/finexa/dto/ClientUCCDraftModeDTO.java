package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
public class ClientUCCDraftModeDTO {
	

	private BigInteger id; 
	private int clientId;
	private int advisorId;
	private String clientCode;
	private String holding;
	private String clientAppli;
	private String dateOfBirth;
	private String gender;
	private String occupation;
	private String taxStatus;
	private String clientsecondAppli;
	private String clientThirdAppli;
	private String guardianName;
	private String nomineeName;
	private String nomineeRelation;
	
	private String firstAppliPan;
	private String secondApplicantPAN;
	private String thirdApplicantPAN;
	private String guardianPAN;
	
	
	private String accountType;
	private String bankAccountNumber;
	private String bankName;
	private String ifsccode;
	private String micrCode;
	private String defaultBankFlag;
	
	
	private String accountType2;
	private String bankAccountNumber2;
	private String bankName2;
	private String ifsccode2;
	private String micrCode2;
	private String defaultBankFlag2;
	
	private String accountType3;
	private String bankAccountNumber3;
	private String bankName3;
	private String ifsccode3;
	private String micrCode3;
	private String defaultBankFlag3;
	
	private String accountType4;
	private String bankAccountNumber4;
	private String bankName4;
	private String ifsccode4;
	private String micrCode4;
	private String defaultBankFlag4;
	
	private String accountType5;
	private String bankAccountNumber5;
	private String bankName5;
	private String ifsccode5;
	private String micrCode5;
	private String defaultBankFlag5;
	
	private String address;
	private String address2;
	private String address3;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String mobile;
	private String emailId;
	private String foreignAddress;
	private String foreignCity;
	private String foreignState;
	private String foreignCountry;
	private String foreignPinCode;
	
	
	private String cKYC;
	private String kYCFirstApplicant;
	private String cKYCFirstApplicant;
	private String kYCTypeSecondApplicant;
	private String cKYCSecondApplicant;
	private String kYCThirdApplicant;
	private String cKYCThirdApplicant;
	private String branch;
	private String status;
	private String dividendPaymentMode;
	private String depositoryDetails;
	private String depositoryName;
	private String dp;
	private String nameBeneficiaryAccNo;
	private String communicationMode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date jointHolderDOB1;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date jointHolderDOB2;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date guardianDOB;
	private String kycTypeGuardian;
	private String ckycGuardian;
	
	
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getAdvisorId() {
		return advisorId;
	}
	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getHolding() {
		return holding;
	}
	public void setHolding(String holding) {
		this.holding = holding;
	}
	public String getClientAppli() {
		return clientAppli;
	}
	public void setClientAppli(String clientAppli) {
		this.clientAppli = clientAppli;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getTaxStatus() {
		return taxStatus;
	}
	public void setTaxStatus(String taxStatus) {
		this.taxStatus = taxStatus;
	}
	public String getClientsecondAppli() {
		return clientsecondAppli;
	}
	public void setClientsecondAppli(String clientsecondAppli) {
		this.clientsecondAppli = clientsecondAppli;
	}
	public String getClientThirdAppli() {
		return clientThirdAppli;
	}
	public void setClientThirdAppli(String clientThirdAppli) {
		this.clientThirdAppli = clientThirdAppli;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getNomineeRelation() {
		return nomineeRelation;
	}
	public void setNomineeRelation(String nomineeRelation) {
		this.nomineeRelation = nomineeRelation;
	}
	public String getFirstAppliPan() {
		return firstAppliPan;
	}
	public void setFirstAppliPan(String firstAppliPan) {
		this.firstAppliPan = firstAppliPan;
	}
	public String getSecondApplicantPAN() {
		return secondApplicantPAN;
	}
	public void setSecondApplicantPAN(String secondApplicantPAN) {
		this.secondApplicantPAN = secondApplicantPAN;
	}
	public String getThirdApplicantPAN() {
		return thirdApplicantPAN;
	}
	public void setThirdApplicantPAN(String thirdApplicantPAN) {
		this.thirdApplicantPAN = thirdApplicantPAN;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getGuardianPAN() {
		return guardianPAN;
	}
	public void setGuardianPAN(String guardianPAN) {
		this.guardianPAN = guardianPAN;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getIfsccode() {
		return ifsccode;
	}
	public void setIfsccode(String ifsccode) {
		this.ifsccode = ifsccode;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public String getDefaultBankFlag() {
		return defaultBankFlag;
	}
	public void setDefaultBankFlag(String defaultBankFlag) {
		this.defaultBankFlag = defaultBankFlag;
	}
	public String getAccountType2() {
		return accountType2;
	}
	public void setAccountType2(String accountType2) {
		this.accountType2 = accountType2;
	}
	public String getBankAccountNumber2() {
		return bankAccountNumber2;
	}
	public void setBankAccountNumber2(String bankAccountNumber2) {
		this.bankAccountNumber2 = bankAccountNumber2;
	}
	public String getBankName2() {
		return bankName2;
	}
	public void setBankName2(String bankName2) {
		this.bankName2 = bankName2;
	}
	public String getIfsccode2() {
		return ifsccode2;
	}
	public void setIfsccode2(String ifsccode2) {
		this.ifsccode2 = ifsccode2;
	}
	public String getMicrCode2() {
		return micrCode2;
	}
	public void setMicrCode2(String micrCode2) {
		this.micrCode2 = micrCode2;
	}
	public String getDefaultBankFlag2() {
		return defaultBankFlag2;
	}
	public void setDefaultBankFlag2(String defaultBankFlag2) {
		this.defaultBankFlag2 = defaultBankFlag2;
	}
	public String getAccountType3() {
		return accountType3;
	}
	public void setAccountType3(String accountType3) {
		this.accountType3 = accountType3;
	}
	public String getBankAccountNumber3() {
		return bankAccountNumber3;
	}
	public void setBankAccountNumber3(String bankAccountNumber3) {
		this.bankAccountNumber3 = bankAccountNumber3;
	}
	public String getBankName3() {
		return bankName3;
	}
	public void setBankName3(String bankName3) {
		this.bankName3 = bankName3;
	}
	public String getIfsccode3() {
		return ifsccode3;
	}
	public void setIfsccode3(String ifsccode3) {
		this.ifsccode3 = ifsccode3;
	}
	public String getMicrCode3() {
		return micrCode3;
	}
	public void setMicrCode3(String micrCode3) {
		this.micrCode3 = micrCode3;
	}
	public String getDefaultBankFlag3() {
		return defaultBankFlag3;
	}
	public void setDefaultBankFlag3(String defaultBankFlag3) {
		this.defaultBankFlag3 = defaultBankFlag3;
	}
	public String getAccountType4() {
		return accountType4;
	}
	public void setAccountType4(String accountType4) {
		this.accountType4 = accountType4;
	}
	public String getBankAccountNumber4() {
		return bankAccountNumber4;
	}
	public void setBankAccountNumber4(String bankAccountNumber4) {
		this.bankAccountNumber4 = bankAccountNumber4;
	}
	public String getBankName4() {
		return bankName4;
	}
	public void setBankName4(String bankName4) {
		this.bankName4 = bankName4;
	}
	public String getIfsccode4() {
		return ifsccode4;
	}
	public void setIfsccode4(String ifsccode4) {
		this.ifsccode4 = ifsccode4;
	}
	public String getMicrCode4() {
		return micrCode4;
	}
	public void setMicrCode4(String micrCode4) {
		this.micrCode4 = micrCode4;
	}
	public String getDefaultBankFlag4() {
		return defaultBankFlag4;
	}
	public void setDefaultBankFlag4(String defaultBankFlag4) {
		this.defaultBankFlag4 = defaultBankFlag4;
	}
	public String getAccountType5() {
		return accountType5;
	}
	public void setAccountType5(String accountType5) {
		this.accountType5 = accountType5;
	}
	public String getBankAccountNumber5() {
		return bankAccountNumber5;
	}
	public void setBankAccountNumber5(String bankAccountNumber5) {
		this.bankAccountNumber5 = bankAccountNumber5;
	}
	public String getBankName5() {
		return bankName5;
	}
	public void setBankName5(String bankName5) {
		this.bankName5 = bankName5;
	}
	public String getIfsccode5() {
		return ifsccode5;
	}
	public void setIfsccode5(String ifsccode5) {
		this.ifsccode5 = ifsccode5;
	}
	public String getMicrCode5() {
		return micrCode5;
	}
	public void setMicrCode5(String micrCode5) {
		this.micrCode5 = micrCode5;
	}
	public String getDefaultBankFlag5() {
		return defaultBankFlag5;
	}
	public void setDefaultBankFlag5(String defaultBankFlag5) {
		this.defaultBankFlag5 = defaultBankFlag5;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getForeignAddress() {
		return foreignAddress;
	}
	public void setForeignAddress(String foreignAddress) {
		this.foreignAddress = foreignAddress;
	}
	public String getForeignCity() {
		return foreignCity;
	}
	public void setForeignCity(String foreignCity) {
		this.foreignCity = foreignCity;
	}
	public String getForeignState() {
		return foreignState;
	}
	public void setForeignState(String foreignState) {
		this.foreignState = foreignState;
	}
	public String getForeignCountry() {
		return foreignCountry;
	}
	public void setForeignCountry(String foreignCountry) {
		this.foreignCountry = foreignCountry;
	}
	public String getForeignPinCode() {
		return foreignPinCode;
	}
	public void setForeignPinCode(String foreignPinCode) {
		this.foreignPinCode = foreignPinCode;
	}
	public String getcKYC() {
		return cKYC;
	}
	public void setcKYC(String cKYC) {
		this.cKYC = cKYC;
	}
	public String getkYCFirstApplicant() {
		return kYCFirstApplicant;
	}
	public void setkYCFirstApplicant(String kYCFirstApplicant) {
		this.kYCFirstApplicant = kYCFirstApplicant;
	}
	public String getcKYCFirstApplicant() {
		return cKYCFirstApplicant;
	}
	public void setcKYCFirstApplicant(String cKYCFirstApplicant) {
		this.cKYCFirstApplicant = cKYCFirstApplicant;
	}
	public String getkYCTypeSecondApplicant() {
		return kYCTypeSecondApplicant;
	}
	public void setkYCTypeSecondApplicant(String kYCTypeSecondApplicant) {
		this.kYCTypeSecondApplicant = kYCTypeSecondApplicant;
	}
	public String getcKYCSecondApplicant() {
		return cKYCSecondApplicant;
	}
	public void setcKYCSecondApplicant(String cKYCSecondApplicant) {
		this.cKYCSecondApplicant = cKYCSecondApplicant;
	}
	public String getkYCThirdApplicant() {
		return kYCThirdApplicant;
	}
	public void setkYCThirdApplicant(String kYCThirdApplicant) {
		this.kYCThirdApplicant = kYCThirdApplicant;
	}
	public String getcKYCThirdApplicant() {
		return cKYCThirdApplicant;
	}
	public void setcKYCThirdApplicant(String cKYCThirdApplicant) {
		this.cKYCThirdApplicant = cKYCThirdApplicant;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDividendPaymentMode() {
		return dividendPaymentMode;
	}
	public void setDividendPaymentMode(String dividendPaymentMode) {
		this.dividendPaymentMode = dividendPaymentMode;
	}
	public String getDepositoryDetails() {
		return depositoryDetails;
	}
	public void setDepositoryDetails(String depositoryDetails) {
		this.depositoryDetails = depositoryDetails;
	}
	public String getDepositoryName() {
		return depositoryName;
	}
	public void setDepositoryName(String depositoryName) {
		this.depositoryName = depositoryName;
	}
	public String getDp() {
		return dp;
	}
	public void setDp(String dp) {
		this.dp = dp;
	}
	public String getNameBeneficiaryAccNo() {
		return nameBeneficiaryAccNo;
	}
	public void setNameBeneficiaryAccNo(String nameBeneficiaryAccNo) {
		this.nameBeneficiaryAccNo = nameBeneficiaryAccNo;
	}
	public String getCommunicationMode() {
		return communicationMode;
	}
	public void setCommunicationMode(String communicationMode) {
		this.communicationMode = communicationMode;
	}
	public Date getJointHolderDOB1() {
		return jointHolderDOB1;
	}
	public void setJointHolderDOB1(Date jointHolderDOB1) {
		this.jointHolderDOB1 = jointHolderDOB1;
	}
	
	public Date getGuardianDOB() {
		return guardianDOB;
	}
	public void setGuardianDOB(Date guardianDOB) {
		this.guardianDOB = guardianDOB;
	}
	public String getKycTypeGuardian() {
		return kycTypeGuardian;
	}
	public void setKycTypeGuardian(String kycTypeGuardian) {
		this.kycTypeGuardian = kycTypeGuardian;
	}
	public String getCkycGuardian() {
		return ckycGuardian;
	}
	public void setCkycGuardian(String ckycGuardian) {
		this.ckycGuardian = ckycGuardian;
	}
	public Date getJointHolderDOB2() {
		return jointHolderDOB2;
	}
	public void setJointHolderDOB2(Date jointHolderDOB2) {
		this.jointHolderDOB2 = jointHolderDOB2;
	}

}
