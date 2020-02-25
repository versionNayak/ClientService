package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.MasterAgeMortalityRate;

public interface MasterAgeMortalityRateRepository extends JpaRepository<MasterAgeMortalityRate, Integer>{

}
