package com.finlabs.finexa.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientRecordContactSearchDTO;
import com.finlabs.finexa.dto.ClientRecordDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.dto.UserHierarchyMappingDTO;
import com.finlabs.finexa.dto.UserRoleCreationDTO;
import com.finlabs.finexa.dto.UserRoleReMappingDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupBucketLogic;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.util.FinexaUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("ClientRecordService")
@Transactional

public class ClientRecordServiceImpl implements ClientRecordService {

	private static Logger log = LoggerFactory.getLogger(ViewUserManagmentServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private AdvisorMasterRepository advisorMasterRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	/*************************************
	 * Search Service
	 ***********************************/
	/*
	 * @Override public List<ClientRecordDTO>
	 * getAllUserNameWithClientNameByUserId(int userId, String firstName) throws
	 * RuntimeException { try { // @SuppressWarnings("unused") // AdvisorMaster
	 * advisorMaster = // advisorUserRepository.findOne(userId).getAdvisorMaster();
	 * List<Object[]> userRoleCreationObjectList = advisorUserRepository
	 * .getAllUserNameWithClientNameByUserId(userId, firstName);
	 * List<ClientRecordDTO> clientRecordDTOList = new ArrayList<>(); for (Object[]
	 * obj : userRoleCreationObjectList) { ClientRecordDTO clientRecordDTO = new
	 * ClientRecordDTO(); clientRecordDTO.setUserId((int) obj[0]);
	 * clientRecordDTO.setUserName((String) obj[1]);
	 * clientRecordDTO.setClientId((int) obj[2]);
	 * clientRecordDTO.setClientName((String) obj[3]);
	 * clientRecordDTO.setLocation((String) obj[4]);
	 * clientRecordDTOList.add(clientRecordDTO);
	 * 
	 * } return clientRecordDTOList; } catch (RuntimeException e) { // TODO
	 * Auto-generated catch block throw new RuntimeException(e); } }
	 */
	
	@Override
	public List<ClientRecordDTO> getAllUserNameWithClientNameByUserId(int userId, String firstName)
			throws RuntimeException {
		try {

			AdvisorUser adv = advisorUserRepository.findOne(userId);
//			List<ClientMaster> clientDetails = new ArrayList<>();
//			clientDetails = clientMasterRepository.findByAdvisorUserAndFirstNameContainingIgnoreCase(adv, firstName);
//			if (clientDetails.size() == 0) {
//				clientDetails = clientMasterRepository.findByAdvisorUserAndLastNameContainingIgnoreCase(adv, firstName);
//			}
			List<ClientMaster> clientDetails = clientMasterRepository.findByAdvisorUser(adv);
			
			List<ClientRecordDTO> clientRecordDTOList = new ArrayList<>();
			
			for (ClientMaster obj : clientDetails) {
				//System.out.println("Name : " + (obj.getFirstName() + " " + obj.getLastName()).toLowerCase());
				//System.out.println("Passed Name : " + (firstName.toLowerCase()));
				if ((obj.getFirstName() + " " + obj.getLastName()).toLowerCase().contains(firstName.toLowerCase())) {
					ClientRecordDTO clientRecordDTO = new ClientRecordDTO();
					clientRecordDTO.setUserId(obj.getAdvisorUser().getId());
					clientRecordDTO.setUserName(obj.getAdvisorUser().getFirstName() + " " + (obj.getAdvisorUser().getMiddleName() == null ? " " : obj.getAdvisorUser().getMiddleName()) + " " + obj.getAdvisorUser().getLastName());
					clientRecordDTO.setClientId(obj.getId());
					clientRecordDTO.setClientName(obj.getFirstName() + " " + (obj.getMiddleName() == null ? " " : obj.getMiddleName()) + " " + obj.getLastName());
					clientRecordDTO.setLocation(obj.getAdvisorUser().getCity());
					clientRecordDTOList.add(clientRecordDTO);
				}
			}
			return clientRecordDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remapClientByNewUser(ClientRecordDTO clientRecordDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientRecordDTO.getClientId());
			if (clientMaster != null) {
				clientMaster.setAdvisorUser(advisorUserRepository.findOne(clientRecordDTO.getUserId()));
				clientMaster.setFinexaUser("Y");
				clientMasterRepository.save(clientMaster);
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientRecordContactSearchDTO> getAllClientNameByClientId(ClientMasterDTO clientMasterDTO) {
		// TODO Auto-generated method stub;
		ArrayList<Integer> filteredClientIdList = new ArrayList<Integer>();

		if (clientMasterDTO.getUserId() > 0) {
			AdvisorUser advUser = advisorUserRepository.findOne(clientMasterDTO.getUserId());
			List<ClientMaster> clientMasterList = clientMasterRepository.findByAdvisorUserAndActiveFlag(advUser, "Y");
			for (ClientMaster obj : clientMasterList) {
				filteredClientIdList.add(obj.getId());
			}
		}
		// search by Location
		if (clientMasterDTO.getCountryOfResidence() > 0) {
			List<ClientMaster> masterList = clientMasterRepository
					.findByCountry(clientMasterDTO.getCountryOfResidence());

			if (masterList != null && !masterList.isEmpty()) {
				for (int index = 0; index < filteredClientIdList.size(); index++) {
					int searchFlag = 0;
					for (ClientMaster obj : masterList) {
						if (filteredClientIdList.get(index) == obj.getId()) {
							searchFlag = 1;
							break;
						}
					}
					if (searchFlag == 1) {
						// selected item
					} else {
						filteredClientIdList.remove(filteredClientIdList.get(index));
					}
				}
			}

		}

		// search by marital Status
		if (clientMasterDTO.getMaritalStatus() > 0) {
			List<ClientMaster> masterList = clientMasterRepository
					.findByMaritalStatus((int) clientMasterDTO.getMaritalStatus());
			if (masterList != null && !masterList.isEmpty()) {
				for (int index = 0; index < filteredClientIdList.size(); index++) {
					int searchFlag = 0;
					for (ClientMaster obj : masterList) {
						if (filteredClientIdList.get(index) == obj.getId()) {
							searchFlag = 1;
							break;
						}
					}
					if (searchFlag == 1) {
						// selected item
					} else {
						filteredClientIdList.remove(filteredClientIdList.get(index));
					}
				}
			}

		}

		// search by Gender
		if (clientMasterDTO.getGender() != null && !clientMasterDTO.getGender().equals("")) {
			List<ClientMaster> masterList = clientMasterRepository.findByGenderLike(clientMasterDTO.getGender());
			if (masterList != null && !masterList.isEmpty()) {
				for (int index = 0; index < filteredClientIdList.size(); index++) {
					int searchFlag = 0;
					for (ClientMaster obj : masterList) {
						if (filteredClientIdList.get(index) == obj.getId()) {
							searchFlag = 1;
							break;
						}
					}
					if (searchFlag == 1) {
						// selected item
					} else {
						filteredClientIdList.remove(filteredClientIdList.get(index));
					}
				}
			}
		}

		// search By Already Retired
		if (clientMasterDTO.getRetiredFlag() != null && !clientMasterDTO.getRetiredFlag().equals("")) {
			List<ClientMaster> masterList = clientMasterRepository
					.findByRetiredFlagLike(clientMasterDTO.getRetiredFlag());
			if (masterList != null && !masterList.isEmpty()) {
				for (int index = 0; index < filteredClientIdList.size(); index++) {
					int searchFlag = 0;
					for (ClientMaster obj : masterList) {
						if (filteredClientIdList.get(index) == obj.getId()) {
							searchFlag = 1;
							break;
						}
					}
					if (searchFlag == 1) {
						// selected item
					} else {
						filteredClientIdList.remove(filteredClientIdList.get(index));
					}
				}
			}

		}

		List<ClientRecordContactSearchDTO> clientRecordDTOList = new ArrayList<>();

		// now finally get the filtered clientMasterList
		if (filteredClientIdList.size() > 0) {
			for (int index = 0; index < filteredClientIdList.size(); index++) {
				ClientRecordContactSearchDTO serachDTO = new ClientRecordContactSearchDTO();
				ClientMaster cm = clientMasterRepository.findOne(filteredClientIdList.get(index));
				serachDTO.setClientID(cm.getId());
				String name = cm.getFirstName();
				if (cm.getMiddleName() == null) {
					name = name + " " + cm.getLastName();
				} else {
					name = name + " " + cm.getMiddleName() + " " + cm.getLastName();
				}
				serachDTO.setClientName(name);
				serachDTO.setUserId(cm.getAdvisorUser().getId());
				serachDTO.setUserName(cm.getAdvisorUser().getFirstName());

				serachDTO.setCountryId(cm.getLookupCountry().getId());
				serachDTO.setCountryName(cm.getLookupCountry().getName());
				serachDTO.setGender(cm.getGender());

				// getting clientRecord
				ClientContact clientContact = cm.getClientContacts().get(0);
				serachDTO.setEmail(clientContact.getEmailID());
				serachDTO.setMobile(clientContact.getMobile());
				if (clientContact.getOfficeAddressLine1() != null) {
					serachDTO.setAddress(clientContact.getOfficeAddressLine1());
					serachDTO.setCity(clientContact.getOfficeCity());
					serachDTO.setState(clientContact.getOfficeState());
				} else if (clientContact.getPermanentAddressLine1() != null) {
					serachDTO.setAddress(clientContact.getPermanentAddressLine1());
					serachDTO.setCity(clientContact.getPermanentCity());
					serachDTO.setState(clientContact.getPermanentState());
				} else if (clientContact.getCorrespondenceAddressLine1() != null) {
					serachDTO.setAddress(clientContact.getCorrespondenceAddressLine1());
					serachDTO.setCity(clientContact.getCorrespondenceCity());
					serachDTO.setState(clientContact.getCorrespondenceState());
				}
				clientRecordDTOList.add(serachDTO);
			}
		}

		return clientRecordDTOList;
	}

	/*************************************
	 * End of Search Service
	 ***********************************/

	@Override
	public void downloadClientRemappingTemplateCSV(HttpServletResponse response) throws RuntimeException, IOException {
		// TODO Auto-generated method stub
		try {
			String csvFileName = "ClientRemappingTemplate.csv";

			response.setContentType("text/csv");

			// creates mock data
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
			response.setHeader(headerKey, headerValue);

			// uses the Super CSV API to generate CSV data from the model data
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

			String[] header = { "Client Name*", "Client Email*", "Current User Name*", "Current User Email*", "Current User Role*", 
								"Remapped User Name*", "Remapped User Email*", "Remapped User Role*", "Remapping Date*", "Remarks*" };

			csvWriter.writeHeader(header);
			csvWriter.close();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public WritableWorkbook downloadClientRemappingTemplateXLS(HttpServletResponse response, int advisorID)
			throws RuntimeException, IOException, RowsExceededException, WriteException {
		try {
			String fileName = "ClientRemappingTemplate.xls";
			WritableWorkbook writableWorkbook = null;

			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());

			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addExcelOutputHeader(excelOutputsheet);
			// writeExcelOutputData(excelOutputsheet);
			
			WritableSheet clientSheet = writableWorkbook.createSheet("Client Information", 1);
			addClientSheetHeader(clientSheet);
			addClientSheetData(clientSheet, advisorID);

			WritableSheet userSheet = writableWorkbook.createSheet("Available Users", 2);
			addUserSheetHeader(userSheet);
			addUserSheetData(userSheet, advisorID);

			writableWorkbook.write();
			writableWorkbook.close();

			return writableWorkbook;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}	
	}

	private void addClientSheetHeader(WritableSheet clientSheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		clientSheet.addCell(new Label(0, 0, "CLient Name"));
		clientSheet.addCell(new Label(1, 0, "CLient Email"));
		clientSheet.addCell(new Label(2, 0, "Mapped User Name"));
		clientSheet.addCell(new Label(3, 0, "Mapped User Email"));
		clientSheet.addCell(new Label(4, 0, "Mapped User Role"));
	}
	
	private void addClientSheetData(WritableSheet sheet, int advisorID) throws RowsExceededException, WriteException {

		int i = 1;
		//List<ClientMaster> clientMasters;
		List<AdvisorUser> advisorMasters = advisorUserRepository.findById(advisorID).getAdvisorMaster().getAdvisorUsers();
		
		for (AdvisorUser obj : advisorMasters) {
			for (ClientMaster ob : obj.getClientMasters()) {
				sheet.addCell(new Label(0, i, "" + ob.getFirstName() + " " + ob.getLastName()));
				sheet.addCell(new Label(1, i, ob.getLoginUsername()));
				sheet.addCell(new Label(2, i, ob.getAdvisorUser().getFirstName() + " " + ob.getAdvisorUser().getLastName()));
				sheet.addCell(new Label(3, i, ob.getAdvisorUser().getEmailID()));
				sheet.addCell(new Label(4, i, ob.getAdvisorUser().getUser().getAdvisorRole().getRoleDescription()));
				i++;
			}
		}

	}
	
	private void addUserSheetHeader(WritableSheet userSheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		userSheet.addCell(new Label(0, 0, "User Name"));
		userSheet.addCell(new Label(1, 0, "User Email"));
		userSheet.addCell(new Label(2, 0, "User Role"));
	}
	
	private void addUserSheetData(WritableSheet sheet, int advisorID) throws RowsExceededException, WriteException {

		int i = 1;
		//List<ClientMaster> clientMasters;
		List<AdvisorUser> advisorMasters = advisorUserRepository.findById(advisorID).getAdvisorMaster().getAdvisorUsers();
		
		for (AdvisorUser obj : advisorMasters) {
			//if (!obj.getUser().getAdvisorRole().getRoleDescription().equalsIgnoreCase("admin")) {
				sheet.addCell(new Label(0, i, "" + obj.getFirstName() + " " + obj.getLastName()));
				sheet.addCell(new Label(1, i, obj.getLoginUsername()));
				sheet.addCell(new Label(2, i, obj.getUser().getAdvisorRole().getRoleDescription()));
				i++;
			//}
		}

	}

	private void addExcelOutputHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub

		// create header row
		sheet.addCell(new Label(0, 0, "Client Name*"));
		sheet.addCell(new Label(1, 0, "Client Email*"));
		sheet.addCell(new Label(2, 0, "Current User Name*"));
		sheet.addCell(new Label(3, 0, "Current User Email*"));
		sheet.addCell(new Label(4, 0, "Current User Role*"));
		sheet.addCell(new Label(5, 0, "Remapped User Name*"));
		sheet.addCell(new Label(6, 0, "Remapped User Email*"));
		sheet.addCell(new Label(7, 0, "Remapped User Role*"));
		sheet.addCell(new Label(8, 0, "Remapping Date*"));
		sheet.addCell(new Label(9, 0, "Remarks*"));

	}

	@Override
	@Transactional(rollbackOn = CustomFinexaException.class)
	public UploadResponseDTO clientMappingBulkUpload(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO)
			throws RuntimeException, CustomFinexaException, IOException, BiffException {

		try {
			if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".csv")) {
				// call csv upload
				uploadResponseDTO = clientMappingBulkUploadCsvFile(fileuploadDTO, uploadResponseDTO);

			} else if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".xls")) {
				// call excel upload
				uploadResponseDTO = clientMappingBulkUploadExcelFile(fileuploadDTO, uploadResponseDTO);
			}
			return uploadResponseDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private UploadResponseDTO clientMappingBulkUploadCsvFile(FileuploadDTO fileuploadDTO,
			UploadResponseDTO uploadResponseDTO) throws RuntimeException, CustomFinexaException, IOException {

		String line = "";
		int rowNum = 0;
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {
			while ((line = br.readLine()) != null) {
				if (rowNum == 0) {
					rowNum++;
					continue;
				}

				String[] fields = line.split(cvsSplitBy);
				
				AdvisorUser advUser = advisorUserRepository.findByEmailID(fields[6]);
				ClientMaster clientMaster = clientMasterRepository.findByLoginUsername(fields[1]);
				
				clientMaster.setAdvisorUser(advUser);

			}
			return uploadResponseDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Remapping Bulk Upload", "Nothing Specific",
					"Failed to upload CSV File to remap Client", e);
		}

	}

	private UploadResponseDTO clientMappingBulkUploadExcelFile(FileuploadDTO fileuploadDTO,
			UploadResponseDTO uploadResponseDTO)
			throws RuntimeException, IOException, BiffException, CustomFinexaException {

		try {
			Workbook workbook = null;

			workbook = Workbook.getWorkbook(fileuploadDTO.getFile()[0].getInputStream());

			Sheet sheet = workbook.getSheet(0);
			for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
				
				AdvisorUser advUser = advisorUserRepository.findByEmailID(sheet.getCell(6, rownum).getContents());
				ClientMaster clientMaster = clientMasterRepository.findByLoginUsername(sheet.getCell(1, rownum).getContents());
				
				clientMaster.setAdvisorUser(advUser);

			}
			return uploadResponseDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Remapping Bulk Upload", "Nothing Specific",
					"Failed to upload EXCEL File to remap Client", e);
		}
	}
	
	private void addImportErrorLogExcelOutputHeader(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// create header row
		sheet.addCell(new Label(0, 0, "Sr. No"));
		sheet.addCell(new Label(1, 0, "Error Log"));
	}

	@Override
	public WritableWorkbook downloadErrorLog(String input, HttpServletResponse response) throws RuntimeException {
		// TODO Auto-generated method stub
		String fileName = "ImportClientRecordsErrorLog.xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Excel Output", 0);
			addImportErrorLogExcelOutputHeader(excelOutputsheet);
			
			addImportErrorLogExcelOutputBody(input, excelOutputsheet);
			
			writableWorkbook.write();
			writableWorkbook.close();
			
		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}
	
	private void addImportErrorLogExcelOutputBody(String input, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// creating Body
		int rowIndex = 1;// because header row is already added
		if (input != null && input != "") {
			int i = 1;
			for (i=1;i==rowIndex;i++){
				sheet.addCell(new Label(0, rowIndex, ""+i));
				sheet.addCell(new Label(1, rowIndex, input));
				rowIndex++;
				i++;
			}
		}
	}
	
	
}
