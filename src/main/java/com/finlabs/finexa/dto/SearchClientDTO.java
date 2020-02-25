package com.finlabs.finexa.dto;

import java.util.Arrays;

public class SearchClientDTO {
	private String advisorId;
	private String advMasterId;
	private String advisorLocation;
	
	private String clientMaritalSatusId;
	private String clientCity;
	private String clientGender;
	private String clientRtdFlag;
	private String clientMinAge;
	private String clientMaxAge;
	private String clientOrgName;
	
	private String searchName;
	private String[] searchNameArr;
	private String searchMobile;
	private String searchPan;
	private String searchAadhar;
	private String searchEmail;
	public String getAdvisorId() {
		return advisorId;
	}
	public void setAdvisorId(String advisorId) {
		this.advisorId = advisorId;
	}
	public String getAdvMasterId() {
		return advMasterId;
	}
	public void setAdvMasterId(String advMasterId) {
		this.advMasterId = advMasterId;
	}

	public String getAdvisorLocation() {
		return advisorLocation;
	}
	public void setAdvisorLocation(String advisorLocation) {
		this.advisorLocation = advisorLocation;
	}
	public String getClientMaritalSatusId() {
		return clientMaritalSatusId;
	}
	public void setClientMaritalSatusId(String clientMaritalSatusId) {
		this.clientMaritalSatusId = clientMaritalSatusId;
	}
	public String getClientCity() {
		return clientCity;
	}
	public void setClientCity(String clientCity) {
		this.clientCity = clientCity;
	}
	public String getClientGender() {
		return clientGender;
	}
	public void setClientGender(String clientGender) {
		this.clientGender = clientGender;
	}
	public String getClientRtdFlag() {
		return clientRtdFlag;
	}
	public void setClientRtdFlag(String clientRtdFlag) {
		this.clientRtdFlag = clientRtdFlag;
	}
	public String getClientMinAge() {
		return clientMinAge;
	}
	public void setClientMinAge(String clientMinAge) {
		this.clientMinAge = clientMinAge;
	}
	public String getClientMaxAge() {
		return clientMaxAge;
	}
	public void setClientMaxAge(String clientMaxAge) {
		this.clientMaxAge = clientMaxAge;
	}
	public String getClientOrgName() {
		return clientOrgName;
	}
	public void setClientOrgName(String clientOrgName) {
		this.clientOrgName = clientOrgName;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public String[] getSearchNameArr() {
		return searchNameArr;
	}
	public void setSearchNameArr(String[] searchNameArr) {
		this.searchNameArr = searchNameArr;
	}
	public String getSearchMobile() {
		return searchMobile;
	}
	public void setSearchMobile(String searchMobile) {
		this.searchMobile = searchMobile;
	}
	public String getSearchPan() {
		return searchPan;
	}
	public void setSearchPan(String searchPan) {
		this.searchPan = searchPan;
	}
	public String getSearchAadhar() {
		return searchAadhar;
	}
	public void setSearchAadhar(String searchAadhar) {
		this.searchAadhar = searchAadhar;
	}
	public String getSearchEmail() {
		return searchEmail;
	}
	public void setSearchEmail(String searchEmail) {
		this.searchEmail = searchEmail;
	}
	
	@Override
	public String toString() {
		return "SearchClientDTO [advisorId=" + advisorId + ", advMasterId=" + advMasterId + ", advisorLocation="
				+ advisorLocation + ", clientMaritalSatusId=" + clientMaritalSatusId + ", clientCity=" + clientCity
				+ ", clientGender=" + clientGender + ", clientRtdFlag=" + clientRtdFlag + ", clientMinAge="
				+ clientMinAge + ", clientMaxAge=" + clientMaxAge + ", clientOrgName=" + clientOrgName + ", searchName="
				+ searchName + ", searchNameArr=" + Arrays.toString(searchNameArr) + ", searchMobile=" + searchMobile
				+ ", searchPan=" + searchPan + ", searchAadhar=" + searchAadhar + ", searchEmail=" + searchEmail + "]";
	}
	
	

}
