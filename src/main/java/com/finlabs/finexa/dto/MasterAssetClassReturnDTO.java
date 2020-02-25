package com.finlabs.finexa.dto;

import java.util.Date;

public class MasterAssetClassReturnDTO {
	
	private byte id;
	private byte assetClass;
    private double bcr;
    private Date fromDate;
    private double mlr;
    private double riskStdDev;
    private Date toDate;
    private double wcr;
	
    public MasterAssetClassReturnDTO() {
		super();
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public double getBcr() {
		return bcr;
	}

	public void setBcr(double bcr) {
		this.bcr = bcr;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public double getMlr() {
		return mlr;
	}

	public void setMlr(double mlr) {
		this.mlr = mlr;
	}

	public double getRiskStdDev() {
		return riskStdDev;
	}

	public void setRiskStdDev(double riskStdDev) {
		this.riskStdDev = riskStdDev;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public double getWcr() {
		return wcr;
	}

	public void setWcr(double wcr) {
		this.wcr = wcr;
	}

	public byte getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(byte assetClass) {
		this.assetClass = assetClass;
	}
    
    

}
