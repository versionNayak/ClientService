package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class SIPCartDTO {
	
	private BigInteger id;
	private String amcName;
	private String fromSchemeName;
	private String schemeType;
	private String regType;
	private BigInteger installmentAmount;
	private int noOfInstallments;
	private String frequency;
	
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
	public String getFromSchemeName() {
		return fromSchemeName;
	}
	public void setFromSchemeName(String fromSchemeName) {
		this.fromSchemeName = fromSchemeName;
	}
	public BigInteger getInstallmentAmount() {
		return installmentAmount;
	}
	public void setInstallmentAmount(BigInteger installmentAmount) {
		this.installmentAmount = installmentAmount;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public int getNoOfInstallments() {
		return noOfInstallments;
	}
	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}
	public String getRegType() {
		return regType;
	}
	public void setRegType(String regType) {
		this.regType = regType;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
}
