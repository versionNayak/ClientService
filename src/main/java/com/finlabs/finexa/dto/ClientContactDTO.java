package com.finlabs.finexa.dto;

import java.math.BigInteger;


import javax.validation.constraints.NotNull;



public class ClientContactDTO {
	private int id;
	private String alternateEmail;
	private String correspondenceAddressLine1;
	private String correspondenceAddressLine2;
	private String correspondenceAddressLine3;
	private String correspondenceCity;
	private int correspondencePincode;
	private String correspondenceState;
    private int address3DropId;
    @NotNull(message = "countryCode must not be null")
    private String countryCode;
	@NotNull(message = "emailID must not be null")
	private String emailID;
    @NotNull(message="Emergency contact should not be Empty ")
    private BigInteger emergencyContact;
	@NotNull(message = "mobile must not be null")
	private BigInteger mobile;
	private String officeAddressLine1;
	private String officeAddressLine2;
	private String officeAddressLine3;
	private String officeCity;
	private int officePincode;
	private String officeState;
	private int address1DropId;
	private String permanentAddressLine1;
	private String permanentAddressLine2;
	private String permanentAddressLine3;
	private String permanentCity;
	private int permanentPincode;
	private String permanentState;
	private int address2DropId;
	private BigInteger phone;
	private int clientId;
	private int lookupOfficeCountryId;
	private String lookupOfficeCountryName;
	private int lookupPermanentCountryId;
	private String lookupPermanentCountryName;
	private int lookupCorrespondenceCountryId;
	private String lookupCorrespondenceCountryName;

	public ClientContactDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
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

	public int getCorrespondencePincode() {
		return correspondencePincode;
	}

	public void setCorrespondencePincode(int correspondencePincode) {
		this.correspondencePincode = correspondencePincode;
	}

	public String getCorrespondenceState() {
		return correspondenceState;
	}

	public void setCorrespondenceState(String correspondenceState) {
		this.correspondenceState = correspondenceState;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public BigInteger getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(BigInteger emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public BigInteger getMobile() {
		return mobile;
	}

	public void setMobile(BigInteger mobile) {
		this.mobile = mobile;
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

	public int getOfficePincode() {
		return officePincode;
	}

	public void setOfficePincode(int officePincode) {
		this.officePincode = officePincode;
	}

	public String getOfficeState() {
		return officeState;
	}

	public void setOfficeState(String officeState) {
		this.officeState = officeState;
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

	public int getPermanentPincode() {
		return permanentPincode;
	}

	public void setPermanentPincode(int permanentPincode) {
		this.permanentPincode = permanentPincode;
	}

	public String getPermanentState() {
		return permanentState;
	}

	public void setPermanentState(String permanentState) {
		this.permanentState = permanentState;
	}

	public BigInteger getPhone() {
		return phone;
	}

	public void setPhone(BigInteger phone) {
		this.phone = phone;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

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

	public int getAddress3DropId() {
		return address3DropId;
	}

	public void setAddress3DropId(int address3DropId) {
		this.address3DropId = address3DropId;
	}

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
	
	

	public String getLookupOfficeCountryName() {
		return lookupOfficeCountryName;
	}

	public void setLookupOfficeCountryName(String lookupOfficeCountryName) {
		this.lookupOfficeCountryName = lookupOfficeCountryName;
	}

	public String getLookupPermanentCountryName() {
		return lookupPermanentCountryName;
	}

	public void setLookupPermanentCountryName(String lookupPermanentCountryName) {
		this.lookupPermanentCountryName = lookupPermanentCountryName;
	}

	public String getLookupCorrespondenceCountryName() {
		return lookupCorrespondenceCountryName;
	}

	public void setLookupCorrespondenceCountryName(String lookupCorrespondenceCountryName) {
		this.lookupCorrespondenceCountryName = lookupCorrespondenceCountryName;
	}

	@Override
	public String toString() {
		return "ClientContactDTO [id=" + id + ", alternateEmail=" + alternateEmail + ", correspondenceAddressLine1="
				+ correspondenceAddressLine1 + ", correspondenceAddressLine2=" + correspondenceAddressLine2
				+ ", correspondenceAddressLine3=" + correspondenceAddressLine3 + ", correspondenceCity="
				+ correspondenceCity + ", correspondencePincode=" + correspondencePincode + ", correspondenceState="
				+ correspondenceState + ", address3DropId=" + address3DropId + ", countryCode=" + countryCode
				+ ", emailID=" + emailID + ", emergencyContact=" + emergencyContact + ", mobile=" + mobile
				+ ", officeAddressLine1=" + officeAddressLine1 + ", officeAddressLine2=" + officeAddressLine2
				+ ", officeAddressLine3=" + officeAddressLine3 + ", officeCity=" + officeCity + ", officePincode="
				+ officePincode + ", officeState=" + officeState + ", address1DropId=" + address1DropId
				+ ", permanentAddressLine1=" + permanentAddressLine1 + ", permanentAddressLine2="
				+ permanentAddressLine2 + ", permanentAddressLine3=" + permanentAddressLine3 + ", permanentCity="
				+ permanentCity + ", permanentPincode=" + permanentPincode + ", permanentState=" + permanentState
				+ ", address2DropId=" + address2DropId + ", phone=" + phone + ", clientId=" + clientId
				+ ", lookupOfficeCountryId=" + lookupOfficeCountryId + ", lookupPermanentCountryId="
				+ lookupPermanentCountryId + ", lookupCorrespondenceCountryId=" + lookupCorrespondenceCountryId + "]";
	}


	
	

}
