package com.finlabs.finexa.dto;

public class MasterPincodeDTO {

	private int id;

	private String district;

	private int pincode;

	private int stateID;

	public MasterPincodeDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public int getStateID() {
		return stateID;
	}

	public void setStateID(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public String toString() {
		return "MasterPincodeDTO [id=" + id + ", district=" + district + ", pincode=" + pincode + ", stateID=" + stateID
				+ "]";
	}

}
