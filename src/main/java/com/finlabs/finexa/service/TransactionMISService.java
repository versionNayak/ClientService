package com.finlabs.finexa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.finlabs.finexa.dto.TransactionMISDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface TransactionMISService {
	
	public TransactionMISDTO getInflowOutflowList(int advisorId, Date fromDate, Date toDate,String arn) throws FinexaBussinessException ;
	public List<Integer> ClientIdList(String arn);
	public Map<String,String> namePANLIST(String arn);

}
