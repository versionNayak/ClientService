package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@SuppressWarnings("serial")
public class ClientEpfDTO implements Serializable {

	private int id;

	private Integer clientID;
	private Integer familyMemberID;
	@NotNull(message = "financialAssetType must not be null")
	private Byte financialAssetType;
	@NotNull(message = "annualContributionIncrease must not be null")
	@DecimalMin(value="0.01", message="annualContributionIncrease cannot be 0")
	private BigDecimal annualContributionIncrease;
	@NotNull(message = "epfCurrentBalance must not be null")
	@DecimalMin(value="0.01", message="epfCurrentBalance cannot be 0")
	private BigDecimal epfCurrentBalance;
	@NotNull(message = "epsCurrentBalance must not be null")
	@DecimalMin(value="0.01", message="epsCurrentBalance cannot be 0")
	private BigDecimal epsCurrentBalance;
	@NotNull(message = "epfWithdrawalAge must not be null")
	@Min(value=1,message = "epfWithdrawalAge must not be 0")
	private Integer epfWithdrawalAge;
	@NotNull(message = "monthlyBasicDA must not be null")
	@DecimalMin(value="0.01", message="monthlyBasicDA cannot be 0")
	private BigDecimal monthlyBasicDA;
	@NotNull(message = "epfoInterestRate must not be null")
	@DecimalMin(value="0.01", message="epfoInterestRate cannot be 0")
	private BigDecimal epfoInterestRate;
	@NotNull(message = "contributionUptoAge must not be null")
	@Min(value=1,message = "contributionUptoAge must not be 0")
	private Integer contributionUptoAge;
	@NotNull(message = "salaryIncreaseRefMonth must not be null")
	private String salaryIncreaseRefMonth;
	@NotNull(message = "serviceYears must not be null")
	private Integer serviceYears;
	private String firstName;
	private byte relationId;
	private String relationName;
	private String gender;
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

	private ClientAnnuityDTO annuityDTO;

	public ClientEpfDTO() {
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

	public BigDecimal getAnnualContributionIncrease() {
		return annualContributionIncrease;
	}

	public void setAnnualContributionIncrease(BigDecimal annualContributionIncrease) {
		this.annualContributionIncrease = annualContributionIncrease;
	}

	public BigDecimal getEpfCurrentBalance() {
		return epfCurrentBalance;
	}

	public void setEpfCurrentBalance(BigDecimal epfCurrentBalance) {
		this.epfCurrentBalance = epfCurrentBalance;
	}

	public BigDecimal getEpsCurrentBalance() {
		return epsCurrentBalance;
	}

	public void setEpsCurrentBalance(BigDecimal epsCurrentBalance) {
		this.epsCurrentBalance = epsCurrentBalance;
	}

	public Integer getEpfWithdrawalAge() {
		return epfWithdrawalAge;
	}

	public void setEpfWithdrawalAge(Integer epfWithdrawalAge) {
		this.epfWithdrawalAge = epfWithdrawalAge;
	}

	public BigDecimal getMonthlyBasicDA() {
		return monthlyBasicDA;
	}

	public void setMonthlyBasicDA(BigDecimal monthlyBasicDA) {
		this.monthlyBasicDA = monthlyBasicDA;
	}

	public BigDecimal getEpfoInterestRate() {
		return epfoInterestRate;
	}

	public void setEpfoInterestRate(BigDecimal epfoInterestRate) {
		this.epfoInterestRate = epfoInterestRate;
	}

	public Integer getContributionUptoAge() {
		return contributionUptoAge;
	}

	public void setContributionUptoAge(Integer contributionUptoAge) {
		this.contributionUptoAge = contributionUptoAge;
	}

	public String getSalaryIncreaseRefMonth() {
		return salaryIncreaseRefMonth;
	}

	public void setSalaryIncreaseRefMonth(String salaryIncreaseRefMonth) {
		this.salaryIncreaseRefMonth = salaryIncreaseRefMonth;
	}

	public Integer getServiceYears() {
		return serviceYears;
	}

	public void setServiceYears(Integer serviceYears) {
		this.serviceYears = serviceYears;
	}

	public ClientAnnuityDTO getAnnuityDTO() {
		return annuityDTO;
	}

	public void setAnnuityDTO(ClientAnnuityDTO annuityDTO) {
		this.annuityDTO = annuityDTO;
	}

	@Override
	public String toString() {
		return "ClientEpfDTO [id=" + id + ", clientID=" + clientID + ", familyMemberID=" + familyMemberID
				+ ", financialAssetType=" + financialAssetType + ", annualContributionIncrease="
				+ annualContributionIncrease + ", epfCurrentBalance=" + epfCurrentBalance + ", epsCurrentBalance="
				+ epsCurrentBalance + ", epfWithdrawalAge=" + epfWithdrawalAge + ", monthlyBasicDA=" + monthlyBasicDA
				+ ", epfoInterestRate=" + epfoInterestRate + ", contributionUptoAge=" + contributionUptoAge
				+ ", salaryIncreaseRefMonth=" + salaryIncreaseRefMonth + ", serviceYears=" + serviceYears
				+ ", firstName=" + firstName + ", relationId=" + relationId + ", relationName=" + relationName
				+ ", gender=" + gender + ", annuityDTO=" + annuityDTO + "]";
	}

	

}