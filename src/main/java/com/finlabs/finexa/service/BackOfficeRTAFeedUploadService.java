package com.finlabs.finexa.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.data.domain.Pageable;

import com.finlabs.finexa.dto.InvestorMasterBODTO;
import com.finlabs.finexa.dto.TransactionMasterBODTO;
import com.finlabs.finexa.dto.UploadHistoryDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;


public interface BackOfficeRTAFeedUploadService {
	
	UploadResponseDTO uploadInvestorMaster(InvestorMasterBODTO investorMasterBODTO, UploadResponseDTO uploadResponseDTO) throws RuntimeException, IOException, InvalidFormatException, ParseException;

	List<UploadHistoryDTO> getAllUploadHistory(int advisorId);

	int getUploadHistoryCount(int advisorId);

	List<UploadHistoryDTO> getFeedUploadHistoryWithPagination(int advisorId, Pageable pageable);
}
