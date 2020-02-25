package com.finlabs.finexa.dto;

import java.util.Date;

public class MasterEquityDailyPriceDTO {
	
	private String isin;
	private String stockName;
	private Date date;
	private Double closingPrice;
	
	public MasterEquityDailyPriceDTO() {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getClosingPrice() {
		return closingPrice;
	}

	public void setClosingPrice(Double closingPrice) {
		this.closingPrice = closingPrice;
	}
	
	

}
