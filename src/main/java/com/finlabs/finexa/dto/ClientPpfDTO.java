package com.finlabs.finexa.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientPpfDTO {

	private int id;
	private Integer clientID;
	private Integer familyMemberID;
	@NotNull(message = "financialAssetType must not be null")
	private Byte financialAssetType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@NotNull(message = "startDate must not be null")
	private Date startDate;
	//@NotNull(message = "currentBalance must not be null")
	//@DecimalMin(value = "0.01", message = "currentBalance cannot be 0")
	private BigDecimal currentBalance;
	//@NotNull(message = "plannedDepositAmount must not be null")
	//@DecimalMin(value = "0.01", message = "plannedDepositAmount cannot be 0")
	private BigDecimal plannedDepositAmount;
	private Byte amountDepositFrequencyExt;
	@NotNull(message = "ppfTenure must not be null")
	@Min(value = 1, message = "ppfTenure must not be 0")
	private Byte ppfTenure;
	@NotNull(message = "interestRate must not be null")
	@DecimalMin(value = "0.01", message = "interestRate cannot be 0")
	private BigDecimal interestRate;
	private Byte compoundingFrequency;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@NotNull(message = "maturityDate must not be null")
	private Date maturityDate;
	@NotNull(message = "extensionFlag must not be null")
	private String extensionFlag;
	@NotNull(message = "extTypeFlag must not be null")
	private String extTypeFlag;
	@DecimalMin(value = "0.01", message = "depositAmountExt cannot be 0")
	private BigDecimal depositAmountExt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date extensionStartDate;
	@DecimalMin(value = "0.01", message = "extensionInterestRate cannot be 0")
	private BigDecimal extensionInterestRate;
	private Byte extensionCompoundingFrequency;
	@Min(value = 1, message = "extensionTenure must not be 0")
	private Byte extensionTenure;
	@DecimalMin(value = "0.01", message = "extensionCurrentBalance cannot be 0")
	private BigDecimal extensionCurrentBalance;
	private Byte amountDepositFrequency;
	private String displayDate;
	private String firstName;
	private byte relationId;
	private String relationName;
	private String gender;

	public ClientPpfDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getClientID() {
		return clientID;
	}

	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}

	public Integer getFamilyMemberID() {
		return familyMemberID;
	}

	public void setFamilyMemberID(Integer familyMemberID) {
		this.familyMemberID = familyMemberID;
	}

	public Byte getFinancialAssetType() {
		return financialAssetType;
	}

	public void setFinancialAssetType(Byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public BigDecimal getPlannedDepositAmount() {
		return plannedDepositAmount;
	}

	public void setPlannedDepositAmount(BigDecimal plannedDepositAmount) {
		this.plannedDepositAmount = plannedDepositAmount;
	}

	public Byte getAmountDepositFrequencyExt() {
		return amountDepositFrequencyExt;
	}

	public void setAmountDepositFrequencyExt(Byte amountDepositFrequencyExt) {
		this.amountDepositFrequencyExt = amountDepositFrequencyExt;
	}

	public Byte getPpfTenure() {
		return ppfTenure;
	}

	public void setPpfTenure(Byte ppfTenure) {
		this.ppfTenure = ppfTenure;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public Byte getCompoundingFrequency() {
		return compoundingFrequency;
	}

	public void setCompoundingFrequency(Byte compoundingFrequency) {
		this.compoundingFrequency = compoundingFrequency;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getExtensionFlag() {
		return extensionFlag;
	}

	public void setExtensionFlag(String extensionFlag) {
		this.extensionFlag = extensionFlag;
	}

	public String getExtTypeFlag() {
		return extTypeFlag;
	}

	public void setExtTypeFlag(String extTypeFlag) {
		this.extTypeFlag = extTypeFlag;
	}

	public BigDecimal getDepositAmountExt() {
		return depositAmountExt;
	}

	public void setDepositAmountExt(BigDecimal depositAmountExt) {
		this.depositAmountExt = depositAmountExt;
	}

	public Date getExtensionStartDate() {
		return extensionStartDate;
	}

	public void setExtensionStartDate(Date extensionStartDate) {
		this.extensionStartDate = extensionStartDate;
	}

	public BigDecimal getExtensionInterestRate() {
		return extensionInterestRate;
	}

	public void setExtensionInterestRate(BigDecimal extensionInterestRate) {
		this.extensionInterestRate = extensionInterestRate;
	}

	public Byte getExtensionCompoundingFrequency() {
		return extensionCompoundingFrequency;
	}

	public void setExtensionCompoundingFrequency(Byte extensionCompoundingFrequency) {
		this.extensionCompoundingFrequency = extensionCompoundingFrequency;
	}

	public Byte getExtensionTenure() {
		return extensionTenure;
	}

	public void setExtensionTenure(Byte extensionTenure) {
		this.extensionTenure = extensionTenure;
	}

	public BigDecimal getExtensionCurrentBalance() {
		return extensionCurrentBalance;
	}

	public void setExtensionCurrentBalance(BigDecimal extensionCurrentBalance) {
		this.extensionCurrentBalance = extensionCurrentBalance;
	}

	public Byte getAmountDepositFrequency() {
		return amountDepositFrequency;
	}

	public void setAmountDepositFrequency(Byte amountDepositFrequency) {
		this.amountDepositFrequency = amountDepositFrequency;
	}

	public String getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public byte getRelationId() {
		return relationId;
	}

	public void setRelationId(byte relationId) {
		this.relationId = relationId;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "ClientPpfDTO [id=" + id + ", clientID=" + clientID + ", familyMemberID=" + familyMemberID
				+ ", financialAssetType=" + financialAssetType + ", startDate=" + startDate + ", currentBalance="
				+ currentBalance + ", plannedDepositAmount=" + plannedDepositAmount + ", amountDepositFrequencyExt="
				+ amountDepositFrequencyExt + ", ppfTenure=" + ppfTenure + ", interestRate=" + interestRate
				+ ", compoundingFrequency=" + compoundingFrequency + ", maturityDate=" + maturityDate
				+ ", extensionFlag=" + extensionFlag + ", extTypeFlag=" + extTypeFlag + ", depositAmountExt="
				+ depositAmountExt + ", extensionStartDate=" + extensionStartDate + ", extensionInterestRate="
				+ extensionInterestRate + ", extensionCompoundingFrequency=" + extensionCompoundingFrequency
				+ ", extensionTenure=" + extensionTenure + ", extensionCurrentBalance=" + extensionCurrentBalance
				+ ", amountDepositFrequency=" + amountDepositFrequency + ", displayDate=" + displayDate + ", firstName="
				+ firstName + ", relationId=" + relationId + ", relationName=" + relationName + ", gender=" + gender
				+ "]";
	}

	

}
