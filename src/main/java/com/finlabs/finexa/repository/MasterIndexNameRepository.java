package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.MasterFundManager;
import com.finlabs.finexa.model.MasterIndexName;

public interface MasterIndexNameRepository extends JpaRepository<MasterIndexName, Integer> {

	MasterIndexName findById(Integer contents);

}
