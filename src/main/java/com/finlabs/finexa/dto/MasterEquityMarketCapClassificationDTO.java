package com.finlabs.finexa.dto;

public class MasterEquityMarketCapClassificationDTO {
	
	private int id;
    private String marketcapCategory;
	private Integer rangeStart;
	private Integer rangeEnd;
	
	public MasterEquityMarketCapClassificationDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarketcapCategory() {
		return marketcapCategory;
	}

	public void setMarketcapCategory(String marketcapCategory) {
		this.marketcapCategory = marketcapCategory;
	}

	public Integer getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(Integer rangeStart) {
		this.rangeStart = rangeStart;
	}

	public Integer getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(Integer rangeEnd) {
		this.rangeEnd = rangeEnd;
	}
	
	

	

}
