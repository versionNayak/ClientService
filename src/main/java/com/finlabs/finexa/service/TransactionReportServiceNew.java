package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
import com.finlabs.finexa.dto.TransactionReportDetailedDTO;
import com.finlabs.finexa.dto.TransactionReportDetailedDTOSecondOption;

public interface TransactionReportServiceNew {
	
	public List<TransactionReportDetailedDTOSecondOption> transactionReport(TransactionReportDTO transactionReportDTO) throws RuntimeException, ParseException; 

}
