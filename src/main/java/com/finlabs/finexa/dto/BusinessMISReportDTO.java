package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class BusinessMISReportDTO {
	
	@Lob
	private byte[] logo;
	private int distributorId;
	private String distributorName;
	private String distributorEmail;
	private String distributorMobile;
	private String distributorAddress;
	private String distributorContactDetails;
	private Date fromDate;
	private Date toDate;
	private String arnNo;
	private int relationshipManagerId;
	private String options;
	
	private Map<String, List<BusinessMISReportColumnDTO>> inputMap;

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public int getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
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

	public String getArnNo() {
		return arnNo;
	}

	public void setArnNo(String arnNo) {
		this.arnNo = arnNo;
	}

	public int getRelationshipManagerId() {
		return relationshipManagerId;
	}

	public void setRelationshipManagerId(int relationshipManagerId) {
		this.relationshipManagerId = relationshipManagerId;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public Map<String, List<BusinessMISReportColumnDTO>> getInputMap() {
		return inputMap;
	}

	public void setInputMap(Map<String, List<BusinessMISReportColumnDTO>> inputMap) {
		this.inputMap = inputMap;
	}

	@Override
	public String toString() {
		return "BusinessMISReport [logo=" + Arrays.toString(logo) + ", distributorId=" + distributorId
				+ ", distributorName=" + distributorName + ", distributorEmail=" + distributorEmail
				+ ", distributorMobile=" + distributorMobile + ", distributorAddress=" + distributorAddress
				+ ", distributorContactDetails=" + distributorContactDetails + ", fromDate=" + fromDate + ", toDate="
				+ toDate + ", arnNo=" + arnNo + ", relationshipManagerId=" + relationshipManagerId + ", options="
				+ options + ", inputMap=" + inputMap + "]";
	}
	
}
