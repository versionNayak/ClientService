package com.finlabs.finexa.dto;

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

public class TransactionMasterBODTO {
	
	private int nameRTA;
	private int nameFileType;
	private int advisorID;
	private MultipartFile[] nameSelectFile;
	private int status;
	public int getNameRTA() {
		return nameRTA;
	}
	public void setNameRTA(int nameRTA) {
		this.nameRTA = nameRTA;
	}
	public int getNameFileType() {
		return nameFileType;
	}
	public void setNameFileType(int nameFileType) {
		this.nameFileType = nameFileType;
	}
	public int getAdvisorID() {
		return advisorID;
	}
	public void setAdvisorID(int advisorID) {
		this.advisorID = advisorID;
	}
	public MultipartFile[] getNameSelectFile() {
		return nameSelectFile;
	}
	public void setNameSelectFile(MultipartFile[] nameSelectFile) {
		this.nameSelectFile = nameSelectFile;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "TransactionMasterBODTO [nameRTA=" + nameRTA + ", nameFileType=" + nameFileType + ", advisorID="
				+ advisorID + ", nameSelectFile=" + Arrays.toString(nameSelectFile) + ", status=" + status + "]";
	}
	
	
	
}
