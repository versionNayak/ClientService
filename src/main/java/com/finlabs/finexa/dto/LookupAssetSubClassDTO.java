package com.finlabs.finexa.dto;



public class LookupAssetSubClassDTO {
	
	private byte id;
    private String description;
    private byte assetClass;
	
    public LookupAssetSubClassDTO() {
		super();
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(byte assetClass) {
		this.assetClass = assetClass;
	}
    
    

}
