package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.ClientLifeInsurance;
import com.finlabs.finexa.model.ClientMaster;


public interface ClientLifeInsuranceRepository extends JpaRepository<ClientLifeInsurance, Integer> {
	
	public ClientLifeInsurance findPolicyNumberByPolicyNumberAndClientMaster(String policyNumber,
			ClientMaster clientId);
	
//	@Query(value ="SELECT * FROM clientLifeInsurance WHERE endDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) AND clientID=?1 ORDER BY lockedUptoDate", nativeQuery = true)
//	 public List<ClientLifeInsurance> getLockedUptoDateForNext1Week(int clientID);
//	
//	@Query(value ="SELECT * FROM clientLifeInsurance WHERE endDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 14 DAY) AND clientID=?1 ORDER BY lockedUptoDate", nativeQuery = true)
//	 public List<ClientLifeInsurance> getLockedUptoDateForNext1Fortnight(int clientID);
//	 
//	
//	 @Query(value ="SELECT * FROM clientLifeInsurance WHERE endDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH) AND clientID=?1 ORDER BY lockedUptoDate", nativeQuery = true)
//	 public List<ClientLifeInsurance> getLockedUptoDateForNext1Month(int clientID);
//	 
//
//	 @Query(value ="SELECT * FROM clientLifeInsurance WHERE endDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 3 MONTH) AND clientID=?1 ORDER BY lockedUptoDate", nativeQuery = true)
//	 public List<ClientLifeInsurance> getLockedUptoDateForNext3Month(int clientID);
//	 
//	 @Query(value ="SELECT * FROM clientLifeInsurance WHERE endDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 6 MONTH) AND clientID=?1 ORDER BY lockedUptoDate", nativeQuery = true)
//	 public List<ClientLifeInsurance> getLockedUptoDateForNext6Month(int clientID);
//	 
//	 @Query(value ="SELECT * FROM clientLifeInsurance WHERE endDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 YEAR) AND clientID=?1 ORDER BY lockedUptoDate", nativeQuery = true)
//	 public List<ClientLifeInsurance> getLockedUptoDateForNext1Year(int clientID);
	 
//	 List<ClientLifeInsurance> findAllByEndDateBetween(Date utilFromDate, Date utilTODate);
}