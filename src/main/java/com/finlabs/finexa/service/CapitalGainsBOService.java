package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.CapitalGainsReportColumnDTO;
import com.finlabs.finexa.dto.CapitalGainsReportDTO;
 
public interface CapitalGainsBOService {
	 
	public Map<String, List<CapitalGainsReportColumnDTO>> getCapitalGainsReport(CapitalGainsReportDTO capitalGainsReportDTO) throws RuntimeException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException;


}
