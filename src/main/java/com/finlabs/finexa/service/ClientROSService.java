package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientROSDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface ClientROSService {
	
	public List<ClientROSDTO> viewClientROSList(int clientId) throws RuntimeException, FinexaBussinessException;
	
}
