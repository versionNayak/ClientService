package com.finlabs.finexa.dto;

public class FinexaImpactAnalysisDTO {

	private int id;
	private String changes;
	private String controller;
	private String impact;
	private String masterTableName;
	private String method;
	private String moduleName;
	private String serviceImpl;
	private String subModuleName;
	private String repository;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChanges() {
		return changes;
	}

	public void setChanges(String changes) {
		this.changes = changes;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getMasterTableName() {
		return masterTableName;
	}

	public void setMasterTableName(String masterTableName) {
		this.masterTableName = masterTableName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getServiceImpl() {
		return serviceImpl;
	}

	public void setServiceImpl(String serviceImpl) {
		this.serviceImpl = serviceImpl;
	}

	public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	@Override
	public String toString() {
		return "FinexaImpactAnalysisDTO [id=" + id + ", changes=" + changes + ", controller=" + controller + ", impact="
				+ impact + ", masterTableName=" + masterTableName + ", method=" + method + ", moduleName=" + moduleName
				+ ", serviceImpl=" + serviceImpl + ", subModuleName=" + subModuleName + ", repository=" + repository
				+ "]";
	}

}
