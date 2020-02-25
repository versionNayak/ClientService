package com.finlabs.finexa.service;

import javax.servlet.http.HttpServletResponse;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;

import jxl.write.WritableWorkbook;

public interface LookupAssetClassService {
	
	public WritableWorkbook downloadLookupAssetClass(String masterName, HttpServletResponse response);

	WritableWorkbook downloadLookupAssetClassTemplate(String masterName, HttpServletResponse response);

	public UploadResponseDTO uploadLookupAssetClass(FileuploadDTO fileUploadDTO);

}
