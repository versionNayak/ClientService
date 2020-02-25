package com.finlabs.finexa.dto;

import java.util.List;
import java.util.Map;

public class TransactionMISDTO {
	
	private double totalMonthlyInflow;
	private double totalMonthlyOutflow;
	private double netMonthlyInflow;
	private String arn;
	private int id;
	private String monthlySummaryDetails;	
	private String totalMonthlyInflowRow;
	private String totalMonthlyOutflowRow;
	private String netMonthlyInflowRow;
	List<TransactionMISInflowOutflowDTO> inflowList;
	List<TransactionMISInflowOutflowDTO> outflowList;
	Map<String, List<TransactionMISInflowOutflowDTO>> inflowListMap;
	Map<String, List<TransactionMISInflowOutflowDTO>> outflowListMap;
	
	
	public List<TransactionMISInflowOutflowDTO> getOutflowList() {
		return outflowList;
	}
	public void setOutflowList(List<TransactionMISInflowOutflowDTO> outflowList) {
		this.outflowList = outflowList;
	}
	public List<TransactionMISInflowOutflowDTO> getInflowList() {
		return inflowList;
	}
	public void setInflowList(List<TransactionMISInflowOutflowDTO> inflowList) {
		this.inflowList = inflowList;
	}
	
	
	public double getTotalMonthlyInflow() {
		return totalMonthlyInflow;
	}
	public void setTotalMonthlyInflow(double totalMonthlyInflow) {
		this.totalMonthlyInflow = totalMonthlyInflow;
	}
	public double getTotalMonthlyOutflow() {
		return totalMonthlyOutflow;
	}
	public void setTotalMonthlyOutflow(double totalMonthlyOutflow) {
		this.totalMonthlyOutflow = totalMonthlyOutflow;
	}
	public double getNetMonthlyInflow() {
		return netMonthlyInflow;
	}
	public void setNetMonthlyInflow(double netMonthlyInflow) {
		this.netMonthlyInflow = netMonthlyInflow;
	}
	public String getArn() {
		return arn;
	}
	public void setArn(String arn) {
		this.arn = arn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMonthlySummaryDetails() {
		return monthlySummaryDetails;
	}
	public void setMonthlySummaryDetails(String monthlySummaryDetails) {
		this.monthlySummaryDetails = monthlySummaryDetails;
	}
	public Map<String, List<TransactionMISInflowOutflowDTO>> getInflowListMap() {
		return inflowListMap;
	}
	public void setInflowListMap(Map<String, List<TransactionMISInflowOutflowDTO>> inflowListMap) {
		this.inflowListMap = inflowListMap;
	}
	public Map<String, List<TransactionMISInflowOutflowDTO>> getOutflowListMap() {
		return outflowListMap;
	}
	public void setOutflowListMap(Map<String, List<TransactionMISInflowOutflowDTO>> outflowListMap) {
		this.outflowListMap = outflowListMap;
	}
	public String getTotalMonthlyInflowRow() {
		return totalMonthlyInflowRow;
	}
	public void setTotalMonthlyInflowRow(String totalMonthlyInflowRow) {
		this.totalMonthlyInflowRow = totalMonthlyInflowRow;
	}
	public String getTotalMonthlyOutflowRow() {
		return totalMonthlyOutflowRow;
	}
	public void setTotalMonthlyOutflowRow(String totalMonthlyOutflowRow) {
		this.totalMonthlyOutflowRow = totalMonthlyOutflowRow;
	}
	public String getNetMonthlyInflowRow() {
		return netMonthlyInflowRow;
	}
	public void setNetMonthlyInflowRow(String netMonthlyInflowRow) {
		this.netMonthlyInflowRow = netMonthlyInflowRow;
	}
		
	
}
