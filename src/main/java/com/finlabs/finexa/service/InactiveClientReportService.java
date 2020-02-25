package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.InactiveClientReportColumnDTO;
import com.finlabs.finexa.dto.InactiveClientReportDTO;

public interface InactiveClientReportService {
	
	public List<InactiveClientReportColumnDTO>	inactiveClientReport(InactiveClientReportDTO inactiveClientReportDTO) throws RuntimeException, ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException;


}
