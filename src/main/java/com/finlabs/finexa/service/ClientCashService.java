package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientCashDTO;
import com.finlabs.finexa.exception.CustomFinexaException;

public interface ClientCashService {
	
	public ClientCashDTO save(ClientCashDTO clientCashDTO, HttpServletRequest request) throws RuntimeException;

	public ClientCashDTO update(ClientCashDTO clientCashDTO, HttpServletRequest request) throws RuntimeException;

	public ClientCashDTO findById(int id) throws RuntimeException;
	
	public List<ClientCashDTO> findAll();
	
	public List<ClientCashDTO> findByClientId(int clientId) throws RuntimeException, CustomFinexaException;
	
	public int delete(int id) throws RuntimeException;

	public void autoSave(ClientCashDTO clientCashDTO) throws RuntimeException;

}
