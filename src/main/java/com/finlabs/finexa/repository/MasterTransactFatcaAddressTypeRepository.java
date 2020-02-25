package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactAddressType;
import com.finlabs.finexa.model.MasterTransactDivPayMode;
import com.finlabs.finexa.model.MasterTransactFatcaAddressType;

public interface MasterTransactFatcaAddressTypeRepository extends JpaRepository<MasterTransactAddressType, String> {

	MasterTransactAddressType findBydetails(String details);

}
