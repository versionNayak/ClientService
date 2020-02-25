package com.finlabs.finexa.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.finlabs.finexa.model.ClientMaster;

public class ClientExpenseDTO {

	private Integer id;
	private Byte endYear;
	@NotNull(message = "expenseAmount must not be null")
	@DecimalMin(value="0.01", message="expenseAmount cannot be 0")
	private Double expenseAmount;
	@NotNull(message = "expenseType must not be null")
	@Min(value=1,message = "expenseType must not be 0")
	private Integer expenseType;
	@NotNull(message = "frequency must not be null")
	@Min(value=1,message = "frequency must not be 0")
	private Byte frequency;
	private Byte referenceMonth;
	private Integer clientId;
	private String option;
	private ClientMaster clientmaster;
	private double totalexpense;
	public ClientExpenseDTO() {
	}

	public Integer getId() {
		return id;
	}

	public Byte getEndYear() {
		return endYear;
	}

	public Double getExpenseAmount() {
		return expenseAmount;
	}

	public Integer getExpenseType() {
		return expenseType;
	}

	public Byte getFrequency() {
		return frequency;
	}

	public Byte getReferenceMonth() {
		return referenceMonth;
	}

	public Integer getClientId() {
		return clientId;
	}

	public String getOption() {
		return option;
	}

	public ClientMaster getClientmaster() {
		return clientmaster;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEndYear(Byte endYear) {
		this.endYear = endYear;
	}

	public void setExpenseAmount(Double expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public void setExpenseType(Integer expenseType) {
		this.expenseType = expenseType;
	}

	public void setFrequency(Byte frequency) {
		this.frequency = frequency;
	}

	public void setReferenceMonth(Byte referenceMonth) {
		this.referenceMonth = referenceMonth;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public void setClientmaster(ClientMaster clientmaster) {
		this.clientmaster = clientmaster;
	}

	public double getTotalexpense() {
		return totalexpense;
	}

	public void setTotalexpense(double totalexpense) {
		this.totalexpense = totalexpense;
	}

	@Override
	public String toString() {
		return "ClientExpenseDTO [id=" + id + ", endYear=" + endYear + ", expenseAmount=" + expenseAmount
				+ ", expenseType=" + expenseType + ", frequency=" + frequency + ", referenceMonth=" + referenceMonth
				+ ", clientId=" + clientId + ", option=" + option + ", clientmaster=" + clientmaster + ", totalexpense="
				+ totalexpense + "]";
	}



	
}