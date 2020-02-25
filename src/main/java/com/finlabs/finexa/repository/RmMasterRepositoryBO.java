package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.RmMasterBO;

public interface RmMasterRepositoryBO extends JpaRepository<RmMasterBO, Integer>{
	
	public RmMasterBO findByAdvisorUser(AdvisorUser advUser);
	public List<RmMasterBO> findAllByAdvisorUser(AdvisorUser advUser);

}
