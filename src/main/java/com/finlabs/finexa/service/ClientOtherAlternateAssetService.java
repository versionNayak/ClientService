package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientOtherAlternateAssetDTO;

public interface ClientOtherAlternateAssetService {
	
	public ClientOtherAlternateAssetDTO save (ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO) throws RuntimeException;
	
	public ClientOtherAlternateAssetDTO update (ClientOtherAlternateAssetDTO clientOtherAlternateAssetDTO) throws RuntimeException;
	
	public List<ClientOtherAlternateAssetDTO> findByClientId(int clientId) throws RuntimeException;
	
	public int delete(int id) throws RuntimeException;
	
	public ClientOtherAlternateAssetDTO findById(int id) throws RuntimeException;
	
	public List<ClientOtherAlternateAssetDTO> findAll();
	
}
