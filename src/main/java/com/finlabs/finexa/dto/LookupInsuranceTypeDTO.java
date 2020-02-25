package com.finlabs.finexa.dto;

public class LookupInsuranceTypeDTO {
	private byte id;

	private String description;

	private Byte displayOrder;

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

	public Byte getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Byte displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	
	
}
