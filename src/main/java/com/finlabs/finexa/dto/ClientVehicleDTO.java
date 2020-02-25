package com.finlabs.finexa.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ClientVehicleDTO {

	private int id;
	@NotNull(message = "financialAssetType must not be null")
	private byte financialAssetType;
	@NotNull(message = "assetTypeId must not be null")
	@Min(value=1,message = "assetTypeId must not be 0")
	private byte assetTypeId;
	@NotNull(message = "currentValue must not be null")
	@DecimalMin(value="0.01", message="currentValue cannot be 0")
    private BigDecimal currentValue;
	@NotNull(message = "vDescription must not be null")
    private String vDescription;
    private int clientID;
    private int familyMemberID;
    private String financialAssetName;
    private String ownerName;
    private String assetTypeName;

	public ClientVehicleDTO() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public BigDecimal getCurrentValue() {
		return currentValue;
	}


	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}





	public String getAssetTypeName() {
		return assetTypeName;
	}


	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}


	public String getvDescription() {
		return vDescription;
	}


	public void setvDescription(String vDescription) {
		this.vDescription = vDescription;
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


	public byte getFinancialAssetType() {
		return financialAssetType;
	}


	public void setFinancialAssetType(byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}


	public byte getAssetTypeId() {
		return assetTypeId;
	}


	public void setAssetTypeId(byte assetTypeId) {
		this.assetTypeId = assetTypeId;
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


	@Override
	public String toString() {
		return "ClientVehicleDTO [id=" + id + ", financialAssetType=" + financialAssetType + ", assetTypeId="
				+ assetTypeId + ", currentValue=" + currentValue + ", vDescription=" + vDescription + ", clientID="
				+ clientID + ", familyMemberID=" + familyMemberID + ", financialAssetName=" + financialAssetName
				+ ", ownerName=" + ownerName + ", assetTypeName=" + assetTypeName + "]";
	}


	
	

}