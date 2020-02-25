package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientPreciousMetalDTO;

public interface ClientPreciousMetalService {
	
	public ClientPreciousMetalDTO save (ClientPreciousMetalDTO clientPreciousMetalDTO, HttpServletRequest request) throws RuntimeException;
	
	public ClientPreciousMetalDTO update (ClientPreciousMetalDTO clientPreciousMetalDTO, HttpServletRequest request) throws RuntimeException;
	
	public List<ClientPreciousMetalDTO> findByClientId(int clientId) throws RuntimeException;
	
	public int delete(int id) throws RuntimeException;
	
	public ClientPreciousMetalDTO findById(int id) throws RuntimeException;
	
	public List<ClientPreciousMetalDTO> findAll();

}
