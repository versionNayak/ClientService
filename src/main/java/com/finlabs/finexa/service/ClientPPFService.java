package com.finlabs.finexa.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientPpfDTO;

public interface ClientPPFService {

	public ClientPpfDTO save(ClientPpfDTO clientPPFDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientPpfDTO> findByClientId(int clientId) throws RuntimeException;

	public int delete(int id) throws RuntimeException;

	public ClientPpfDTO findById(int id) throws RuntimeException;

	public ClientPpfDTO update(ClientPpfDTO clientPPFDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientPpfDTO> findAll();
	
	public double getInterestRate(Date date) throws RuntimeException;

	public boolean checkIfPpfPresent(Integer clientId);

	public List<ClientFamilyMemberDTO> checkIfPpfPresentForAll(Integer clientId) throws RuntimeException;

	public ClientPpfDTO calculatePPFMaturityDate(ClientPpfDTO clientPpfDTO) throws RuntimeException;

	public void autoSave(ClientPpfDTO clientPPFDTO) throws RuntimeException;
	
}
