package com.finlabs.finexa.dto;

public class MasterDirectEquityMarketCapDTO {
	
	private String ISIN;
	private String stockName;
	private Integer marketcap;
	private double p_e;
	private double p_b;
	private double divYield;
	private double price52WeekHigh;
	private double price52WeekLow;
	private Integer dailyTradedVolume;
	private double lastClosingPrice;
	private Integer classificationID;
	
	public MasterDirectEquityMarketCapDTO() {
		super();
	}

	public String getISIN() {
		return ISIN;
	}

	public void setISIN(String ISIN) {
		this.ISIN = ISIN;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Integer getMarketcap() {
		return marketcap;
	}

	public void setMarketcap(Integer marketcap) {
		this.marketcap = marketcap;
	}

	public double getP_e() {
		return p_e;
	}

	public void setP_e(double p_e) {
		this.p_e = p_e;
	}

	public double getP_b() {
		return p_b;
	}

	public void setP_b(double p_b) {
		this.p_b = p_b;
	}

	public double getDivYield() {
		return divYield;
	}

	public void setDivYield(double divYield) {
		this.divYield = divYield;
	}

	public double getPrice52WeekHigh() {
		return price52WeekHigh;
	}

	public void setPrice52WeekHigh(double price52WeekHigh) {
		this.price52WeekHigh = price52WeekHigh;
	}

	public double getPrice52WeekLow() {
		return price52WeekLow;
	}

	public void setPrice52WeekLow(double price52WeekLow) {
		this.price52WeekLow = price52WeekLow;
	}

	public Integer getDailyTradedVolume() {
		return dailyTradedVolume;
	}

	public void setDailyTradedVolume(Integer dailyTradedVolume) {
		this.dailyTradedVolume = dailyTradedVolume;
	}

	public double getLastClosingPrice() {
		return lastClosingPrice;
	}

	public void setLastClosingPrice(double lastClosingPrice) {
		this.lastClosingPrice = lastClosingPrice;
	}

	public Integer getClassificationID() {
		return classificationID;
	}

	public void setClassificationID(Integer classificationID) {
		this.classificationID = classificationID;
	}
	
	

}
