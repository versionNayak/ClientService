package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class BusinessTransactionReportDTO {
	
	@Lob
	private byte[] logo;
	
	private int clientId;
	private String nameClient;
	private Date fromDate;
	private Date toDate;
	private String fundHouse;
	private String schemeName;
	private String distributorName;
	private String distributorEmail;
	private String distributorMobile;
	private String distributorAddress;
	private String distributorContactDetails;
	private String familyName;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String clientPAN;
	private String clientEmail;
	private String clientPhNo;
	private String clientMobile;
	private BigDecimal runningTotal;
	private boolean status;
	private Integer[] familyMemberIdArr;
	private String arn;
	private TransactionMISDTO mainDataSource;

	private String folioNo;
	private String transactionDate;
	private String transTypeCode;
	private String nav;
	private String transUnits;
	
	private int advisorID;
	

	public int getAdvisorID() {
		return advisorID;
	}

	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}

	public TransactionMISDTO getMainDataSource() {
		return mainDataSource;
	}

	public void setMainDataSource(TransactionMISDTO mainDataSource) {
		this.mainDataSource = mainDataSource;
	}

	public String getArn() {
		return arn;
	}

	public void setArn(String arn) {
		this.arn = arn;
	}

	
	

	
	public BusinessTransactionReportDTO() {
		
	}
	
	public BusinessTransactionReportDTO(String transactionDate, String transTypeCode, String nav,
			String transUnits) {
		this.transactionDate = transactionDate;
		this.transTypeCode = transTypeCode;
		this.nav = nav;
		this.transUnits = transUnits;
	}
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getNameClient() {
		return nameClient;
	}
	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getFundHouse() {
		return fundHouse;
	}
	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getDistributorAddress() {
		return distributorAddress;
	}
	public void setDistributorAddress(String distributorAddress) {
		this.distributorAddress = distributorAddress;
	}
	public String getDistributorContactDetails() {
		return distributorContactDetails;
	}
	public void setDistributorContactDetails(String distributorContactDetails) {
		this.distributorContactDetails = distributorContactDetails;
	}
	
	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
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

	public String getClientPAN() {
		return clientPAN;
	}
	public void setClientPAN(String clientPAN) {
		this.clientPAN = clientPAN;
	}
	
	public BigDecimal getRunningTotal() {
		return runningTotal;
	}
	public void setRunningTotal(BigDecimal runningTotal) {
		this.runningTotal = runningTotal;
	}
	
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	public String getClientEmail() {
		return clientEmail;
	}
	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}
	public String getClientPhNo() {
		return clientPhNo;
	}
	public void setClientPhNo(String clientPhNo) {
		this.clientPhNo = clientPhNo;
	}
	public String getClientMobile() {
		return clientMobile;
	}
	public void setClientMobile(String clientMobile) {
		this.clientMobile = clientMobile;
	}
	
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransTypeCode() {
		return transTypeCode;
	}
	public void setTransTypeCode(String transTypeCode) {
		this.transTypeCode = transTypeCode;
	}
	public String getNav() {
		return nav;
	}
	public void setNav(String nav) {
		this.nav = nav;
	}
	public String getTransUnits() {
		return transUnits;
	}
	public void setTransUnits(String transUnits) {
		this.transUnits = transUnits;
	}

	
	
	public String getDistributorEmail() {
		return distributorEmail;
	}

	public void setDistributorEmail(String distributorEmail) {
		this.distributorEmail = distributorEmail;
	}

	public String getDistributorMobile() {
		return distributorMobile;
	}

	public void setDistributorMobile(String distributorMobile) {
		this.distributorMobile = distributorMobile;
	}

	public Integer[] getFamilyMemberIdArr() {
		return familyMemberIdArr;
	}

	public void setFamilyMemberIdArr(Integer[] familyMemberIdArr) {
		this.familyMemberIdArr = familyMemberIdArr;
	}

	@Override
	public String toString() {
		return "BusinessTransactionReportDTO [logo=" + Arrays.toString(logo) + ", clientId=" + clientId
				+ ", nameClient=" + nameClient + ", fromDate=" + fromDate + ", toDate=" + toDate + ", fundHouse="
				+ fundHouse + ", schemeName=" + schemeName + ", distributorName=" + distributorName
				+ ", distributorEmail=" + distributorEmail + ", distributorMobile=" + distributorMobile
				+ ", distributorAddress=" + distributorAddress + ", distributorContactDetails="
				+ distributorContactDetails + ", familyName=" + familyName + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3 + ", clientPAN=" + clientPAN
				+ ", clientEmail=" + clientEmail + ", clientPhNo=" + clientPhNo + ", clientMobile=" + clientMobile
				+ ", runningTotal=" + runningTotal + ", status=" + status + ", familyMemberIdArr="
				+ Arrays.toString(familyMemberIdArr) + ", arn=" + arn + ", mainDataSource=" + mainDataSource
				+ ", folioNo=" + folioNo + ", transactionDate=" + transactionDate + ", transTypeCode=" + transTypeCode
				+ ", nav=" + nav + ", transUnits=" + transUnits + ", advisorID=" + advisorID + "]";
	}
}
