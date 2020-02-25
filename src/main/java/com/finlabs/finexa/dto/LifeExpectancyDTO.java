package com.finlabs.finexa.dto;


import java.util.Date;


import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LifeExpectancyDTO {
	    private int familyMemberId;
	    
	    private int id;
	    @NotNull(message = "gender cannot be null")
	    private String gender;
	    @NotNull(message = "annualIncome cannot be null")
	    private double annualIncome;
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
		@NotNull(message = "birthDate cannot be null")
		private Date birthDate;
		@NotNull(message = "isTobaccoUser cannot be null")
	    private String isTobaccoUser;
		@NotNull(message = "isProperBMI cannot be null")
		private String isProperBMI;
		@NotNull(message = "hasDiseaseHistory cannot be null")
		private String hasDiseaseHistory;
		@NotNull(message = "hasNormalBP cannot be null")
		private String hasNormalBP;
		
		private Byte totalLifeExpectancy;
		
		private int futureLifeExpectancy;

		
		

		public int getFamilyMemberId() {
			return familyMemberId;
		}

		public void setFamilyMemberId(int familyMemberId) {
			this.familyMemberId = familyMemberId;
		}

		

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public double getAnnualIncome() {
			return annualIncome;
		}

		public void setAnnualIncome(double annualIncome) {
			this.annualIncome = annualIncome;
		}

		public Date getBirthDate() {
			return birthDate;
		}

		public void setBirthDate(Date birthDate) {
			this.birthDate = birthDate;
		}

		public String getIsTobaccoUser() {
			return isTobaccoUser;
		}

		public void setIsTobaccoUser(String isTobaccoUser) {
			this.isTobaccoUser = isTobaccoUser;
		}

		public String getIsProperBMI() {
			return isProperBMI;
		}

		public void setIsProperBMI(String isProperBMI) {
			this.isProperBMI = isProperBMI;
		}

		public String getHasDiseaseHistory() {
			return hasDiseaseHistory;
		}

		public void setHasDiseaseHistory(String hasDiseaseHistory) {
			this.hasDiseaseHistory = hasDiseaseHistory;
		}

		public String getHasNormalBP() {
			return hasNormalBP;
		}

		public void setHasNormalBP(String hasNormalBP) {
			this.hasNormalBP = hasNormalBP;
		}

		public Byte getTotalLifeExpectancy() {
			return totalLifeExpectancy;
		}

		public void setTotalLifeExpectancy(Byte totalLifeExpectancy) {
			this.totalLifeExpectancy = totalLifeExpectancy;
		}

		public int getFutureLifeExpectancy() {
			return futureLifeExpectancy;
		}

		public void setFutureLifeExpectancy(int futureLifeExpectancy) {
			this.futureLifeExpectancy = futureLifeExpectancy;
		}

		
	

}