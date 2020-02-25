package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.MasterTransactAccountType;
import com.finlabs.finexa.model.MasterTransactOccupationCode;
import com.finlabs.finexa.model.MasterTransactSourceOfWealth;

public interface MasterTransactSourceOfWealthRepository extends JpaRepository<MasterTransactSourceOfWealth, String> {

	MasterTransactSourceOfWealthRepository findBysource(String details);
}
