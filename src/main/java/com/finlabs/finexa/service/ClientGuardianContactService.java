package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientGuardianContactDTO;

public interface ClientGuardianContactService {
	public ClientGuardianContactDTO save(ClientGuardianContactDTO ClientGuardianContactDTO) throws RuntimeException;

	public ClientGuardianContactDTO update(ClientGuardianContactDTO ClientGuardianContactDTO);

	public ClientGuardianContactDTO findByClientID(int clientId) throws RuntimeException;

	public int delete(int id);

	public ClientGuardianContactDTO findById(int id);

	public List<ClientGuardianContactDTO> findAll();
	public boolean checkUniquePincode(int pincode, int stateId) throws RuntimeException;
}
