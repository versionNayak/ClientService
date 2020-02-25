package com.finlabs.finexa.dto;

public class TransactionMISInflowOutflowDTO {
	
	private String investorName;
	private String amc;
	private String schemeName;
	private String folioNo;
	private String transType;
	private String transDate;
	private String transAmt;
	private String nav;
	private String units;
	private int SRNo;
	private String sNO;
	
	
	public String getsNO() {
		return sNO;
	}
	public void setsNO(String sNO) {
		this.sNO = sNO;
	}
	public int getSRNo() {
		return SRNo;
	}
	public void setSRNo(int sRNo) {
		SRNo = sRNo;
	}
	public String getInvestorName() {
		return investorName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public String getAmc() {
		return amc;
	}
	public void setAmc(String amc) {
		this.amc = amc;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getFolioNo() {
		return folioNo;
	}
	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getNav() {
		return nav;
	}
	public void setNav(String nav) {
		this.nav = nav;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
}


