package com.finlabs.finexa.dto;

import java.util.List;

public class ClientFamilyIncomeListDTO {
	private List<ClientFamilyIncomeDTO> clientFamilyIncomeList ;

	public ClientFamilyIncomeListDTO() {
		super();
	}

	public List<ClientFamilyIncomeDTO> getClientFamilyIncomeList() {
		return clientFamilyIncomeList;
	}

	public void setClientFamilyIncomeList(List<ClientFamilyIncomeDTO> clientFamilyIncomeList) {
		this.clientFamilyIncomeList = clientFamilyIncomeList;
	}
	
	
}
