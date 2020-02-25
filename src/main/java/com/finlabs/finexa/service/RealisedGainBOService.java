package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.CurrentHoldingReportColumnDTO;
import com.finlabs.finexa.dto.CurrentHoldingReportDTO;
import com.finlabs.finexa.dto.TransactionReportDTO;
 
public interface RealisedGainBOService {
	 
	public Map<String, List<CurrentHoldingReportColumnDTO>>	realisedGainReport(CurrentHoldingReportDTO currentHoldingReportDTO) throws RuntimeException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException;


}
