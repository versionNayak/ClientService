package com.finlabs.finexa.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;

public class AumReconciliationReportDTO {
	
	@Lob
	private byte[] logo;
	private int distributorId;
	private String distributorName;
	private String distributorEmail;
	private String distributorMobile;
	private String distributorAddress;
	private String distributorContactDetails;
	private Date asOnDate;
	private String diffGreaterThan;
	private boolean mismatchFolioCheck;
	private boolean missingFolioCheck;
	
	private Map<String, List<AumReconciliationReportColumnDTO>> inputMap;
	
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
	
	public Date getAsOnDate() {
		return asOnDate;
	}
	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}
	
	public String getDiffGreaterThan() {
		return diffGreaterThan;
	}
	public void setDiffGreaterThan(String diffGreaterThan) {
		this.diffGreaterThan = diffGreaterThan;
	}
	
	public boolean isMismatchFolioCheck() {
		return mismatchFolioCheck;
	}
	public void setMismatchFolioCheck(boolean mismatchFolioCheck) {
		this.mismatchFolioCheck = mismatchFolioCheck;
	}
	
	public boolean isMissingFolioCheck() {
		return missingFolioCheck;
	}
	public void setMissingFolioCheck(boolean missingFolioCheck) {
		this.missingFolioCheck = missingFolioCheck;
	}
	
	public Map<String, List<AumReconciliationReportColumnDTO>> getInputMap() {
		return inputMap;
	}
	public void setInputMap(Map<String, List<AumReconciliationReportColumnDTO>> inputMap) {
		this.inputMap = inputMap;
	}
	
	@Override
	public String toString() {
		return "AumReconciliationReportDTO [logo=" + Arrays.toString(logo) + ", distributorId=" + distributorId
				+ ", distributorName=" + distributorName + ", distributorEmail=" + distributorEmail
				+ ", distributorMobile=" + distributorMobile + ", distributorAddress=" + distributorAddress
				+ ", distributorContactDetails=" + distributorContactDetails + ", asOnDate=" + asOnDate
				+ ", diffGreaterThan=" + diffGreaterThan + ", mismatchFolioCheck=" + mismatchFolioCheck
				+ ", missingFolioCheck=" + missingFolioCheck + ", inputMap=" + inputMap + "]";
	}
	
}
