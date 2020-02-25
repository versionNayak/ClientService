package com.finlabs.finexa.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;

import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public interface LookupAssetAllocationCategoryService {

	public WritableWorkbook downloadLookupAssetAllocationCategory(String masterName, HttpServletResponse response)
			throws RuntimeException;

	WritableWorkbook downloadLookupAssetAllocationCategoryTemplate(String masterName, HttpServletResponse response)
			throws RuntimeException, IOException, RowsExceededException, WriteException;

	public UploadResponseDTO uploadLookupAssetAllocationCategory(FileuploadDTO fileUploadDTO)
			throws RuntimeException, BiffException, IOException;

	public void downloadLookupAssetAllocationCategoryTemplateCSV(HttpServletResponse response)
			throws RuntimeException, IOException;

	public UploadResponseDTO uploadLookupAssetAllocationCategoryCSV(FileuploadDTO fileuploadDTO)
			throws RuntimeException, IOException;

}
