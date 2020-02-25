package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.StagingFolioMasterBO;
import com.finlabs.finexa.model.StagingInvestorMasterBO;

public interface StagingFolioMasterBORepository extends JpaRepository<StagingFolioMasterBO, Integer>{

	public StagingFolioMasterBO findByStaginginvestormasterboAndInvestorFolio(StagingInvestorMasterBO investorMaster, String folio);
	
	@Query("select distinct investorFolio, staginginvestormasterbo.id from StagingFolioMasterBO sfBO")
	public List<Object[]> findAllDistinctFolioNumberAndInvestorId();
	
	@Query("SELECT distinct sfBO.investorFolio FROM StagingFolioMasterBO sfBO WHERE sfBO.staginginvestormasterbo.id = :id")
	public List<String> getAllDistinctFolioById(@Param("id") int id);
	
	public List<StagingFolioMasterBO> findByInvestorFolio(String folio);
	

}
