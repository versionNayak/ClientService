package com.finlabs.finexa.dto;


public class RiskProfileMasterDTO {
	
	private int id;
	private Byte riskprofileID;
    private String name;
	private  int masterID;
	private double scoreFrom;
	private double  scoreTo;
	private int numberOfRiskProfiles;
	private double maxScore;
	private double minScore;
	private double scoreInterval;
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getAdvisorID() {
		return masterID;
	}
	public double getScoreFrom() {
		return scoreFrom;
	}
	public double getScoreTo() {
		return scoreTo;
	}
	public int getNumberOfRiskProfiles() {
		return numberOfRiskProfiles;
	}
	public double getMaxScore() {
		return maxScore;
	}
	public double getMinScore() {
		return minScore;
	}
	public double getScoreInterval() {
		return scoreInterval;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAdvisorID(int masterID) {
		this.masterID = masterID;
	}
	public void setScoreFrom(double scoreFrom) {
		this.scoreFrom = scoreFrom;
	}
	public void setScoreTo(double scoreTo) {
		this.scoreTo = scoreTo;
	}
	public void setNumberOfRiskProfiles(int numberOfRiskProfiles) {
		this.numberOfRiskProfiles = numberOfRiskProfiles;
	}
	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}
	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}
	public void setScoreInterval(double scoreInterval) {
		this.scoreInterval = scoreInterval;
	}
	public Byte getRiskprofileID() {
		return riskprofileID;
	}
	public void setRiskprofileID(Byte riskprofileID) {
		this.riskprofileID = riskprofileID;
	}
	

	

}
