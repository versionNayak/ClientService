package com.finlabs.finexa.dto;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

public class ClientTransactNACHMandateDTO {
	
	private MultipartFile[] file;
	
	private String encryptedPassword;
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	private BigInteger id;

	private String bseResponse;

	private String bseResponseCode;

	private String clientCode;

	private String createdBy;

	private Date createdOn;

	private String lastUpdatedBy;

	private Timestamp lastUpdatedOn;

	private byte[] nachMandateForm;

	private String saveMode;
	
	private int advisorId;
	
	private int clientId;
	
	private int mandateId;

	public MultipartFile[] getFile() {
		return file;
	}

	public void setFile(MultipartFile[] file) {
		this.file = file;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getBseResponse() {
		return bseResponse;
	}

	public void setBseResponse(String bseResponse) {
		this.bseResponse = bseResponse;
	}

	public String getBseResponseCode() {
		return bseResponseCode;
	}

	public void setBseResponseCode(String bseResponseCode) {
		this.bseResponseCode = bseResponseCode;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public byte[] getNachMandateForm() {
		return nachMandateForm;
	}

	public void setNachMandateForm(byte[] nachMandateForm) {
		this.nachMandateForm = nachMandateForm;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getMandateId() {
		return mandateId;
	}

	public void setMandateId(int mandateId) {
		this.mandateId = mandateId;
	}
}
