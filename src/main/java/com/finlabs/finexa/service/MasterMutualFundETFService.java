package com.finlabs.finexa.service;

import javax.servlet.http.HttpServletResponse;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;

import jxl.write.WritableWorkbook;

public interface MasterMutualFundETFService {

	public WritableWorkbook downloadMasterMutualFundETF(String masterName, HttpServletResponse response);
	
	WritableWorkbook downloadMasterMutualFundETFTemplate(String masterName, HttpServletResponse response);
	
	public UploadResponseDTO uploadMasterMutualFundETF(FileuploadDTO fileUploadDTO);
	
}
