package com.finlabs.finexa.dto;

public class EmployeeDTO {
	
	
	 private int empNo;
	 private String name;    
	 private int salary;
	 private float commission;
	 
	 public EmployeeDTO() {
	 
	 }
	 
    public EmployeeDTO(int empNo, String name, int salary, float commission) {
        this.empNo = empNo;
        this.name = name;        
        this.salary = salary;
        this.commission = salary*commission;
    }

	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public float getCommission() {
		return commission;
	}

	public void setCommission(float commission) {
		this.commission = commission;
	}
    
    
	    

}
