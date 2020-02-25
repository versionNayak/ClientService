package com.finlabs.finexa.service;

import java.util.List;

import com.finlabs.finexa.dto.MasterTablesUploadHistoryDTO;

public interface MasterUploadHistoryService {
	
	public List<MasterTablesUploadHistoryDTO> findByLoggedInId(int loggedId) throws RuntimeException;

}
