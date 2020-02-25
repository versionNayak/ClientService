package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.ClientAlternateInvestmentsDTO;
import com.finlabs.finexa.exception.CustomFinexaException;

public interface ClientAlternateInvestmentsService {

	public List<ClientAlternateInvestmentsDTO> viewClientAlternateInvestmentsList(int clientId) 
			throws RuntimeException, CustomFinexaException;
	
}
