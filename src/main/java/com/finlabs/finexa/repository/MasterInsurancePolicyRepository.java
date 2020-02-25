package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finlabs.finexa.model.LookupInsurancePolicyType;
import com.finlabs.finexa.model.MasterInsuranceCompanyName;
import com.finlabs.finexa.model.MasterInsurancePolicy;
import com.finlabs.finexa.model.LookupInsurancePolicyType;

public interface MasterInsurancePolicyRepository extends JpaRepository<MasterInsurancePolicy, Integer> {

	@Query("SELECT DISTINCT micn FROM MasterInsurancePolicy mip LEFT JOIN mip.masterInsuranceCompanyName micn LEFT JOIN mip.lookupInsurancePolicyType lpt LEFT JOIN lpt.lookupInsuranceType lit WHERE lit.id =:insTypeId ORDER BY   micn.id")
	//@Query("SELECT DISTINCT micn FROM masterInsuranceCompanyName micn LEFT JOIN micn.lookupinsurancetype lit WHERE lit.id =:insTypeId ORDER BY micn.id")
	public List<MasterInsuranceCompanyName> getInsuranceCompanyNameList(
			@Param("insTypeId") Byte insTypeId);

	//@Query("SELECT DISTINCT lpt FROM lookupinsurancepolicytype lpt LEFT JOIN lpt.lookupinsurancetype lit WHERE lit.id =:insTypeId ORDER BY lpt.id")
	//public List<LookupInsurancePolicyType> getInsurancePolicyTypeList(
	//		@Param("insTypeId") Byte insTypeId);

	@Query("SELECT DISTINCT mip FROM MasterInsurancePolicy mip LEFT JOIN mip.masterInsuranceCompanyName micn LEFT JOIN mip.lookupInsurancePolicyType lpt LEFT JOIN lpt.lookupInsuranceType lit WHERE lit.id =:insTypeId AND micn.id=:insCompanyId ")
	public List<MasterInsurancePolicy> getInsurancePolicyTypeForCompanyId(
			@Param("insTypeId") Byte insTypeId,@Param("insCompanyId") Integer insCompanyId);
	
	@Query("SELECT DISTINCT mip FROM MasterInsurancePolicy mip LEFT JOIN mip.masterInsuranceCompanyName micn LEFT JOIN mip.lookupInsurancePolicyType lpt LEFT JOIN lpt.lookupInsuranceType lit WHERE lit.id =:insTypeId AND micn.id=:insCompanyId AND lpt.id=:insPolicyTypeId")
	public MasterInsurancePolicy getInsurancePolicyNameForCompanyIdAndPolicyTypeId(
			@Param("insTypeId") Byte insTypeId,@Param("insCompanyId") Integer insCompanyId,@Param("insPolicyTypeId") Byte insPolicyTypeId);
	
	@Query("SELECT DISTINCT mip FROM MasterInsurancePolicy mip LEFT JOIN mip.masterInsuranceCompanyName micn LEFT JOIN mip.lookupInsurancePolicyType lpt LEFT JOIN lpt.lookupInsuranceType lit WHERE lit.id =:insTypeId AND lpt.id=:insPolicyTypeId ")
	public List<MasterInsurancePolicy> getInsuranceCompanyForPolicyId(
			@Param("insTypeId") Byte insTypeId,@Param("insPolicyTypeId") Byte insPolicyTypeId);
	
	@Query("SELECT DISTINCT lpt FROM MasterInsurancePolicy mip LEFT JOIN mip.masterInsuranceCompanyName micn LEFT JOIN mip.lookupInsurancePolicyType lpt LEFT JOIN lpt.lookupInsuranceType lit WHERE lit.id =:insTypeId GROUP BY lpt.id")
	public List<LookupInsurancePolicyType> getInsuranceCompanyPolicyListByType(
			@Param("insTypeId") Byte insTypeId);
	
	
	
}