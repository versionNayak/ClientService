package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientFixedIncomeDTO;



public interface ClientFixedIncomeService {

	/********** For Saving FD/ RD/ Bonds / CPCD *************/
	public ClientFixedIncomeDTO save(ClientFixedIncomeDTO clientFixedIncomeDTO, HttpServletRequest request) throws RuntimeException;
	
	/********** For Finding FD/ RD/ Bonds / CPCD  by Client Id*************/
	public List<ClientFixedIncomeDTO> findByClientId(int clientId) throws RuntimeException;
	
	/********** For Deleting FD/ RD/ Bonds / CPCD *************/
	public int delete(int id) throws RuntimeException;
	
	/********** For Retrieving particular  FD/ RD/ Bonds / CPCD *************/
	public ClientFixedIncomeDTO findById(int id) throws RuntimeException;

	public ClientFixedIncomeDTO update(ClientFixedIncomeDTO clientFixedIncomeDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientFixedIncomeDTO> findAll();

	public void autoSave(ClientFixedIncomeDTO clientFixedIncomeDTO) throws RuntimeException;
	
}
