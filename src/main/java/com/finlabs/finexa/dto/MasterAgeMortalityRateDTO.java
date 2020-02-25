package com.finlabs.finexa.dto;

public class MasterAgeMortalityRateDTO {
	
	private Integer age;
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Double getMortalityRate() {
		return mortalityRate;
	}
	public void setMortalityRate(Double mortalityRate) {
		this.mortalityRate = mortalityRate;
	}
	private Double mortalityRate;
}
