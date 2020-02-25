package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.model.ClientMaster;

public interface ClientDeletionService {

	public List<ClientMaster> deleteClientByAdvisor (int advisorID, String isFinexaUserOrNot);
}
