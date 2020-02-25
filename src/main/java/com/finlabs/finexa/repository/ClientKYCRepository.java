package com.finlabs.finexa.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.ClientCKYCDetail;
import com.finlabs.finexa.model.ClientUCCDetail;
import com.finlabs.finexa.model.ClientUCCDetailsDraftMode;

public interface ClientKYCRepository extends JpaRepository<ClientCKYCDetail, BigInteger> {

	public ClientCKYCDetail getByClientCodeAndSaveMode(String clientCode, String saveMode);
	
	public ClientCKYCDetail findByClientCode(String clientCode);
}
