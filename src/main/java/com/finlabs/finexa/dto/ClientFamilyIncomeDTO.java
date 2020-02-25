package com.finlabs.finexa.dto;

import java.math.BigDecimal;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;



public class ClientFamilyIncomeDTO {
	private int id;
	

	private Integer clientId;

	private Integer familyMemberId;
	@NotNull(message = "incomeType must not be null")
	@Min(value=1,message = "incomeType must not be 0")
	private Integer incomeType;
	@NotNull(message = "incomeAmount must not be null")
	@DecimalMin(value="0.01", message="incomeAmount cannot be 0")
	private BigDecimal incomeAmount;

	private Byte incomeFrequency;

	private Byte referenceMonth;

	private Byte incomeEndYear;
	
	private String option;
	
	private BigDecimal Total;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private Byte relationId;
	
	private String relationName;
	
	private String gender;

	public int getId() {
		return id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public Integer getFamilyMemberId() {
		return familyMemberId;
	}

	public Integer getIncomeType() {
		return incomeType;
	}

	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}



	public String getOption() {
		return option;
	}

	public BigDecimal getTotal() {
		return Total;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public Byte getRelationId() {
		return relationId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public void setFamilyMemberId(Integer familyMemberId) {
		this.familyMemberId = familyMemberId;
	}

	public void setIncomeType(Integer incomeType) {
		this.incomeType = incomeType;
	}

	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	

	public Byte getIncomeFrequency() {
		return incomeFrequency;
	}

	public Byte getReferenceMonth() {
		return referenceMonth;
	}

	public Byte getIncomeEndYear() {
		return incomeEndYear;
	}

	public void setIncomeFrequency(Byte incomeFrequency) {
		this.incomeFrequency = incomeFrequency;
	}

	public void setReferenceMonth(Byte referenceMonth) {
		this.referenceMonth = referenceMonth;
	}

	public void setIncomeEndYear(Byte incomeEndYear) {
		this.incomeEndYear = incomeEndYear;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public void setTotal(BigDecimal total) {
		Total = total;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setRelationId(Byte relationId) {
		this.relationId = relationId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	@Override
	public String toString() {
		return "ClientFamilyIncomeDTO [id=" + id + ", clientId=" + clientId + ", familyMemberId=" + familyMemberId
				+ ", incomeType=" + incomeType + ", incomeAmount=" + incomeAmount + ", incomeFrequency="
				+ incomeFrequency + ", referenceMonth=" + referenceMonth + ", incomeEndYear=" + incomeEndYear
				+ ", option=" + option + ", Total=" + Total + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", relationId=" + relationId + ", relationName=" + relationName
				+ ", gender=" + gender + "]";
	}

	
	
}
