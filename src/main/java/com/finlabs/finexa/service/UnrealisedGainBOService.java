package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.CurrentHoldingReportColumnDTO;
import com.finlabs.finexa.dto.CurrentHoldingReportDTO;

public interface UnrealisedGainBOService {
	
	 public Map<String, List<CurrentHoldingReportColumnDTO>>    unrealisedGainReport(CurrentHoldingReportDTO currentHoldingReportDTO) throws RuntimeException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException;

}
