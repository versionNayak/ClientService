package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientLoanDTO;

public interface ClientLoanService {
	
	public ClientLoanDTO save(ClientLoanDTO clientLoanDTO) throws RuntimeException;
    
	public ClientLoanDTO update(ClientLoanDTO clientLoanDTO) throws RuntimeException;

	public ClientLoanDTO findById(int id) throws RuntimeException;
	
	public List<ClientLoanDTO> findAll();
	
	public List<ClientLoanDTO> findByClientId(int clientId) throws RuntimeException;
	
	public int delete(int id) throws RuntimeException;

	public ClientLoanDTO findLoanEndDateEMI(ClientLoanDTO clientLoanDTO) throws RuntimeException;

	public ClientLoanDTO findLoanEndDateNONEMI(ClientLoanDTO clientLoanDTO) throws RuntimeException;

	public ClientLoanDTO autoSave(ClientLoanDTO clientLoanDTO) throws RuntimeException;


}
