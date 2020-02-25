package com.finlabs.finexa.dto;

public class LookupTransactHoldingTypeDTO {
	private int id;

	private String description;
	private byte displayOrder;
	private String value;
	
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public byte getDisplayOrder() {
		return displayOrder;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDisplayOrder(byte displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
