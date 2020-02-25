package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.LookupAssetSubClass;


public interface LookupAssetSubClassRepository extends JpaRepository<LookupAssetSubClass,Byte>{ 
	LookupAssetSubClass findById(byte id);
	List<LookupAssetSubClass> findByLookupAssetClass(LookupAssetClass assetClass);
	LookupAssetSubClass findByDescription(String description);
}
