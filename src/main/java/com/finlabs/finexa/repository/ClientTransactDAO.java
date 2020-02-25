package com.finlabs.finexa.repository;
import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AdvisorUser;

public interface ClientTransactDAO {
	
	public AdvisorUser getAdvisorById(int advisorId, Session session) throws FinexaDaoException;
}
