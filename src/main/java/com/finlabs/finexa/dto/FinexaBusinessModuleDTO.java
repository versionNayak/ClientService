package com.finlabs.finexa.dto;

import java.util.List;



public class FinexaBusinessModuleDTO {
	private int id;
	private String code;
	private String description;
	private List<FinexaBusinessSubmoduleDTO> subModuleList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<FinexaBusinessSubmoduleDTO> getSubModuleList() {
		return subModuleList;
	}
	public void setSubModuleList(List<FinexaBusinessSubmoduleDTO> subModuleList) {
		this.subModuleList = subModuleList;
	}
	
	@Override
	public String toString() {
		return "FinexaBusinessModuleDTO [id=" + id + ", code=" + code + ", description=" + description + "]";
	} 
	
	
	
}
