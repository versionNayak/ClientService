package com.finlabs.finexa.service;


import java.util.List;

import com.finlabs.finexa.dto.ClientContactDTO;
import com.finlabs.finexa.model.ClientContact;

public interface ClientContactService {

	public ClientContactDTO save(ClientContactDTO clientContactDTO) throws RuntimeException;

	public ClientContactDTO update(ClientContactDTO clientContactDTO);

	public List<ClientContactDTO> findByClientId(int clientId) throws RuntimeException;

	public int delete(int id);

	public ClientContactDTO findById(int id);

	public List<ClientContactDTO> findAll();

	public boolean checkEmailExists(String email, int clientId) throws RuntimeException;

	public boolean checkMobileExists(long mobile, int clientId) throws RuntimeException;

	public boolean checkUniqueEmail(String email) throws RuntimeException;

	public boolean checkUniqueMobile(long mobile) throws RuntimeException;

	public List<String> findAllCities() throws RuntimeException;

	public boolean checkUniquePincode(int pincode, int stateId) throws RuntimeException;

	public ClientContact autoSave(ClientContactDTO clientContactDTO) throws RuntimeException;

	public List<String> findAllCities(int advisorID) throws RuntimeException;

	public ClientContactDTO save(int userId, ClientContactDTO clientContactDTO);



}
