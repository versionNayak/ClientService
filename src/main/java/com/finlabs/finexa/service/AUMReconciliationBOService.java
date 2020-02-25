package com.finlabs.finexa.service;

import java.util.List;
import com.finlabs.finexa.dto.AumReconciliationReportColumnDTO;

public interface AUMReconciliationBOService {

	List<AumReconciliationReportColumnDTO> generateAUMReconciliationReport(int advisorID);
}
