package com.finlabs.finexa.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientRecordContactSearchDTO;
import com.finlabs.finexa.dto.ClientRecordDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.dto.UserDTO;
import com.finlabs.finexa.dto.UserRoleCreationDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupEducationalQualification;
import com.finlabs.finexa.model.LookupEmploymentType;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.LookupResidentType;
import com.finlabs.finexa.model.LookupRole;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.LookupRoleRepository;
import com.finlabs.finexa.service.ClientRecordService;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@RestController
public class ClientRecordController {
	private static Logger log = LoggerFactory.getLogger(ClientRecordController.class);

	@Autowired
	ClientRecordService clientRecordService;
	@Autowired
	ClientMasterRepository clientMasterRepository;
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	AdvisorRoleRepository advisorRoleRepository;
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	LookupRoleRepository lookupRoleRepository;
	
	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/getAllClients/{userId}/{firstName}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllClients(@PathVariable int userId,
			@PathVariable String firstName) throws FinexaBussinessException {
		try {
			List<ClientRecordDTO> userList = clientRecordService.getAllUserNameWithClientNameByUserId(userId,
					firstName);
			return new ResponseEntity<List<ClientRecordDTO>>(userList, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_CLIENT_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_VIEW_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_VIEW_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "clientRecord/getAllClientNameByClientId", method = RequestMethod.POST)
	public ResponseEntity<?> getAllClientNameByClientId(@RequestBody SearchClientDTO searchClientDTO) {

		return null;
	}

	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "clientRecord/remapClientByUser", method = RequestMethod.POST)
	public ResponseEntity<?> remapClientByUser(@RequestBody ClientRecordDTO clientRecordDTO)
			throws FinexaBussinessException {
		try {
			clientRecordService.remapClientByNewUser(clientRecordDTO);
			return new ResponseEntity<ClientRecordDTO>(clientRecordDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_CLIENT_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_REMAP_CLIENT_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_REMAP_CLIENT_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/{fileType}/downloadClientRemappingTemplate/{advisorID}", method = RequestMethod.GET)
	public ResponseEntity<?> downloadClientRemappingTemplate(@PathVariable String fileType, @PathVariable int advisorID,
			HttpServletResponse response)
			throws IOException, FinexaBussinessException, RowsExceededException, WriteException {
		try {
			if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_CSV)) {
				clientRecordService.downloadClientRemappingTemplateCSV(response);
			} else if (fileType.equalsIgnoreCase(FinexaConstant.FILE_TYPE_EXCEL)) {
				clientRecordService.downloadClientRemappingTemplateXLS(response, advisorID);
			}

			return null;
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_CLIENT_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_DOWNLOAD_TEMPLATE_ERROR);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_DOWNLOAD_TEMPLATE_ERROR,
					exception != null ? exception.getErrorMessage() : "", e);
		}
	}

	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clientRecord/bulkUpload", method = RequestMethod.POST)
	public ResponseEntity<?> bulkUpload(@ModelAttribute FileuploadDTO fileuploadDTO)
			throws FinexaBussinessException, CustomFinexaException, IOException, BiffException {
		try {
			UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();
			// validate the uploading file and return error else call service
			if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".xls")) {
				if (validUploadFileExcel(fileuploadDTO, uploadResponseDTO)) {
					uploadResponseDTO = clientRecordService.clientMappingBulkUpload(fileuploadDTO, uploadResponseDTO);
				}
			}
			if (fileuploadDTO.getFile()[0].getOriginalFilename().contains(".csv")) {
				if (validUploadFileCsv(fileuploadDTO, uploadResponseDTO)) {
				uploadResponseDTO = clientRecordService.clientMappingBulkUpload(fileuploadDTO, uploadResponseDTO);
			   }
			}

			return new ResponseEntity<UploadResponseDTO>(uploadResponseDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
					.findByCode(FinexaConstant.MY_BUSINESS_CLIENT_RECORDS_CLIENT_MAPPING);
			FinexaExceptionHandling exception = finexaExceptionHandlingRepository
					.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
							FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_UPLOAD_CLIENT_REMAPPING);
			throw new FinexaBussinessException(FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_MODULE,
					FinexaConstant.MY_BUSINESS_CLIENT_REMAPPING_UPLOAD_CLIENT_REMAPPING,
					exception != null ? exception.getErrorMessage() : "", e);
		}

	}

	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	private boolean validUploadFileCsv(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO) {

		String line = "";
		long rowNum = 0;
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream()))) {

			if (new BufferedReader(new InputStreamReader(fileuploadDTO.getFile()[0].getInputStream())).lines()
					.count() < 2) {
				uploadResponseDTO.getErrors().add("No Data in CSV file");
			}

			while ((line = br.readLine()) != null) {
				if (rowNum == 0) {
					rowNum++;
					continue;
				}
				rowNum++;

				long displayRow = rowNum;
				String[] fields = line.split(cvsSplitBy);
			
				// User email
				if (StringUtils.isEmpty(fields[3])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) : " + "Current User Email should not be Empty");
				} else {

					AdvisorUser currUser = advisorUserRepository
							.findByEmailID(fields[3]);
					if (currUser == null) {
						uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) : " + "Current User doesn't exist");
					} else {
						if (StringUtils.isNotEmpty(fields[6])) {
							AdvisorUser reamppedUser = advisorUserRepository
									.findByEmailID(fields[6]);
							if (currUser == reamppedUser) {
								uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) : " + "Current User and Remapped User must not be same");
							}
						}
					}
				}
				// current user name
				if (StringUtils.isEmpty(fields[2])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", C) : " + "Current User Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[2])) {
						if (!StringUtils.isAlphaSpace(fields[2])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", C) : " + "Current User Name should contain alphabets");
						}
					}
				}
				// remapped user name
				if (StringUtils.isEmpty(fields[5])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", F) : " + "Remapped User Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[5])) {
						if (!StringUtils.isAlphaSpace(fields[5])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", F) : " + "Remapped User Name should contain alphabets");
						}
					}
				}
				// client name
				if (StringUtils.isEmpty(fields[0])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A) : " + "Client Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[0])) {
						if (!StringUtils.isAlphaSpace(fields[0])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", A) : " + "Client Name should contain alphabets");
						}
					}
				}
				// current user Role
				if (StringUtils.isEmpty(fields[4])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", E) : " + "Current User Role should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[4])) {
						if (!StringUtils.isAlphaSpace(fields[4])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", E) : " + "Current User Role should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(fields[4])) {
								LookupRole role = lookupRoleRepository
										.findByDescription(fields[4]);
								if (role == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", E) : " + "Current User Role doesn't exist");
								} else {
									if (StringUtils.isNotEmpty(fields[3])) {
										AdvisorUser currUser = advisorUserRepository
												.findByEmailID(fields[3]);
										if (!currUser.getUser().getAdvisorRole().getRoleDescription().equalsIgnoreCase(role.getDescription())) {
											System.out.println("curr usr role " + currUser.getUser().getAdvisorRole().getRoleDescription());
											System.out.println("curr usr lookup role " + role.getDescription());
											uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", E) : " + "Current User has a different Role than provided");
										}
									}
								}
							}
						}
					}
				}
				// Client email
				if (StringUtils.isEmpty(fields[1])) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", B) : " + "Client Email should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[1])) {

						// ClientMaster clientMaster =
						// clientMasterRepository.getByFirstNameAndLastName(parts[0], parts[1]);
						ClientMaster clientMaster = clientMasterRepository
								.findByLoginUsername(fields[1]);

						if (clientMaster == null) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", B) : " + "Client doesnt exist");
						} else {
							if (StringUtils.isNotEmpty(fields[3])) {
								AdvisorUser currUser = advisorUserRepository
										.findByEmailID(fields[3]);
								if (currUser != null) {
									if (clientMaster.getAdvisorUser() != currUser) {
										uploadResponseDTO.getErrors()
										.add("Cell (" + displayRow + ", B) : " + "Client doesnt exist for this user");
									}
								}
							}
						}

					}
				}
				// Remapped User email
				if (StringUtils.isEmpty(fields[6])) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", G) : " + "Remapped User should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[6])) {

						// AdvisorUser userName =
						// advisorUserRepository.findByFirstNameAndLastName(parts[0], parts[1]);
						AdvisorUser userName = advisorUserRepository
								.findByEmailID(fields[6]);
						if (userName == null) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", G) : " + "Remapped User doesn't exist");
						}

					}
				}
				// Remapped Role
				if (StringUtils.isEmpty(fields[7])) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", H) : " + "Remapped User Role should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(fields[7])) {
						if (!StringUtils.isAlphaSpace(fields[7])) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", H) : " + "Remapped User Role should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(fields[7])) {
								LookupRole role = lookupRoleRepository
										.findByDescription(fields[7]);
								if (role == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", H) : " + "Remapped User Role doesn't exist");
								} else {
									if (StringUtils.isNotEmpty(fields[6])) {
										AdvisorUser currUser = advisorUserRepository
												.findByEmailID(fields[6]);
										if (!currUser.getUser().getAdvisorRole().getRoleDescription().equalsIgnoreCase(role.getDescription())) {
											System.out.println("remap usr role " + currUser.getUser().getAdvisorRole().getRoleDescription());
											System.out.println("remap usr lookup role " + role.getDescription());
											uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", H) : " + "Remapped User has a different Role than provided");
										}
									}
								}
							}
						}
					}
				}
				// Remapped Date
				if (StringUtils.isEmpty(fields[8])) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", G ) : " + "Remapped Date should not be Empty");
				} else {
					Date remappedDate = FinexaUtil.parseDate(fields[8]);
					if (remappedDate == null) {
						uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", G) : " + "Remapped Date should be in dd-mm-yyyy format");
					}
				}

				// Remarks
				if (StringUtils.isEmpty(fields[9])) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", J) : " + "Remarks should contain alphabets");
				}
				continue;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;
	}

	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	private boolean validUploadFileExcel(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO) {
		Workbook workbook = null;
		Sheet sheet = null;

		try {
			workbook = Workbook.getWorkbook(fileuploadDTO.getFile()[0].getInputStream());
			sheet = workbook.getSheet(0);

			if (sheet.getRows() < 2) {
				uploadResponseDTO.getErrors().add("No Data in Excel sheet");
			}

			for (int rownum = 1; rownum < sheet.getRows(); rownum++) {

				int displayRow = rownum + 1;

				// current User email
				if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) : " + "Current User Email should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {

						// AdvisorUser userName =
						// advisorUserRepository.findByFirstNameAndLastName(parts[0], parts[1]);
						AdvisorUser currUser = advisorUserRepository
								.findByEmailID(sheet.getCell(3, rownum).getContents());
						if (currUser == null) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) : " + "Current User doesn't exist");
						} else {
							if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {
								AdvisorUser reamppedUser = advisorUserRepository
										.findByEmailID(sheet.getCell(6, rownum).getContents());
								if (currUser == reamppedUser) {
									uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", D) : " + "Current User and Remapped User must not be same");
								}
							}
						}

					}
				}
				// current user name
				if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", C) : " + "Current User Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(2, rownum).getContents().trim())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", C) : " + "Current User Name should contain alphabets");
						}
					}
				}
				// remapped user name
				if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", F) : " + "Remapped User Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(5, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(5, rownum).getContents().trim())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", F) : " + "Remapped User Name should contain alphabets");
						}
					}
				}
				// client name
				if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", A) : " + "Client Name should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(0, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(0, rownum).getContents().trim())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", A) : " + "Client Name should contain alphabets");
						}
					}
				}

				// current user Role
				if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", E) : " + "Current User Role should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(4, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(4, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", B) : " + "Current User Role should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(sheet.getCell(4, rownum).getContents())) {
								LookupRole role = lookupRoleRepository
										.findByDescription(sheet.getCell(4, rownum).getContents());
								if (role == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", B) : " + "Current User Role doesn't exist");
								} else {
									if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {
										AdvisorUser currUser = advisorUserRepository
												.findByEmailID(sheet.getCell(3, rownum).getContents());
										if (!currUser.getUser().getAdvisorRole().getRoleDescription().equalsIgnoreCase(role.getDescription())) {
											System.out.println("curr usr role " + currUser.getUser().getAdvisorRole().getRoleDescription());
											System.out.println("curr usr lookup role " + role.getDescription());
											uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", B) : " + "Current User has a different Role than provided");
										}
									}
								}
							}
						}
					}
				}

				// Location
//				if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
//					uploadResponseDTO.getErrors()
//							.add("Cell (" + displayRow + ", C) : " + "Location should not be Empty");
//				} else {
//					if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
//						if (!StringUtils.isAlphaSpace(sheet.getCell(2, rownum).getContents())) {
//							uploadResponseDTO.getErrors()
//									.add("Cell (" + displayRow + ", C) : " + "Location should contain alphabets");
//						}
//					}
//				}

				// Client email
				if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", B) : " + "Client Email should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {

						// ClientMaster clientMaster =
						// clientMasterRepository.getByFirstNameAndLastName(parts[0], parts[1]);
						ClientMaster clientMaster = clientMasterRepository
								.findByLoginUsername(sheet.getCell(1, rownum).getContents());

						if (clientMaster == null) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", B) : " + "Client doesnt exist");
						} else {
							if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {
								AdvisorUser currUser = advisorUserRepository
										.findByEmailID(sheet.getCell(3, rownum).getContents());
								if (currUser != null) {
									if (clientMaster.getAdvisorUser() != currUser) {
										uploadResponseDTO.getErrors()
										.add("Cell (" + displayRow + ", B) : " + "Client doesnt exist for this user");
									}
								}
							}
						}

					}
				}

				// Remapped User email
				if (StringUtils.isEmpty(sheet.getCell(6, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", G) : " + "Remapped User should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {

						// AdvisorUser userName =
						// advisorUserRepository.findByFirstNameAndLastName(parts[0], parts[1]);
						AdvisorUser userName = advisorUserRepository
								.findByEmailID(sheet.getCell(6, rownum).getContents());
						if (userName == null) {
							uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", G) : " + "Remapped User doesn't exist");
						}

					}
				}

				// Remapped Role
				if (StringUtils.isEmpty(sheet.getCell(7, rownum).getContents())) {
					uploadResponseDTO.getErrors().add("Cell (" + displayRow + ", H) : " + "Remapped User Role should not be Empty");
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(7, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(7, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("Cell (" + displayRow + ", H) : " + "Remapped User Role should contain alphabets");
						} else {
							if (StringUtils.isAlphaSpace(sheet.getCell(7, rownum).getContents())) {
								LookupRole role = lookupRoleRepository
										.findByDescription(sheet.getCell(7, rownum).getContents());
								if (role == null) {
									uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", H) : " + "Remapped User Role doesn't exist");
								} else {
									if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {
										AdvisorUser currUser = advisorUserRepository
												.findByEmailID(sheet.getCell(6, rownum).getContents());
										if (!currUser.getUser().getAdvisorRole().getRoleDescription().equalsIgnoreCase(role.getDescription())) {
											System.out.println("remap usr role " + currUser.getUser().getAdvisorRole().getRoleDescription());
											System.out.println("remap usr lookup role " + role.getDescription());
											uploadResponseDTO.getErrors()
											.add("Cell (" + displayRow + ", H) : " + "Remapped User has a different Role than provided");
										}
									}
								}
							}
						}
					}
				}

				// Remapped Date
				if (StringUtils.isEmpty(sheet.getCell(8, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", G ) : " + "Remapped Date should not be Empty");
				} else {
					Date remappedDate = FinexaUtil.parseDate(sheet.getCell(8, rownum).getContents());
					if (remappedDate == null) {
						uploadResponseDTO.getErrors()
						.add("Cell (" + displayRow + ", G) : " + "Remapped Date should be in dd-mm-yyyy format");
					}
				}

				// Remarks
				if (StringUtils.isEmpty(sheet.getCell(9, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("Cell (" + displayRow + ", J) : " + "Remarks should contain alphabets");
				}

			}

		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (uploadResponseDTO.getErrors().size() > 0) ? false : true;

	}
	
	@PreAuthorize("hasAnyRole('Admin','ClientRecordsView')")
	@RequestMapping(value = "/clientImport/{errorInput}/downloadErrorLog", method = RequestMethod.GET)
	public ResponseEntity<?> downloadErrorLog(@PathVariable String errorInput, HttpServletResponse response) {
		clientRecordService.downloadErrorLog(errorInput, response);
		return null;
	}

}
