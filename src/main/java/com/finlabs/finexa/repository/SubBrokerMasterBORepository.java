package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.SubBrokerMasterBO;
public interface SubBrokerMasterBORepository extends JpaRepository<SubBrokerMasterBO, Integer>{

public List<SubBrokerMasterBO> findByAdvisorUser(AdvisorUser advUser);
	

}
