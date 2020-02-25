package com.finlabs.finexa.dto;

import java.util.List;

public class ClientExpenseListDTO {
	private List<ClientExpenseDTO> clientExpenseList;

	public ClientExpenseListDTO() {
		super();
	}

	public List<ClientExpenseDTO> getClientExpenseList() {
		return clientExpenseList;
	}

	public void setClientExpenseList(List<ClientExpenseDTO> clientExpenseList) {
		this.clientExpenseList = clientExpenseList;
	}
	
	
}
