package com.finlabs.finexa.dto;


public class LookupCountryDTO {
	private int id;
	private String iso;	
    private String name;
    private String niceName;
    private String iso3;
    private int numCode;
    private int phoneCode;
	public int getId() {
		return id;
	}
	public String getIso() {
		return iso;
	}
	public String getName() {
		return name;
	}
	public String getNiceName() {
		return niceName;
	}
	public String getIso3() {
		return iso3;
	}
	public int getNumCode() {
		return numCode;
	}
	public int getPhoneCode() {
		return phoneCode;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setIso(String iso) {
		this.iso = iso;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNiceName(String niceName) {
		this.niceName = niceName;
	}
	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}
	public void setNumCode(int numCode) {
		this.numCode = numCode;
	}
	public void setPhoneCode(int phoneCode) {
		this.phoneCode = phoneCode;
	}
    
    

}
