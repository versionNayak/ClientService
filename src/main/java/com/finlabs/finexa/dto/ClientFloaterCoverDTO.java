package com.finlabs.finexa.dto;

import java.util.ArrayList;
import java.util.List;

public class ClientFloaterCoverDTO {

	private int id;
	private List<Integer> checkedFamilyMemberID=new ArrayList<>();
	private int familyMemberID;
	private byte insuranceID;

	public ClientFloaterCoverDTO() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public byte getInsuranceID() {
		return insuranceID;
	}

	public void setInsuranceID(byte insuranceID) {
		this.insuranceID = insuranceID;
	}

	public List<Integer> getCheckedFamilyMemberID() {
		return checkedFamilyMemberID;
	}

	public void setCheckedFamilyMemberID(List<Integer> checkedFamilyMemberID) {
		this.checkedFamilyMemberID = checkedFamilyMemberID;
	}

	public void setFamilyMemberID(int familyMemberID) {
		this.familyMemberID = familyMemberID;
	}

	public int getFamilyMemberID() {
		// TODO Auto-generated method stub
		return familyMemberID;
	}

	

	

}
