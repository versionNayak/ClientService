package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.DividendReportColumnDTO;
import com.finlabs.finexa.dto.DividendReportColumnNewDTO;
import com.finlabs.finexa.dto.DividendReportDTO;
import com.finlabs.finexa.dto.PortfolioValuationReportColumnDTO;
import com.finlabs.finexa.dto.PortfolioValuationReportDTO;
import com.finlabs.finexa.dto.TransactionReportColumnDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;

public interface PortfolioValuationReportservice {
	
	public Map<String,List<PortfolioValuationReportColumnDTO>> portfolioValuationReport(PortfolioValuationReportDTO portfolioValuationReportDTO) throws RuntimeException, ParseException; 

}
