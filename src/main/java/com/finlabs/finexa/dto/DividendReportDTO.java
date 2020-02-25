package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class DividendReportDTO {
	
	@Lob
	private byte[] logo;
	
	private int clientId;
	private String nameClient;
	private Date fromDate;
	private Date toDate;
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
	private Integer[] familyMemberIdArr;
	
	private Map<String, List<DividendReportColumnDTO>> folioSchemeMap;
	private Map<String, List<DividendReportColumnNewDTO>> folioSchemeMapNew;

	public DividendReportDTO() {
	
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

	public Map<String, List<DividendReportColumnDTO>> getFolioSchemeMap() {
		return folioSchemeMap;
	}

	public void setFolioSchemeMap(Map<String, List<DividendReportColumnDTO>> folioSchemeMap) {
		this.folioSchemeMap = folioSchemeMap;
	}
	
	public Map<String, List<DividendReportColumnNewDTO>> getFolioSchemeMapNew() {
		return folioSchemeMapNew;
	}

	public void setFolioSchemeMapNew(Map<String, List<DividendReportColumnNewDTO>> folioSchemeMapNew) {
		this.folioSchemeMapNew = folioSchemeMapNew;
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
		return "DividendReportDTO [logo=" + Arrays.toString(logo) + ", clientId=" + clientId + ", nameClient="
				+ nameClient + ", fromDate=" + fromDate + ", toDate=" + toDate + ", fundHouse=" + fundHouse
				+ ", schemeName=" + schemeName + ", distributorName=" + distributorName + ", distributorAddress="
				+ distributorAddress + ", distributorContactDetails=" + distributorContactDetails
				+ ", distributorEmail=" + distributorEmail + ", distributorMobile=" + distributorMobile
				+ ", familyName=" + familyName + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
				+ ", addressLine3=" + addressLine3 + ", clientPAN=" + clientPAN + ", clientEmail=" + clientEmail
				+ ", clientPhNo=" + clientPhNo + ", clientMobile=" + clientMobile + ", familyMemberIdArr="
				+ Arrays.toString(familyMemberIdArr) + ", folioSchemeMap=" + folioSchemeMap + ", folioSchemeMapNew="
				+ folioSchemeMapNew + "]";
	}

	

}
