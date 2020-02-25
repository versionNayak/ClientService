package com.finlabs.finexa.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupFundCategory;
import com.finlabs.finexa.model.LookupFundType;


public interface FundTypeRepository extends JpaRepository<LookupFundType, Integer>{

	

}
