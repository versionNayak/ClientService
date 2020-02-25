package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterEquityDailyPrice;

public interface MasterEquityDailyPriceRepository extends JpaRepository<MasterEquityDailyPrice, String>{

	List<MasterEquityDailyPrice> findByStockNameIgnoreCaseContaining(String searchString);
	
	/*//Sql
	@Query("SELECT medp FROM MasterEquityDailyPrice medp join MasterDirectEquity mde on mde.ISIN = medp.isin where mde.ISIN = :isin")
	List<MasterEquityDailyPrice> findByIsin(@Param("isin") String isin);*/
	
	@Query("SELECT medp FROM MasterEquityDailyPrice medp left join medp.id.masterDirectEquity mde where mde.isin = :isin and medp.id.date = :date")
	MasterEquityDailyPrice findClosingPrice(@Param("isin") String isin, @Param("date") Date date);
	
	//Hibernate
	@Query("SELECT medp FROM MasterEquityDailyPrice medp left join medp.id.masterDirectEquity mde where mde.isin = :isin")
	List<MasterEquityDailyPrice> findByIsin(@Param("isin") String isin);
	
	@Query("select MIN(medp.id.date) from MasterEquityDailyPrice medp where medp.id.masterDirectEquity.isin = :isin")
	Date checkMinDate(@Param("isin") String isin); 
	
	//SpringJPA
	//List<MasterEquityDailyPrice> findByMasterEquityDailyPricePKmasterDirectEquity(MasterDirectEquity mde);
	
	
	
}
