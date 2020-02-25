package com.finlabs.finexa.dto;


public class MasterProductTypeDTO {
	
	private byte id;
    private String productType;
	
    public MasterProductTypeDTO() {
		super();
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
    
    

}
