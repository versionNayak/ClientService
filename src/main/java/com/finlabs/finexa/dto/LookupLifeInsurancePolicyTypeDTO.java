package com.finlabs.finexa.dto;


public class LookupLifeInsurancePolicyTypeDTO {

	private byte id;
	private String description;
	private Byte displayOrder;
	
	public LookupLifeInsurancePolicyTypeDTO() {
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

	public Byte getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Byte displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	
	
}
