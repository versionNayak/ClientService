package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.LookupTransactKYCType;
import com.finlabs.finexa.model.LookupTransactUCCClientType;

public interface LookupTransactKYCTypeRepository extends JpaRepository<LookupTransactKYCType, Byte> {

	LookupTransactKYCType findBydescription(String findBydescription);

}
