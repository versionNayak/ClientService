package com.finlabs.finexa.dto;



public class UploadHistoryDTO {
	
	private int advisorId;
	private String RTAType;
	private String RTAFileType;
	private String fileName;
	private String status;
	private int rejectedRecords;
	private String Date;
	private long uploadTimeInSec;
	private String autoClientCreationStatus;
	private String reasonOfRejection;

	
	public String getAutoClientCreationStatus() {
		return autoClientCreationStatus;
	}
	public void setAutoClientCreationStatus(String autoClientCreationStatus) {
		this.autoClientCreationStatus = autoClientCreationStatus;
	}
	public String getReasonOfRejection() {
		return reasonOfRejection;
	}
	public void setReasonOfRejection(String reasonOfRejection) {
		this.reasonOfRejection = reasonOfRejection;
	}
	public int getAdvisorId() {
		return advisorId;
	}
	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}
	public String getRTAFileType() {
		return RTAFileType;
	}
	public void setRTAFileType(String rTAFileType) {
		RTAFileType = rTAFileType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRejectedRecords() {
		return rejectedRecords;
	}
	public void setRejectedRecords(int rejectedRecords) {
		this.rejectedRecords = rejectedRecords;
	}
	public String getRTAType() {
		return RTAType;
	}
	public void setRTAType(String rTAType) {
		RTAType = rTAType;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public long getUploadTimeInSec() {
		return uploadTimeInSec;
	}
	public void setUploadTimeInSec(long uploadTimeInSec) {
		this.uploadTimeInSec = uploadTimeInSec;
	}
	
	
}
