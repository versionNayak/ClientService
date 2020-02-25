package com.finlabs.finexa.dto;


import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;


public class ClientGoalDTO  {

	private int id;
	private Byte createdBy;
	private Timestamp createdOn;
//	@NotNull(message = "description must not be null")
	private String description;
//	@NotNull(message = "estimatedCostOfGoal must not be null")
	@DecimalMin(value="0.01", message="estimatedCostOfGoal cannot be 0")
	private Double estimatedCostOfGoal;
	private Double expectedInflationRate;
	private Double expectedReturnOnCorpus;
	private Byte lastUpdateddBy;
	private Timestamp lastUpdatedOn;
	private Float loanInterestRate;
	private Float loanPercent;
//	@NotNull(message = "loanRequiredFlag must not be null")
	private String loanRequiredFlag;
	private Byte loanTenure;
	private Double postRetirementAnnualExpense;	
	private Byte postRetirementExpectedPayoutFrequency;
//	@NotNull(message = "priority must not be null")
//	@Min(value=1,message = "priority must not be 0")
	private Byte priority;
//	@NotNull(message = "recurringFlag must not be null")
	private String recurringFlag;
	private String startMonthYear;
	private Byte yearsToGoal;
	private int clientId;
	private int clientFamilyMemberId;
//	@NotNull(message = "lookupGoalTypeId must not be null")
//	@Min(value=1,message = "lookupGoalTypeId must not be 0")
	private byte lookupGoalTypeId;	
	private String lookupGoalTypeName;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")	
    private Date dt;
    private int var1;
 // @NotNull(message = "lookupGoalCorpusUtilizationFrequencyId must not be null")
//	@Min(value=1,message = "lookupGoalCorpusUtilizationFrequencyId must not be 0")
	private byte lookupGoalCorpusUtilizationFrequencyId;
	private int lookupFrequencyId;
	private Double lumpsum;
	private Double sip;
	private String name;
	private Double corpusReqdAtGoalStartOutput;

	public ClientGoalDTO() {
		
		
	}

	

	public int getVar1() {
		return var1;
	}



	public void setVar1(int var1) {
		this.var1 = var1;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Byte getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Byte createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getEstimatedCostOfGoal() {
		return estimatedCostOfGoal;
	}

	public void setEstimatedCostOfGoal(Double estimatedCostOfGoal) {
		this.estimatedCostOfGoal = estimatedCostOfGoal;
	}

	public Double getExpectedInflationRate() {
		return expectedInflationRate;
	}

	public void setExpectedInflationRate(Double expectedInflationRate) {
		this.expectedInflationRate = expectedInflationRate;
	}

	public Double getExpectedReturnOnCorpus() {
		return expectedReturnOnCorpus;
	}

	public void setExpectedReturnOnCorpus(Double expectedReturnOnCorpus) {
		this.expectedReturnOnCorpus = expectedReturnOnCorpus;
	}

	public Byte getLastUpdateddBy() {
		return lastUpdateddBy;
	}

	public void setLastUpdateddBy(Byte lastUpdateddBy) {
		this.lastUpdateddBy = lastUpdateddBy;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Float getLoanInterestRate() {
		return loanInterestRate;
	}

	public void setLoanInterestRate(Float loanInterestRate) {
		this.loanInterestRate = loanInterestRate;
	}

	public Float getLoanPercent() {
		return loanPercent;
	}

	public void setLoanPercent(Float loanPercent) {
		this.loanPercent = loanPercent;
	}

	public String getLoanRequiredFlag() {
		return loanRequiredFlag;
	}

	public void setLoanRequiredFlag(String loanRequiredFlag) {
		this.loanRequiredFlag = loanRequiredFlag;
	}

	public Byte getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(Byte loanTenure) {
		this.loanTenure = loanTenure;
	}

	public Double getPostRetirementAnnualExpense() {
		return postRetirementAnnualExpense;
	}

	public void setPostRetirementAnnualExpense(Double postRetirementAnnualExpense) {
		this.postRetirementAnnualExpense = postRetirementAnnualExpense;
	}

	public Byte getPriority() {
		return priority;
	}

	public void setPriority(Byte priority) {
		this.priority = priority;
	}

	public String getRecurringFlag() {
		return recurringFlag;
	}

	public void setRecurringFlag(String recurringFlag) {
		this.recurringFlag = recurringFlag;
	}

	public String getStartMonthYear() {
		return startMonthYear;
	}

	public void setStartMonthYear(String startMonthYear) {
		this.startMonthYear = startMonthYear;
	}

	public Byte getYearsToGoal() {
		return yearsToGoal;
	}

	public void setYearsToGoal(Byte yearsToGoal) {
		this.yearsToGoal = yearsToGoal;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getClientFamilyMemberId() {
		return clientFamilyMemberId;
	}

	public void setClientFamilyMemberId(int clientFamilyMemberId) {
		this.clientFamilyMemberId = clientFamilyMemberId;
	}

	public byte getLookupGoalTypeId() {
		return lookupGoalTypeId;
	}

	public void setLookupGoalTypeId(byte lookupGoalTypeId) {
		this.lookupGoalTypeId = lookupGoalTypeId;
	}

	public byte getLookupGoalCorpusUtilizationFrequencyId() {
		return lookupGoalCorpusUtilizationFrequencyId;
	}

	public void setLookupGoalCorpusUtilizationFrequencyId(byte lookupGoalCorpusUtilizationFrequencyId) {
		this.lookupGoalCorpusUtilizationFrequencyId = lookupGoalCorpusUtilizationFrequencyId;
	}

	public int getLookupFrequencyId() {
		return lookupFrequencyId;
	}

	public void setLookupFrequencyId(int lookupFrequencyId) {
		this.lookupFrequencyId = lookupFrequencyId;
	}

	public Byte getPostRetirementExpectedPayoutFrequency() {
		return postRetirementExpectedPayoutFrequency;
	}

	public void setPostRetirementExpectedPayoutFrequency(Byte postRetirementExpectedPayoutFrequency) {
		this.postRetirementExpectedPayoutFrequency = postRetirementExpectedPayoutFrequency;
	}

	public String getLookupGoalTypeName() {
		return lookupGoalTypeName;
	}

	public void setLookupGoalTypeName(String lookupGoalTypeName) {
		this.lookupGoalTypeName = lookupGoalTypeName;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public Double getLumpsum() {
		return lumpsum;
	}

	public void setLumpsum(Double lumpsum) {
		this.lumpsum = lumpsum;
	}

	public Double getSip() {
		return sip;
	}

	public void setSip(Double sip) {
		this.sip = sip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCorpusReqdAtGoalStartOutput() {
		return corpusReqdAtGoalStartOutput;
	}

	public void setCorpusReqdAtGoalStartOutput(Double corpusReqdAtGoalStartOutput) {
		this.corpusReqdAtGoalStartOutput = corpusReqdAtGoalStartOutput;
	}
    

}