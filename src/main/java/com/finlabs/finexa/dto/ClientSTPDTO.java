package com.finlabs.finexa.dto;


import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
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


public class ClientSTPDTO  {
	private static final long serialVersionUID = 1L;
	
	private BigInteger id;

	private String bseResponse;

	private String bseResponseCode;

	private String buySellType;

	private String clientCode;

	private Date createdAt;

	private String euinDeclaration;

	private String euinNumber;

	private String firstOrderToday;

	private String folioNo;

	private String frequencyType;

	private BigInteger installmentAmount;

	private String internalRefNo;

	private Timestamp lastModifiedOn;

	private int noOfTransfers;

	private String remarks;

	private String saveMode;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date startDate;

	private String subBroker_ARN;

	private String subBrokerCode;

	private String transactionMode;

	private int advisorId;

	private int clientId;
	
	private int fromSchemeUniqueNo;
	
	private int toSchemeUniqueNo;
	
	private String purchaseMode;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
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

	public String getEuinDeclaration() {
		return euinDeclaration;
	}

	public void setEuinDeclaration(String euinDeclaration) {
		this.euinDeclaration = euinDeclaration;
	}

	public String getEuinNumber() {
		return euinNumber;
	}

	public void setEuinNumber(String euinNumber) {
		this.euinNumber = euinNumber;
	}

	public String getFirstOrderToday() {
		return firstOrderToday;
	}

	public void setFirstOrderToday(String firstOrderToday) {
		this.firstOrderToday = firstOrderToday;
	}

	public String getFolioNo() {
		return folioNo;
	}

	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
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

	public String getInternalRefNo() {
		return internalRefNo;
	}

	public void setInternalRefNo(String internalRefNo) {
		this.internalRefNo = internalRefNo;
	}

	public Timestamp getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Timestamp lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public int getNoOfTransfers() {
		return noOfTransfers;
	}

	public void setNoOfTransfers(int noOfTransfers) {
		this.noOfTransfers = noOfTransfers;
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

	public String getSubBroker_ARN() {
		return subBroker_ARN;
	}

	public void setSubBroker_ARN(String subBroker_ARN) {
		this.subBroker_ARN = subBroker_ARN;
	}

	public String getSubBrokerCode() {
		return subBrokerCode;
	}

	public void setSubBrokerCode(String subBrokerCode) {
		this.subBrokerCode = subBrokerCode;
	}

	public String getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPurchaseMode() {
		return purchaseMode;
	}

	public void setPurchaseMode(String purchaseMode) {
		this.purchaseMode = purchaseMode;
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
	
}