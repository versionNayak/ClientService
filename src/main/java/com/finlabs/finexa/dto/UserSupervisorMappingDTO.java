package com.finlabs.finexa.dto;

import java.util.Date;



import com.fasterxml.jackson.annotation.JsonFormat;

public class UserSupervisorMappingDTO {
	private int id;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date effectiveFromDate;
	private int userId;
	private int superVisorId;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSuperVisorId() {
		return superVisorId;
	}
	public void setSuperVisorId(int superVisorId) {
		this.superVisorId = superVisorId;
	}
	
	
}
