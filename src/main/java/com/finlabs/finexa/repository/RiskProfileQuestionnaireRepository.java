package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.RiskProfileQuestionnaire;

public interface RiskProfileQuestionnaireRepository extends JpaRepository<RiskProfileQuestionnaire, Integer> {
	@Modifying
	@Query(value = "DELETE FROM RiskProfileQuestionnaire rpq  where rpq.advisorMaster.id =?1")
	public void deleteByAdvisorId(Integer advisorId);
}
