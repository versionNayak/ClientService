package com.finlabs.finexa.dto;


import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FileuploadDTO {

	private MultipartFile[] file;
	private int loggedUserID;
	private int advisorId;
	private String masterName;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="IST")
    private Date effectiveFromDate;
    private Integer recordsUploaded;
    private String status;
    private String tableName;
	private Integer uploadedBy;
    private Date uploadDate;
    private String uploadedByName;
	private int advisorUserId;
	private String filename;
	
	public MultipartFile[] getFile() {
		return file;
	}
	public int getAdvisorId() {
		return advisorId;
	}
	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}
	public void setFile(MultipartFile[] file) {
		this.file = file;
	}
	public int getAdvisorUserId() {
		return advisorUserId;
	}
	public void setAdvisorUserId(int advisorUserId) {
		this.advisorUserId = advisorUserId;
	}
	public String getMasterName() {
		return masterName;
	}
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}
	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}
	public Integer getRecordsUploaded() {
		return recordsUploaded;
	}
	public void setRecordsUploaded(Integer recordsUploaded) {
		this.recordsUploaded = recordsUploaded;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(Integer uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getUploadedByName() {
		return uploadedByName;
	}
	public void setUploadedByName(String uploadedByName) {
		this.uploadedByName = uploadedByName;
	}
	
	public int getLoggedUserID() {
		return loggedUserID;
	}
	public void setLoggedUserID(int loggedUserID) {
		this.loggedUserID = loggedUserID;
	}
	@Override
	public String toString() {
		return "FileuploadDTO [ advisorUserId=" + advisorUserId + ", masterName="
				+ masterName + ", effectiveFromDate=" + effectiveFromDate + ", recordsUploaded=" + recordsUploaded
				+ ", status=" + status + ", tableName=" + tableName + ", uploadedBy=" + uploadedBy + ", uploadDate="
				+ uploadDate + ", uploadedByName=" + uploadedByName + "]";
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
    

	
	
}
