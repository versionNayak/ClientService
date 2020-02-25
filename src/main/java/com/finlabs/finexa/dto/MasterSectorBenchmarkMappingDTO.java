package com.finlabs.finexa.dto;

public class MasterSectorBenchmarkMappingDTO {
	
	private int id;
	private byte sectorID;
	private Integer benchmarkIndex;

	public MasterSectorBenchmarkMappingDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getSectorID() {
		return sectorID;
	}

	public void setSectorID(byte sectorID) {
		this.sectorID = sectorID;
	}

	public Integer getBenchmarkIndex() {
		return benchmarkIndex;
	}

	public void setBenchmarkIndex(Integer benchmarkIndex) {
		this.benchmarkIndex = benchmarkIndex;
	}
	
	

}
