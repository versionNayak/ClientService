package com.finlabs.finexa.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;

import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public interface LookupAssetAllocationService {

	public WritableWorkbook downloadLookupAssetAllocation(String masterName, HttpServletResponse response)
			throws RuntimeException;

	WritableWorkbook downloadLookupAssetAllocationTemplate(String masterName, HttpServletResponse response)
			throws RuntimeException, IOException, RowsExceededException, WriteException;

	public UploadResponseDTO uploadLookupAssetAllocation(FileuploadDTO fileUploadDTO)
			throws RuntimeException, BiffException, IOException;

	public void downloadLookupAssetAllocationTemplateCSV(HttpServletResponse response)
			throws RuntimeException, IOException;

	public UploadResponseDTO uploadLookupAssetAllocationCSV(FileuploadDTO fileuploadDTO)
			throws RuntimeException, IOException;
}
