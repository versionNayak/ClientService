package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientLumpsumDTO {

	private int id;
	
	private String ownerName;

	private Byte createdBy;

	private Timestamp createdOn;

	private Byte lastUpdateddBy;

	private Timestamp lastUpdatedOn;

	@NotNull(message = "clientId must not be null")
	private int clientId;
	
	@NotNull(message = "expectedInflow must not be null")
	@DecimalMin(value="0.01", message="expectedInflow cannot be 0")
	private BigDecimal expectedInflow;
	@NotNull(message = "expectedInflowDate must not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="IST")
	private Date expectedInflowDate;
	@NotNull(message = "inflowDesc must not be null")
	private String inflowDesc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Byte getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Byte createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Byte getLastUpdateddBy() {
		return lastUpdateddBy;
	}

	public void setLastUpdateddBy(Byte lastUpdateddBy) {
		this.lastUpdateddBy = lastUpdateddBy;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public BigDecimal getExpectedInflow() {
		return expectedInflow;
	}

	public void setExpectedInflow(BigDecimal expectedInflow) {
		this.expectedInflow = expectedInflow;
	}

	public Date getExpectedInflowDate() {
		return expectedInflowDate;
	}

	public void setExpectedInflowDate(Date expectedInflowDate) {
		this.expectedInflowDate = expectedInflowDate;
	}

	public String getInflowDesc() {
		return inflowDesc;
	}

	public void setInflowDesc(String inflowDesc) {
		this.inflowDesc = inflowDesc;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Override
	public String toString() {
		return "ClientLumpsumDTO [id=" + id + ", ownerName=" + ownerName + ", createdBy=" + createdBy + ", createdOn="
				+ createdOn + ", lastUpdateddBy=" + lastUpdateddBy + ", lastUpdatedOn=" + lastUpdatedOn + ", clientId="
				+ clientId + ", expectedInflow=" + expectedInflow + ", expectedInflowDate=" + expectedInflowDate
				+ ", inflowDesc=" + inflowDesc + "]";
	}

}
