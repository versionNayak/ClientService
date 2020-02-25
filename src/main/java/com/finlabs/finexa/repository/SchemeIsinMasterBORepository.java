package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.SchemeIsinMasterBO;

public interface SchemeIsinMasterBORepository extends JpaRepository<SchemeIsinMasterBO, String>{
    
	List<SchemeIsinMasterBO> findByCamsCodeAndStatus(String rtaCode, String status);
	
	List<SchemeIsinMasterBO> findByIsinAndStatus(String isin, String status);
	
    
}
