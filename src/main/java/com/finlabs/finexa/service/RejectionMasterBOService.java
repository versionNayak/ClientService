package com.finlabs.finexa.service;

import java.io.IOException;
import java.text.ParseException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.finlabs.finexa.dto.RejectionMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;

public interface RejectionMasterBOService {

	UploadResponseDTO uploadRejectionMaster(RejectionMasterBODTO rejectionMasterBODTO, UploadResponseDTO uploadResponseDTO) throws RuntimeException, IOException, InvalidFormatException, ParseException;

}
