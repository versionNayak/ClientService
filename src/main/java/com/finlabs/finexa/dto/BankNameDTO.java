package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BankNameDTO {
	
	private String bankName;
	
	private List<IFSCCodeDTO> ifscCodeList;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public List<IFSCCodeDTO> getIfscCodeList() {
		return ifscCodeList;
	}

	public void setIfscCodeList(List<IFSCCodeDTO> ifscCodeList) {
		this.ifscCodeList = ifscCodeList;
	}
	
}
