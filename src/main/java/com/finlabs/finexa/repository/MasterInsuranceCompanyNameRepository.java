package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.MasterInsuranceCompanyName;

public interface MasterInsuranceCompanyNameRepository extends JpaRepository<MasterInsuranceCompanyName, Integer>{

	@Query("SELECT DISTINCT micn FROM MasterInsuranceCompanyName micn ORDER BY micn.id")
	public List<MasterInsuranceCompanyName> getInsuranceCompanyNameList(
			);

}
