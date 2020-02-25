package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Lob;

public class InactiveClientReportDTO {

	@Lob
	private byte[] logo;
	
	private Date fromDate;
	private Date toDate;
	private String fundHouse;
	private String schemeName;
	private String distributorName;
	private String distributorAddress;
	private String distributorContactDetails;
	private String distributorEmail;
	private String distributorMobile;
	private String reportType;
	private int advisorId;
	
	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	private List<InactiveClientReportColumnDTO> clientNameTransactionList;

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
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

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public List<InactiveClientReportColumnDTO> getClientNameTransactionList() {
		return clientNameTransactionList;
	}

	public void setClientNameTransactionList(List<InactiveClientReportColumnDTO> clientNameTransactionList) {
		this.clientNameTransactionList = clientNameTransactionList;
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

	@Override
	public String toString() {
		return "InactiveClientReportDTO [logo=" + Arrays.toString(logo) + ", fromDate=" + fromDate + ", toDate="
				+ toDate + ", fundHouse=" + fundHouse + ", schemeName=" + schemeName + ", distributorName="
				+ distributorName + ", distributorAddress=" + distributorAddress + ", distributorContactDetails="
				+ distributorContactDetails + ", distributorEmail=" + distributorEmail + ", distributorMobile="
				+ distributorMobile + ", reportType=" + reportType + ", advisorId=" + advisorId
				+ ", clientNameTransactionList=" + clientNameTransactionList + "]";
	}

	
	
	
		
}
