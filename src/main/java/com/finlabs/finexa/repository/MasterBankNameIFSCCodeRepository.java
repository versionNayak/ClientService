package com.finlabs.finexa.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.MasterBankNameIFSCCode;

public interface MasterBankNameIFSCCodeRepository extends JpaRepository<MasterBankNameIFSCCode, String> {

	MasterBankNameIFSCCode findByIfsc(String strIfsc);
	
	
	
	
}
