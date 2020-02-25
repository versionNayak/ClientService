package com.finlabs.finexa.dto;

public class LookupGoalFrequencyDTO {
private byte id;

	
	private String description;


	private byte displayOrder;
	
	private int frequency;


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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
}





