package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientLoanDTO {

	private int id;
	private BigDecimal emiAmount;
	private byte interestPaymentFrequency;
	private BigDecimal interestRate;
	private BigDecimal loanAmount;
	private byte loanCategoryId;
	@NotNull(message = "loanDescription must not be null")
	private String loanDescription;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy" , timezone="IST")
	private Date loanEndDate;
	private String loanOriginalFlag;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="IST")
	private Date loanStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="IST")
	private Date loanStartDateOut;
	private byte loanTenure;
	private byte loanType;
	private String otherLoanCategory;
	private String emiLoanProviderName;
	private Integer pendingInstalments;
	private int clientID;
	private int familyMemberId;
	private String loanProviderName;
	private String ownerName;
	private String loanCategoryName;
	private String outstandingPrincipalToday;
	private BigDecimal emiAmountOut;
	private BigDecimal interestRateOut;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="IST")
	private Date loanEndDateOut;
	private String displayDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="IST")
	private Date loanEndDateNE;
	private int loanTenureNE;
	private BigDecimal interestRateNE;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="IST")
	private Date loanStartDateNE;
	private BigDecimal loanAmountNE;
	private String loanDescriptionNE;
	private String otherLoanCategoryNE;
	private byte loanCategoryIdNE;
	
	public String getLoanProviderName() {
		return loanProviderName;
	}

	public void setLoanProviderName(String loanProviderName) {
		this.loanProviderName = loanProviderName;
	}

	public ClientLoanDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmiLoanProviderName() {
		return emiLoanProviderName;
	}

	public void setEmiLoanProviderName(String emiLoanProviderName) {
		this.emiLoanProviderName = emiLoanProviderName;
	}
	
	public BigDecimal getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}
	
	public byte getInterestPaymentFrequency() {
		return interestPaymentFrequency;
	}

	public void setInterestPaymentFrequency(byte interestPaymentFrequency) {
		this.interestPaymentFrequency = interestPaymentFrequency;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public byte getLoanCategoryId() {
		return loanCategoryId;
	}

	public void setLoanCategoryId(byte loanCategoryId) {
		this.loanCategoryId = loanCategoryId;
	}

	public String getLoanDescription() {
		return loanDescription;
	}

	public void setLoanDescription(String loanDescription) {
		this.loanDescription = loanDescription;
	}

	public Date getLoanEndDate() {
		return loanEndDate;
	}

	public void setLoanEndDate(Date loanEndDate) {
		this.loanEndDate = loanEndDate;
	}

	public String getLoanOriginalFlag() {
		return loanOriginalFlag;
	}

	public void setLoanOriginalFlag(String loanOriginalFlag) {
		this.loanOriginalFlag = loanOriginalFlag;
	}

	public Date getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(Date loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public byte getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(byte loanTenure) {
		this.loanTenure = loanTenure;
	}

	public byte getLoanType() {
		return loanType;
	}

	public void setLoanType(byte loanType) {
		this.loanType = loanType;
	}

	public Integer getPendingInstalments() {
		return pendingInstalments;
	}

	public void setPendingInstalments(Integer pendingInstalments) {
		this.pendingInstalments = pendingInstalments;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getFamilyMemberId() {
		return familyMemberId;
	}

	public void setFamilyMemberId(int familyMemberId) {
		this.familyMemberId = familyMemberId;
	}

	public String getOtherLoanCategory() {
		return otherLoanCategory;
	}

	public void setOtherLoanCategory(String otherLoanCategory) {
		this.otherLoanCategory = otherLoanCategory;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getLoanCategoryName() {
		return loanCategoryName;
	}

	public void setLoanCategoryName(String loanCategoryName) {
		this.loanCategoryName = loanCategoryName;
	}

	public String getOutstandingPrincipalToday() {
		return outstandingPrincipalToday;
	}

	public void setOutstandingPrincipalToday(String outstandingPrincipalToday) {
		this.outstandingPrincipalToday = outstandingPrincipalToday;
	}

	public BigDecimal getEmiAmountOut() {
		return emiAmountOut;
	}

	public void setEmiAmountOut(BigDecimal emiAmountOut) {
		this.emiAmountOut = emiAmountOut;
	}

	public BigDecimal getInterestRateOut() {
		return interestRateOut;
	}

	public void setInterestRateOut(BigDecimal interestRateOut) {
		this.interestRateOut = interestRateOut;
	}

	public Date getLoanEndDateOut() {
		return loanEndDateOut;
	}

	public void setLoanEndDateOut(Date loanEndDateOut) {
		this.loanEndDateOut = loanEndDateOut;
	}

	public String getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}

	public Date getLoanEndDateNE() {
		return loanEndDateNE;
	}

	public void setLoanEndDateNE(Date loanEndDateNE) {
		this.loanEndDateNE = loanEndDateNE;
	}

	public int getLoanTenureNE() {
		return loanTenureNE;
	}

	public void setLoanTenureNE(int loanTenureNE) {
		this.loanTenureNE = loanTenureNE;
	}

	public BigDecimal getInterestRateNE() {
		return interestRateNE;
	}

	public void setInterestRateNE(BigDecimal interestRateNE) {
		this.interestRateNE = interestRateNE;
	}

	public Date getLoanStartDateNE() {
		return loanStartDateNE;
	}

	public void setLoanStartDateNE(Date loanStartDateNE) {
		this.loanStartDateNE = loanStartDateNE;
	}

	public BigDecimal getLoanAmountNE() {
		return loanAmountNE;
	}

	public void setLoanAmountNE(BigDecimal loanAmountNE) {
		this.loanAmountNE = loanAmountNE;
	}

	public String getLoanDescriptionNE() {
		return loanDescriptionNE;
	}

	public void setLoanDescriptionNE(String loanDescriptionNE) {
		this.loanDescriptionNE = loanDescriptionNE;
	}

	public String getOtherLoanCategoryNE() {
		return otherLoanCategoryNE;
	}

	public void setOtherLoanCategoryNE(String otherLoanCategoryNE) {
		this.otherLoanCategoryNE = otherLoanCategoryNE;
	}

	public byte getLoanCategoryIdNE() {
		return loanCategoryIdNE;
	}

	public void setLoanCategoryIdNE(byte loanCategoryIdNE) {
		this.loanCategoryIdNE = loanCategoryIdNE;
	}

	
	public Date getLoanStartDateOut() {
		return loanStartDateOut;
	}

	public void setLoanStartDateOut(Date loanStartDateOut) {
		this.loanStartDateOut = loanStartDateOut;
	}


}
