package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class AumReportDTO {
	
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
	private String clientPAN;
	private String clientEmail;
	private String clientPhNo;
	private String clientMobile;
	private String familyName;
	private Map<String,List<AumReportColumnDTO>> inputMap;
	private boolean status;
	private Integer[] familyMemberIdArr;
	
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getNameClient() {
		return nameClient;
	}
	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}
	public Map<String, List<AumReportColumnDTO>> getInputMap() {
		return inputMap;
	}
	public void setInputMap(Map<String, List<AumReportColumnDTO>> inputMap) {
		this.inputMap = inputMap;
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
		return "AumReportDTO [logo=" + Arrays.toString(logo) + ", clientId=" + clientId + ", nameClient=" + nameClient
				+ ", asOnDate=" + asOnDate + ", fundHouse=" + fundHouse + ", schemeName=" + schemeName
				+ ", distributorName=" + distributorName + ", distributorAddress=" + distributorAddress
				+ ", distributorContactDetails=" + distributorContactDetails + ", distributorEmail=" + distributorEmail
				+ ", distributorMobile=" + distributorMobile + ", clientPAN=" + clientPAN + ", clientEmail="
				+ clientEmail + ", clientPhNo=" + clientPhNo + ", clientMobile=" + clientMobile + ", familyName="
				+ familyName + ", inputMap=" + inputMap + ", status=" + status + ", familyMemberIdArr="
				+ Arrays.toString(familyMemberIdArr) + "]";
	}
	
	
	
}
