package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.finlabs.finexa.model.LookupFixedDepositType;
import com.finlabs.finexa.model.LookupNPSPlanType;

public interface LookupNPSPlanTypeRepository extends JpaRepository<LookupNPSPlanType, Byte>{

}
