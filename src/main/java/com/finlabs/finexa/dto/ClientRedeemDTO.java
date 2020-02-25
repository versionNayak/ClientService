package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class ClientRedeemDTO {
	
	private BigInteger ID;
	private BigInteger clientID;
	private BigInteger advisorID;
	private String transactionCode;
	private String URN;
	
	private BigInteger orderID;
	private String clientCode;
	private int uniqueCodeDemat;
	
	private int uniqueSchemeCode;
	private String buySell;
	
	private String buySellType;
	private String dpTxn;
	private BigInteger amount;
	private BigInteger qty;
	private String allRedeem;
	
	private String folioNo;
	private String remarks;
	private String kycStatus;
	private String refNo;
	private String subBrCode;
	
	private String euin;
	private String euinFlag;
	private String minRedeem;
	private String dpc;
	private String ipAdd;
	
	private String param1;
	private String param2;
	private String param3;
	
	private String encryptedPassword;
	
	private String passKey;
	
	private String purchaseMode;
	
	public BigInteger getID() {
		return ID;
	}
	public void setID(BigInteger iD) {
		ID = iD;
	}
	public BigInteger getClientID() {
		return clientID;
	}
	public void setClientID(BigInteger clientID) {
		this.clientID = clientID;
	}
	public BigInteger getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(BigInteger advisorID) {
		this.advisorID = advisorID;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getURN() {
		return URN;
	}
	public void setURN(String uRN) {
		URN = uRN;
	}
	public BigInteger getOrderID() {
		return orderID;
	}
	public void setOrderID(BigInteger orderID) {
		this.orderID = orderID;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
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
	public String getDpTxn() {
		return dpTxn;
	}
	public void setDpTxn(String dpTxn) {
		this.dpTxn = dpTxn;
	}
	public BigInteger getAmount() {
		return amount;
	}
	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}
	public BigInteger getQty() {
		return qty;
	}
	public void setQty(BigInteger qty) {
		this.qty = qty;
	}
	public String getAllRedeem() {
		return allRedeem;
	}
	public void setAllRedeem(String allRedeem) {
		this.allRedeem = allRedeem;
	}
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getKycStatus() {
		return kycStatus;
	}
	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getSubBrCode() {
		return subBrCode;
	}
	public void setSubBrCode(String subBrCode) {
		this.subBrCode = subBrCode;
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
	public String getMinRedeem() {
		return minRedeem;
	}
	public void setMinRedeem(String minRedeem) {
		this.minRedeem = minRedeem;
	}
	public String getDpc() {
		return dpc;
	}
	public void setDpc(String dpc) {
		this.dpc = dpc;
	}
	public String getIpAdd() {
		return ipAdd;
	}
	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
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
	public int getUniqueCodeDemat() {
		return uniqueCodeDemat;
	}
	public void setUniqueCodeDemat(int uniqueCodeDemat) {
		this.uniqueCodeDemat = uniqueCodeDemat;
	}
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	public String getPassKey() {
		return passKey;
	}
	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}
	public String getPurchaseMode() {
		return purchaseMode;
	}
	public void setPurchaseMode(String purchaseMode) {
		this.purchaseMode = purchaseMode;
	}
	public int getUniqueSchemeCode() {
		return uniqueSchemeCode;
	}
	public void setUniqueSchemeCode(int uniqueSchemeCode) {
		this.uniqueSchemeCode = uniqueSchemeCode;
	}
	
}
