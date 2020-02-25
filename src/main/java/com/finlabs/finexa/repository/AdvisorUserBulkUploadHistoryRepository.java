package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserBulkUploadHistory;

public interface AdvisorUserBulkUploadHistoryRepository extends JpaRepository<AdvisorUserBulkUploadHistory, Integer> {

	public AdvisorUserBulkUploadHistory findByAdvisorUserAndStatus(AdvisorUser advisorUser, String status);

	public int countByAdvisorUser(AdvisorUser advisorUser);

	public List<AdvisorUserBulkUploadHistory> findByAdvisorUser(AdvisorUser advisorUser);
	
	public List<AdvisorUserBulkUploadHistory> findByAdvisorUser(AdvisorUser advisorUser, Pageable pageable);

	public AdvisorUserBulkUploadHistory findByAdvisorUserAndStatusIgnoreCase(AdvisorUser advisorUser, String status);

	public List<AdvisorUserBulkUploadHistory> findByStatusIgnoreCaseAndAdvisorUser(String string, AdvisorUser findOne);

	public List<AdvisorUserBulkUploadHistory> findByStatusIgnoreCaseAndAdvisorUser(String string, AdvisorUser findOne,
			Pageable pageable);
	
	

}
