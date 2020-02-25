package com.finlabs.finexa.dto;

import java.util.List;
import java.util.Map;

public class TransactionReportDetailedDTO {

	private Map<String,List<TransactionReportColumnDTO>> folioShemeMap;
	private Map<String,List<TransactionSummaryDetailsColumnDTO>> summaryMap;
	
	public Map<String, List<TransactionReportColumnDTO>> getFolioShemeMap() {
		return folioShemeMap;
	}
	public void setFolioShemeMap(Map<String, List<TransactionReportColumnDTO>> folioShemeMap) {
		this.folioShemeMap = folioShemeMap;
	}
	
	public Map<String, List<TransactionSummaryDetailsColumnDTO>> getSummaryMap() {
		return summaryMap;
	}
	public void setSummaryMap(Map<String, List<TransactionSummaryDetailsColumnDTO>> summaryMap) {
		this.summaryMap = summaryMap;
	}
	@Override
	public String toString() {
		return "TransactionReportDetailedDTO [folioShemeMap=" + folioShemeMap + ", summaryMap=" + summaryMap + "]";
	}
	
	
	
	
	
}
