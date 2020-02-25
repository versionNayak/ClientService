package com.finlabs.finexa.dto;

public class FormDataDTO {
	int id;
	String fileFormat;
	String userName;
	int advUserId;
	int loggedUserid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAdvUserId() {
		return advUserId;
	}

	public void setAdvUserId(int advUserId) {
		this.advUserId = advUserId;
	}

	public int getLoggedUserid() {
		return loggedUserid;
	}

	public void setLoggedUserid(int loggedUserid) {
		this.loggedUserid = loggedUserid;
	}

	@Override
	public String toString() {
		return "FormDataDTO [id=" + id + ", fileFormat=" + fileFormat + ", userName=" + userName + ", advUserId="
				+ advUserId + ", loggedUserid=" + loggedUserid + "]";
	}

}
