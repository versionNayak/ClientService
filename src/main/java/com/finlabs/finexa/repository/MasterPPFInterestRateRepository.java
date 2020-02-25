package com.finlabs.finexa.repository;



import java.util.Date;


import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.MasterPPFInterestRate;


public interface MasterPPFInterestRateRepository extends JpaRepository<MasterPPFInterestRate, Date>{
	
}
