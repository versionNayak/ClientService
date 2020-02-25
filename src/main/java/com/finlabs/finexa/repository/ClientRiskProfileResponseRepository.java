package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finlabs.finexa.model.ClientRiskProfileResponse;
import com.finlabs.finexa.model.ClientMaster;

@Repository
public interface ClientRiskProfileResponseRepository extends JpaRepository<ClientRiskProfileResponse, Integer> {

	@Modifying
	@Query(value = "UPDATE ClientRiskProfileResponse crpr set crpr.activeFlag = 'N' where crpr.clientMaster.id = :clienId")
	public void disableRiskResponseData(@Param("clienId") Integer clienId);
	
	@Modifying
	@Query(value = "DELETE FROM ClientRiskProfileResponse crpr  where crpr.clientMaster.id = :clienId")
	public void deleteByClienId(@Param("clienId") Integer clienId);
	
	
	 public List<ClientRiskProfileResponse> findByClientMaster(ClientMaster clientmaster);
	
	
	
}
