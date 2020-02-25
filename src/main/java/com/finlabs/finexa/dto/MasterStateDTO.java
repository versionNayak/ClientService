package com.finlabs.finexa.dto;

import com.finlabs.finexa.model.MasterPincode;

public class MasterStateDTO {

	private int id;

	private String state;

	private String stateCode;

	private MasterPincode masterPincodes;

	public MasterStateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public MasterPincode getMasterPincodes() {
		return masterPincodes;
	}

	public void setMasterPincodes(MasterPincode masterPincodes) {
		this.masterPincodes = masterPincodes;
	}

	@Override
	public String toString() {
		return "MasterStateDTO [id=" + id + ", state=" + state + ", stateCode=" + stateCode + ", masterPincodes="
				+ masterPincodes + "]";
	}

}
