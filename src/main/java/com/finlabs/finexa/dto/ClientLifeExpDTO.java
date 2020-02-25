package com.finlabs.finexa.dto;

public class ClientLifeExpDTO {

	private int memberId;
	private int relationId;
	private String memberGender;
	private String memberFirstName;
	private String memberName;
	private String memberRelation;
	private int lifeExp;
	private String relationName;
	
	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberRelation() {
		return memberRelation;
	}
	public void setMemberRelation(String memberRelation) {
		this.memberRelation = memberRelation;
	}
	public int getLifeExp() {
		return lifeExp;
	}
	public void setLifeExp(int lifeExp) {
		this.lifeExp = lifeExp;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getRelationId() {
		return relationId;
	}
	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}
	public String getMemberGender() {
		return memberGender;
	}
	public void setMemberGender(String memberGender) {
		this.memberGender = memberGender;
	}
	public String getMemberFirstName() {
		return memberFirstName;
	}
	public void setMemberFirstName(String memberFirstName) {
		this.memberFirstName = memberFirstName;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	
	
}
