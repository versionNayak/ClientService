package com.finlabs.finexa.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finlabs.finexa.dto.FileuploadDTO;
import com.finlabs.finexa.dto.UploadResponseDTO;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorRole;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserBulkUploadHistory;
import com.finlabs.finexa.model.AdvisorUserRoleMapping;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.AdvisorRoleRepository;
import com.finlabs.finexa.repository.AdvisorUserBulkUploadHistoryRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserRoleMappingRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupRoleRepository;
import com.finlabs.finexa.repository.LookupTransactBSEAccessModeRepository;
import com.finlabs.finexa.repository.UserRepository;
import com.finlabs.finexa.util.EmailUtil;
import com.finlabs.finexa.util.FinexaConstant;

import jxl.Sheet;
import jxl.Workbook;

@Component
public class AdvisorBulkUploadThread implements Runnable {
	
	private static Logger log = LoggerFactory.getLogger(AdvisorBulkUploadThread.class);
	
	@Autowired
	AdvisorUserRepository advisorUserRepository;
	
	@Autowired
	ClientContactRepository clientContactRepository;
	
	@Autowired
	AdvisorMasterRepository advisorMasterRepository;
	
	@Autowired
	AdvisorRoleRepository advisorRoleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AdvisorUserRoleMappingRepository advisorUserRoleMappingRepository;
	
	@Autowired
	LookupTransactBSEAccessModeRepository lookupTransactBSEAccessModeRepository;
	
	@Autowired
	LookupCountryRepository lookupCountryRepository;
	
	@Autowired
	AdvisorUserBulkUploadHistoryRepository advisorUserBulkUploadHistoryRepository;
	
	@Autowired			
	LookupRoleRepository lookupRoleRepository;
	
	private FileuploadDTO fileuploadDTO;
	private AdvisorUserBulkUploadHistory advisorUserBulkUploadHistory;
	private UploadResponseDTO uploadResponseDTO;

	public void initialize(FileuploadDTO fileuploadDTO, UploadResponseDTO uploadResponseDTO, AdvisorUserBulkUploadHistory advisorUserBulkUploadHistory) {
		this.fileuploadDTO = fileuploadDTO;
		this.uploadResponseDTO = uploadResponseDTO;
		this.advisorUserBulkUploadHistory = advisorUserBulkUploadHistory;
	}

	@Override
	public void run() {
		try {
			Workbook workbook = Workbook.getWorkbook(fileuploadDTO.getFile()[0].getInputStream());
			Sheet sheet = workbook.getSheet(0);
			AdvisorUser advisorUser = advisorUserRepository.findOne(fileuploadDTO.getAdvisorUserId());
			//System.out.println("ID : " + advisorUser.getId());
			//System.out.println("admin? : " + advisorUser.getUser().getAdmin());
			if(advisorUser.getUser().getAdmin().equalsIgnoreCase("y")) {
				boolean status = validateAndSave(sheet, uploadResponseDTO, advisorUserBulkUploadHistory);
			} else {
				AdvisorMaster advisorMaster = advisorUser.getAdvisorMaster();
				boolean status = validateAndSaveForAdvisorUser(advisorUser, advisorMaster, sheet, uploadResponseDTO, advisorUserBulkUploadHistory);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private boolean validateAndSaveForAdvisorUser(AdvisorUser advisorUser, AdvisorMaster advisorMaster, Sheet sheet, UploadResponseDTO uploadResponseDTO2,
			AdvisorUserBulkUploadHistory advisorUserBulkUploadHistory) {
		// TODO Auto-generated method stub

		log.debug("inside validate sheet");
		
		int rejectedRecords = 0;
		String reason = null;
		AdvisorRole advRole;
		
		if (sheet.getRows() < 2) {
			uploadResponseDTO.getErrors().add("\nNo Data in Excel sheet");
		}

		for (int rownum = 1; rownum < sheet.getRows(); rownum++) {

			boolean uploadFlag = true;
			
			int displayRow = rownum + 1;
			
			//unique emp code for org
			List<AdvisorUser> emp = advisorUserRepository.
					findByAdvisorMasterAndEmployeeCode(advisorMaster, sheet.getCell(9, rownum).getContents());
			//user is created only if empCode does not exist in database
			if(emp == null || emp.isEmpty()) {
				uploadFlag = true;
			} else {
				uploadResponseDTO.getErrors().add("\nThis Employee code already exists for this Organization");
				uploadFlag = false;
			}
			// systemId
			if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())) {
				log.debug("System Id is empty");
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", A ) :" + "System Id should not be Empty");
				uploadFlag = false;
			}
			// firstName
			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", B) :" + "First Name should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(1, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", B) :" + "First Name should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			// lastName
			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", C) :" + "Last Name should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(2, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", C) :" + "Last Name should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			
			//Gender
			if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", D) :" + "Gender should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(3, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", D) :" + "Gender should contain alphabets");
						uploadFlag = false;
					}
					String gender = sheet.getCell(3, rownum).getContents();
					//System.out.println("role: " + role);
					if(!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) {
						uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", D) :" + "Gender should be M or F");
						uploadFlag = false;
					}
				}
			}
			
			// emailID
			if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", E) :" + "Email ID should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(4, rownum).getContents())) {
					log.debug("email check: " + checkUniqueEmail(sheet.getCell(4, rownum).getContents()));
					if (checkUniqueEmail(sheet.getCell(4, rownum).getContents()) == false) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", E) :" + "Email ID should be unique");
						uploadFlag = false;
					}
				}
			}
			// MobileNo.
			if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", F) :" + "Mobile No. should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(5, rownum).getContents())) {
					//log.debug("mobile check: " + checkUniqueMobile(sheet.getCell(5, rownum).getContents()));
					if (StringUtils.isNumeric(sheet.getCell(5, rownum).getContents()) == false) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", F) :" + "Mobile number should contain only numbers");
						uploadFlag = false;
					} else {
					
						if(sheet.getCell(5, rownum).getContents().length() != 10) {
							uploadResponseDTO.getErrors()
							.add("\nCell (" + displayRow + ", F) :" + "Mobile number should be of exactly 10 digits");
							uploadFlag = false;
						}
						
						if (checkUniqueMobile(sheet.getCell(5, rownum).getContents()) == false) {
							uploadResponseDTO.getErrors()
									.add("\nCell (" + displayRow + ", F) :" + "Mobile number should be unique");
							uploadFlag = false;
						}
					}
					
				}
			}
			// countryName
			if (StringUtils.isEmpty(sheet.getCell(6, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("\nCell (" + displayRow + ", G) :" + "Country Name should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(6, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", G) :" + "Country Name should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			// state
			if (StringUtils.isEmpty(sheet.getCell(7, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", H) :" + "State should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(7, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(7, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", H) :" + "State should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			// city
			if (StringUtils.isEmpty(sheet.getCell(8, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", I) :" + "City should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(8, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(8, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", I) :" + "City should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			
			// employeeCode
			if (StringUtils.isEmpty(sheet.getCell(9, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("\nCell (" + displayRow + ", J) :" + "Employee Code should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(9, rownum).getContents())) {
					if (!StringUtils.isAlphanumeric(sheet.getCell(9, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", J) :" + "Employee Code is not valid");
						uploadFlag = false;
					}
				}
			}
			// roleName
			if (StringUtils.isEmpty(sheet.getCell(10, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", K) :" + "Role Name should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(10, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(10, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", K) :" + "Role Name should contain alphabets");
						uploadFlag = false;
					}
					String role = sheet.getCell(10, rownum).getContents();
					System.out.println("role: " + role);
					if(!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("Head") && !role.equalsIgnoreCase("Branch Manager") 
							&& !role.equalsIgnoreCase("Relationship Manager") && !role.equalsIgnoreCase("Sub Broker Manager")) {
						uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", K) :" + "Role Name should be one of these : "
								+ "Admin,"
								+ "Head,"
								+ "Branch Manager,"
								+ "Relationship Manager,"
								+ "Sub Broker Manager,");
						uploadFlag = false;
					}
				}
			}
			// SupervisorRole
			if (!(sheet.getCell(10, rownum).getContents().equalsIgnoreCase("Admin"))) {
				if (StringUtils.isEmpty(sheet.getCell(11, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("\nCell (" + displayRow + ", L) :" + "Supervisor Role should not be Empty");
					uploadFlag = false;
				} else {
					if (StringUtils.isNotEmpty(sheet.getCell(11, rownum).getContents())) {
						if (!StringUtils.isAlphaSpace(sheet.getCell(11, rownum).getContents())) {
							uploadResponseDTO.getErrors()
									.add("\nCell (" + displayRow + ", L) :" + "Supervisor Role should contain alphabets");
							uploadFlag = false;
						}
					}
					String supRole = sheet.getCell(11, rownum).getContents();
					System.out.println("supRole: " + supRole);
					if(!supRole.equalsIgnoreCase("Admin") && !supRole.equalsIgnoreCase("Head") && !supRole.equalsIgnoreCase("Branch Manager") 
							&& !supRole.equalsIgnoreCase("Relationship Manager") && !supRole.equalsIgnoreCase("Sub Broker Manager")) {
						uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", L) :" + "Supervisor Role Name should be one of these : "
								+ "Admin,"
								+ "Head,"
								+ "Branch Manager,"
								+ "Relationship Manager,"
								+ "Sub Broker Manager,");
						uploadFlag = false;
					}
				}
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(11, rownum).getContents())) {
					uploadResponseDTO.getErrors()
					.add("\nCell (" + displayRow + ", L) :" + "Supervisor Role should be empty if role is Admin");
					uploadFlag = false;
				}
			}

			if (uploadFlag == true) {
				// advisorMaster save
				AdvisorMaster advMaster;
				try {
					// AdvisorMaster advisorMaster = new AdvisorMaster();

					advMaster = advisorMaster;

					String roleName = sheet.getCell(10, rownum).getContents();
					String supRoleName = null;
					if (!roleName.equalsIgnoreCase("Admin")) {

						supRoleName = sheet.getCell(11, rownum).getContents();
					}
					// supervisorRole

					// AdvisorRole advSupRole1 =
					// advisorRoleRepository.findByRoleDescription(supRoleName);
					System.out.println("master: " + advMaster.getId());
					if (supRoleName != null) {
						AdvisorRole advSupRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advMaster,
								supRoleName);
						if (advSupRole == null) {
							advSupRole = new AdvisorRole();
							advSupRole.setRoleDescription(supRoleName);
							if (supRoleName.equalsIgnoreCase("admin")) {
								advSupRole.setSupervisorRoleID(null);
							} else {
								advSupRole.setSupervisorRoleID(advSupRole.getId());
							}
							advSupRole.setAdvisorMaster(advMaster);
							advSupRole = advisorRoleRepository.save(advSupRole);
						}
					}

					advRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advMaster, roleName);
					if (advRole == null) {
						advRole = new AdvisorRole();
						advRole.setRoleDescription(roleName);
						if (supRoleName != null) {
							advRole.setSupervisorRoleID(
									(int) lookupRoleRepository.findByDescription(supRoleName).getId());
						} else {
							advRole.setSupervisorRoleID(null);
						}
						advRole.setAdvisorMaster(advMaster);
						advRole = advisorRoleRepository.save(advRole);
					}

					// AdvisorUser save

					// user
					User user;
					User savedUser;
					AdvisorUser advUser;
					AdvisorUser savedAdvUser;

					user = new User();
					user.setAdvisorMaster(advMaster);
					user.setLoginUsername(sheet.getCell(4, rownum).getContents());
					user.setLoginPassword(sheet.getCell(1, rownum).getContents());
					user.setActiveFlag("Y");
					user.setAdvisorRole(advRole);
					user.setAdmin("N");
					user.setAdvisorAdmin("N");
					user.setClientInfoView("Y");
					user.setClientInfoAddEdit("Y");
					user.setClientInfoDelete("Y");

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					System.out.println(formatter.format(date));

					user.setCreatedOn(date);

					savedUser = userRepository.save(user);

					advUser = new AdvisorUser();
					advUser.setAdvisorMaster(advMaster); // save masterID
					advUser.setFirstName(sheet.getCell(1, rownum).getContents());
					advUser.setLastName(sheet.getCell(2, rownum).getContents());
					advUser.setGender(sheet.getCell(3, rownum).getContents().toUpperCase());
					advUser.setEmailID(sheet.getCell(4, rownum).getContents());
					advUser.setPhoneNo(new BigInteger(sheet.getCell(5, rownum).getContents()));
					advUser.setState(sheet.getCell(7, rownum).getContents());
					advUser.setCity(sheet.getCell(8, rownum).getContents());
					advUser.setLoginUsername(sheet.getCell(4, rownum).getContents());
					advUser.setLoginPassword(sheet.getCell(1, rownum).getContents());
					advUser.setEmployeeCode(sheet.getCell(9, rownum).getContents());
					advUser.setActiveFlag("Y");
					// advUser.setLastLoginTime(new Date());
					advUser.setLoggedInFlag("N");

					advUser.setLookupCountry(
							lookupCountryRepository.findByName(sheet.getCell(6, rownum).getContents().toUpperCase()));

					advUser.setLookupTransactBseaccessMode(lookupTransactBSEAccessModeRepository.findOne((byte) 1));

					LookupCountry lookupCountry = lookupCountryRepository
							.findByName(sheet.getCell(6, rownum).getContents().toUpperCase());
					advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());

					advUser.setUser(savedUser);
					
					if (sheet.getCell(3, rownum).getContents().equals("M")) {
						advUser.setSalutation("Mr");
					} else {
						advUser.setSalutation("Ms");
					}

					savedAdvUser = advisorUserRepository.save(advUser);

					// save userROLE MAPPING

					AdvisorUserRoleMapping userRoleMap = new AdvisorUserRoleMapping();
					userRoleMap.setAdvisorRole(advRole);
					userRoleMap.setAdvisorUser(savedAdvUser);
					Calendar cal = Calendar.getInstance();
					userRoleMap.setEffectiveFromDate(cal.getTime());
					advisorUserRoleMappingRepository.save(userRoleMap);
					
					//email fire
					String URLForEmail = "";
					Properties prop = new Properties();
					InputStream input = null;
					try {
					    input = new FileInputStream("/home/forgot_password.properties");
					    prop.load(input);
					    URLForEmail = prop.getProperty("URLForEmail");
					} catch (IOException ex) {
					    ex.printStackTrace();
					}
					
					StringBuilder sb = new StringBuilder();
					sb.append("Dear ");
					sb.append(savedAdvUser.getFirstName() + " " + savedAdvUser.getLastName() + ",\n");
					sb.append("\n");
					sb.append("Welcome to Finexa , the financial advisor's ERP\n\n"
							+ "Please find below your login credentials : \n"
							+ "User ID : ");
					sb.append(savedAdvUser.getLoginUsername() + "\n");
					sb.append("Password : ");
					sb.append(savedAdvUser.getLoginPassword() + "\n");
					sb.append("URL : " + URLForEmail + "\n\n");
					sb.append("We can be reached on the following contacts :\n" + 
							  "Phone: "+ advisorUser.getPhoneNo() +"\n" + 
							  "Email: "+ advisorUser.getEmailID() +"\n\n" +
							  "Regards,\n" + advisorUser.getFirstName() + " " + advisorUser.getLastName() + ".\n\n");
					List<String> toList = new ArrayList<String>();
					toList.add(advUser.getEmailID());

					EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_WELCOME_MESSAGE, sb.toString());


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// throw new CustomFinexaException("Advisor Role", "Nothing Specific", "Failed
					// to save data to Advisor Role");

				}

			} else {
				rejectedRecords ++;
				reason = uploadResponseDTO.getErrors().toString();
				
			}

		}
		
		advisorUserBulkUploadHistory.setStatus("Completed");
		advisorUserBulkUploadHistory.setEndTime(new Date());
		advisorUserBulkUploadHistory.setRejectedRecords(rejectedRecords);
		advisorUserBulkUploadHistory.setReasonOfRejection(reason);
		advisorUserBulkUploadHistoryRepository.save(advisorUserBulkUploadHistory);

		return true;
	}

	private boolean validateAndSave(Sheet sheet, UploadResponseDTO uploadResponseDTO, AdvisorUserBulkUploadHistory advisorUserBulkUploadHistory) {
		// TODO Auto-generated method stub
		log.debug("inside validate sheet");

		//boolean ret = true;
		int rejectedRecords = 0;
		String reason = null;
		AdvisorRole advRole;
		AdvisorMaster advMaster;

		if (sheet.getRows() < 2) {
			uploadResponseDTO.getErrors().add("\nNo Data in Excel sheet");
		}
		
		for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
			
			boolean uploadFlag = true;

			int displayRow = rownum + 1;
			
			//unique org name and distributor code
			advMaster = advisorMasterRepository.findByOrgNameAndDistributorCode
					(sheet.getCell(11, rownum).getContents(), sheet.getCell(12, rownum).getContents());
			if (advMaster != null) {
				uploadResponseDTO.getErrors().add("\nThis combination of Organization Name and Distributor Code already exists.");
				uploadFlag = false;
			}
			// systemId
			if (StringUtils.isEmpty(sheet.getCell(0, rownum).getContents())) {
				log.debug("System Id is empty");
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", A ) :" + "System Id should not be Empty");
				uploadFlag = false;
			}
			// firstName
			if (StringUtils.isEmpty(sheet.getCell(1, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", B) :" + "First Name should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(1, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(1, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", B) :" + "First Name should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			// lastName
			if (StringUtils.isEmpty(sheet.getCell(2, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", C) :" + "Last Name should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(2, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(2, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", C) :" + "Last Name should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			
			//Gender
			if (StringUtils.isEmpty(sheet.getCell(3, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", D) :" + "Gender should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(3, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(3, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", D) :" + "Gender should contain alphabets");
						uploadFlag = false;
					}
					String gender = sheet.getCell(3, rownum).getContents();
					//System.out.println("role: " + role);
					if(!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) {
						uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", L) :" + "Gender should be M or F");
						uploadFlag = false;
					}
				}
			}
			
			// emailID
			if (StringUtils.isEmpty(sheet.getCell(4, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", E) :" + "Email ID should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(4, rownum).getContents())) {
					log.debug("email check: " + checkUniqueEmail(sheet.getCell(4, rownum).getContents()));
					if (checkUniqueEmail(sheet.getCell(4, rownum).getContents()) == false) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", E) :" + "Email ID should be unique");
						uploadFlag = false;
					}
				}
			}
			// MobileNo.
			if (StringUtils.isEmpty(sheet.getCell(5, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", F) :" + "Mobile No. should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(5, rownum).getContents())) {
					//log.debug("mobile check: " + checkUniqueMobile(sheet.getCell(5, rownum).getContents()));
					if (StringUtils.isNumeric(sheet.getCell(5, rownum).getContents()) == false) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", F) :" + "Mobile number should contain only numbers");
						uploadFlag = false;
					} else {
					
						if(sheet.getCell(5, rownum).getContents().length() != 10) {
							uploadResponseDTO.getErrors()
							.add("\nCell (" + displayRow + ", F) :" + "Mobile number should be of exactly 10 digits");
							uploadFlag = false;
						}
						
						if (checkUniqueMobile(sheet.getCell(5, rownum).getContents()) == false) {
							uploadResponseDTO.getErrors()
									.add("\nCell (" + displayRow + ", F) :" + "Mobile number should be unique");
							uploadFlag = false;
						}
					}
					
				}
			}
			// countryName
			if (StringUtils.isEmpty(sheet.getCell(6, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("\nCell (" + displayRow + ", G) :" + "Country Name should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(6, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(6, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", G) :" + "Country Name should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			// state
			if (StringUtils.isEmpty(sheet.getCell(7, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", H) :" + "State should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(7, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(7, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", H) :" + "State should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			// city
			if (StringUtils.isEmpty(sheet.getCell(8, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", I) :" + "City should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(8, rownum).getContents())) {
					if (!StringUtils.isAlphaSpace(sheet.getCell(8, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", I) :" + "City should contain alphabets");
						uploadFlag = false;
					}
				}
			}
			
			// employeeCode
			if (sheet.getCell(10, rownum).getContents().equalsIgnoreCase("y")) {
				if (StringUtils.isEmpty(sheet.getCell(9, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("\nCell (" + displayRow + ", J) :" + "Employee Code should not be Empty when Organization Flag is Y");
					uploadFlag = false;
				}
			}
			if (StringUtils.isNotEmpty(sheet.getCell(9, rownum).getContents())) {
				if (!StringUtils.isAlphanumeric(sheet.getCell(9, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("\nCell (" + displayRow + ", J) :" + "Employee Code is not valid");
					uploadFlag = false;
				}
			}
			
			// orgFlag
			if (StringUtils.isEmpty(sheet.getCell(10, rownum).getContents())) {
				uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", K) :" + "Organization Flag value should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(10, rownum).getContents())) {
					if (!StringUtils.isAlpha(sheet.getCell(10, rownum).getContents())) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", K) :" + "Organization Flag should be Y or N");
						uploadFlag = false;
					}
					String orgFlag = sheet.getCell(10, rownum).getContents();
					//System.out.println("role: " + role);
					if(!orgFlag.equalsIgnoreCase("Y") && !orgFlag.equalsIgnoreCase("N")) {
						uploadResponseDTO.getErrors().add("\nCell (" + displayRow + ", K) :" + "Organization Flag should be Y or N");
						uploadFlag = false;
					}
				}
			}
			// organizationName
			if (sheet.getCell(10, rownum).getContents().equalsIgnoreCase("y")) {
				if (StringUtils.isEmpty(sheet.getCell(11, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("\nCell (" + displayRow + ", L) :" + "Organization Name should not be Empty when Organization Flag is Y");
					uploadFlag = false;
				}
			}
			if (StringUtils.isNotEmpty(sheet.getCell(11, rownum).getContents())) {
				if (!StringUtils.isAlphaSpace(sheet.getCell(11, rownum).getContents())) {
					uploadResponseDTO.getErrors()
							.add("\nCell (" + displayRow + ", L) :" + "Organization Name should contain alphabets");
					uploadFlag = false;
				} 
				
				String orgName = sheet.getCell(11, rownum).getContents();
				AdvisorMaster advisorMaster = advisorMasterRepository.findByOrgName(orgName);
				if(advisorMaster != null) {
					uploadResponseDTO.getErrors()
					.add("\nCell (" + displayRow + ", L) :" + "This Organization Name already exists");
					uploadFlag = false;
				}
			}
			
			// distributorCode
			if (StringUtils.isEmpty(sheet.getCell(12, rownum).getContents())) {
				uploadResponseDTO.getErrors()
						.add("\nCell (" + displayRow + ", M) :" + "Distributor Code should not be Empty");
				uploadFlag = false;
			} else {
				if (StringUtils.isNotEmpty(sheet.getCell(12, rownum).getContents())) {
					//if (!StringUtils.isAlphanumeric(sheet.getCell(12, rownum).getContents())) {
					if (!sheet.getCell(12, rownum).getContents().matches("^ARN-\\d+") 
							&& !sheet.getCell(12, rownum).getContents().matches("^ARN–\\d+") 
							&& !sheet.getCell(12, rownum).getContents().matches("^INA\\d+")) {
						uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", M) :" + "Distributor Code is not valid.\n"
										+ "For example, Distributor Code should be in one of these formats :\n"
										+ "INA000001142, "
										+ "ARN-121074");
						uploadFlag = false;
					} else {
						if (sheet.getCell(12, rownum).getContents().matches("^ARN-\\d+") 
								|| !sheet.getCell(12, rownum).getContents().matches("^ARN–\\d+")) {
							String value = sheet.getCell(12, rownum).getContents().substring(4);
							int numLength = value.length();
							if(numLength < 2 || numLength > 6) {
								uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", M) :" + "Number of Digits in ARN Code is not valid.\n" + 
										"The range of digits for ARN should be 2 to 6 digits");
								uploadFlag = false;
							}
						}
						if (sheet.getCell(12, rownum).getContents().matches("^INA\\d+")) {
							String value = sheet.getCell(12, rownum).getContents().substring(3);
							int numLength = value.length();
							if(numLength < 9 || numLength > 9) {
								uploadResponseDTO.getErrors()
								.add("\nCell (" + displayRow + ", M) :" + "Number of Digits in INA Code is not valid.\n" + 
										"The INA Code should contain 9 digits");
								uploadFlag = false;
							}
						}
					}
					String distCode = sheet.getCell(12, rownum).getContents();
					AdvisorMaster advMaster1 = advisorMasterRepository.findByDistributorCode(distCode);
					if(advMaster1 != null) {
						uploadResponseDTO.getErrors()
						.add("\nCell (" + displayRow + ", M) :" + "This Distributor Code already exists");
						uploadFlag = false;
					}
				}
			}
			
			if (uploadFlag == true) {

				// advisorMaster save
				try {

//					advMaster = advisorMasterRepository.findByOrgNameAndDistributorCode
//							(sheet.getCell(11, rownum).getContents(), sheet.getCell(12, rownum).getContents());
//					if (advMaster == null) {
					advMaster = new AdvisorMaster();
					advMaster.setOrgName(sheet.getCell(11, rownum).getContents());
					advMaster.setDistributorCode(sheet.getCell(12, rownum).getContents());
					advMaster.setOrgFlag(sheet.getCell(10, rownum).getContents().toUpperCase());
					advMaster.setAutoCreateClient("Y");
					advMaster = advisorMasterRepository.save(advMaster);

					// avisorRole save

					String roleName = "Admin";
					// String supRoleName = null;

					System.out.println("master: " + advMaster.getId());

					advRole = advisorRoleRepository.findByAdvisorMasterAndRoleDescription(advMaster, roleName);
					if (advRole == null) {

						advRole = new AdvisorRole();
						advRole.setRoleDescription(roleName);
						advRole.setSupervisorRoleID(null);
						advRole.setAdvisorMaster(advMaster);

						advRole = advisorRoleRepository.save(advRole);
					}

					// user save
					User user;
					User savedUser;

					user = new User();
					user.setAdvisorMaster(advMaster);
					user.setLoginUsername(sheet.getCell(4, rownum).getContents());
					user.setLoginPassword(sheet.getCell(1, rownum).getContents());
					user.setActiveFlag("Y");
					user.setAdvisorRole(advRole);
					user.setAdmin("N");
					user.setAdvisorAdmin("Y");
					user.setClientInfoView("Y");
					user.setClientInfoAddEdit("Y");
					user.setClientInfoDelete("Y");
					if (advMaster.getOrgFlag().equalsIgnoreCase("y")) {
						user.setUserManagementView("Y");
						user.setUserManagementAddEdit("Y");
						user.setUserManagementDelete("Y");
					} else {
						user.setUserManagementView("N");
						user.setUserManagementAddEdit("Y");
						user.setUserManagementDelete("Y");
					}

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					// System.out.println(formatter.format(date));
					user.setCreatedOn(date);

					savedUser = userRepository.save(user);

					// AdvisorUser save
					AdvisorUser advUser;
					AdvisorUser savedAdvUser;

					advUser = new AdvisorUser();
					advUser.setAdvisorMaster(advMaster); // save masterID
					advUser.setFirstName(sheet.getCell(1, rownum).getContents());
					advUser.setLastName(sheet.getCell(2, rownum).getContents());
					advUser.setGender(sheet.getCell(3, rownum).getContents().toUpperCase());
					advUser.setEmailID(sheet.getCell(4, rownum).getContents());
					advUser.setPhoneNo(new BigInteger(sheet.getCell(5, rownum).getContents()));
					advUser.setState(sheet.getCell(7, rownum).getContents());
					advUser.setCity(sheet.getCell(8, rownum).getContents());
					advUser.setLoginUsername(sheet.getCell(4, rownum).getContents());
					advUser.setLoginPassword(sheet.getCell(1, rownum).getContents());
					advUser.setEmployeeCode(sheet.getCell(9, rownum).getContents());
					advUser.setActiveFlag("Y");
					// advUser.setLastLoginTime(new Date());
					advUser.setLoggedInFlag("N");

					advUser.setLookupCountry(
							lookupCountryRepository.findByName(sheet.getCell(6, rownum).getContents().toUpperCase()));

					advUser.setLookupTransactBseaccessMode(lookupTransactBSEAccessModeRepository.findOne((byte) 1));

					LookupCountry lookupCountry = lookupCountryRepository
							.findByName(sheet.getCell(6, rownum).getContents().toUpperCase());
					advUser.setPhoneCountryCode(lookupCountry.getPhonecode().toString());

					advUser.setUser(savedUser);
					
					if (sheet.getCell(3, rownum).getContents().equals("M")) {
						advUser.setSalutation("Mr");
					} else {
						advUser.setSalutation("Ms");
					}

					savedAdvUser = advisorUserRepository.save(advUser);

					// save userROLE MAPPING

					AdvisorUserRoleMapping userRoleMap = new AdvisorUserRoleMapping();
					userRoleMap.setAdvisorRole(advRole);
					userRoleMap.setAdvisorUser(savedAdvUser);
					Calendar cal = Calendar.getInstance();
					userRoleMap.setEffectiveFromDate(cal.getTime());
					advisorUserRoleMappingRepository.save(userRoleMap);
					
					//email fire
					String URLForEmail = "";
					Properties prop = new Properties();
					InputStream input = null;
					try {
					    input = new FileInputStream("/home/forgot_password.properties");
					    prop.load(input);
					    URLForEmail = prop.getProperty("URLForEmail");
					} catch (IOException ex) {
					    ex.printStackTrace();
					}
					
					StringBuilder sb = new StringBuilder();
					sb.append("Dear ");
					sb.append(savedAdvUser.getFirstName() + " " + savedAdvUser.getLastName() + ",\n");
					sb.append("\n");
					sb.append("Welcome to Finexa , the financial advisor's ERP\n\n"
							+ "Please find below your login credentials : \n"
							+ "User ID : ");
					sb.append(savedAdvUser.getLoginUsername() + "\n");
					sb.append("Password : ");
					sb.append(savedAdvUser.getLoginPassword() + "\n");
					sb.append("URL : " + URLForEmail + "\n\n");
					sb.append("We can be reached during the regular business hours on the following contacts :\n" + 
							  "Phone: +91-022-62360605\n" + 
							  "Email: finexahelp@finlabsindia.com\n\n" +
							  "Relationship Manager: Shantanu Rathore\n" + 
							  "Email Id: shantanu.r@finlabsindia.com\n" + 
							  "Ph. No. +919986909709\n\n" +
							  "Regards,\nFinexa Admin.\n\n");
					List<String> toList = new ArrayList<String>();
					toList.add(savedAdvUser.getEmailID());

					EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_WELCOME_MESSAGE, sb.toString());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// throw new CustomFinexaException("Advisor Role", "Nothing Specific", "Failed
					// to save data to Advisor Role");

				}

			
			} else {
				rejectedRecords ++;
				reason = uploadResponseDTO.getErrors().toString();
				
			}

		}
		
		advisorUserBulkUploadHistory.setStatus("Completed");
		advisorUserBulkUploadHistory.setEndTime(new Date());
		advisorUserBulkUploadHistory.setRejectedRecords(rejectedRecords);
		advisorUserBulkUploadHistory.setReasonOfRejection(reason);
		advisorUserBulkUploadHistoryRepository.save(advisorUserBulkUploadHistory);

		return true;
	}
	
	public boolean checkUniqueEmail(String email) {
		boolean status;
		AdvisorUser au = advisorUserRepository.findByEmailID(email);
		if (au != null) {
			status = false;
		} else {
			ClientContact clientContact = clientContactRepository.findByEmailID(email);
			if (clientContact != null) {
				status = false;
			} else {
				status = true;
			}
		}
		//return (au != null) ? false : true;
		return status;
	}
	
	public boolean checkUniqueMobile(String mobileNo) {
		// TODO Auto-generated method stub
		AdvisorUser au = advisorUserRepository.findByPhoneNo(new BigInteger(mobileNo));
		return (au != null) ? false : true;
	}

}
