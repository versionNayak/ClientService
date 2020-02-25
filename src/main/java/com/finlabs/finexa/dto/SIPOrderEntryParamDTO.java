package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;


public class SIPOrderEntryParamDTO {
	private static final long serialVersionUID = 1L;

	
	private BigInteger id;

	private BigInteger brokerage;

	private String bseResponse;

	private String bseResponseCode;

	private String clientCode;

	private Date createdAt;

	private String dpc;

	private String dpTransactionMode;

	private String euin;

	private String euinFlag;

	private String firstOrderFlag;

	private String folioNo;

	private Integer frequencyAllowed;

	private String frequencyType;

	private BigInteger installmentAmount;

	private String ipAdd;

	@Column(name="IRN")
	private String irn;

	private Timestamp lastUpdatedOn;

	private Long noOfInstallment;

	private String param1;

	private String param2;

	private String param3;

	private String remarks;

	private String saveMode;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date startDate;

	private String subBrCode;

	private String transactionCode;

	private String transMode;

	private Integer uniqueSchemeCode;

	private String regType;

	private String urn;

	private String xsipMandateid;

	private int xsipRegId;

	private int advisorId;
	
	private int clientId;
	
	private String encryptedPassword;
	
	private String purchaseMode;
	
	private String passKey;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigInteger brokerage) {
		this.brokerage = brokerage;
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

	public String getDpc() {
		return dpc;
	}

	public void setDpc(String dpc) {
		this.dpc = dpc;
	}

	public String getDpTransactionMode() {
		return dpTransactionMode;
	}

	public void setDpTransactionMode(String dpTransactionMode) {
		this.dpTransactionMode = dpTransactionMode;
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

	public String getFirstOrderFlag() {
		return firstOrderFlag;
	}

	public void setFirstOrderFlag(String firstOrderFlag) {
		this.firstOrderFlag = firstOrderFlag;
	}

	public String getFolioNo() {
		return folioNo;
	}

	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}

	public Integer getFrequencyAllowed() {
		return frequencyAllowed;
	}

	public void setFrequencyAllowed(Integer frequencyAllowed) {
		this.frequencyAllowed = frequencyAllowed;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public BigInteger getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(BigInteger installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public String getIpAdd() {
		return ipAdd;
	}

	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}

	public String getIrn() {
		return irn;
	}

	public void setIrn(String irn) {
		this.irn = irn;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Long getNoOfInstallment() {
		return noOfInstallment;
	}

	public void setNoOfInstallment(Long noOfInstallment) {
		this.noOfInstallment = noOfInstallment;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getSubBrCode() {
		return subBrCode;
	}

	public void setSubBrCode(String subBrCode) {
		this.subBrCode = subBrCode;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransMode() {
		return transMode;
	}

	public void setTransMode(String transMode) {
		this.transMode = transMode;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getXsipMandateid() {
		return xsipMandateid;
	}

	public void setXsipMandateid(String xsipMandateid) {
		this.xsipMandateid = xsipMandateid;
	}

	public int getXsipRegId() {
		return xsipRegId;
	}

	public void setXsipRegId(int xsipRegId) {
		this.xsipRegId = xsipRegId;
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

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getUniqueSchemeCode() {
		return uniqueSchemeCode;
	}

	public void setUniqueSchemeCode(Integer uniqueSchemeCode) {
		this.uniqueSchemeCode = uniqueSchemeCode;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getPurchaseMode() {
		return purchaseMode;
	}

	public void setPurchaseMode(String purchaseMode) {
		this.purchaseMode = purchaseMode;
	}

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

}
