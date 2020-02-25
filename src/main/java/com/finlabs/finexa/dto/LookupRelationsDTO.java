package com.finlabs.finexa.dto;

public class LookupRelationsDTO {
  
private int id;

	
	private String description;


	private byte displayOrder;

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
	
	
}


