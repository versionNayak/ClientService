package com.finlabs.finexa.dto;

import java.util.List;

public class AccessRightDTO {
	 private int id;
	 private String accessRight;
	 private List<String> accessRights;
	 private String addeditAllowedFlag;
	 private String deleteAllowedFlag;
	 private String viewAllowedFlag;
	
	 
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
	}

	public List<String> getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(List<String> accessRights) {
		this.accessRights = accessRights;
	}

	public String getAddeditAllowedFlag() {
		return addeditAllowedFlag;
	}

	public void setAddeditAllowedFlag(String addeditAllowedFlag) {
		this.addeditAllowedFlag = addeditAllowedFlag;
	}

	public String getDeleteAllowedFlag() {
		return deleteAllowedFlag;
	}

	public void setDeleteAllowedFlag(String deleteAllowedFlag) {
		this.deleteAllowedFlag = deleteAllowedFlag;
	}

	public String getViewAllowedFlag() {
		return viewAllowedFlag;
	}

	public void setViewAllowedFlag(String viewAllowedFlag) {
		this.viewAllowedFlag = viewAllowedFlag;
	}
	
}
