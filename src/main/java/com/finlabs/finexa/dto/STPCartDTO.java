package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class STPCartDTO {
	
	private BigInteger id;
	private String amcName;
	private String fromSchemeName;
	private String fromSchemeType;
	private String toSchemeName;
	private String toSchemeType;
	private BigInteger transferAmount;
	private int noOfInstallment;
	private String frequencyType;
	
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
	public BigInteger getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(BigInteger transferAmount) {
		this.transferAmount = transferAmount;
	}
	public int getNoOfInstallment() {
		return noOfInstallment;
	}
	public void setNoOfInstallment(int noOfInstallment) {
		this.noOfInstallment = noOfInstallment;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
}
