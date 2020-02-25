package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupAssetClass;
import com.finlabs.finexa.model.MasterFundManager;

public interface MasterFundManagerRepository extends JpaRepository<MasterFundManager, Integer> {

	MasterFundManager findByManagerCode(Integer contents);

}
