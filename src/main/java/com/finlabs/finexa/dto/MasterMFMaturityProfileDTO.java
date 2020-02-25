package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MasterMFMaturityProfileDTO {
	
	private Date date;
	private String schemeName;
	private String amfiCode;
	private String isin;
	private Integer maturityID;
	private BigDecimal allocation;
	
	public MasterMFMaturityProfileDTO() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(String amfiCode) {
		this.amfiCode = amfiCode;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public Integer getMaturityID() {
		return maturityID;
	}

	public void setMaturityID(Integer maturityID) {
		this.maturityID = maturityID;
	}

	public BigDecimal getAllocation() {
		return allocation;
	}

	public void setAllocation(BigDecimal allocation) {
		this.allocation = allocation;
	}
	
	
	

}
