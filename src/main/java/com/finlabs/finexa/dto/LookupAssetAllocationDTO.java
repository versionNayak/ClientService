package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;


public class LookupAssetAllocationDTO {

	private int id;
	private int assetAllocationCategory;
	private byte assetSubClass;
	private BigDecimal weightage;
	private Date fromDate;
	private Date toDate;
	
	public LookupAssetAllocationDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAssetAllocationCategory() {
		return assetAllocationCategory;
	}

	public void setAssetAllocationCategory(int assetAllocationCategory) {
		this.assetAllocationCategory = assetAllocationCategory;
	}

	public byte getAssetSubClass() {
		return assetSubClass;
	}

	public void setAssetSubClass(byte assetSubClass) {
		this.assetSubClass = assetSubClass;
	}

	public BigDecimal getWeightage() {
		return weightage;
	}

	public void setWeightage(BigDecimal weightage) {
		this.weightage = weightage;
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
	
	
	
	
	
}
