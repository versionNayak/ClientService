package com.finlabs.finexa.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFloaterCover;
import com.finlabs.finexa.model.ClientNonLifeInsurance;


public interface ClientFloaterCoverRepository extends JpaRepository<ClientFloaterCover, Integer> {
	
	@Modifying
	@Query(value="delete from ClientFloaterCover cfc where cfc.clientNonLifeInsurance.id= ?1")
	public void deleteClientFloaterCover(Integer insuranceID);
	
	
	List<ClientFloaterCover> findClientFamilyMemberByClientNonLifeInsurance(ClientNonLifeInsurance insuranceID);


	public List<ClientFloaterCover> findByClientFamilyMember(ClientFamilyMember clientFamilyMember);
	
}
