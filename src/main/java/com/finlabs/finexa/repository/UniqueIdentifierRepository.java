package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.AccordSchemeIsinMaster;
import com.finlabs.finexa.model.AccordSchemeIsinMasterPK;
import com.finlabs.finexa.model.MasterPincode;
import com.finlabs.finexa.model.UniqueIdentifierNumberDateWise;


public interface UniqueIdentifierRepository extends JpaRepository<UniqueIdentifierNumberDateWise, String>{
	
}
