package com.finlabs.finexa.dto;

public class FamilyAttributeMasterDTO {
	
	private int clientId;
	private int advUserBranchId;
	private int advUserRMId;
	private int advUserSBId;
	private String status;
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getAdvUserBranchId() {
		return advUserBranchId;
	}
	public void setAdvUserBranchId(int advUserBranchId) {
		this.advUserBranchId = advUserBranchId;
	}
	public int getAdvUserRMId() {
		return advUserRMId;
	}
	public void setAdvUserRMId(int advUserRMId) {
		this.advUserRMId = advUserRMId;
	}
	public int getAdvUserSBId() {
		return advUserSBId;
	}
	public void setAdvUserSBId(int advUserSBId) {
		this.advUserSBId = advUserSBId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "FamilyAttributeMasterDTO [clientId=" + clientId + ", advUserBranchId=" + advUserBranchId
				+ ", advUserRMId=" + advUserRMId + ", advUserSBId=" + advUserSBId + ", status=" + status + "]";
	}
	
	
	
	

}
