package com.finlabs.finexa.dto;


public class MasterFundManagerDTO {
	
	private int managerCode;
    private String action;
    private String education;
    private String experience;
    private String managerName;
	
    public MasterFundManagerDTO() {
		super();
	}

	public int getManagerCode() {
		return managerCode;
	}

	public void setManagerCode(int managerCode) {
		this.managerCode = managerCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
    
    

}
