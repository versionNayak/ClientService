package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.MasterPincode;
import com.finlabs.finexa.model.MasterState;

public interface MasterPincodeRepository extends JpaRepository<MasterPincode, Integer>{
	
	

}
