package com.finlabs.finexa.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.finlabs.finexa.dto.AccessRightDTO;
import com.finlabs.finexa.dto.MasterNAVHistoryDTO;
import com.finlabs.finexa.model.AdvisorRoleSubmoduleMapping;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public interface AccordMonitoringService {

	public List<MasterNAVHistoryDTO> findSchemeNoDetailsList();
	
	public List<MasterNAVHistoryDTO> findSchemeNoDetailsList(Pageable pageable);

	public WritableWorkbook downloadNoNAVDataExcel(HttpServletResponse response);
	
	
}
