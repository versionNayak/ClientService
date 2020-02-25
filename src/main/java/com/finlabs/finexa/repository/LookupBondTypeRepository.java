package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupBondType;


public interface LookupBondTypeRepository extends JpaRepository<LookupBondType, Byte> {

}
