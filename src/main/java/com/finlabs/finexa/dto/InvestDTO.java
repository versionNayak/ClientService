package com.finlabs.finexa.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvestDTO {
	
	private int rowCount;
	private int clientID;
	private int advisorID;
	private String clientCode;
	private String productName;
	private String productIsin;
	private String idTransMode;
	private String modeOfInvestment;
	private String lumpsumAmt;
	private String sipAmt;
	private String sipFreq;
	private String sipTenure;
	private String regType;
	private String mandateType;
	private String orderType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date startDate;
	private String orderStatus;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductIsin() {
		return productIsin;
	}
	public void setProductIsin(String productIsin) {
		this.productIsin = productIsin;
	}
	public String getModeOfInvestment() {
		return modeOfInvestment;
	}
	public void setModeOfInvestment(String modeOfInvestment) {
		this.modeOfInvestment = modeOfInvestment;
	}
	public String getLumpsumAmt() {
		return lumpsumAmt;
	}
	public void setLumpsumAmt(String lumpsumAmt) {
		this.lumpsumAmt = lumpsumAmt;
	}
	public String getSipAmt() {
		return sipAmt;
	}
	public void setSipAmt(String sipAmt) {
		this.sipAmt = sipAmt;
	}
	public String getSipFreq() {
		return sipFreq;
	}
	public void setSipFreq(String sipFreq) {
		this.sipFreq = sipFreq;
	}
	public String getSipTenure() {
		return sipTenure;
	}
	public void setSipTenure(String sipTenure) {
		this.sipTenure = sipTenure;
	}
	public String getRegType() {
		return regType;
	}
	public void setRegType(String regType) {
		this.regType = regType;
	}
	public String getMandateType() {
		return mandateType;
	}
	public void setMandateType(String mandateType) {
		this.mandateType = mandateType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public int getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getIdTransMode() {
		return idTransMode;
	}
	public void setIdTransMode(String idTransMode) {
		this.idTransMode = idTransMode;
	}

}
