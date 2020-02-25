package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class BrokerageReportDTO {
	
	@Lob
	private byte[] logo;
	private int clientId;
	private String nameClient;
	private String clientPAN;
	private String clientEmail;
	private String clientMobile;
	private Date fromDate;
	private Date toDate;
	private String distributorName;
	private String distributorAddress;
	private String distributorContactDetails;
	private String distributorEmail;
	private String distributorMobile;
	private String schemeName;
	private int advisorId;
	private Map<String,List<BrokerageReportColumnDTO>> inputMap;
	private Integer[] familyMemberIdArr;
	
	public int getAdvisorId() {
		return advisorId;
	}
	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
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
	public Map<String, List<BrokerageReportColumnDTO>> getInputMap() {
		return inputMap;
	}
	public void setInputMap(Map<String, List<BrokerageReportColumnDTO>> inputMap) {
		this.inputMap = inputMap;
	}
	public String getNameClient() {
		return nameClient;
	}
	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}
	public String getClientPAN() {
		return clientPAN;
	}
	public void setClientPAN(String clientPAN) {
		this.clientPAN = clientPAN;
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
	
	public Integer[] getFamilyMemberIdArr() {
		return familyMemberIdArr;
	}
	public void setFamilyMemberIdArr(Integer[] familyMemberIdArr) {
		this.familyMemberIdArr = familyMemberIdArr;
	}
	
	@Override
	public String toString() {
		return "BrokerageReportDTO [logo=" + Arrays.toString(logo) + ", clientId=" + clientId + ", nameClient="
				+ nameClient + ", clientPAN=" + clientPAN + ", clientEmail=" + clientEmail + ", clientMobile="
				+ clientMobile + ", fromDate=" + fromDate + ", toDate=" + toDate + ", distributorName="
				+ distributorName + ", distributorAddress=" + distributorAddress + ", distributorContactDetails="
				+ distributorContactDetails + ", distributorEmail=" + distributorEmail + ", distributorMobile="
				+ distributorMobile + ", schemeName=" + schemeName + ", advisorId=" + advisorId + ", inputMap="
				+ inputMap + ", familyMemberIdArr=" + Arrays.toString(familyMemberIdArr) + "]";
	}
	
}
