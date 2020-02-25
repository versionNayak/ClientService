package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientLumpsumDTO;



public interface ClientLumpsumService {

	public ClientLumpsumDTO save(ClientLumpsumDTO clientLumpsumDTO,  HttpServletRequest request) throws RuntimeException;
	public ClientLumpsumDTO update(ClientLumpsumDTO clientLumpsumDTO,  HttpServletRequest request) throws RuntimeException;
	public int delete(int id) throws RuntimeException;
	public ClientLumpsumDTO findById(int id) throws RuntimeException;
	public List<ClientLumpsumDTO> findAll();
	public List<ClientLumpsumDTO> findByClientId(int clientId) throws RuntimeException;
	public void autoSave(ClientLumpsumDTO clientLumpsumDTO) throws RuntimeException;
	
}
