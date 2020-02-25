package com.finlabs.finexa.dto;



public class ViewClientUCCDetailsDTO {
	
	private int id;
	private String clientCode;
	private String clientAppName1;
	private String clientPan;
	private String clientAppName2;
	private String clientPan2;
	private String clientAppName3;
	private String clientPan3;
	private boolean fatcaStatus;
	private boolean mandateStatus;
	private boolean aofStatus;
	private boolean isPhysial;
	private String clientGuardian;
	private String clientGuardianPan;
	
	public boolean isPhysial() {
		return isPhysial;
	}
	public void setPhysial(boolean isPhysial) {
		this.isPhysial = isPhysial;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientAppName1() {
		return clientAppName1;
	}
	public void setClientAppName1(String clientAppName1) {
		this.clientAppName1 = clientAppName1;
	}
	public String getClientAppName2() {
		return clientAppName2;
	}
	public void setClientAppName2(String clientAppName2) {
		this.clientAppName2 = clientAppName2;
	}
	public String getClientAppName3() {
		return clientAppName3;
	}
	public void setClientAppName3(String clientAppName3) {
		this.clientAppName3 = clientAppName3;
	}
	public boolean isFatcaStatus() {
		return fatcaStatus;
	}
	public void setFatcaStatus(boolean fatcaStatus) {
		this.fatcaStatus = fatcaStatus;
	}
	public boolean isMandateStatus() {
		return mandateStatus;
	}
	public void setMandateStatus(boolean mandateStatus) {
		this.mandateStatus = mandateStatus;
	}
	public boolean isAofStatus() {
		return aofStatus;
	}
	public void setAofStatus(boolean aofStatus) {
		this.aofStatus = aofStatus;
	}
	public String getClientPan() {
		return clientPan;
	}
	public void setClientPan(String clientPan) {
		this.clientPan = clientPan;
	}
	public String getClientPan2() {
		return clientPan2;
	}
	public void setClientPan2(String clientPan2) {
		this.clientPan2 = clientPan2;
	}
	public String getClientPan3() {
		return clientPan3;
	}
	public void setClientPan3(String clientPan3) {
		this.clientPan3 = clientPan3;
	}
	public String getClientGuardian() {
		return clientGuardian;
	}
	public void setClientGuardian(String clientGuardian) {
		this.clientGuardian = clientGuardian;
	}
	public String getClientGuardianPan() {
		return clientGuardianPan;
	}
	public void setClientGuardianPan(String clientGuardianPan) {
		this.clientGuardianPan = clientGuardianPan;
	}
}
