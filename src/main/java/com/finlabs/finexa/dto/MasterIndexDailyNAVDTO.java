package com.finlabs.finexa.dto;

import java.util.Date;


public class MasterIndexDailyNAVDTO {
	
	private int id;
	private Integer indexName;
    private Date date;
    private double nav;
	
    public MasterIndexDailyNAVDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getIndexName() {
		return indexName;
	}

	public void setIndexName(Integer indexName) {
		this.indexName = indexName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getNav() {
		return nav;
	}

	public void setNav(double nav) {
		this.nav = nav;
	}
    
    


}
