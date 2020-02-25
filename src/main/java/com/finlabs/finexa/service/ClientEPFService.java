package com.finlabs.finexa.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientEpfDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;


public interface ClientEPFService {

	public ClientEpfDTO save(ClientEpfDTO clientAnnuityDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientEpfDTO> findByClientId(int clientId) throws RuntimeException;

	public int delete(int id) throws RuntimeException;

	public ClientEpfDTO findById(int id) throws RuntimeException;

	public ClientEpfDTO update(ClientEpfDTO clientEPFDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientEpfDTO> findAll();
	
	public double getInterestRate(Date date) throws RuntimeException;
	
	public double getCagr(Date date) throws RuntimeException;
	
	public int existAnnuity(int id);

	public List<ClientFamilyMemberDTO> checkIfEpfPresentForAll(Integer clientId) throws RuntimeException;

	public int checkIfEpfPresent(int clientId);

}
