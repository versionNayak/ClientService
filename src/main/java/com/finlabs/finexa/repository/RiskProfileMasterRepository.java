package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.RiskProfileMaster;


public interface RiskProfileMasterRepository extends JpaRepository<RiskProfileMaster, Integer>{

	
	@Modifying
	@Query(value = "DELETE FROM RiskProfileMaster rpm  where rpm.advisorMaster.id =?1")
	public void deleteByadvisorId(Integer advisorId);
	
}
