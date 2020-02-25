package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientVehicleDTO;

public interface ClientVehicleService {
	
	public ClientVehicleDTO save (ClientVehicleDTO clientVehicleDTO) throws RuntimeException;
	
	public ClientVehicleDTO update (ClientVehicleDTO clientVehicleDTO) throws RuntimeException;
	
	public List<ClientVehicleDTO> findByClientId(int clientId) throws RuntimeException;
	
	public int delete(int id) throws RuntimeException;
	
	public ClientVehicleDTO findById(int id) throws RuntimeException;
	
	public List<ClientVehicleDTO> findAll();

}
