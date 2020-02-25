package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientRealEstateDTO;


public interface ClientRealEstateService {
	
	public ClientRealEstateDTO save (ClientRealEstateDTO clientRealEstateDTO, HttpServletRequest request) throws RuntimeException;
	
	public ClientRealEstateDTO update (ClientRealEstateDTO clientRealEstateDTO, HttpServletRequest request) throws RuntimeException;
	
	public List<ClientRealEstateDTO> findByClientId(int clientId) throws RuntimeException;
	
	public int delete(int id) throws RuntimeException;
	
	public ClientRealEstateDTO findById(int id) throws RuntimeException;

	public List<ClientRealEstateDTO> findAll();

	public void autoSave(ClientRealEstateDTO clientRealEstateDTO) throws RuntimeException;
}
