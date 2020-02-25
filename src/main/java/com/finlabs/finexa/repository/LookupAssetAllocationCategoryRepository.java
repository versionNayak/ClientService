package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupAssetAllocationCategory;

public interface LookupAssetAllocationCategoryRepository extends JpaRepository<LookupAssetAllocationCategory, Integer>{
	
	LookupAssetAllocationCategory findById(int id);
	LookupAssetAllocationCategory findByDescription(String description);

}
