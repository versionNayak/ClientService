package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientMutualFund;

public interface ClientMutualFundRepository extends JpaRepository<ClientMutualFund, Integer> {
	
	public ClientMutualFund findByClientMasterAndClientFamilyMemberAndIsinAndBackOfficeEntry(ClientMaster clientMaster, ClientFamilyMember clientFamilyMember, String isin,  String backOfficeEntry);
	
	public ClientMutualFund findByClientMasterAndClientFamilyMemberAndFolioNumberAndIsinAndBackOfficeEntry(ClientMaster clientMaster, ClientFamilyMember clientFamilyMember, String folioNumber, String isin,  String backOfficeEntry);

	public List<ClientMutualFund> findByClientMasterAndBackOfficeEntry(ClientMaster clientMaster, String backOfficeEntry);

}