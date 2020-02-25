package com.finlabs.finexa.dto;



public class MasterInsuranceCompanyNameDTO {
	
	private int id;
	private String description;
	private Integer displayOrder;
	
	public MasterInsuranceCompanyNameDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}
