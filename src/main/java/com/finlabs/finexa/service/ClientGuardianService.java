package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientGuardianDTO;

public interface ClientGuardianService {
	public ClientGuardianDTO save(ClientGuardianDTO ClientGuardianDTO) throws RuntimeException;

	public ClientGuardianDTO update(ClientGuardianDTO ClientGuardianDTO);

	public ClientGuardianDTO findByClientId(int clientId) throws RuntimeException;

	public int delete(int id);

	public ClientGuardianDTO findById(int id);

	public List<ClientGuardianDTO> findAll();
	
	public int deleteGuardianandGuardianContact(int id) throws RuntimeException;
}
