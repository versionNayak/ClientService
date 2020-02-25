package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupTransactBSEAccessMode;

public interface LookupTransactBSEAccessModeRepository extends JpaRepository<LookupTransactBSEAccessMode, Byte> {

}
