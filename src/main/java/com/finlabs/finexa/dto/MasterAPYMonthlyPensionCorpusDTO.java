package com.finlabs.finexa.dto;

public class MasterAPYMonthlyPensionCorpusDTO {

	private int monthlyPension;
	private int corpus;

	public MasterAPYMonthlyPensionCorpusDTO() {
		super();
	}

	public int getMonthlyPension() {
		return monthlyPension;
	}

	public void setMonthlyPension(int monthlyPension) {
		this.monthlyPension = monthlyPension;
	}

	public int getCorpus() {
		return corpus;
	}

	public void setCorpus(int corpus) {
		this.corpus = corpus;
	}

	@Override
	public String toString() {
		return "MasterAPYMonthlyPensionCorpusDTO [monthlyPension=" + monthlyPension + ", corpus=" + corpus + "]";
	}

}
