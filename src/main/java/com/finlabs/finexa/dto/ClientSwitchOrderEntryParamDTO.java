package com.finlabs.finexa.dto;


import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterTransactBSEMFPhysicalScheme;


public class ClientSwitchOrderEntryParamDTO  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String allUnitsFlag;

	private BigInteger amount;

	private String bseResponse;

	private String bseResponseCode;

	private String buySell;

	private String buySellType;

	private String clientCode;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	private String createdBy;

	private String dpTxn;

	private String euin;

	private String euinFlag;

	private String folioNo;

	private String ipAddress;

	private String kycStatus;

	private Timestamp lastModifiedAt;

	private String lastModifiedBy;

	private String minRedeem;

	private String param1;

	private String param2;

	private String param3;

	private String remarks;

	private String saveMode;

	private String subBrokerCode;

	private String transCode;

	private BigInteger units;

	private String urn;

	private int advisorId;
	
	private int clientId;
	
	private int fromSchemeUniqueNo;
	
	private int toSchemeUniqueNo;
	
	private String encryptedPassword;
	
	private String passKey;
	
	private String purchaseMode;
	

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAllUnitsFlag() {
		return allUnitsFlag;
	}

	public void setAllUnitsFlag(String allUnitsFlag) {
		this.allUnitsFlag = allUnitsFlag;
	}

	public BigInteger getAmount() {
		return amount;
	}

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	public String getBseResponse() {
		return bseResponse;
	}

	public void setBseResponse(String bseResponse) {
		this.bseResponse = bseResponse;
	}

	public String getBseResponseCode() {
		return bseResponseCode;
	}

	public void setBseResponseCode(String bseResponseCode) {
		this.bseResponseCode = bseResponseCode;
	}

	public String getBuySell() {
		return buySell;
	}

	public void setBuySell(String buySell) {
		this.buySell = buySell;
	}

	public String getBuySellType() {
		return buySellType;
	}

	public void setBuySellType(String buySellType) {
		this.buySellType = buySellType;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDpTxn() {
		return dpTxn;
	}

	public void setDpTxn(String dpTxn) {
		this.dpTxn = dpTxn;
	}

	public String getEuin() {
		return euin;
	}

	public void setEuin(String euin) {
		this.euin = euin;
	}

	public String getEuinFlag() {
		return euinFlag;
	}

	public void setEuinFlag(String euinFlag) {
		this.euinFlag = euinFlag;
	}

	public String getFolioNo() {
		return folioNo;
	}

	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public Timestamp getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Timestamp lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getMinRedeem() {
		return minRedeem;
	}

	public void setMinRedeem(String minRedeem) {
		this.minRedeem = minRedeem;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}

	public String getSubBrokerCode() {
		return subBrokerCode;
	}

	public void setSubBrokerCode(String subBrokerCode) {
		this.subBrokerCode = subBrokerCode;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public BigInteger getUnits() {
		return units;
	}

	public void setUnits(BigInteger units) {
		this.units = units;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getFromSchemeUniqueNo() {
		return fromSchemeUniqueNo;
	}

	public void setFromSchemeUniqueNo(int fromSchemeUniqueNo) {
		this.fromSchemeUniqueNo = fromSchemeUniqueNo;
	}

	public int getToSchemeUniqueNo() {
		return toSchemeUniqueNo;
	}

	public void setToSchemeUniqueNo(int toSchemeUniqueNo) {
		this.toSchemeUniqueNo = toSchemeUniqueNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPurchaseMode() {
		return purchaseMode;
	}

	public void setPurchaseMode(String purchaseMode) {
		this.purchaseMode = purchaseMode;
	}
	

}