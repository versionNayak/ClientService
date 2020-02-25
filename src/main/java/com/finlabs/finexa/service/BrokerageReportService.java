package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.BrokerageReportColumnDTO;
import com.finlabs.finexa.dto.BrokerageReportDTO;

public interface BrokerageReportService {
	
	public Map<String,List<BrokerageReportColumnDTO>> brokerageReport(BrokerageReportDTO brokerageReportDTO) throws RuntimeException, ParseException; 

}
