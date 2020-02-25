package com.finlabs.finexa.dto;


import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;



public class ClientCashDTO  {

	private int id;
	
	private String ownerName;
	
	@NotNull(message = "clientId must not be null")
	private int clientId;
	@NotNull(message = "familyMemberId must not be null")
	private int familyMemberId;

	private byte createdBy;
	
	private Timestamp createdOn;
	
	private byte lastUpdateddBy;
	
	private Timestamp lastUpdatedOn;
	
	@NotNull(message = "currentBalance must not be null")
	@DecimalMin(value="0.01", message="currentBalance cannot be 0")
	private BigDecimal currentBalance;
	
	private Integer bankID;
	private String bankName;
	@Min(value=1,message = "cashBalanceTypeId must not be 0")
	private byte cashBalanceTypeId;
	private String cashBalanceTypeName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getFamilyMemberId() {
		return familyMemberId;
	}

	public void setFamilyMemberId(int familyMemberId) {
		this.familyMemberId = familyMemberId;
	}

	public byte getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(byte createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public byte getLastUpdateddBy() {
		return lastUpdateddBy;
	}

	public void setLastUpdateddBy(byte lastUpdateddBy) {
		this.lastUpdateddBy = lastUpdateddBy;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Integer getBankID() {
		return bankID;
	}

	public void setBankID(Integer bankID) {
		this.bankID = bankID;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public byte getCashBalanceTypeId() {
		return cashBalanceTypeId;
	}

	public void setCashBalanceTypeId(byte cashBalanceTypeId) {
		this.cashBalanceTypeId = cashBalanceTypeId;
	}

	public String getCashBalanceTypeName() {
		return cashBalanceTypeName;
	}

	public void setCashBalanceTypeName(String cashBalanceTypeName) {
		this.cashBalanceTypeName = cashBalanceTypeName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Override
	public String toString() {
		return "ClientCashDTO [id=" + id + ", ownerName=" + ownerName + ", clientId=" + clientId + ", familyMemberId="
				+ familyMemberId + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastUpdateddBy="
				+ lastUpdateddBy + ", lastUpdatedOn=" + lastUpdatedOn + ", currentBalance=" + currentBalance
				+ ", bankID=" + bankID + ", bankName=" + bankName + ", cashBalanceTypeId=" + cashBalanceTypeId
				+ ", cashBalanceTypeName=" + cashBalanceTypeName + "]";
	}

	
}