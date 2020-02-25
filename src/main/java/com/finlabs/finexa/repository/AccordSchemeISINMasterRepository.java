package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.AccordSchemeIsinMaster;
import com.finlabs.finexa.model.AccordSchemeIsinMasterPK;


public interface AccordSchemeISINMasterRepository extends JpaRepository<AccordSchemeIsinMaster, AccordSchemeIsinMasterPK>{

	/*
	 * @Query("select asim from accordSchemeIsinMaster asim where asim.id.isin = :isin"
	 * ) public List<AccordSchemeIsinMaster>
	 * getAccordSchemeIsinMasterByIsin(@Param("isin") String isin);
	 */
	public List<AccordSchemeIsinMaster> findByIdIsin(String isin);
	
	//public List<AccordSchemeIsinMaster> findBy
	
	public List<AccordSchemeIsinMaster> findByRTASchemeCode(String rtaSchemeCode);
	
}
