package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;

public interface TransactionReportService {
	
	public Map<String, List<TransactionReportColumnDTO>> transactionReport(TransactionReportDTO transactionReportDTO) throws RuntimeException, ParseException; 

}
