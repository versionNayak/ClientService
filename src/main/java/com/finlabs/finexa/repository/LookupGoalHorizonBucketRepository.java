package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupGoalHorizonBucket;

public interface LookupGoalHorizonBucketRepository extends JpaRepository<LookupGoalHorizonBucket, Integer>{

	LookupGoalHorizonBucket findById(int id);
	
}
