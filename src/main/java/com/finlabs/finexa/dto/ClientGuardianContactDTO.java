package com.finlabs.finexa.dto;

import java.math.BigInteger;

public class ClientGuardianContactDTO {
	
	private int id;
	
    private String officeAddressLine1;
	
    private String officeAddressLine2;
	
    private String officeAddressLine3;
	
    private String officeCity;
    
    private String officeState;
    
    private int officePincode;
    
    private Integer officeCountry;
    
    private int address1DropId;
    
    public int getAddress1DropId() {
		return address1DropId;
	}

	public void setAddress1DropId(int address1DropId) {
		this.address1DropId = address1DropId;
	}

	public int getAddress2DropId() {
		return address2DropId;
	}

	public void setAddress2DropId(int address2DropId) {
		this.address2DropId = address2DropId;
	}

	public int getAddress3DropId() {
		return address3DropId;
	}

	public void setAddress3DropId(int address3DropId) {
		this.address3DropId = address3DropId;
	}

	private String permanentAddressLine1;
    
    private String permanentAddressLine2;
    
    private String permanentAddressLine3;
    
    private String permanentCity;
    
    private String permanentState;
    
    private int permanentPincode;
    
    private Integer permanentCountry;
    
    private int address2DropId;
    
    private String correspondenceAddressLine1;
    
    private String correspondenceAddressLine2;
    
    private String correspondenceAddressLine3;
    
    private String correspondenceCity;
    
    private String correspondenceState;
    
    private int correspondencePincode;
    
    private Integer correspondenceCountry;
    
    private int address3DropId;
    
    private String emailID;
    
    private String countryCode;
    
    private long mobile;
    
    private BigInteger phone;
    
    private String alternateEmail;
    
    private long emergencyContactNo;
    
    private int guardianID;
    
	private int lookupOfficeCountryId;
	private int lookupPermanentCountryId;
	private int lookupCorrespondenceCountryId;

	public int getLookupOfficeCountryId() {
		return lookupOfficeCountryId;
	}

	public void setLookupOfficeCountryId(int lookupOfficeCountryId) {
		this.lookupOfficeCountryId = lookupOfficeCountryId;
	}

	public int getLookupPermanentCountryId() {
		return lookupPermanentCountryId;
	}

	public void setLookupPermanentCountryId(int lookupPermanentCountryId) {
		this.lookupPermanentCountryId = lookupPermanentCountryId;
	}

	public int getLookupCorrespondenceCountryId() {
		return lookupCorrespondenceCountryId;
	}

	public void setLookupCorrespondenceCountryId(int lookupCorrespondenceCountryId) {
		this.lookupCorrespondenceCountryId = lookupCorrespondenceCountryId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOfficeAddressLine1() {
		return officeAddressLine1;
	}

	public void setOfficeAddressLine1(String officeAddressLine1) {
		this.officeAddressLine1 = officeAddressLine1;
	}

	public String getOfficeAddressLine2() {
		return officeAddressLine2;
	}

	public void setOfficeAddressLine2(String officeAddressLine2) {
		this.officeAddressLine2 = officeAddressLine2;
	}

	public String getOfficeAddressLine3() {
		return officeAddressLine3;
	}

	public void setOfficeAddressLine3(String officeAddressLine3) {
		this.officeAddressLine3 = officeAddressLine3;
	}

	public String getOfficeCity() {
		return officeCity;
	}

	public void setOfficeCity(String officeCity) {
		this.officeCity = officeCity;
	}

	public String getOfficeState() {
		return officeState;
	}

	public void setOfficeState(String officeState) {
		this.officeState = officeState;
	}

	public int getOfficePincode() {
		return officePincode;
	}

	public void setOfficePincode(int officePincode) {
		this.officePincode = officePincode;
	}

	public Integer getOfficeCountry() {
		return officeCountry;
	}

	public void setOfficeCountry(Integer officeCountry) {
		this.officeCountry = officeCountry;
	}

	public String getPermanentAddressLine1() {
		return permanentAddressLine1;
	}

	public void setPermanentAddressLine1(String permanentAddressLine1) {
		this.permanentAddressLine1 = permanentAddressLine1;
	}

	public String getPermanentAddressLine2() {
		return permanentAddressLine2;
	}

	public void setPermanentAddressLine2(String permanentAddressLine2) {
		this.permanentAddressLine2 = permanentAddressLine2;
	}

	public String getPermanentAddressLine3() {
		return permanentAddressLine3;
	}

	public void setPermanentAddressLine3(String permanentAddressLine3) {
		this.permanentAddressLine3 = permanentAddressLine3;
	}

	public String getPermanentCity() {
		return permanentCity;
	}

	public void setPermanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
	}

	public String getPermanentState() {
		return permanentState;
	}

	public void setPermanentState(String permanentState) {
		this.permanentState = permanentState;
	}

	public int getPermanentPincode() {
		return permanentPincode;
	}

	public void setPermanentPincode(int permanentPincode) {
		this.permanentPincode = permanentPincode;
	}

	public Integer getPermanentCountry() {
		return permanentCountry;
	}

	public void setPermanentCountry(Integer permanentCountry) {
		this.permanentCountry = permanentCountry;
	}

	public String getCorrespondenceAddressLine1() {
		return correspondenceAddressLine1;
	}

	public void setCorrespondenceAddressLine1(String correspondenceAddressLine1) {
		this.correspondenceAddressLine1 = correspondenceAddressLine1;
	}

	public String getCorrespondenceAddressLine2() {
		return correspondenceAddressLine2;
	}

	public void setCorrespondenceAddressLine2(String correspondenceAddressLine2) {
		this.correspondenceAddressLine2 = correspondenceAddressLine2;
	}

	public String getCorrespondenceAddressLine3() {
		return correspondenceAddressLine3;
	}

	public void setCorrespondenceAddressLine3(String correspondenceAddressLine3) {
		this.correspondenceAddressLine3 = correspondenceAddressLine3;
	}

	public String getCorrespondenceCity() {
		return correspondenceCity;
	}

	public void setCorrespondenceCity(String correspondenceCity) {
		this.correspondenceCity = correspondenceCity;
	}

	public String getCorrespondenceState() {
		return correspondenceState;
	}

	public void setCorrespondenceState(String correspondenceState) {
		this.correspondenceState = correspondenceState;
	}

	public int getCorrespondencePincode() {
		return correspondencePincode;
	}

	public void setCorrespondencePincode(int correspondencePincode) {
		this.correspondencePincode = correspondencePincode;
	}

	public Integer getCorrespondenceCountry() {
		return correspondenceCountry;
	}

	public void setCorrespondenceCountry(Integer correspondenceCountry) {
		this.correspondenceCountry = correspondenceCountry;
	}

	

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	
	public BigInteger getPhone() {
		return phone;
	}

	public void setPhone(BigInteger phone) {
		this.phone = phone;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	
	public long getEmergencyContactNo() {
		return emergencyContactNo;
	}

	public void setEmergencyContactNo(long emergencyContactNo) {
		this.emergencyContactNo = emergencyContactNo;
	}

	public int getGuardianID() {
		return guardianID;
	}

	public void setGuardianID(int guardianID) {
		this.guardianID = guardianID;
	}

	
    
    
    
}
