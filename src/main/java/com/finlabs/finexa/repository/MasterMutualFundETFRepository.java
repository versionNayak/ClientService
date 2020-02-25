package com.finlabs.finexa.repository;



import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.dto.MasterMutualFundETFDTO;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.MasterMutualFundETF;


public interface MasterMutualFundETFRepository extends JpaRepository<MasterMutualFundETF, String>{
	
	MasterMutualFundETF findByAmfiCode(Integer amfiCode);
	
	List<MasterMutualFundETF> findByFundHouseAndStatus(String fundHouse, String status);
	
	MasterMutualFundETF findByFundHouseAndSchemeName(String fundHouse, String schemeName);
	
	List <MasterMutualFundETF> findCloseEndedFlagBySchemeName(String schemeName);
	
	List<MasterMutualFundETF> findBySchemeName(String schemeName);
	
	List<MasterMutualFundETF> findBySchemeNameIgnoreCaseContaining(String searchString);
	
	List<MasterMutualFundETF> findByStatus(String status);
	
	@Query("select mmfetf from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' and asim.status = 'NEW'")
	List<MasterMutualFundETF> findForDropdown();
	
//	@Query("select mmfetf from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' and asim.status = 'NEW'")
//	List<MasterMutualFundETF> findForDropdown(Pageable pageable);
	
	@Query("select distinct(mmfetf.fundHouse) from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim "
			+ "where mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' and asim.status = 'NEW'"
			+ "order by mmfetf.fundHouse ")
	List<String> findForDropdown(Pageable pageable);
	
	@Query("select mmfetf from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim "
			+ "where mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' and asim.status = 'NEW' "
			+ "and mmfetf.fundHouse like :matchString")
	List<MasterMutualFundETF> findForDropdown(@Param(value = "matchString") String matchString);
	
	@Query("select mmfetf from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' and asim.status = 'NEW' and mmfetf.lookupAssetSubClass.id=?1")
	List<MasterMutualFundETF> findFundHouseBySubAssetClassId(byte subAssetClassId);
	
	@Query("select distinct mmfetf.isin,mmfetf.fundHouse,mmfetf.schemeName,mmfetf.amfiCode,"
			+ "mmfetf.lookupAssetClass.id,mmfetf.lookupAssetSubClass.id,mmfetf.schemeOption, "
			+ "mmfetf.closeEndedFlag, mmfetf.schemeInceptionDate,mmfetf.regularDirectFlag,"
			+ "mmfetf.schemeEndDate, mmfetf.exitLoadAndPeriod, mmfetf.minInvestmentAmount, "
			+ "mmfetf.masterFundManager.managerCode, mmfetf.masterIndexName.id, mmfetf.status ,asim.series "
			+ "from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where "
			+ "mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' "
			+ "and asim.status = 'NEW' and mmfetf.fundHouse = ?1 and mmfetf.lookupAssetSubClass.id = ?2")
	List<Object[]> findForSecondDropdownList(String fundHouse,byte subAssetClassID);
	
	@Query("select distinct mmfetf.isin,mmfetf.fundHouse,mmfetf.schemeName,mmfetf.amfiCode,"
			+ "mmfetf.lookupAssetClass.id,mmfetf.lookupAssetSubClass.id,mmfetf.schemeOption, "
			+ "mmfetf.closeEndedFlag, mmfetf.schemeInceptionDate,mmfetf.regularDirectFlag,"
			+ "mmfetf.schemeEndDate, mmfetf.exitLoadAndPeriod, mmfetf.minInvestmentAmount, "
			+ "mmfetf.masterFundManager.managerCode, mmfetf.masterIndexName.id, mmfetf.status ,asim.series "
			+ "from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where "
			+ "mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' "
			+ "and asim.status = 'NEW' and mmfetf.fundHouse = ?1")
	List<Object[]> findForSecondDropdown(String fundHouse);
	
	@Query("select distinct mmfetf.isin,mmfetf.fundHouse,mmfetf.schemeName,mmfetf.amfiCode,"
			+ "mmfetf.lookupAssetClass.id,mmfetf.lookupAssetSubClass.id,mmfetf.schemeOption, "
			+ "mmfetf.closeEndedFlag, mmfetf.schemeInceptionDate,mmfetf.regularDirectFlag,"
			+ "mmfetf.schemeEndDate, mmfetf.exitLoadAndPeriod, mmfetf.minInvestmentAmount, "
			+ "mmfetf.masterFundManager.managerCode, mmfetf.masterIndexName.id, mmfetf.status ,asim.series "
			+ "from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where "
			+ "mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' "
			+ "and asim.status = 'NEW' and mmfetf.fundHouse = ?1")
	List<Object[]> findForSecondDropdown(String fundHouse, Pageable pageable);	
	
	
	@Query("select distinct mmfetf.isin,mmfetf.fundHouse,mmfetf.schemeName,mmfetf.amfiCode,"
			+ "mmfetf.lookupAssetClass.id,mmfetf.lookupAssetSubClass.id,mmfetf.schemeOption, "
			+ "mmfetf.closeEndedFlag, mmfetf.schemeInceptionDate,mmfetf.regularDirectFlag,"
			+ "mmfetf.schemeEndDate, mmfetf.exitLoadAndPeriod, mmfetf.minInvestmentAmount, "
			+ "mmfetf.masterFundManager.managerCode, mmfetf.masterIndexName.id, mmfetf.status ,asim.series "
			+ "from MasterMutualFundETF mmfetf, AccordSchemeIsinMaster asim where "
			+ "mmfetf.isin = asim.id.isin and mmfetf.status = 'Active' "
			+ "and asim.status = 'NEW' and mmfetf.fundHouse = :fundHouse "
			+ "and mmfetf.schemeName like :matchString ")
	List<Object[]> findForSecondDropdown(@Param(value = "matchString") String matchString,@Param(value = "fundHouse") String fundHouse);
	
	@Query("select mmfetf.lookupAssetSubClass from MasterMutualFundETF mmfetf WHERE mmfetf.lookupAssetSubClass is NOT NULL  GROUP BY mmfetf.lookupAssetSubClass ")
	List<LookupAssetSubClass> findAllSubAssetinMutualfundETF();
	
	@Query("SELECT DISTINCT mmfetf.fundHouse FROM MasterMutualFundETF mmfetf WHERE mmfetf.schemeName LIKE :scheme")
	public String getParticularFundHouseFromScheme(@Param("scheme") String scheme);
	
	@Query("SELECT DISTINCT mmfetf FROM MasterMutualFundETF mmfetf WHERE mmfetf.schemeName LIKE :scheme")
	public List<MasterMutualFundETF> getAllFromScheme(@Param("scheme") String scheme);
	
	MasterMutualFundETF findByIsin(String isin);
	
}
