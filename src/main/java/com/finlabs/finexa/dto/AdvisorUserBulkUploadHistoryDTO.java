package com.finlabs.finexa.dto;

import java.util.Date;

public class AdvisorUserBulkUploadHistoryDTO {
	
	private int id;
	
	private Date endTime;

	private String fileName;

	private String reasonOfRejection;

	private int rejectedRecords;

	private Date startTime;

	private String status;

	private int advisorUserID;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getReasonOfRejection() {
		return this.reasonOfRejection;
	}

	public void setReasonOfRejection(String reasonOfRejection) {
		this.reasonOfRejection = reasonOfRejection;
	}

	public int getRejectedRecords() {
		return this.rejectedRecords;
	}

	public void setRejectedRecords(int rejectedRecords) {
		this.rejectedRecords = rejectedRecords;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAdvisorUserID() {
		return this.advisorUserID;
	}

	public void setAdvisorUserID(int advisorUser) {
		this.advisorUserID = advisorUser;
	}

}
