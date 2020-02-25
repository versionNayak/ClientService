package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class SWPCartDTO {
	
	private BigInteger id;
	private String amcName;
	private String schemeName;
	private String schemeType;
	private BigInteger withdrawalAmt;
	private BigInteger withdrawalunits;
	private String frequencyType;
	private BigInteger noOfInstallment;
	
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
	public BigInteger getWithdrawalAmt() {
		return withdrawalAmt;
	}
	public void setWithdrawalAmt(BigInteger withdrawalAmt) {
		this.withdrawalAmt = withdrawalAmt;
	}
	public BigInteger getWithdrawalunits() {
		return withdrawalunits;
	}
	public void setWithdrawalunits(BigInteger withdrawalunits) {
		this.withdrawalunits = withdrawalunits;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public BigInteger getNoOfInstallment() {
		return noOfInstallment;
	}
	public void setNoOfInstallment(BigInteger noOfInstallment) {
		this.noOfInstallment = noOfInstallment;
	}
}
