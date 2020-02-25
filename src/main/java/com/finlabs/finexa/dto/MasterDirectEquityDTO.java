package com.finlabs.finexa.dto;

public class MasterDirectEquityDTO {
	private String isin;
	private int bseCode;
	private String nseCode;
	private String stockName;
	private byte sectorID;
	
	public MasterDirectEquityDTO() {
		super();
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public int getBseCode() {
		return bseCode;
	}
	public void setBseCode(int bseCode) {
		this.bseCode = bseCode;
	}
	public String getNseCode() {
		return nseCode;
	}
	public void setNseCode(String nseCode) {
		this.nseCode = nseCode;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public byte getSectorID() {
		return sectorID;
	}
	public void setSectorID(byte sectorID) {
		this.sectorID = sectorID;
	}
	
	
}

