package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.FinexaSubscriptionDTO;

public interface FinexaSubscriptionService {

	public FinexaSubscriptionDTO save(FinexaSubscriptionDTO finexaSubscriptionDTO) throws RuntimeException;
	
	List<FinexaSubscriptionDTO> getAllSubscriptionByAdvisor(int advisorId) throws RuntimeException;

	public FinexaSubscriptionDTO getSubscriptionById(int subId) throws RuntimeException;

}
