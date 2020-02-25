package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;




public class MasterSubAssetClassReturnDTO {
	
	private byte id;
    private BigDecimal bcr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fromDate;
    private BigDecimal mlr;
    private BigDecimal riskStdDev;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date toDate;
    private BigDecimal wcr;
	private byte lookupAssetSubClassId;

	public MasterSubAssetClassReturnDTO() {
		super();
	}



	public byte getId() {
		return id;
	}
    public void setId(byte id) {
		this.id = id;
	}


    public BigDecimal getBcr() {
		return bcr;
	}
    public void setBcr(BigDecimal bcr) {
		this.bcr = bcr;
	}



	public Date getFromDate() {
		return fromDate;
	}
    public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}



	public BigDecimal getMlr() {
		return mlr;
	}
    public void setMlr(BigDecimal mlr) {
		this.mlr = mlr;
	}


    public BigDecimal getRiskStdDev() {
		return riskStdDev;
	}
    public void setRiskStdDev(BigDecimal riskStdDev) {
		this.riskStdDev = riskStdDev;
	}


    public Date getToDate() {
		return toDate;
	}
    public void setToDate(Date toDate) {
		this.toDate = toDate;
	}



	public BigDecimal getWcr() {
		return wcr;
	}
    public void setWcr(BigDecimal wcr) {
		this.wcr = wcr;
	}



	public byte getLookupAssetSubClassId() {
		return lookupAssetSubClassId;
	}
    public void setLookupAssetSubClassId(byte lookupAssetSubClassId) {
		this.lookupAssetSubClassId = lookupAssetSubClassId;
	}



	@Override
	public String toString() {
		return "MasterSubAssetClassReturnDTO [id=" + id + ", bcr=" + bcr + ", fromDate=" + fromDate + ", mlr=" + mlr
				+ ", riskStdDev=" + riskStdDev + ", toDate=" + toDate + ", wcr=" + wcr + "]";
	}



	
	

}