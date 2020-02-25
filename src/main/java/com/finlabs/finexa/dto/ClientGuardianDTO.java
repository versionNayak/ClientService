package com.finlabs.finexa.dto;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientGuardianDTO {
	private int id;
    private String salutation;
    //@NotEmpty(message="firstName can not be Empty")
    private String firstName;
    private String middleName;
    private String lastName;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	//@NotEmpty(message="birthDate can not be Empty")
    private Date birthDate;
	//@NotEmpty(message="gender can not be Empty")
    private String gender;
	//@NotEmpty(message="pan can not be Empty")
    private String pan;
    private long aadhar;
    private int residentType;
    private String otherResidentType;
    private Integer countryOfResidence;
    private int clientID;
    private ClientGuardianContactDTO clientGuardianContactDTO;
	
	
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public long getAadhar() {
		return aadhar;
	}
	public void setAadhar(long aadhar) {
		this.aadhar = aadhar;
	}
	public int getResidentType() {
		return residentType;
	}
	public void setResidentType(int residentType) {
		this.residentType = residentType;
	}
	public String getOtherResidentType() {
		return otherResidentType;
	}
	public void setOtherResidentType(String otherResidentType) {
		this.otherResidentType = otherResidentType;
	}
	public Integer getCountryOfResidence() {
		return countryOfResidence;
	}
	public void setCountryOfResidence(Integer countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ClientGuardianContactDTO getClientGuardianContactDTO() {
		return clientGuardianContactDTO;
	}
	public void setClientGuardianContactDTO(ClientGuardianContactDTO clientGuardianContactDTO) {
		this.clientGuardianContactDTO = clientGuardianContactDTO;
	}
	@Override
	public String toString() {
		return "ClientGuardianDTO [id=" + id + ", salutation=" + salutation + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName + ", birthDate=" + birthDate + ", gender="
				+ gender + ", pan=" + pan + ", aadhar=" + aadhar + ", residentType=" + residentType
				+ ", otherResidentType=" + otherResidentType + ", countryOfResidence=" + countryOfResidence
				+ ", clientID=" + clientID + ", clientGuardianContactDTO=" + clientGuardianContactDTO + "]";
	}
	
	
	
    
}
