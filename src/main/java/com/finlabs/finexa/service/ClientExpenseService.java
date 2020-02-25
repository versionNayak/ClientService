package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientExpenseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;

public interface ClientExpenseService {


	public void save(List<ClientExpenseDTO> clientExpenseList, HttpServletRequest request) throws RuntimeException, CustomFinexaException;
	
	public void update(List<ClientExpenseDTO> clientExpenseList, HttpServletRequest request) throws RuntimeException, CustomFinexaException;

	public int deleteClientExpense(Integer clientId) throws RuntimeException;

	/*public void delete(int id);*/

	public ClientExpenseDTO getAllExpense(Integer clientId) throws RuntimeException;

	public boolean checkIfExpensePresent(Integer clientId);

	public List<ClientExpenseDTO> viewAllExpense(Integer clientId);

	public boolean getExpenceByClientID(Integer cliendID) throws CustomFinexaException;

	public ClientExpenseDTO autoSave(ClientExpenseDTO clientExpenseDto) throws RuntimeException, CustomFinexaException;


}
