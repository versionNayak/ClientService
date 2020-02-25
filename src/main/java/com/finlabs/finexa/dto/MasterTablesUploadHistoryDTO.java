package com.finlabs.finexa.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class MasterTablesUploadHistoryDTO {
	
	private int id;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="IST")
    private Date effectiveFromDate;
    private Integer recordsUploaded;
    private String status;
    private String tableName;
	private Integer uploadedBy;
    private Date uploadDate;
    private String uploadedByName;
	
    public MasterTablesUploadHistoryDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
    
    
    
    

}
