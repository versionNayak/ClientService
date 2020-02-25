package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class SwitchCartDTO {
	
	private BigInteger id;
	private String amcName;
	private String fromSchemeName;
	private String fromSchemeType;
	private String toSchemeName;
	private String toSchemeType;
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
	public String getFromSchemeName() {
		return fromSchemeName;
	}
	public void setFromSchemeName(String fromSchemeName) {
		this.fromSchemeName = fromSchemeName;
	}
	public String getFromSchemeType() {
		return fromSchemeType;
	}
	public void setFromSchemeType(String fromSchemeType) {
		this.fromSchemeType = fromSchemeType;
	}
	public String getToSchemeName() {
		return toSchemeName;
	}
	public void setToSchemeName(String toSchemeName) {
		this.toSchemeName = toSchemeName;
	}
	public String getToSchemeType() {
		return toSchemeType;
	}
	public void setToSchemeType(String toSchemeType) {
		this.toSchemeType = toSchemeType;
	}
}
