package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class PortfolioGainLossReportDTO {
	
	@Lob
	private byte[] logo;
	private int clientId;
	private String nameClient;
	private Date asOnDate;
	private String fundHouse;
	private String schemeName;
	private String distributorName;
	private String distributorAddress;
	private String distributorContactDetails;
	private String distributorEmail;
	private String distributorMobile;
	private String familyName;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String clientPAN;
	private String clientEmail;
	private String clientPhNo;
	private String clientMobile;
	private Map<String,List<PortfolioGainLossReportColumnDTO>> schemeMap;
	private Integer[] familyMemberIdArr;
	
	public PortfolioGainLossReportDTO() {
		
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
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

	public Date getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
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

	public Map<String, List<PortfolioGainLossReportColumnDTO>> getSchemeMap() {
		return schemeMap;
	}

	public void setSchemeMap(Map<String, List<PortfolioGainLossReportColumnDTO>> schemeMap) {
		this.schemeMap = schemeMap;
	}

	public Integer[] getFamilyMemberIdArr() {
		return familyMemberIdArr;
	}

	public void setFamilyMemberIdArr(Integer[] familyMemberIdArr) {
		this.familyMemberIdArr = familyMemberIdArr;
	}

	@Override
	public String toString() {
		return "PortfolioGainLossReportNewDTO [logo=" + Arrays.toString(logo) + ", clientId=" + clientId
				+ ", nameClient=" + nameClient + ", asOnDate=" + asOnDate + ", fundHouse=" + fundHouse + ", schemeName="
				+ schemeName + ", distributorName=" + distributorName + ", distributorAddress=" + distributorAddress
				+ ", distributorContactDetails=" + distributorContactDetails + ", distributorEmail=" + distributorEmail
				+ ", distributorMobile=" + distributorMobile + ", familyName=" + familyName + ", addressLine1="
				+ addressLine1 + ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3 + ", clientPAN="
				+ clientPAN + ", clientEmail=" + clientEmail + ", clientPhNo=" + clientPhNo + ", clientMobile="
				+ clientMobile + ", schemeMap=" + schemeMap + ", familyMemberIdArr="
				+ Arrays.toString(familyMemberIdArr) + "]";
	}
	
	

}
