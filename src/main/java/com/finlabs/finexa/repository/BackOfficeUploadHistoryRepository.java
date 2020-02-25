package com.finlabs.finexa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.model.LookupRTABO;
import com.finlabs.finexa.model.LookupRTAFileName;

public interface BackOfficeUploadHistoryRepository extends JpaRepository<BackOfficeUploadHistory, Integer>{

	/*@Query("SELECT uploadHistory FROM BackOfficeUploadHistory uploadHistory WHERE uploadHistory.advisorUser.Id = :invName AND tmbo.transactionDate between :fromDate AND :toDate AND tmbo.schemeName LIKE :scheme")
	public BackOfficeUploadHistory findCurrentStatus(@Param("invName") String invName, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("scheme") String scheme);
*/
	public BackOfficeUploadHistory findByAdvisorUserAndLookupRtaboAndLookupRtafileNameAndFileNameAndStatusAndAutoClientCreationStatusAndReasonOfRejectionOrderByStartTimeAsc(AdvisorUser advUser,
			LookupRTABO rtaBO, LookupRTAFileName fileType, String fileName, String status, String autoClientCreationStatus, String reasonOfRejection);
	
	public List<BackOfficeUploadHistory> findByAdvisorUser(AdvisorUser adv);

	public int countByAdvisorUser(AdvisorUser advisorUser);

	public List<BackOfficeUploadHistory> findByAdvisorUser(AdvisorUser advUser, Pageable pageable);
}

