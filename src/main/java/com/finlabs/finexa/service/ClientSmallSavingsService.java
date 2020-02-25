package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientSmallSavingsDTO;


public interface ClientSmallSavingsService {

	public ClientSmallSavingsDTO save(ClientSmallSavingsDTO clientSSDTO, HttpServletRequest request) throws RuntimeException;

	public ClientSmallSavingsDTO update(ClientSmallSavingsDTO clientSSDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientSmallSavingsDTO> findByClientId(int clientId) throws RuntimeException;

	public int delete(int id) throws RuntimeException;

	public ClientSmallSavingsDTO findById(int id) throws RuntimeException;

	public List<ClientSmallSavingsDTO> findAll();

	public void autoSave(ClientSmallSavingsDTO clientSSDTO) throws RuntimeException;
}
