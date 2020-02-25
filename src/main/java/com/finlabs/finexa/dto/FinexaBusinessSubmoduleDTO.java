package com.finlabs.finexa.dto;

import java.util.List;

public class FinexaBusinessSubmoduleDTO {
	private int id;
	private String code;
	private String description;
	private FinexaBusinessModuleDTO finexaBusinessModule;
	private List<AdvisorRoleSubmoduleMappingDTO> advisorRoleSubmoduleMappings;
	//private List<FinexaBusinessFunctionDTO> finexaBusinessFunctions;
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
	public FinexaBusinessModuleDTO getFinexaBusinessModule() {
		return finexaBusinessModule;
	}
	public void setFinexaBusinessModule(FinexaBusinessModuleDTO finexaBusinessModule) {
		this.finexaBusinessModule = finexaBusinessModule;
	}
	public List<AdvisorRoleSubmoduleMappingDTO> getAdvisorRoleSubmoduleMappings() {
		return advisorRoleSubmoduleMappings;
	}
	public void setAdvisorRoleSubmoduleMappings(List<AdvisorRoleSubmoduleMappingDTO> advisorRoleSubmoduleMappings) {
		this.advisorRoleSubmoduleMappings = advisorRoleSubmoduleMappings;
	}
	
	
}
