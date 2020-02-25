package com.finlabs.finexa.dto;


import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;



public class ClientRealEstateDTO  {

	private int id;
	@NotNull(message = "financialAssetType must not be null")
    private byte financialAssetType;
	@NotNull(message = "assetTypeId must not be null")
	@Min(value=1,message = "assetTypeId must not be 0")
    private byte assetTypeId;
	@NotNull(message = "currentValue must not be null")
	@DecimalMin(value="0.01", message="currentValue cannot be 0")
    private BigDecimal currentValue;
	@NotNull(message = "description must not be null")
    private String description;
	@NotNull(message = "lienMarked must not be null")
    private Byte lienMarked;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	private Date lienReleaseDate;
	//@NotNull(message = "rentalIncome must not be null")
	@DecimalMin(value="0.01", message="rentalIncome cannot be 0")
    private BigDecimal rentalIncome;
	//@NotNull(message = "rentalFrequency must not be null")
	//@Min(value=1,message = "rentalFrequency must not be 0")
    private byte rentalFrequency;
    private int clientID;
    private int familyMemberID;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone="IST")
    @NotNull(message = "investmentDate must not be null")
    private Date investmentDate;
    @NotNull(message = "investmentValue must not be null")
	@DecimalMin(value="0.01", message="investmentValue cannot be 0")
    private BigDecimal investmentValue;
    private String financialAssetName;
    private String ownerName;
    private String assetTypeName;

	public ClientRealEstateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getFamilyMemberID() {
		return familyMemberID;
	}

	public void setFamilyMemberID(int familyMemberID) {
		this.familyMemberID = familyMemberID;
	}

	public byte getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(byte assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getLienMarked() {
		return lienMarked;
	}

	
	public void setLienMarked(Byte lienMarked) {
		this.lienMarked = lienMarked;
	}

	public Date getLienReleaseDate() {
		return lienReleaseDate;
	}

	public void setLienReleaseDate(Date lienReleaseDate) {
		this.lienReleaseDate = lienReleaseDate;
	}

	public BigDecimal getRentalIncome() {
		return rentalIncome;
	}

	public void setRentalIncome(BigDecimal rentalIncome) {
		this.rentalIncome = rentalIncome;
	}

	public byte getRentalFrequency() {
		return rentalFrequency;
	}

	public void setRentalFrequency(byte rentalFrequency) {
		this.rentalFrequency = rentalFrequency;
	}

	public byte getFinancialAssetType() {
		return financialAssetType;
	}

	public void setFinancialAssetType(byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}

	public Date getInvestmentDate() {
		return investmentDate;
	}

	public void setInvestmentDate(Date investmentDate) {
		this.investmentDate = investmentDate;
	}

	public BigDecimal getInvestmentValue() {
		return investmentValue;
	}

	public void setInvestmentValue(BigDecimal investmentValue) {
		this.investmentValue = investmentValue;
	}
	

	public String getFinancialAssetName() {
		return financialAssetName;
	}

	public void setFinancialAssetName(String financialAssetName) {
		this.financialAssetName = financialAssetName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAssetTypeName() {
		return assetTypeName;
	}

	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}

	@Override
	public String toString() {
		return "ClientRealEstateDTO [id=" + id + ", financialAssetType=" + financialAssetType + ", assetTypeId="
				+ assetTypeId + ", currentValue=" + currentValue + ", description=" + description + ", lienMarked="
				+ lienMarked + ", lienReleaseDate=" + lienReleaseDate + ", rentalIncome=" + rentalIncome
				+ ", rentalFrequency=" + rentalFrequency + ", clientID=" + clientID + ", familyMemberID="
				+ familyMemberID + ", investmentDate=" + investmentDate + ", investmentValue=" + investmentValue
				+ ", financialAssetName=" + financialAssetName + ", ownerName=" + ownerName + ", assetTypeName="
				+ assetTypeName + "]";
	}

	

	
	
	
}