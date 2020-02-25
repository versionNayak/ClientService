package com.finlabs.finexa.service;

import java.io.IOException;
import java.text.ParseException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.finlabs.finexa.dto.SIPSTPMasterBODTO;
import com.finlabs.finexa.dto.UploadResponseDTO;

public interface SIPSTPMasterBOService {
	UploadResponseDTO uploadSIPSTPMaster(SIPSTPMasterBODTO sIPSTPMasterBODTO, UploadResponseDTO uploadResponseDTO) throws RuntimeException, IOException, InvalidFormatException, ParseException;

}
