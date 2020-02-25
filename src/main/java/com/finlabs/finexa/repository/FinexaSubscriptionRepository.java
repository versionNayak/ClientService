
package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.FinexaSubscription;

public interface FinexaSubscriptionRepository extends JpaRepository<FinexaSubscription, Integer> {
	
	List<FinexaSubscription> findByAdvisorMaster(AdvisorMaster advisorMaster);
}
