package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.model.LookupInsurancePolicyType;

public interface ClientNonlifeInsuranceRepository extends JpaRepository<ClientNonLifeInsurance, Integer> {

	public ClientNonLifeInsurance findPolicyNumberByPolicyNumberAndClientMaster(String policyNumber,ClientMaster clientId);
	
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) AND clientID=?1 AND insuranceTypeID=?2", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext1Week(int clientID,int insuranceTypeID);
//	 
//	
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 14 DAY) AND clientID=?1 AND insuranceTypeID=?2", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateNext1FortNight(int clientID,int insuranceTypeID);
//	 
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH) AND clientID=?1 AND insuranceTypeID=?2", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateNext1Month(int clientID,int insuranceTypeID);
//	 
//
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 3 MONTH) AND clientID=?1 AND insuranceTypeID=?2", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext3Month(int clientID,int insuranceTypeID);
//	 
//	
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 6 MONTH) AND clientID=?1 AND insuranceTypeID=?2", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateNext6Month(int clientID,int insuranceTypeID);
//	 
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 YEAR) AND clientID=?1 AND insuranceTypeID=?2", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateNext1YEAR(int clientID,int insuranceTypeID);
//	 
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) AND clientID=?1 ORDER BY policyEndDate", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext1Week(int clientID);
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 14 DAY) AND clientID=?1 ORDER BY policyEndDate", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext1FortNight(int clientID);
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH) AND clientID=?1 ORDER BY policyEndDate", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext1Month(int clientID);
//	 
//
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 3 MONTH) AND clientID=?1 ORDER BY policyEndDate", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext3Month(int clientID);
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 6 MONTH) AND clientID=?1 ORDER BY policyEndDate", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext6Month(int clientID);
//	 
//	 @Query(value ="SELECT * FROM clientNonLifeInsurance WHERE policyEndDate BETWEEN  NOW() AND DATE_ADD(NOW(), INTERVAL 1 YEAR) AND clientID=?1 ORDER BY policyEndDate", nativeQuery = true)
//	 public List<ClientNonLifeInsurance> getPolicyEndDateForNext1YEAR(int clientID);
	 
	 //ClientNonLifeInsurance findByLookupInsurancePolicyTypeAndInsuranceTypeID(LookupInsurancePolicyType lookupInsurancePolicyTypeID, byte insuranceTypeID);
	 
}