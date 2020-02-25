package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class SIPSTPSWPReportDTO {
	
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
	private String clientPAN;
	private String clientEmail;
	private String clientMobile;
	private String familyName;
	private String reportType;
	private Integer[] familyMemberIdArr;
	
	private Map<String, List<SIPSTPSWPReportColumnDTO>> sipStpSwpDataMap;
	
	private boolean status;
	
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
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
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
	public String getClientMobile() {
		return clientMobile;
	}
	public void setClientMobile(String clientMobile) {
		this.clientMobile = clientMobile;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Map<String, List<SIPSTPSWPReportColumnDTO>> getSipStpSwpDataMap() {
		return sipStpSwpDataMap;
	}
	public void setSipStpSwpDataMap(Map<String, List<SIPSTPSWPReportColumnDTO>> sipStpSwpDataMap) {
		this.sipStpSwpDataMap = sipStpSwpDataMap;
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
		return "SIPSTPSWPReportDTO [logo=" + Arrays.toString(logo) + ", clientId=" + clientId + ", nameClient="
				+ nameClient + ", fromDate=" + fromDate + ", toDate=" + toDate + ", fundHouse=" + fundHouse
				+ ", schemeName=" + schemeName + ", distributorName=" + distributorName + ", distributorAddress="
				+ distributorAddress + ", distributorContactDetails=" + distributorContactDetails
				+ ", distributorEmail=" + distributorEmail + ", distributorMobile=" + distributorMobile + ", clientPAN="
				+ clientPAN + ", clientEmail=" + clientEmail + ", clientMobile=" + clientMobile + ", familyName="
				+ familyName + ", reportType=" + reportType + ", familyMemberIdArr="
				+ Arrays.toString(familyMemberIdArr) + ", sipStpSwpDataMap=" + sipStpSwpDataMap + ", status=" + status
				+ "]";
	}
	
	
	
	
}
