package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.finlabs.finexa.model.ClientAccessRight;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.ClientMaster;

import java.math.BigInteger;
import java.util.List;

public interface ClientContactRepository extends JpaRepository<ClientContact, Integer> {
	ClientContact findByEmailID(String emailID);

	ClientContact findByMobile(BigInteger mobile);

	//void findAllByCities();
	
	List<ClientContact> findByCorrespondenceCityIsNotNull();
	List<ClientContact> findByPermanentCityIsNotNull();
	List<ClientContact> findByOfficeCityIsNotNull();
	
	public ClientContact findByClientMaster(ClientMaster clientMaster);
	
	@Query("SELECT cc.mobile FROM ClientContact cc")
	List<BigInteger> getMobile();
	
	@Query("SELECT cc.emailID FROM ClientContact cc")
	List<String> getEmail();
}
