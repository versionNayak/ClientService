package com.finlabs.finexa.service;

import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.PortfolioGainLossReportColumnDTO;
import com.finlabs.finexa.dto.PortfolioGainLossReportDTO;

public interface PortfolioGainLossRpeortBOService {

	public Map<String,List<PortfolioGainLossReportColumnDTO>> portfolioGainLossReport (PortfolioGainLossReportDTO portfolioGainLossReportDTO);
	
}
