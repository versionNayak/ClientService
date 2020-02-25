package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.ClientFamilyMember;

public interface AuxillaryFamilyMemberRepository extends JpaRepository<AuxillaryFamilyMember, Integer> {

	List<AuxillaryFamilyMember> findByClientFamilyMember(ClientFamilyMember clientFamilyMember);


}
