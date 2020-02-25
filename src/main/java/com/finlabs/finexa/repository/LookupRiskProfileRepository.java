package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupRiskProfile;

public interface LookupRiskProfileRepository extends JpaRepository<LookupRiskProfile, Byte>{

	LookupRiskProfile findByDescription(String description);
}
