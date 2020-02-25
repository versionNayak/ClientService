package com.finlabs.finexa.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IFSCCodeDTO {
	
	private String branchName;
	
	private String ifscCode;
	
	private List<String> accountNo;

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public List<String> getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(List<String> accountNo) {
		this.accountNo = accountNo;
	}
}
