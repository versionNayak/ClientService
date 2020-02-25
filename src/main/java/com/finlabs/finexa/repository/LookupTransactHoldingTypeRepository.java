package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientUCCDetail;
import com.finlabs.finexa.model.LookupTansactHoldingType;

public interface LookupTransactHoldingTypeRepository extends JpaRepository<LookupTansactHoldingType, Byte>{

	LookupTansactHoldingType findBydescription(String description);
	LookupTansactHoldingType findByvalue(String value);
}
