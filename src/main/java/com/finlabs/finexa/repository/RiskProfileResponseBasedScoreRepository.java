package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.RiskProfileResponseBasedScore;

public interface RiskProfileResponseBasedScoreRepository extends JpaRepository<RiskProfileResponseBasedScore, Integer> {

	
	@Modifying
	@Query(value = "DELETE FROM RiskProfileResponseBasedScore rprb  where rprb.advisorMaster.id =?1")
	public void deleteByadvisorId(Integer advisorId);
}
