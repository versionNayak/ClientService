package com.finlabs.finexa.dto;

public class FinexaBusinessFunctionDTO {

	private int id;
	private int subModuleID;
	private String event;
	private String function;
	private String subEvent;
	private FinexaBusinessSubmoduleDTO finexaBusinessSubmodule;
	// private String subModuleName;

	public FinexaBusinessFunctionDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubModuleID() {
		return subModuleID;
	}

	public void setSubModuleID(int subModuleID) {
		this.subModuleID = subModuleID;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getSubEvent() {
		return subEvent;
	}

	public void setSubEvent(String subEvent) {
		this.subEvent = subEvent;
	}

	public FinexaBusinessSubmoduleDTO getFinexaBusinessSubmodule() {
		return finexaBusinessSubmodule;
	}

	public void setFinexaBusinessSubmodule(FinexaBusinessSubmoduleDTO finexaBusinessSubmodule) {
		this.finexaBusinessSubmodule = finexaBusinessSubmodule;
	}

}
