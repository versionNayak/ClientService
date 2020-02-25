package com.finlabs.finexa.dto;

public class LookupFundCategoryDTO {
	
	private byte id;
    private String description;
    private byte displayOrder;

	public LookupFundCategoryDTO() {
	}

	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(byte displayOrder) {
		this.displayOrder = displayOrder;
	}

}
