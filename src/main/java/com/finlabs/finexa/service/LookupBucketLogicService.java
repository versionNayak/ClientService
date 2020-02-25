package com.finlabs.finexa.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;

import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public interface LookupBucketLogicService {

	WritableWorkbook downloadLookupBucketLogicTemplate(String masterName, HttpServletResponse response)
			throws RuntimeException, IOException, RowsExceededException, WriteException;

	public WritableWorkbook downloadLookupBucketLogic(String masterName, HttpServletResponse response)
			throws RuntimeException;

	public UploadResponseDTO uploadLookupBucketLogic(FileuploadDTO fileUploadDTO)
			throws RuntimeException, BiffException, IOException;

	public void downloadLookupBucketLogicTemplateCSV(HttpServletResponse response) throws RuntimeException, IOException;

	public UploadResponseDTO uploadLookupBucketLogicCSV(FileuploadDTO fileuploadDTO)
			throws RuntimeException, IOException;

}
