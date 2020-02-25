package com.finlabs.finexa.dto;


public class LookupMonthDTO {


	
	private byte id;

	private String description;

	private byte displayOrder;

	public byte getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public byte getDisplayOrder() {
		return displayOrder;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplayOrder(byte displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	

}
