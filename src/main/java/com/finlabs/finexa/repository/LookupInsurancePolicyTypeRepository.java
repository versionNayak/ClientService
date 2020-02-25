package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.LookupInsurancePolicyType;

public interface LookupInsurancePolicyTypeRepository extends JpaRepository<LookupInsurancePolicyType, Byte>{

	@Query("SELECT DISTINCT lpt FROM LookupInsurancePolicyType lpt LEFT JOIN lpt.lookupInsuranceType lit WHERE lit.id =:insTypeId ORDER BY lpt.id")
	public List<LookupInsurancePolicyType> getInsurancePolicyTypeList(
			@Param("insTypeId") Byte insTypeId);
	
}
