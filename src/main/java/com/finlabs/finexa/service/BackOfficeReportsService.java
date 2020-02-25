package com.finlabs.finexa.service;

import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.AumReportColumnDTO;
import com.finlabs.finexa.dto.AumReportDTO;

public interface BackOfficeReportsService {
	
	public Map<String,List<AumReportColumnDTO>> aumReport(AumReportDTO aumReportDTO) throws RuntimeException;
	
}
