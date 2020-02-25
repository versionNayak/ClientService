package com.finlabs.finexa.service;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.finlabs.finexa.dto.AumMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.BackOfficeUploadHistory;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AumMasterBORepository;
import com.finlabs.finexa.repository.BackOfficeUploadHistoryRepository;
import com.finlabs.finexa.repository.LookupRTABORepository;
import com.finlabs.finexa.repository.LookupRTAMasterFileDetailsBORepository;
import com.finlabs.finexa.repository.LookupTransactionRuleRepository;
import com.linuxense.javadbf.DBFReader;

@Component

@Scope("prototype")

public class SipSTPTest implements Runnable {

	private AumMasterBODTO aumMasterBODTO;
	private UploadResponseDTO uploadResponseDTO;
	private BackOfficeUploadHistory backOfficeUploadHistory;
	DBFReader dbfReader;
	@Autowired
	private Mapper mapper;

	@Autowired
	private AumMasterBORepository aumMasterBORepository;

	@Autowired
	private LookupRTABORepository lookupRTABORepository;

	@Autowired
	private LookupTransactionRuleRepository lookupTransactionRuleRepository;

	@Autowired
	private LookupRTAMasterFileDetailsBORepository lookupRTAMasterFileDetailsBORepository;

	@Autowired BackOfficeUploadHistoryRepository uploadHistoryRepo;

	@Autowired 
	AdvisorUserRepository advisorUserRepository;

	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_REJECTED = "REJECTED";

	public void initialize(AumMasterBODTO aumMasterBODTO, UploadResponseDTO uploadResponseDTO,
			BackOfficeUploadHistory backOfficeUploadHistory) {
		this.aumMasterBODTO = aumMasterBODTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.backOfficeUploadHistory = backOfficeUploadHistory;
	}
	@Override
	public void run() {
		try {
			System.out.println("AdvisorUserRepository" + advisorUserRepository);
			
			System.out.println("AdvisorUserRepository" + mapper);
			
		} catch (RuntimeException e) {

			throw new RuntimeException(e);
		}
	}

}