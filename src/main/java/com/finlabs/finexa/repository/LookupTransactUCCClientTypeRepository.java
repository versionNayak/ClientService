package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.LookupTransactUCCClientType;
import com.finlabs.finexa.model.MasterTransactStateCode;

public interface LookupTransactUCCClientTypeRepository extends JpaRepository<LookupTransactUCCClientType, Byte> {

	LookupTransactUCCClientType findBydescription(String findBydescription);

}
