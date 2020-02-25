package com.finlabs.finexa.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.MasterNAVHistory;

public interface MasterNAVHistoryRepository extends JpaRepository<MasterNAVHistory, String>{

	/*
	 * @Query("SELECT mnh.isin, mnh.schemeName, mnh.assetClass, mnh.dayOneNAV FROM MasterNAVHistory mnh WHERE mnh.dayOneNAV = 0 "
	 * ) public List<MasterNAVHistory> findSchemeNoDetailsList();
	 */	
	public List<MasterNAVHistory> findAll();

	public List<MasterNAVHistory> findByDayOneNAV(BigDecimal dayOneNAV);
	
	//public List<MasterNAVHistory> findByDayOneNAV(@Param )
	
}
