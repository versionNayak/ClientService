package com.finlabs.finexa.dto;

public class MasterExpectedHistoricalReturnDTO {
	
	private int id;
    private double returnRate;
    private int year;
    private byte subAssetClassName;
    private byte assetClassName;
	
    public MasterExpectedHistoricalReturnDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(double returnRate) {
		this.returnRate = returnRate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public byte getSubAssetClassName() {
		return subAssetClassName;
	}

	public void setSubAssetClassName(byte subAssetClassName) {
		this.subAssetClassName = subAssetClassName;
	}

	public byte getAssetClassName() {
		return assetClassName;
	}

	public void setAssetClassName(byte assetClassName) {
		this.assetClassName = assetClassName;
	}
    
    

}
