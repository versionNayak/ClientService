package com.finlabs.finexa.dto;

public class LookupBucketLogicDTO {
	
	private int id;
	private byte riskProfile;
	private int goalHorizonBucket;
	private int assetAllocationCategory;
	
	public LookupBucketLogicDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(byte riskProfile) {
		this.riskProfile = riskProfile;
	}

	public int getGoalHorizonBucket() {
		return goalHorizonBucket;
	}

	public void setGoalHorizonBucket(int goalHorizonBucket) {
		this.goalHorizonBucket = goalHorizonBucket;
	}

	public int getAssetAllocationCategory() {
		return assetAllocationCategory;
	}

	public void setAssetAllocationCategory(int assetAllocationCategory) {
		this.assetAllocationCategory = assetAllocationCategory;
	}
	
	

}
