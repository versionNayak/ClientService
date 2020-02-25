package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.finlabs.finexa.dto.ClientAtalPensionYojanaDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;

public interface ClientAtalPensionYojanaService {

	public ClientAtalPensionYojanaDTO save(ClientAtalPensionYojanaDTO clientATPDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientAtalPensionYojanaDTO> findByClientId(int clientId) throws RuntimeException;

	public int delete(int id) throws RuntimeException;

	public ClientAtalPensionYojanaDTO findById(int id) throws RuntimeException;

	public ClientAtalPensionYojanaDTO update(ClientAtalPensionYojanaDTO clientATPDTO, HttpServletRequest request) throws RuntimeException;

	public List<ClientAtalPensionYojanaDTO> findAll();

	public List<ClientFamilyMemberDTO> checkIfApyPresentForAll(Integer clientId) throws RuntimeException;

	public boolean checkIfApyPresent(Integer clientId);

}
