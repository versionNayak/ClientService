package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupRelation;

public interface ClientFamilyMemberRepository extends JpaRepository<ClientFamilyMember, Integer> {

	ClientFamilyMember findBypan(String pan);
	
	ClientFamilyMember findByPanAndClientMasterAndLookupRelation(String pan, ClientMaster cm, LookupRelation lookupRelation);

	@Query("SELECT cfm FROM ClientFamilyMember cfm where cfm.clientMaster.id = :id AND cfm.lookupRelation.id != :relationID")
	List<ClientFamilyMember> findByClientIdAndRelationId(@Param("id") int id,@Param("relationID") byte relationID);

	ClientFamilyMember findByClientMasterAndLookupRelation(ClientMaster clientMaster, LookupRelation lookupRelation);

	ClientFamilyMember findByClientMasterAndFirstName(ClientMaster clientMaster1, String firstName);
	
/*	@Query("SELECT c FROM ClientFamilyMember c WHERE c.clientMaster.clientId =?1 ORDER BY c.relationID asc")
	List<ClientFamilyMember> findByClientMasterClientIdOrderByrelationIDAsc(int clientId);
	
	@Query("SELECT c FROM ClientFamilyMember c WHERE c.clientMaster.clientId =?1 and c.relationID=?2")
	ClientFamilyMember findByClientMasterClientIdAndrelationID(int clientid,int reltionid);
	
	//List<ClientFamilyMember> findLifeExpectancyByClientId(long clientId);
	//List<ClientFamilyMember> findAllFamilyMembersByClientId(long clientId);
	
	@Query("SELECT fm FROM ClientMaster c JOIN c.clientFamilyMembers fm  where fm.id=?1 ")
	ClientFamilyMember findByMemberId(int memberId);*/

	
	
	
	


}
