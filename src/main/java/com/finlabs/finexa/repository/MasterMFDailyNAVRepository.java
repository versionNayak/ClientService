package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMFDailyNAVPK;


public interface MasterMFDailyNAVRepository extends JpaRepository<MasterMFDailyNAV, MasterMFDailyNAVPK>{
    @Query("select nav from MasterMFDailyNAV nav WHERE nav.id.masterMutualFundEtf.isin = :isin and nav.id.date = :navDate")
    MasterMFDailyNAV findNAV(@Param("isin") String isin, @Param("navDate") Date navDate);

    @Query("select max(nav.id.date) from MasterMFDailyNAV nav WHERE nav.id.masterMutualFundEtf.isin = :isin and nav.id.date <= :navDate")
    Date findMaxDate(@Param("isin") String isin, @Param("navDate") Date navDate);

	@Query("select nav from MasterMFDailyNAV nav where nav.id.masterMutualFundEtf.isin= :isin order by nav.id.date desc") 
	List <MasterMFDailyNAV> findNAVValue(@Param("isin") String isin);
	 
	public MasterMFDailyNAV findTopByIdMasterMutualFundEtfIsinOrderByIdDateDesc(String isin); 
	 
	@Query("SELECT mfNAV.nav from MasterMFDailyNAV mfNAV WHERE mfNAV.schemeName = :schemeName")
	public Double findNAVBySchemeCode (@Param("schemeName") String schemeName);
	
	@Query("SELECT DISTINCT nav.id.masterMutualFundEtf.isin, max(nav.id.date) from MasterMFDailyNAV nav GROUP BY nav.id.masterMutualFundEtf.isin")
	List<Object[]> findDistinctIsinMaxDateList();

}
