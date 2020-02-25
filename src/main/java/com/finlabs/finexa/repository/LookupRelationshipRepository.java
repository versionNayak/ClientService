package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupRelation;

public interface LookupRelationshipRepository extends JpaRepository<LookupRelation, Byte> {

	LookupRelation findById(byte id);

}
