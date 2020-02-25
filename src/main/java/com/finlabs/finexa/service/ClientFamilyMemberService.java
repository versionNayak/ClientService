package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientFamilyMember;

public interface ClientFamilyMemberService {

	public ClientFamilyMemberDTO save(ClientFamilyMemberDTO clientFamilyMemberDTO, HttpServletRequest request) throws RuntimeException, CustomFinexaException;

	public ClientFamilyMemberDTO update(ClientFamilyMemberDTO clientFamilyMemberDTO, HttpServletRequest request) throws RuntimeException, CustomFinexaException;

	public int delete(int id) throws RuntimeException;

	public List<ClientFamilyMemberDTO> findAll();

	public ClientFamilyMemberDTO findById(int id) throws RuntimeException;

	public List<ClientFamilyMemberDTO> findByClientId(int clientId) throws RuntimeException;

	public List<ClientFamilyMemberDTO> getClientFamilyMemberImageByClient(int clientId) throws RuntimeException;
	
	public void deleteClientFamilyMember(int id);

	public int existassetOrLoan(int id) throws RuntimeException;

	public ClientFamilyMemberDTO findClientByClientId(int clientId) throws RuntimeException;

	public ClientFamilyMemberDTO autoSave(ClientFamilyMemberDTO clientFamilyMemberDTO)
			throws RuntimeException, CustomFinexaException;


}
