package com.finlabs.finexa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.finlabs.finexa.dto.ClientSearchDTO;
import com.finlabs.finexa.model.ClientMaster;

public class ClientMasterRepositoryCustomImpl implements ClientMasterRepositoryCustom {
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@Override
	public List<ClientMaster> searchClient(ClientSearchDTO searchObj) {
		// TODO Auto-generated method stub
		EntityManager em = entityManagerFactory.createEntityManager();
		String nativeQuery = "select ";
		Query query = em.createNativeQuery(nativeQuery);
		
		return null;
	}

}
