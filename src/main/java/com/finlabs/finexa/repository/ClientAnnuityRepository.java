package com.finlabs.finexa.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientFamilyMember;

public interface ClientAnnuityRepository extends JpaRepository<ClientAnnuity, Integer> {
	
	//ClientAnnuity findByClientFamilyMember(ClientFamilyMember clientFamilyMember);
	
	//ClientAnnuity findByClientEpf(ClientEpf clientEpf);
	
	@Modifying
    @Query("UPDATE ClientAnnuity c SET c.clientFamilyMember.id = :memberId WHERE c.clientEpf.id = :clientEPFID")
	void updateClientFamilyMember(@Param("clientEPFID") int clientEPFID, @Param("memberId") int memberId);
	
	@Modifying
	@Query("UPDATE ClientAnnuity c SET c.monthlyBasicDA = :monthlyBasicDA WHERE c.clientEpf.id = :clientEPFID")
	void updateMonthlyBasicDA(@Param("clientEPFID") int clientEPFID, @Param("monthlyBasicDA") BigDecimal monthlyBasicDA);
	
	@Modifying
	@Query("UPDATE ClientAnnuity c SET c.serviceYears = :serviceYears WHERE c.clientEpf.id = :clientEPFID")
	void updateServiceYears(@Param("clientEPFID") int clientEPFID, @Param("serviceYears") Byte serviceYears);

	@Modifying
	@Query("UPDATE ClientAnnuity c SET c.annualContributionIncrease = :annualContributionIncrease WHERE c.clientEpf.id = :clientEPFID")
	void updateAnnualContributionIncrease(@Param("clientEPFID") int clientEPFID, @Param("annualContributionIncrease") BigDecimal annualContributionIncrease);
}
