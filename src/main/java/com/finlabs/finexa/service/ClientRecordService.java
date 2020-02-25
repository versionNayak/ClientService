package com.finlabs.finexa.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientRecordContactSearchDTO;
import com.finlabs.finexa.dto.ClientRecordDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;

import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public interface ClientRecordService {

	public void remapClientByNewUser(ClientRecordDTO clientRecordDTO) throws RuntimeException;

	List<ClientRecordContactSearchDTO> getAllClientNameByClientId(ClientMasterDTO clientMasterDTO);

	public void downloadClientRemappingTemplateCSV(HttpServletResponse response) throws RuntimeException, IOException;

	public WritableWorkbook downloadClientRemappingTemplateXLS(HttpServletResponse response, int advisorID)
			throws RuntimeException, IOException, RowsExceededException, WriteException;

	// public ErrorDTO clientMappingBulkUpload(MultipartFile fileData);
	public List<ClientRecordDTO> getAllUserNameWithClientNameByUserId(int userId, String firstName) throws RuntimeException;

	public UploadResponseDTO clientMappingBulkUpload(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO)
			throws RuntimeException, CustomFinexaException, IOException, BiffException;
	
	public WritableWorkbook downloadErrorLog(String input, HttpServletResponse response) throws RuntimeException;

	
}
