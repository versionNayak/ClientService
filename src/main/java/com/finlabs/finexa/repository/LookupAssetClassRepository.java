package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupAssetClass;

public interface LookupAssetClassRepository extends JpaRepository<LookupAssetClass, Byte> {

	LookupAssetClass findById(byte contents);
	
	LookupAssetClass findByDisplayOrder(int displayOrder);

}
