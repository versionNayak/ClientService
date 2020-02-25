package com.finlabs.finexa.service;

import java.util.Date;

public interface AUMReportBOService {

	public double getTotalClientAUMFromTransaction(int advisorId, Date asOfDate) throws RuntimeException;
	
	public double getTotalClientAUMFromAumTable(int advisorId, Date asOfDate) throws RuntimeException;

}
