package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Lob;


public class MasterTransactSipLiveDTO {
	
	private int id;

	private String amcCode;

	private String amcName;

	private String schemeCode;

	private String schemeIsin;

	private String schemeName;

	private String schemeType;

	private String sipDates;

	private String sipFrequency;

	private Integer sipInstallmentGap;

	private Integer sipMaximumGap;

	private Integer sipMaximumInstallmentAmount;

	private Integer sipMaximumInstallmentNumbers;

	private Integer sipMinimumGap;

	private Integer sipMinimumInstallmentAmount;

	private Integer sipMinimumInstallmentNumbers;

	private Integer sipMultiplierAmount;

	private Integer sipStatus;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAmcCode() {
		return amcCode;
	}

	public void setAmcCode(String amcCode) {
		this.amcCode = amcCode;
	}

	public String getAmcName() {
		return amcName;
	}

	public void setAmcName(String amcName) {
		this.amcName = amcName;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public String getSchemeIsin() {
		return schemeIsin;
	}

	public void setSchemeIsin(String schemeIsin) {
		this.schemeIsin = schemeIsin;
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

	public String getSipDates() {
		return sipDates;
	}

	public void setSipDates(String sipDates) {
		this.sipDates = sipDates;
	}

	public String getSipFrequency() {
		return sipFrequency;
	}

	public void setSipFrequency(String sipFrequency) {
		this.sipFrequency = sipFrequency;
	}

	public Integer getSipInstallmentGap() {
		return sipInstallmentGap;
	}

	public void setSipInstallmentGap(Integer sipInstallmentGap) {
		this.sipInstallmentGap = sipInstallmentGap;
	}

	public Integer getSipMaximumGap() {
		return sipMaximumGap;
	}

	public void setSipMaximumGap(Integer sipMaximumGap) {
		this.sipMaximumGap = sipMaximumGap;
	}

	public Integer getSipMaximumInstallmentAmount() {
		return sipMaximumInstallmentAmount;
	}

	public void setSipMaximumInstallmentAmount(Integer sipMaximumInstallmentAmount) {
		this.sipMaximumInstallmentAmount = sipMaximumInstallmentAmount;
	}

	public Integer getSipMaximumInstallmentNumbers() {
		return sipMaximumInstallmentNumbers;
	}

	public void setSipMaximumInstallmentNumbers(Integer sipMaximumInstallmentNumbers) {
		this.sipMaximumInstallmentNumbers = sipMaximumInstallmentNumbers;
	}

	public Integer getSipMinimumGap() {
		return sipMinimumGap;
	}

	public void setSipMinimumGap(Integer sipMinimumGap) {
		this.sipMinimumGap = sipMinimumGap;
	}

	public Integer getSipMinimumInstallmentAmount() {
		return sipMinimumInstallmentAmount;
	}

	public void setSipMinimumInstallmentAmount(Integer sipMinimumInstallmentAmount) {
		this.sipMinimumInstallmentAmount = sipMinimumInstallmentAmount;
	}

	public Integer getSipMinimumInstallmentNumbers() {
		return sipMinimumInstallmentNumbers;
	}

	public void setSipMinimumInstallmentNumbers(Integer sipMinimumInstallmentNumbers) {
		this.sipMinimumInstallmentNumbers = sipMinimumInstallmentNumbers;
	}

	public Integer getSipMultiplierAmount() {
		return sipMultiplierAmount;
	}

	public void setSipMultiplierAmount(Integer sipMultiplierAmount) {
		this.sipMultiplierAmount = sipMultiplierAmount;
	}

	public Integer getSipStatus() {
		return sipStatus;
	}

	public void setSipStatus(Integer sipStatus) {
		this.sipStatus = sipStatus;
	}
	
}
