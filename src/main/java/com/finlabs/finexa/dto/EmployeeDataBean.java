package com.finlabs.finexa.dto;

import java.util.Collection;

public class EmployeeDataBean {

	private Collection<EmployeeDTO> beanCollection = null;

    public EmployeeDataBean() {
    }

    public Collection<EmployeeDTO> getBeanCollection() {
        return beanCollection;
    }

    public void setBeanCollection(Collection<EmployeeDTO> beanCollection) {
        this.beanCollection = beanCollection;
    }
	
}
