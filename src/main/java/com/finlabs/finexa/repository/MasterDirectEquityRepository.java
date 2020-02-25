package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.MasterDirectEquity;

public interface MasterDirectEquityRepository extends JpaRepository<MasterDirectEquity, String>{

	List <MasterDirectEquity> findSecurityNameByIsin(String isin);
	MasterDirectEquity findByStockNameIgnoreCaseContainingAndIsinIgnoreCaseContaining(String stockName, String isin);
	List<MasterDirectEquity> findByStockNameIgnoreCaseContaining(String stockName);
}
