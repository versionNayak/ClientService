package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientARNMapping;


public interface ClientARNMappingRepository extends JpaRepository<ClientARNMapping, Integer> {

	List<ClientARNMapping> findByArn(String arn);

	List<ClientARNMapping> findByAdvisorUser(AdvisorUser advisorUser);

}
