package com.finlabs.finexa.dto;

public class MasterDirectEquityForPCDTO {

	private String isin;
	private String stockName;

	public MasterDirectEquityForPCDTO() {
		super();
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@Override
	public String toString() {
		return "MasterDirectEquityForPCDTO [isin=" + isin + ", stockName=" + stockName + "]";
	}

}
