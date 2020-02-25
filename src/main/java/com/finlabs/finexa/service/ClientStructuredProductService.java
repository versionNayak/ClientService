package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientStructuredProductDTO;

    public interface ClientStructuredProductService {
    public ClientStructuredProductDTO save(ClientStructuredProductDTO clientStructuredProductDTO, HttpServletRequest request) throws RuntimeException;
    
	public ClientStructuredProductDTO update(ClientStructuredProductDTO clientStructuredProductDTO, HttpServletRequest request) throws RuntimeException;

	public ClientStructuredProductDTO findById(int id) throws RuntimeException;
	
	public List<ClientStructuredProductDTO> findAll();
	
	public List<ClientStructuredProductDTO> findByClientId(int clientId) throws RuntimeException;
	
	public int delete(int id) throws RuntimeException; 
}
