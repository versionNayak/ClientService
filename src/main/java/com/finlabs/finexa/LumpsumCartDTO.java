package com.finlabs.finexa;

import java.math.BigInteger;

public class LumpsumCartDTO {
	
	private BigInteger id;
	private String amcName;
	private String schemeName;
	private String schemeType;
	private BigInteger amountInvested;
	private BigInteger units;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getAmcName() {
		return amcName;
	}
	public void setAmcName(String amcName) {
		this.amcName = amcName;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public BigInteger getAmountInvested() {
		return amountInvested;
	}
	public void setAmountInvested(BigInteger amountInvested) {
		this.amountInvested = amountInvested;
	}
	public BigInteger getUnits() {
		return units;
	}
	public void setUnits(BigInteger units) {
		this.units = units;
	}
}
