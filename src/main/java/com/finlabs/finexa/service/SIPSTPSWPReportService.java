package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.SIPSTPSWPReportColumnDTO;
import com.finlabs.finexa.dto.SIPSTPSWPReportDTO;


public interface SIPSTPSWPReportService {
	
	public Map<String, List<SIPSTPSWPReportColumnDTO>> sipSTPSWPReport(SIPSTPSWPReportDTO sipSTPSWPReportDTO)  throws RuntimeException, ParseException;

}
