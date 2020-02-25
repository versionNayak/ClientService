package com.finlabs.finexa.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.finlabs.finexa.dto.ClientContactDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientFixedIncomeDTO;
import com.finlabs.finexa.dto.ClientGuardianContactDTO;
import com.finlabs.finexa.dto.ClientInfoDTO;
import com.finlabs.finexa.dto.ClientLoginInfoDTO;
import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientAccessRight;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientEquity;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFatcaReport;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientFloaterCover;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientGuardian;
import com.finlabs.finexa.model.ClientGuardianContact;
import com.finlabs.finexa.model.ClientLifeInsurance;
import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.ClientLoginInfo;
import com.finlabs.finexa.model.ClientLumpsumInflow;
import com.finlabs.finexa.model.ClientMandateRegistration;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientMeeting;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.ClientNPS;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.model.ClientOtherAlternateAsset;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.ClientPreciousMetal;
import com.finlabs.finexa.model.ClientRealEstate;
import com.finlabs.finexa.model.ClientRiskProfileResponse;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.ClientStructuredProduct;
import com.finlabs.finexa.model.ClientTask;
import com.finlabs.finexa.model.ClientVehicle;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupCountry;
import com.finlabs.finexa.model.LookupEducationalQualification;
import com.finlabs.finexa.model.LookupEmploymentType;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.model.LookupRelation;
import com.finlabs.finexa.model.LookupResidentType;
import com.finlabs.finexa.model.MasterState;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientAccessRightsRepository;
import com.finlabs.finexa.repository.ClientAnnuityRepository;
import com.finlabs.finexa.repository.ClientAtalPensionYojanaRepository;
import com.finlabs.finexa.repository.ClientCashRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientEpfRepository;
import com.finlabs.finexa.repository.ClientEquityRepository;
import com.finlabs.finexa.repository.ClientExpenseRepository;
import com.finlabs.finexa.repository.ClientFamilyIncomeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientFatcaRepository;
import com.finlabs.finexa.repository.ClientFixedIncomeRepository;
import com.finlabs.finexa.repository.ClientFloaterCoverRepository;
import com.finlabs.finexa.repository.ClientGoalRepository;
import com.finlabs.finexa.repository.ClientGuardianContactRepository;
import com.finlabs.finexa.repository.ClientGuardianRepository;
import com.finlabs.finexa.repository.ClientLifeInsuranceRepository;
import com.finlabs.finexa.repository.ClientLoanRepository;
import com.finlabs.finexa.repository.ClientLoginInfoRepository;
import com.finlabs.finexa.repository.ClientLumpsumRepository;
import com.finlabs.finexa.repository.ClientMandateRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientMeetingRepository;
import com.finlabs.finexa.repository.ClientMutualFundRepository;
import com.finlabs.finexa.repository.ClientNonlifeInsuranceRepository;
import com.finlabs.finexa.repository.ClientNpsRepository;
import com.finlabs.finexa.repository.ClientOtherAlternateAssetRepository;
import com.finlabs.finexa.repository.ClientPpfRepository;
import com.finlabs.finexa.repository.ClientPreciousMetalRepository;
import com.finlabs.finexa.repository.ClientRealEstateRepository;
import com.finlabs.finexa.repository.ClientRiskProfileResponseRepository;
import com.finlabs.finexa.repository.ClientSmallSavingRepository;
import com.finlabs.finexa.repository.ClientStructuredProductRepository;
import com.finlabs.finexa.repository.ClientTaskRepository;
import com.finlabs.finexa.repository.ClientVehicleRepository;
import com.finlabs.finexa.repository.EducationalQualificationRepository;
import com.finlabs.finexa.repository.EmploymentTypeRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.LookupRelationshipRepository;
import com.finlabs.finexa.repository.LookupResidentTypeRepository;
import com.finlabs.finexa.repository.MaritalStatusRepository;
import com.finlabs.finexa.repository.MasterStateRepository;
import com.finlabs.finexa.repository.ResidentTypeRepository;
import com.finlabs.finexa.util.ClientMasterSpecification;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Service("ClientMasterService")
@Transactional
public class ClientMasterServiceImpl implements ClientMasterService {
	private static Logger log = LoggerFactory.getLogger(ClientMasterServiceImpl.class);
	@Autowired
	private Mapper mapper;
	

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private MasterStateRepository masterStateRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private MaritalStatusRepository maritalStatusRepository;

	@Autowired
	private LookupCountryRepository lookupCountryRepository;
	
	@Autowired 
	private LookupResidentTypeRepository lookupResidentTypeRepository;

	@Autowired
	private EducationalQualificationRepository educationalQualificationRepository;
	@Autowired
	EmploymentTypeRepository employmentTypeRepository;

	@Autowired
	ResidentTypeRepository residentTypeRepository;

	@Autowired
	LookupRelationshipRepository lookupRelationshipRepository;

	@Autowired
	ClientContactRepository clientContactRepository;

	@Autowired
	private ClientGuardianRepository clientGuardianRepository;

	@Autowired
	private ClientGuardianContactRepository clientGuardianContactRepository;

	@Autowired
	private ClientFamilyIncomeService clientFamilyIncomeService;

	@Autowired
	private ClientGoalService clientGoalService;

	@Autowired
	FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	
	@Autowired
	FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	
	@Autowired
	AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;
	
	@Autowired
	AdvisorService advisorService;
	
	@Autowired
	ClientAccessRightsRepository clientAccessRightsRepository;
	
	@Autowired
	ClientAnnuityRepository clientAnnuityRepository;
	
	@Autowired
	ClientAtalPensionYojanaRepository clientAtalPensionYojanaRepository;
	
	@Autowired
	ClientCashRepository clientCashRepository;
	
	@Autowired
	ClientEpfRepository clientEpfRepository;
	
	@Autowired
	ClientEquityRepository clientEquityRepository;
	
	@Autowired
	ClientExpenseRepository clientExpenseRepository;
	
	@Autowired
	ClientFamilyIncomeRepository clientFamilyIncomeRepository;
	
	@Autowired
	ClientFatcaRepository clientFatcaRepository;
	
	@Autowired
	ClientGoalRepository clientGoalRepository;
	
	@Autowired
	ClientFixedIncomeRepository clientFixedIncomeRepository;
	
	@Autowired
	ClientLifeInsuranceRepository clientLifeInsuranceRepository;
	
	@Autowired
	ClientLoanRepository clientLoanRepository;
	
	@Autowired
	ClientLoginInfoRepository clientLoginInfoRepository;
	
	@Autowired
	ClientLumpsumRepository clientLumpsumRepository;
	
	@Autowired
	ClientMandateRepository clientMandateRepository;
	
	@Autowired
	ClientMeetingRepository clientMeetingRepository;
	
	@Autowired
	ClientMutualFundRepository clientMutualFundRepository;
	
	@Autowired
	ClientNonlifeInsuranceRepository clientNonLifeInsuranceRepository;
	
	@Autowired
	ClientNpsRepository clientNPSRepository;
	
	@Autowired
	ClientOtherAlternateAssetRepository clientOtherAlternateAssetRepository;
	
	@Autowired
	ClientPpfRepository clientPpfRepository;
	
	@Autowired
	ClientPreciousMetalRepository clientPreciousMetalRepository;
	
	@Autowired
	ClientRealEstateRepository clientRealEstateRepository;
	
	@Autowired
	ClientRiskProfileResponseRepository clientRiskProfileResponseRepository;
	
	@Autowired
	ClientSmallSavingRepository clientSmallSavingRepository;
	
	@Autowired
	ClientStructuredProductRepository clientStructuredProductRepository;
	
	@Autowired
	ClientTaskRepository clientTaskRepository;
	
	@Autowired
	ClientVehicleRepository clientVehicleRepository;
	
	@Autowired
	ClientFloaterCoverRepository clientFloaterCoverRepository;
	
	@Override
	public ClientMasterDTO save(ClientMasterDTO clientMasterDTO) throws RuntimeException, CustomFinexaException {
		int advuserId;
		try {
			ClientMaster clientMaster = mapper.map(clientMasterDTO, ClientMaster.class);
			clientMaster.setActiveFlag("Y");
			clientMaster.setAdvisorUser(advisorUserRepository.findOne(clientMasterDTO.getUserId()));
			LookupMaritalStatus lookupMaritalStatus = (clientMasterDTO.getMaritalStatus() > 0)
					? maritalStatusRepository.findOne(clientMasterDTO.getMaritalStatus()) : null;
			clientMaster.setLookupMaritalStatus(lookupMaritalStatus);
			LookupCountry lookupCountry = (clientMasterDTO.getCountryOfResidence() > 0)
					? lookupCountryRepository.findOne(clientMasterDTO.getCountryOfResidence()) : null;
			clientMaster.setLookupCountry(lookupCountry);
			LookupEducationalQualification lookupEducationalQualification = (clientMasterDTO.getEduQualification() > 0)
					? educationalQualificationRepository.findOne(clientMasterDTO.getEduQualification()) : null;
			clientMaster.setLookupEducationalQualification(lookupEducationalQualification);
			LookupEmploymentType lookupEmploymentType = (clientMasterDTO.getEmploymentType() > 0)
					? employmentTypeRepository.findOne(clientMasterDTO.getEmploymentType()) : null;
			clientMaster.setLookupEmploymentType(lookupEmploymentType);
			LookupResidentType lookupResidentType = (clientMasterDTO.getResidentType() > 0)
					? residentTypeRepository.findOne(clientMasterDTO.getResidentType()) : null;
			clientMaster.setLookupResidentType(lookupResidentType);

			if (clientMaster.getId() != 0) {
				if (clientMasterDTO.getRiskProfileScore() != null) {
					clientMaster.setRiskProfile(clientMasterDTO.getRiskProfileScore());
				}
			}
		
			clientMaster.setCreatedOn(new Date());
			
			if(!clientMasterDTO.getAadhar().equals(" ")) {
				clientMaster.setAadhar(clientMasterDTO.getAadhar());
			}
			String generatedString = RandomStringUtils.randomAlphabetic(6);
			clientMaster.setLoginPassword(generatedString);
			clientMaster.setLoggedInFlag("N");
			clientMaster.setClient("Y");
			//clientMaster.setLastLoginTime(new Date());// closed for mandatory change password feature during login
			
			// clientMaster.setLookupEducationalQualification(lookupEducationalQualification);
			clientMaster.setFinexaUser("Y");
			ClientMaster clientMasterSave = clientMasterRepository.save(clientMaster);
			ClientAccessRight clientAccessRight = new ClientAccessRight();
			clientAccessRight.setClientMaster(clientMasterSave);			
			clientAccessRight.setClientInfoView("Y");
			clientAccessRight.setClientInfoAddEdit("N");
			clientAccessRight.setClientInfoDelete("N");
			clientAccessRight.setBudgetManagementView("N");
			clientAccessRight.setGoalPlanningView("N");
			clientAccessRight.setGoalPlanningAddEdit("N");
			clientAccessRight.setPortfolioManagementView("N");
			clientAccessRight.setPortfolioManagementAddEdit("N");
			clientAccessRight.setFinancialPlanningView("N");
			clientAccessRight.setFinancialPlanningAddEdit("N");
			clientAccessRight.setInvestView("N");
			clientAccessRight.setInvestAddEdit("N");
			clientAccessRight.setMfBackOfficeView("N");
			clientAccessRight.setCreatedOn(new Date());
			clientAccessRightsRepository.save(clientAccessRight);

			ClientMasterDTO retcmDTO = mapper.map(clientMasterSave, ClientMasterDTO.class);

			retcmDTO.setUserId(clientMasterSave.getAdvisorUser().getId());
			byte maritalStatusId = (clientMasterSave.getLookupMaritalStatus() != null)
					? clientMasterSave.getLookupMaritalStatus().getId() : 0;
			retcmDTO.setMaritalStatus(maritalStatusId);
			int countryOfResidenceId = (clientMasterSave.getLookupCountry() != null)
					? clientMasterSave.getLookupCountry().getId() : 0;
			retcmDTO.setCountryOfResidence(countryOfResidenceId);
			byte eduQualification = (clientMasterSave.getLookupEducationalQualification() != null)
					? clientMasterSave.getLookupEducationalQualification().getId() : 0;
			retcmDTO.setEduQualification(eduQualification);
			byte employmentType = (clientMasterSave.getLookupEmploymentType() != null)
					? clientMasterSave.getLookupEmploymentType().getId() : 0;
			retcmDTO.setEmploymentType(employmentType);
			byte residentType = (clientMasterSave.getLookupResidentType() != null)
					? clientMasterSave.getLookupResidentType().getId() : 0;
			retcmDTO.setResidentType(residentType);

			if (clientMasterDTO.getClientContactDTO() != null) {
				ClientContact clientContact = mapper.map(clientMasterDTO.getClientContactDTO(), ClientContact.class);
				clientContact.setClientMaster(clientMasterSave);
				LookupCountry lookupCountry1 = (clientMasterDTO.getClientContactDTO().getLookupOfficeCountryId() > 0)
						? lookupCountryRepository.findOne(clientMasterDTO.getClientContactDTO().getLookupOfficeCountryId())
						: null;
				LookupCountry lookupCountry2 = (clientMasterDTO.getClientContactDTO().getLookupPermanentCountryId() > 0)
						? lookupCountryRepository.findOne(clientMasterDTO.getClientContactDTO().getLookupPermanentCountryId())
						: null;
				LookupCountry lookupCountry3 = (clientMasterDTO.getClientContactDTO()
						.getLookupCorrespondenceCountryId() > 0)
								? lookupCountryRepository.findOne(
										clientMasterDTO.getClientContactDTO().getLookupCorrespondenceCountryId())
								: null;
				clientContact.setLookupCountry1(lookupCountry1);
				clientContact.setLookupCountry2(lookupCountry2);
				clientContact.setLookupCountry3(lookupCountry3);

				if (clientMasterDTO.getClientContactDTO().getAddress1DropId() != 0) {
					MasterState state = masterStateRepository
							.findOne(clientMasterDTO.getClientContactDTO().getAddress1DropId());
					clientContact.setOfficeState(state.getState());
				} else {
					clientContact.setOfficeState(clientMasterDTO.getClientContactDTO().getOfficeState());
				}

				if (clientMasterDTO.getClientContactDTO().getAddress2DropId() != 0) {
					MasterState state = masterStateRepository
							.findOne(clientMasterDTO.getClientContactDTO().getAddress2DropId());
					clientContact.setPermanentState(state.getState());
				} else {
					clientContact.setPermanentState(clientMasterDTO.getClientContactDTO().getPermanentState());
				}

				if (clientMasterDTO.getClientContactDTO().getAddress3DropId() != 0) {
					MasterState state = masterStateRepository
							.findOne(clientMasterDTO.getClientContactDTO().getAddress3DropId());
					clientContact.setCorrespondenceState(state.getState());
				} else {
					clientContact
							.setCorrespondenceState(clientMasterDTO.getClientContactDTO().getCorrespondenceState());
				}
				ClientContact clientContactSave = clientContactRepository.save(clientContact);
				clientMaster.setLoginUsername(clientContactSave.getEmailID());
				// clientMaster.setLookupEducationalQualification(lookupEducationalQualification);
				//ClientMaster clientMasterSave = clientMasterRepository.save(clientMaster);// save
																								// client
																								// in
																								// master
				ClientContactDTO ccDTO = mapper.map(clientContactSave, ClientContactDTO.class);
				ccDTO.setClientId(clientContactSave.getClientMaster().getId());
				int lookupCorrespondenceCountryId = (clientContactSave.getLookupCountry3() != null)
						? clientContactSave.getLookupCountry3().getId() : 0;
				int lookupOfficeCountryId = (clientContactSave.getLookupCountry1() != null)
						? clientContactSave.getLookupCountry1().getId() : 0;
				int lookupPermanentCountryId = (clientContactSave.getLookupCountry2() != null)
						? clientContactSave.getLookupCountry2().getId() : 0;
				ccDTO.setLookupCorrespondenceCountryId(lookupCorrespondenceCountryId);
				ccDTO.setLookupOfficeCountryId(lookupOfficeCountryId);
				ccDTO.setLookupPermanentCountryId(lookupPermanentCountryId);

				retcmDTO.setClientContactDTO(mapper.map(clientContactSave, ClientContactDTO.class));
			}
			ClientGuardian clientGuardian = null;
			if (clientMasterDTO.getClientGuardianDTO() != null) {
				clientGuardian = mapper.map(clientMasterDTO.getClientGuardianDTO(), ClientGuardian.class);
				clientGuardian.setClientMaster(clientMasterSave);
				clientGuardian = clientGuardianRepository.save(clientGuardian);
			}
			if (clientMasterDTO.getClientGuardianContactDTO() != null) {

				try {
					// log.debug("email " +
					// clientMasterDTO.getClientGuardianContactDTO().getEmailID());
					ClientGuardianContact clientGuardianContact = mapper
							.map(clientMasterDTO.getClientGuardianContactDTO(), ClientGuardianContact.class);
					// log.debug("email " + clientGuardianContact.getEmailID());
					clientGuardianContact.setClientGuardian(clientGuardian);
					// log.debug(("aaa " +
					// clientMasterDTO.getClientGuardianContactDTO().getLookupOfficeCountryId()));
					LookupCountry lookupCountry1 = (clientMasterDTO.getClientGuardianContactDTO()
							.getLookupOfficeCountryId() > 0)
									? lookupCountryRepository.findOne(
											clientMasterDTO.getClientGuardianContactDTO().getLookupOfficeCountryId())
									: null;
					LookupCountry lookupCountry2 = (clientMasterDTO.getClientGuardianContactDTO()
							.getLookupPermanentCountryId() > 0)
									? lookupCountryRepository.findOne(
											clientMasterDTO.getClientGuardianContactDTO().getLookupPermanentCountryId())
									: null;
					LookupCountry lookupCountry3 = (clientMasterDTO.getClientGuardianContactDTO()
							.getLookupCorrespondenceCountryId() > 0)
									? lookupCountryRepository.findOne(
											clientMasterDTO.getClientGuardianContactDTO().getLookupOfficeCountryId())
									: null;
					clientGuardianContact.setLookupCountry1(lookupCountry1);
					clientGuardianContact.setLookupCountry2(lookupCountry2);
					clientGuardianContact.setLookupCountry3(lookupCountry3);

					if (clientMasterDTO.getClientGuardianContactDTO().getAddress1DropId() != 0) {
						MasterState state = masterStateRepository
								.findOne(clientMasterDTO.getClientGuardianContactDTO().getAddress1DropId());
						clientGuardianContact.setOfficeState(state.getState());
					} else {
						clientGuardianContact
								.setOfficeState(clientMasterDTO.getClientGuardianContactDTO().getOfficeState());
					}

					if (clientMasterDTO.getClientGuardianContactDTO().getAddress2DropId() != 0) {
						MasterState state = masterStateRepository
								.findOne(clientMasterDTO.getClientGuardianContactDTO().getAddress2DropId());
						clientGuardianContact.setPermanentState(state.getState());
					} else {
						clientGuardianContact
								.setPermanentState(clientMasterDTO.getClientGuardianContactDTO().getPermanentState());
					}

					if (clientMasterDTO.getClientGuardianContactDTO().getAddress3DropId() != 0) {
						MasterState state = masterStateRepository
								.findOne(clientMasterDTO.getClientGuardianContactDTO().getAddress3DropId());
						clientGuardianContact.setCorrespondenceState(state.getState());
					} else {
						clientGuardianContact.setCorrespondenceState(
								clientMasterDTO.getClientGuardianContactDTO().getCorrespondenceState());
					}

					ClientGuardianContact clientGuardianContactSave = clientGuardianContactRepository
							.save(clientGuardianContact); // save client in
															// master
					ClientGuardianContactDTO ccDTO = mapper.map(clientGuardianContactSave,
							ClientGuardianContactDTO.class);
					/*
					 * ccDTO.setGuardianID(clientGuardianContactSave.
					 * getClientGuardian().getId()); int
					 * lookupCorrespondenceCountryId =
					 * (clientGuardianContactSave.getLookupCountry3() != null) ?
					 * clientGuardianContactSave.getLookupCountry3().getId() :
					 * 0; int lookupOfficeCountryId =
					 * (clientGuardianContactSave.getLookupCountry1() != null) ?
					 * clientGuardianContactSave.getLookupCountry1().getId() :
					 * 0; int lookupPermanentCountryId =
					 * (clientGuardianContactSave.getLookupCountry2() != null) ?
					 * clientGuardianContactSave.getLookupCountry2().getId() :
					 * 0; ccDTO.setCorrespondenceCountry(
					 * lookupCorrespondenceCountryId) ;
					 * ccDTO.setOfficeCountry(lookupOfficeCountryId);
					 * ccDTO.setPermanentCountry(lookupPermanentCountryId);
					 */
					// retcmDTO.setClientContactDTO(mapper.map(clientContactSave,
					// ClientContactDTO.class));
				} catch (RuntimeException e) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
									FinexaConstant.CLIENT_GUARDIAN_CONTACT_ADD_ERROR);
					throw new CustomFinexaException(FinexaConstant.CLIENT_GUARDIAN_CONTACT_MODULE,
							FinexaConstant.CLIENT_GUARDIAN_CONTACT_ADD_ERROR,
							exception != null ? exception.getErrorMessage() : "");
				}
			}
			// ClientFamilyMember clientFamilyMember = null;

			if (clientMasterDTO.getClientFamilyMemberDTO() != null) {

				if (clientMasterDTO.getClientFamilyMemberDTO().getBirthDate() != null) {
					ClientFamilyMember clientFamilyMember = mapper.map(clientMasterDTO.getClientFamilyMemberDTO(),
							ClientFamilyMember.class);
					clientFamilyMember.setHasDiseaseHistory("Y");
					clientFamilyMember.setHasNormalBP("Y");
					clientFamilyMember.setIsProperBMI("Y");
					clientFamilyMember.setIsTobaccoUser("N");
					clientFamilyMember.setClientMaster(clientMasterSave);
					clientFamilyMember.setLookupRelation(lookupRelationshipRepository
							.findOne((byte)clientMasterDTO.getClientFamilyMemberDTO().getRelationID()));

					ClientFamilyMember clientFamilyMemberSave = clientFamilyMemberRepository.save(clientFamilyMember);
					ClientFamilyMemberDTO clientFamilyMemberDTOSave = mapper.map(clientFamilyMemberSave,
							ClientFamilyMemberDTO.class);
					clientFamilyMemberDTOSave.setClientID(clientFamilyMemberSave.getClientMaster().getId());
					clientFamilyMemberDTOSave.setRelationID(clientFamilyMemberSave.getLookupRelation().getId());
					retcmDTO.setClientFamilyMemberDTO(clientFamilyMemberDTOSave);
				}

			}

			// add to clientFamilymemebr as a self member
			ClientFamilyMember cfm = mapper.map(clientMasterDTO, ClientFamilyMember.class);
			cfm.setClientMaster(clientMasterSave);
			if (clientMasterDTO.getFamilyMemberId() != 0) {
				cfm.setId(clientMasterDTO.getFamilyMemberId());
				ClientFamilyMember cfm1 = clientFamilyMemberRepository.findOne(clientMasterDTO.getFamilyMemberId());

				cfm.setLifeExpectancy(cfm1.getLifeExpectancy());
				cfm.setIsTobaccoUser(cfm1.getIsTobaccoUser());
				cfm.setIsProperBMI(cfm1.getIsProperBMI());
				cfm.setHasNormalBP(cfm1.getHasNormalBP());
				cfm.setHasDiseaseHistory(cfm1.getHasDiseaseHistory());
			}

			// self relationship type id = 0;
			LookupRelation rel = lookupRelationshipRepository.findOne((byte)0);
			cfm.setLookupRelation(rel);
			// adding Life Expectancy

			cfm.setDependentFlag("N");

			// clientMasterDTO.getClientLifeExpDTO().setFamilyMemberId(clientFamilyMember.getId());
			if (clientMasterDTO.getClientLifeExpDTO() != null) {
				if (cfm.getLookupRelation().getId() == 0) {
					if (clientMasterDTO.getClientLifeExpDTO().getIsProperBMI().equalsIgnoreCase("on")) {
						cfm.setIsProperBMI("Y");
					} else {
						cfm.setIsProperBMI("N");
					}

					if (clientMasterDTO.getClientLifeExpDTO().getHasNormalBP().equalsIgnoreCase("on")) {
						cfm.setHasNormalBP("Y");
					} else {
						cfm.setHasNormalBP("N");
					}

					if (clientMasterDTO.getClientLifeExpDTO().getIsTobaccoUser().equalsIgnoreCase("on")) {
						cfm.setIsTobaccoUser("Y");
					} else {
						cfm.setIsTobaccoUser("N");
					}

					if (clientMasterDTO.getClientLifeExpDTO().getHasDiseaseHistory().equalsIgnoreCase("on")) {
						cfm.setHasDiseaseHistory("Y");
					} else {
						cfm.setHasDiseaseHistory("N");
					}
					cfm.setLifeExpectancy(clientMasterDTO.getClientLifeExpDTO().getTotalLifeExpectancy());

				}
			}
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.save(cfm);
			if (clientMasterDTO.getClientLifeExpDTO() != null) {
				clientMasterDTO.getClientLifeExpDTO().setFamilyMemberId(clientFamilyMember.getId());
				clientFamilyIncomeService.saveFamilyIncomeAfterChangeInLifeExp(clientMasterDTO.getClientLifeExpDTO());
			}
			return retcmDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientMasterDTO update(ClientMasterDTO clientMasterDTO) throws RuntimeException, CustomFinexaException {

		try {		
			ClientMaster master = clientMasterRepository.findOne(clientMasterDTO.getId());
			clientMasterDTO.setLoginPassword(master.getLoginPassword());
			ClientMaster clientMaster = mapper.map(clientMasterDTO, ClientMaster.class);
			clientMaster.setActiveFlag("Y");
			clientMaster.setAdvisorUser(advisorUserRepository.findOne(clientMasterDTO.getUserId()));
			LookupMaritalStatus lookupMaritalStatus = (clientMasterDTO.getMaritalStatus() > 0)
					? maritalStatusRepository.findOne(clientMasterDTO.getMaritalStatus()) : null;
			clientMaster.setLookupMaritalStatus(lookupMaritalStatus);
			LookupCountry lookupCountry = (clientMasterDTO.getCountryOfResidence() > 0)
					? lookupCountryRepository.findOne(clientMasterDTO.getCountryOfResidence()) : null;
			clientMaster.setLookupCountry(lookupCountry);
			LookupEducationalQualification lookupEducationalQualification = (clientMasterDTO.getEduQualification() > 0)
					? educationalQualificationRepository.findOne(clientMasterDTO.getEduQualification()) : null;
			clientMaster.setLookupEducationalQualification(lookupEducationalQualification);
			LookupEmploymentType lookupEmploymentType = (clientMasterDTO.getEmploymentType() > 0)
					? employmentTypeRepository.findOne(clientMasterDTO.getEmploymentType()) : null;
			clientMaster.setLookupEmploymentType(lookupEmploymentType);
			LookupResidentType lookupResidentType = (clientMasterDTO.getResidentType() > 0)
					? residentTypeRepository.findOne(clientMasterDTO.getResidentType()) : null;
			clientMaster.setLookupResidentType(lookupResidentType);

			if (clientMaster.getId() != 0) {
				if (clientMasterDTO.getRiskProfileScore() != null) {
					clientMaster.setRiskProfile(clientMasterDTO.getRiskProfileScore());
				}
			}
              
			// clientMaster.setLookupEducationalQualification(lookupEducationalQualification);
			clientGoalService.updateGoalStartMonthYear(clientMaster);
			
			ClientMaster cm = clientMasterRepository.getOne(clientMasterDTO.getId());
			clientMaster.setCreatedOn(cm.getCreatedOn());
			
			if(!clientMasterDTO.getAadhar().equals(" ")) {
				clientMaster.setAadhar(clientMasterDTO.getAadhar());
			}
			
			clientMaster.setFinexaUser("Y");
			ClientMaster clientMasterSave = clientMasterRepository.save(clientMaster);

			ClientMasterDTO retcmDTO = mapper.map(clientMasterSave, ClientMasterDTO.class);

			retcmDTO.setUserId(clientMasterSave.getAdvisorUser().getId());
			byte maritalStatusId = (clientMasterSave.getLookupMaritalStatus() != null)
					? clientMasterSave.getLookupMaritalStatus().getId() : 0;
			retcmDTO.setMaritalStatus(maritalStatusId);
			int countryOfResidenceId = (clientMasterSave.getLookupCountry() != null)
					? clientMasterSave.getLookupCountry().getId() : 0;
			retcmDTO.setCountryOfResidence(countryOfResidenceId);
			byte eduQualification = (clientMasterSave.getLookupEducationalQualification() != null)
					? clientMasterSave.getLookupEducationalQualification().getId() : 0;
			retcmDTO.setEduQualification(eduQualification);
			byte employmentType = (clientMasterSave.getLookupEmploymentType() != null)
					? clientMasterSave.getLookupEmploymentType().getId() : 0;
			retcmDTO.setEmploymentType(employmentType);
			byte residentType = (clientMasterSave.getLookupResidentType() != null)
					? clientMasterSave.getLookupResidentType().getId() : 0;
			retcmDTO.setResidentType(residentType);

			if (clientMasterDTO.getClientContactDTO() != null) {
				ClientContact clientContact = mapper.map(clientMasterDTO.getClientContactDTO(), ClientContact.class);
				clientContact.setClientMaster(clientMasterSave);
				LookupCountry lookupCountry1 = (clientMasterDTO.getClientContactDTO().getLookupOfficeCountryId() > 0)
						? lookupCountryRepository.findOne(clientMasterDTO.getClientContactDTO().getLookupOfficeCountryId())
						: null;
				LookupCountry lookupCountry2 = (clientMasterDTO.getClientContactDTO().getLookupPermanentCountryId() > 0)
						? lookupCountryRepository.findOne(clientMasterDTO.getClientContactDTO().getLookupPermanentCountryId())
						: null;
				LookupCountry lookupCountry3 = (clientMasterDTO.getClientContactDTO()
						.getLookupCorrespondenceCountryId() > 0)
								? lookupCountryRepository.findOne(
										clientMasterDTO.getClientContactDTO().getLookupCorrespondenceCountryId())
								: null;
				clientContact.setLookupCountry1(lookupCountry1);
				clientContact.setLookupCountry2(lookupCountry2);
				clientContact.setLookupCountry3(lookupCountry3);

				if (clientMasterDTO.getClientContactDTO().getAddress1DropId() != 0) {
					MasterState state = masterStateRepository
							.findOne(clientMasterDTO.getClientContactDTO().getAddress1DropId());
					clientContact.setOfficeState(state.getState());
				} else {
					clientContact.setOfficeState(clientMasterDTO.getClientContactDTO().getOfficeState());
				}

				if (clientMasterDTO.getClientContactDTO().getAddress2DropId() != 0) {
					MasterState state = masterStateRepository
							.findOne(clientMasterDTO.getClientContactDTO().getAddress2DropId());
					clientContact.setPermanentState(state.getState());
				} else {
					clientContact.setPermanentState(clientMasterDTO.getClientContactDTO().getPermanentState());
				}

				if (clientMasterDTO.getClientContactDTO().getAddress3DropId() != 0) {
					MasterState state = masterStateRepository
							.findOne(clientMasterDTO.getClientContactDTO().getAddress3DropId());
					clientContact.setCorrespondenceState(state.getState());
				} else {
					clientContact
							.setCorrespondenceState(clientMasterDTO.getClientContactDTO().getCorrespondenceState());
				}
				ClientContact clientContactSave = clientContactRepository.save(clientContact); // save
																								// client
																								// in
																								// master
				ClientContactDTO ccDTO = mapper.map(clientContactSave, ClientContactDTO.class);
				ccDTO.setClientId(clientContactSave.getClientMaster().getId());
				int lookupCorrespondenceCountryId = (clientContactSave.getLookupCountry3() != null)
						? clientContactSave.getLookupCountry3().getId() : 0;
				int lookupOfficeCountryId = (clientContactSave.getLookupCountry1() != null)
						? clientContactSave.getLookupCountry1().getId() : 0;
				int lookupPermanentCountryId = (clientContactSave.getLookupCountry2() != null)
						? clientContactSave.getLookupCountry2().getId() : 0;
				ccDTO.setLookupCorrespondenceCountryId(lookupCorrespondenceCountryId);
				ccDTO.setLookupOfficeCountryId(lookupOfficeCountryId);
				ccDTO.setLookupPermanentCountryId(lookupPermanentCountryId);

				retcmDTO.setClientContactDTO(mapper.map(clientContactSave, ClientContactDTO.class));
			}
			ClientGuardian clientGuardian = null;
			if (clientMasterDTO.getClientGuardianDTO() != null) {
				clientGuardian = mapper.map(clientMasterDTO.getClientGuardianDTO(), ClientGuardian.class);
				clientGuardian.setClientMaster(clientMasterSave);
				clientGuardian = clientGuardianRepository.save(clientGuardian);
			}
			if (clientMasterDTO.getClientGuardianContactDTO() != null) {

				try {
					// log.debug("email " +
					// clientMasterDTO.getClientGuardianContactDTO().getEmailID());
					ClientGuardianContact clientGuardianContact = mapper
							.map(clientMasterDTO.getClientGuardianContactDTO(), ClientGuardianContact.class);
					// log.debug("email " + clientGuardianContact.getEmailID());
					clientGuardianContact.setClientGuardian(clientGuardian);
					// log.debug(("aaa " +
					// clientMasterDTO.getClientGuardianContactDTO().getLookupOfficeCountryId()));
					LookupCountry lookupCountry1 = (clientMasterDTO.getClientGuardianContactDTO()
							.getLookupOfficeCountryId() > 0)
									? lookupCountryRepository.findOne(
											clientMasterDTO.getClientGuardianContactDTO().getLookupOfficeCountryId())
									: null;
					LookupCountry lookupCountry2 = (clientMasterDTO.getClientGuardianContactDTO()
							.getLookupPermanentCountryId() > 0)
									? lookupCountryRepository.findOne(
											clientMasterDTO.getClientGuardianContactDTO().getLookupPermanentCountryId())
									: null;
					LookupCountry lookupCountry3 = (clientMasterDTO.getClientGuardianContactDTO()
							.getLookupCorrespondenceCountryId() > 0)
									? lookupCountryRepository.findOne(
											clientMasterDTO.getClientGuardianContactDTO().getLookupOfficeCountryId())
									: null;
					clientGuardianContact.setLookupCountry1(lookupCountry1);
					clientGuardianContact.setLookupCountry2(lookupCountry2);
					clientGuardianContact.setLookupCountry3(lookupCountry3);

					if (clientMasterDTO.getClientGuardianContactDTO().getAddress1DropId() != 0) {
						MasterState state = masterStateRepository
								.findOne(clientMasterDTO.getClientGuardianContactDTO().getAddress1DropId());
						clientGuardianContact.setOfficeState(state.getState());
					} else {
						clientGuardianContact
								.setOfficeState(clientMasterDTO.getClientGuardianContactDTO().getOfficeState());
					}

					if (clientMasterDTO.getClientGuardianContactDTO().getAddress2DropId() != 0) {
						MasterState state = masterStateRepository
								.findOne(clientMasterDTO.getClientGuardianContactDTO().getAddress2DropId());
						clientGuardianContact.setPermanentState(state.getState());
					} else {
						clientGuardianContact
								.setPermanentState(clientMasterDTO.getClientGuardianContactDTO().getPermanentState());
					}

					if (clientMasterDTO.getClientGuardianContactDTO().getAddress3DropId() != 0) {
						MasterState state = masterStateRepository
								.findOne(clientMasterDTO.getClientGuardianContactDTO().getAddress3DropId());
						clientGuardianContact.setCorrespondenceState(state.getState());
					} else {
						clientGuardianContact.setCorrespondenceState(
								clientMasterDTO.getClientGuardianContactDTO().getCorrespondenceState());
					}

					ClientGuardianContact clientGuardianContactSave = clientGuardianContactRepository
							.save(clientGuardianContact); // save client in
															// master
					ClientGuardianContactDTO ccDTO = mapper.map(clientGuardianContactSave,
							ClientGuardianContactDTO.class);
					/*
					 * ccDTO.setGuardianID(clientGuardianContactSave.
					 * getClientGuardian().getId()); int
					 * lookupCorrespondenceCountryId =
					 * (clientGuardianContactSave.getLookupCountry3() != null) ?
					 * clientGuardianContactSave.getLookupCountry3().getId() :
					 * 0; int lookupOfficeCountryId =
					 * (clientGuardianContactSave.getLookupCountry1() != null) ?
					 * clientGuardianContactSave.getLookupCountry1().getId() :
					 * 0; int lookupPermanentCountryId =
					 * (clientGuardianContactSave.getLookupCountry2() != null) ?
					 * clientGuardianContactSave.getLookupCountry2().getId() :
					 * 0; ccDTO.setCorrespondenceCountry(
					 * lookupCorrespondenceCountryId) ;
					 * ccDTO.setOfficeCountry(lookupOfficeCountryId);
					 * ccDTO.setPermanentCountry(lookupPermanentCountryId);
					 */
					// retcmDTO.setClientContactDTO(mapper.map(clientContactSave,
					// ClientContactDTO.class));
				} catch (RuntimeException e) {
					FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
							.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_PERSONAL_PROFILE);
					FinexaExceptionHandling exception = finexaExceptionHandlingRepository
							.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
									FinexaConstant.CLIENT_GUARDIAN_CONTACT_ADD_ERROR);
					throw new CustomFinexaException(FinexaConstant.CLIENT_GUARDIAN_CONTACT_MODULE,
							FinexaConstant.CLIENT_GUARDIAN_CONTACT_ADD_ERROR,
							exception != null ? exception.getErrorMessage() : "");
				}
			}
			// ClientFamilyMember clientFamilyMember = null;

			if (clientMasterDTO.getClientFamilyMemberDTO() != null) {

				if (clientMasterDTO.getClientFamilyMemberDTO().getBirthDate() != null) {
					
					ClientFamilyMember clientFamilyMember = mapper.map(clientMasterDTO.getClientFamilyMemberDTO(),
							ClientFamilyMember.class);
					clientFamilyMember.setHasDiseaseHistory("Y");
					clientFamilyMember.setHasNormalBP("Y");
					clientFamilyMember.setIsProperBMI("Y");
					clientFamilyMember.setIsTobaccoUser("N");
					clientFamilyMember.setClientMaster(clientMasterSave);
					clientFamilyMember.setLookupRelation(lookupRelationshipRepository
							.findOne((byte)clientMasterDTO.getClientFamilyMemberDTO().getRelationID()));

					ClientFamilyMember clientFamilyMemberSave = clientFamilyMemberRepository.save(clientFamilyMember);
					ClientFamilyMemberDTO clientFamilyMemberDTOSave = mapper.map(clientFamilyMemberSave,
							ClientFamilyMemberDTO.class);
					clientFamilyMemberDTOSave.setClientID(clientFamilyMemberSave.getClientMaster().getId());
					clientFamilyMemberDTOSave.setRelationID(clientFamilyMemberSave.getLookupRelation().getId());
					retcmDTO.setClientFamilyMemberDTO(clientFamilyMemberDTOSave);
					
					clientGoalService.updateGoalStartMonthYear(clientFamilyMemberSave.getClientMaster());
					
				}

			}

			// add to clientFamilymemebr as a self member
			ClientFamilyMember cfm = mapper.map(clientMasterDTO, ClientFamilyMember.class);
			cfm.setClientMaster(clientMasterSave);
			if (clientMasterDTO.getFamilyMemberId() != 0) {
				cfm.setId(clientMasterDTO.getFamilyMemberId());
				ClientFamilyMember cfm1 = clientFamilyMemberRepository.findOne(clientMasterDTO.getFamilyMemberId());
				cfm.setLifeExpectancy(cfm1.getLifeExpectancy());
				cfm.setIsTobaccoUser(cfm1.getIsTobaccoUser());
				cfm.setIsProperBMI(cfm1.getIsProperBMI());
				cfm.setHasNormalBP(cfm1.getHasNormalBP());
				cfm.setHasDiseaseHistory(cfm1.getHasDiseaseHistory());
			}

			// self relationship type id = 0;
			LookupRelation rel = lookupRelationshipRepository.findOne((byte)0);
			cfm.setLookupRelation(rel);
			// adding Life Expectancy

			cfm.setDependentFlag("N");

			// clientMasterDTO.getClientLifeExpDTO().setFamilyMemberId(clientFamilyMember.getId());
			if (clientMasterDTO.getClientLifeExpDTO() != null) {
				if (cfm.getLookupRelation().getId() == 0) {
					if (clientMasterDTO.getClientLifeExpDTO().getIsProperBMI().equalsIgnoreCase("on")) {
						cfm.setIsProperBMI("Y");
					} else {
						cfm.setIsProperBMI("N");
					}

					if (clientMasterDTO.getClientLifeExpDTO().getHasNormalBP().equalsIgnoreCase("on")) {
						cfm.setHasNormalBP("Y");
					} else {
						cfm.setHasNormalBP("N");
					}

					if (clientMasterDTO.getClientLifeExpDTO().getIsTobaccoUser().equalsIgnoreCase("on")) {
						cfm.setIsTobaccoUser("Y");
					} else {
						cfm.setIsTobaccoUser("N");
					}

					if (clientMasterDTO.getClientLifeExpDTO().getHasDiseaseHistory().equalsIgnoreCase("on")) {
						cfm.setHasDiseaseHistory("Y");
					} else {
						cfm.setHasDiseaseHistory("N");
					}
					cfm.setLifeExpectancy(clientMasterDTO.getClientLifeExpDTO().getTotalLifeExpectancy());

				}
			}

			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.save(cfm);
			if (clientMasterDTO.getClientLifeExpDTO() != null) {
				clientMasterDTO.getClientLifeExpDTO().setFamilyMemberId(clientFamilyMember.getId());
				clientFamilyIncomeService.saveFamilyIncomeAfterChangeInLifeExp(clientMasterDTO.getClientLifeExpDTO());
			}
			return retcmDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException();
		}

	}

	@Override
	public List<ClientInfoDTO> findAll() {
		// TODO Auto-generated method stub
		List<ClientMaster> list = clientMasterRepository.findByActiveFlag("Y");
		List<ClientInfoDTO> listDTO = new ArrayList<ClientInfoDTO>();
		for (ClientMaster clientMaster : list) {
			ClientContact clientContact = clientMaster.getClientContacts().get(0);
			ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);
			clientInfoDTO.setEmailId(clientContact.getEmailID());
			clientInfoDTO.setMobile(clientContact.getMobile());
			listDTO.add(clientInfoDTO);
		}
		return listDTO;
	}

	@Override
	public List<ClientInfoDTO> findAllClientByUserID(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			
			AdvisorUser user = advisorUserRepository.findOne(userId);
			List<ClientInfoDTO> listDTO = new ArrayList<ClientInfoDTO>();
			List<ClientMaster> list = user.getClientMasters();
			if (list.size() > 0) {
				for (ClientMaster clientMaster : list) {
					if (clientMaster.getActiveFlag().equals("Y")) {
						List<ClientContact> l = clientMaster.getClientContacts();
						ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);
						clientInfoDTO.setRetirementStatus(clientMaster.getRetiredFlag());
						if (l.size() > 0) {
							ClientContact c = l.get(0);
							clientInfoDTO.setEmailId(c.getEmailID());
							clientInfoDTO.setMobile(c.getMobile());
						}
						listDTO.add(clientInfoDTO);
					}
				}
			}
			listDTO.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
	
	@Override
	public List<ClientInfoDTO> findAllClientByUserHierarchy(int userId) throws RuntimeException {
		// TODO Auto-generated method stub
		List<ClientInfoDTO> listDTO = null;
		try {
			
			
			listDTO = new ArrayList<ClientInfoDTO>(); 
			// =========get master List===================
			AdvisorUser advisorUser = advisorUserRepository.findOne(userId);
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(advisorUser,advisorUserSupervisorMappingRepository,clientMasterRepository);
			//==========================================
			if (clientMasterListTotal.size() > 0) {
				for (ClientMaster clientMaster : clientMasterListTotal) {
					//if (clientMaster.getActiveFlag().equals("Y")) {
						List<ClientContact> l = clientMaster.getClientContacts();
						ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);
						clientInfoDTO.setRetirementStatus(clientMaster.getRetiredFlag());
						if (l.size() > 0) {
							ClientContact c = l.get(0);
							clientInfoDTO.setEmailId(c.getEmailID());
							clientInfoDTO.setMobile(c.getMobile());
						    clientInfoDTO.setUserID(clientMaster.getAdvisorUser().getId());
						if (clientMaster.getClientAccessRight() != null) {
							clientInfoDTO.setClientInfoView(clientMaster.getClientAccessRight().getClientInfoView());
							clientInfoDTO.setClientInfoAddEdit(clientMaster.getClientAccessRight().getClientInfoAddEdit());
							clientInfoDTO.setBudgetManagementView(clientMaster.getClientAccessRight().getBudgetManagementView());
							clientInfoDTO.setGoalPlanningView(clientMaster.getClientAccessRight().getGoalPlanningView());
							clientInfoDTO.setGoalPlanningAddEdit(clientMaster.getClientAccessRight().getGoalPlanningAddEdit());
							clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
							clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
							clientInfoDTO.setFinancialPlanningView(clientMaster.getClientAccessRight().getFinancialPlanningView());
							clientInfoDTO.setFinancialPlanningAddEdit(clientMaster.getClientAccessRight().getFinancialPlanningAddEdit());
							clientInfoDTO.setInvestView(clientMaster.getClientAccessRight().getInvestView());
							clientInfoDTO.setInvestAddEdit(clientMaster.getClientAccessRight().getInvestAddEdit());
							clientInfoDTO.setMfBackOfficeView(clientMaster.getClientAccessRight().getMfBackOfficeView());
							clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
							clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
							}
							clientInfoDTO.setName(clientMaster.getFirstName() + " " + (clientMaster.getMiddleName() == null ? " " : clientMaster.getMiddleName()) + " " + clientMaster.getLastName());
							listDTO.add(clientInfoDTO);
						}
					}
				}
			listDTO.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return listDTO;
	}
	
	//pagination
	@Override
	public List<ClientInfoDTO> findAllClientByUserHierarchy(int userId, Pageable pageable) throws RuntimeException {
		// TODO Auto-generated method stub
		List<ClientInfoDTO> listDTO = null;
		try {
			
			
			listDTO = new ArrayList<ClientInfoDTO>(); 
			// =========get master List===================
			AdvisorUser advisorUser = advisorUserRepository.findOne(userId);
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(advisorUser, advisorUserSupervisorMappingRepository, 
					clientMasterRepository, pageable);
			//==========================================
			if (clientMasterListTotal.size() > 0) {
				for (ClientMaster clientMaster : clientMasterListTotal) {
					//if (clientMaster.getActiveFlag().equals("Y")) {
						List<ClientContact> l = clientMaster.getClientContacts();
						ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);
						clientInfoDTO.setRetirementStatus(clientMaster.getRetiredFlag());
						if (l.size() > 0) {
							ClientContact c = l.get(0);
							clientInfoDTO.setEmailId(c.getEmailID());
							clientInfoDTO.setMobile(c.getMobile());
						    clientInfoDTO.setUserID(clientMaster.getAdvisorUser().getId());
							if (clientMaster.getClientAccessRight() != null) {
								clientInfoDTO.setClientInfoView(clientMaster.getClientAccessRight().getClientInfoView());
								clientInfoDTO.setClientInfoAddEdit(clientMaster.getClientAccessRight().getClientInfoAddEdit());
								clientInfoDTO.setBudgetManagementView(clientMaster.getClientAccessRight().getBudgetManagementView());
								clientInfoDTO.setGoalPlanningView(clientMaster.getClientAccessRight().getGoalPlanningView());
								clientInfoDTO.setGoalPlanningAddEdit(clientMaster.getClientAccessRight().getGoalPlanningAddEdit());
								clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
								clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
								clientInfoDTO.setFinancialPlanningView(clientMaster.getClientAccessRight().getFinancialPlanningView());
								clientInfoDTO.setFinancialPlanningAddEdit(clientMaster.getClientAccessRight().getFinancialPlanningAddEdit());
								clientInfoDTO.setInvestView(clientMaster.getClientAccessRight().getInvestView());
								clientInfoDTO.setInvestAddEdit(clientMaster.getClientAccessRight().getInvestAddEdit());
								clientInfoDTO.setMfBackOfficeView(clientMaster.getClientAccessRight().getMfBackOfficeView());
								clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
								clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
								}
							clientInfoDTO.setName(clientMaster.getFirstName() + " " + (clientMaster.getMiddleName() == null ? " " : clientMaster.getMiddleName()) + " " + (clientMaster.getLastName() == null ? "" : clientMaster.getLastName()));
							clientInfoDTO.setGender(clientMaster.getGender());
							listDTO.add(clientInfoDTO);
						}
					}
				}
			listDTO.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return listDTO;
	}
	
	@Override
	public List<ClientInfoDTO> searchClientDynamicallyByUserHierarchy(int userId, String matchString) throws RuntimeException {
		// TODO Auto-generated method stub
		List<ClientInfoDTO> listDTO = null;
		try {
			
			
			listDTO = new ArrayList<ClientInfoDTO>(); 
			// =========get master List===================
			AdvisorUser advisorUser = advisorUserRepository.findOne(userId);
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.searchClientDynamicallyByUserHierarchy(advisorUser, advisorUserSupervisorMappingRepository, 
					clientMasterRepository, matchString);
			//==========================================
			if (clientMasterListTotal.size() > 0) {
				for (ClientMaster clientMaster : clientMasterListTotal) {
					//if (clientMaster.getActiveFlag().equals("Y")) {
						List<ClientContact> l = clientMaster.getClientContacts();
						ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);
						clientInfoDTO.setRetirementStatus(clientMaster.getRetiredFlag());
						if (l.size() > 0) {
							ClientContact c = l.get(0);
							clientInfoDTO.setEmailId(c.getEmailID());
							clientInfoDTO.setMobile(c.getMobile());
						    clientInfoDTO.setUserID(clientMaster.getAdvisorUser().getId());
							if (clientMaster.getClientAccessRight() != null) {
								clientInfoDTO.setClientInfoView(clientMaster.getClientAccessRight().getClientInfoView());
								clientInfoDTO.setClientInfoAddEdit(clientMaster.getClientAccessRight().getClientInfoAddEdit());
								clientInfoDTO.setBudgetManagementView(clientMaster.getClientAccessRight().getBudgetManagementView());
								clientInfoDTO.setGoalPlanningView(clientMaster.getClientAccessRight().getGoalPlanningView());
								clientInfoDTO.setGoalPlanningAddEdit(clientMaster.getClientAccessRight().getGoalPlanningAddEdit());
								clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
								clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
								clientInfoDTO.setFinancialPlanningView(clientMaster.getClientAccessRight().getFinancialPlanningView());
								clientInfoDTO.setFinancialPlanningAddEdit(clientMaster.getClientAccessRight().getFinancialPlanningAddEdit());
								clientInfoDTO.setInvestView(clientMaster.getClientAccessRight().getInvestView());
								clientInfoDTO.setInvestAddEdit(clientMaster.getClientAccessRight().getInvestAddEdit());
								clientInfoDTO.setMfBackOfficeView(clientMaster.getClientAccessRight().getMfBackOfficeView());
								clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
								clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
								}
							clientInfoDTO.setName(clientMaster.getFirstName() + " " + (clientMaster.getMiddleName() == null ? " " : clientMaster.getMiddleName()) + " " + clientMaster.getLastName());
							clientInfoDTO.setGender(clientMaster.getGender());
							listDTO.add(clientInfoDTO);
						}
					}
				}
			listDTO.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return listDTO;
	}
	
	@Override
	public List<ClientInfoDTO> searchClientByEmailDynamicallyByUserHierarchy(int userId, String matchString) throws RuntimeException {
		// TODO Auto-generated method stub
		List<ClientInfoDTO> listDTO = null;
		try {
			
			
			listDTO = new ArrayList<ClientInfoDTO>(); 
			// =========get master List===================
			AdvisorUser advisorUser = advisorUserRepository.findOne(userId);
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.searchClientByEmailDynamicallyByUserHierarchy(advisorUser, advisorUserSupervisorMappingRepository, 
					clientMasterRepository, matchString);
			//==========================================
			if (clientMasterListTotal.size() > 0) {
				for (ClientMaster clientMaster : clientMasterListTotal) {
					//if (clientMaster.getActiveFlag().equals("Y")) {
						List<ClientContact> l = clientMaster.getClientContacts();
						ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);
						clientInfoDTO.setRetirementStatus(clientMaster.getRetiredFlag());
						if (l.size() > 0) {
							ClientContact c = l.get(0);
							clientInfoDTO.setEmailId(c.getEmailID());
							clientInfoDTO.setMobile(c.getMobile());
						    clientInfoDTO.setUserID(clientMaster.getAdvisorUser().getId());
							if (clientMaster.getClientAccessRight() != null) {
								clientInfoDTO.setClientInfoView(clientMaster.getClientAccessRight().getClientInfoView());
								clientInfoDTO.setClientInfoAddEdit(clientMaster.getClientAccessRight().getClientInfoAddEdit());
								clientInfoDTO.setBudgetManagementView(clientMaster.getClientAccessRight().getBudgetManagementView());
								clientInfoDTO.setGoalPlanningView(clientMaster.getClientAccessRight().getGoalPlanningView());
								clientInfoDTO.setGoalPlanningAddEdit(clientMaster.getClientAccessRight().getGoalPlanningAddEdit());
								clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
								clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
								clientInfoDTO.setFinancialPlanningView(clientMaster.getClientAccessRight().getFinancialPlanningView());
								clientInfoDTO.setFinancialPlanningAddEdit(clientMaster.getClientAccessRight().getFinancialPlanningAddEdit());
								clientInfoDTO.setInvestView(clientMaster.getClientAccessRight().getInvestView());
								clientInfoDTO.setInvestAddEdit(clientMaster.getClientAccessRight().getInvestAddEdit());
								clientInfoDTO.setMfBackOfficeView(clientMaster.getClientAccessRight().getMfBackOfficeView());
								clientInfoDTO.setPortfolioManagementView(clientMaster.getClientAccessRight().getPortfolioManagementView());
								clientInfoDTO.setPortfolioManagementAddEdit(clientMaster.getClientAccessRight().getPortfolioManagementAddEdit());
								}
							clientInfoDTO.setName(clientMaster.getFirstName() + " " + (clientMaster.getMiddleName() == null ? " " : clientMaster.getMiddleName()) + " " + clientMaster.getLastName());
							clientInfoDTO.setGender(clientMaster.getGender());
							listDTO.add(clientInfoDTO);
						}
					}
				}
			listDTO.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
		}catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return listDTO;
	}

	@Override
	public List<ClientInfoDTO> findAllClientDashBoardByUserID(int userId) {
		// TODO Auto-generated method stub
		List<ClientInfoDTO> listDTO = new ArrayList<ClientInfoDTO>();
		
		try {
			AdvisorUser user = advisorUserRepository.findOne(userId);
			//====  new get master List  =======
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(user,advisorUserSupervisorMappingRepository,clientMasterRepository);
			//===== end master list  ====
			///List<ClientMaster> list = user.getClientMasters();
			if (clientMasterListTotal.size() > 0) {
				
				for (ClientMaster clientMaster : clientMasterListTotal) {
					if (clientMaster.getActiveFlag().equals("Y")) {
						List<ClientContact> l = clientMaster.getClientContacts();
						ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);

						if (l.size() > 0) {
							ClientContact c = l.get(0);
							clientInfoDTO.setEmailId(c.getEmailID());
							clientInfoDTO.setMobile(c.getMobile());

							listDTO.add(clientInfoDTO);
						}
					}
				}
			}
			
			Collections.sort(listDTO, new Comparator<ClientInfoDTO>() {
				public int compare(ClientInfoDTO o1, ClientInfoDTO o2) {
					return (o2.getCreatedOn()).compareTo(o1.getCreatedOn());
				}
			});
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("size "+listDTO.size());
		return listDTO;
	}

	@Override
	public int findAllAddedClientByUserID(int userId, int value) {
		// TODO Auto-generated method stub
		List<ClientMaster> addedList = null;
		Calendar cal = null;
		Date utilFromDate = null;
		Date utilTODate = null;
		//DateTime today = new DateTime();
		try {
			AdvisorUser user = advisorUserRepository.findOne(userId);
			//get user list
			FinexaUtil finexaUtil = new FinexaUtil();
		
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();
			
			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(user,
					advisorUserSupervisorMappingRepository, clientMasterRepository);
			if (value == 1) {
				cal.add(Calendar.DATE, -7);
				//addedList = clientMasterRepository.getAddedClientsForLast1Week(userIds);
				// Date lastDate=FinexaUtil.getPastDate(0,0,0,1);
				
			}
			if (value == 2) {
				cal.add(Calendar.DATE, -14);
				//addedList = clientMasterRepository.getAddedClientsForLast1Fortnight(userId);
				// Date lastDate=FinexaUtil.getPastDate(0,0,0,2);
				
			}
			if (value == 3) {
				cal.add(Calendar.MONTH, -1);
				//addedList = clientMasterRepository.getAddedClientsForLast1Month(userIds);

				//Date lastDate=FinexaUtil.getPastDate(0,1,0,0);
				
			}
			if (value == 4) {
				cal.add(Calendar.MONTH, -3);
				 //addedList = clientMasterRepository.getAddedClientsForLast3Month(userIds);
				 // Date lastDate=FinexaUtil.getPastDate(0,3,0,0);
				 
			}
			if (value == 5) {
				cal.add(Calendar.MONTH, -6);
				//addedList = clientMasterRepository.getAddedClientsForLast6Month(userIds);
				// Date lastDate=FinexaUtil.getPastDate(0,6,0,0);
				 
			}
			if (value == 6) {
				cal.add(Calendar.YEAR, -1);
				//addedList = clientMasterRepository.getAddedClientsForLast1YEAR(userIds);
				// Date lastDate=FinexaUtil.getPastDate(1,0,0,0);
				 
			}
			utilTODate = cal.getTime();
			//System.out.println("utilFromDate "+utilFromDate);
			//System.out.println("utilTODate "+utilTODate);
			
			addedList = new FinexaUtil().addedClientsInlist(utilFromDate, utilTODate, clientMasterListTotal);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addedList.size();

	}

	@Override
	public ClientMasterDTO find(int clientId) throws RuntimeException, CustomFinexaException {
		try {

			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
            //log.debug("clientMaster = " + clientMaster);
			ClientMasterDTO dto = mapper.map(clientMaster, ClientMasterDTO.class);
			//log.debug("dto = " + dto);
			dto.setId(clientId);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			//log.debug("<<<<<<<< Client Family Member >>>>>>>");

			List<ClientFamilyMemberDTO> cfmd = new ArrayList<ClientFamilyMemberDTO>();
			for (ClientFamilyMember cfm : clientMaster.getClientFamilyMembers()) {
				ClientFamilyMemberDTO icfmd = mapper.map(cfm, ClientFamilyMemberDTO.class);
				icfmd.setRelationID(cfm.getLookupRelation().getId());
				icfmd.setRelationName(cfm.getLookupRelation().getDescription());
				
				icfmd.setFmFullName(cfm.getFirstName().trim()
						+ (cfm.getMiddleName() == null ? " " : cfm.getMiddleName().trim() + " ") + cfm.getLastName().trim());
				
				icfmd.setDateOfBirth(formatter.format(cfm.getBirthDate()));
				
				cfmd.add(icfmd);
			}
			
			// dto.setClientFamilyMembersDTO(mapper.map(clientMaster.getClientFamilyMembers(),new
			// ArrayList<ClientFamilyMemberDTO>().getClass()));
			dto.setClientFamilyMembersDTO(cfmd);
			//log.debug("<<<<<<<< Client Family Member >>>>>>>");

			//log.debug("<<<<<<<< Client Contact Info >>>>>>>");
			List<ClientContactDTO> ccd = new ArrayList<ClientContactDTO>();
			for (ClientContact cc : clientMaster.getClientContacts()) {
				ccd.add(mapper.map(cc, ClientContactDTO.class));
			}
			// dto.setClientFamilyMembersDTO(mapper.map(clientMaster.getClientFamilyMembers(),new
			// ArrayList<ClientFamilyMemberDTO>().getClass()));
			dto.setClientContactsDTO(ccd);
			//log.debug("<<<<<<<< Client Contact Info >>>>>>>");

			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					dto.setFamilyMemberId(clientFamilyMember.getId());
				}
			}
			if (clientMaster.getAdvisorUser() != null) {
				dto.setUserId(clientMaster.getAdvisorUser().getId());
			} else {
				dto.setUserId(0);
			}
			if (clientMaster.getLookupMaritalStatus() != null) {
				dto.setMaritalStatus(clientMaster.getLookupMaritalStatus().getId());
			} else {
				dto.setMaritalStatus((byte) 0);
			}
			if (clientMaster.getLookupCountry() != null) {
				dto.setCountryOfResidence(clientMaster.getLookupCountry().getId());
			} else {
				dto.setCountryOfResidence(0);
			}
			if (clientMaster.getLookupEducationalQualification() != null) {
				dto.setEduQualification(clientMaster.getLookupEducationalQualification().getId());
			} else {
				dto.setEduQualification((byte) 0);
			}
			if (clientMaster.getLookupEmploymentType() != null) {
				dto.setEmploymentType(clientMaster.getLookupEmploymentType().getId());
			} else {
				dto.setEmploymentType((byte) 0);
			}
			if (clientMaster.getLookupResidentType() != null) {
				dto.setResidentType(clientMaster.getLookupResidentType().getId());
				dto.setResidentTypeName(clientMaster.getLookupResidentType().getDescription());
			} else {
				dto.setResidentType((byte) 0);
			}
			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					dto.setFamilyMemberId(clientFamilyMember.getId());
					if (clientFamilyMember.getLifeExpectancy() != null) {
						dto.setLifeExpectancy(clientFamilyMember.getLifeExpectancy());
					}
					break;
				}
			}
			dto.setAge(new FinexaUtil().getAge(clientMaster));
			dto.setRetiredFlag(clientMaster.getRetiredFlag());
			dto.setRiskProfileScore(clientMaster.getRiskProfile());
			
			dto.setClientFullName(clientMaster.getFirstName().trim()
					+ (clientMaster.getMiddleName() == null ? " " : clientMaster.getMiddleName().trim() + " ") + clientMaster.getLastName().trim());
			
			dto.setDateOfBirth(formatter.format(clientMaster.getBirthDate()));
			
		
			dto.setMaritalStatusDesc(clientMaster.getLookupMaritalStatus().getDescription());
			
			log.debug(dto.toString());
			
			return dto;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	
	@Override
	public boolean isUniqueEmailId(String emailId) {
//		ClientContact cc = clientContactRepository.findByEmailID(emailId);
//		return (cc != null) ? false : true;
		
		boolean status;
		AdvisorUser au = advisorUserRepository.findByEmailID(emailId);
		if (au != null) {
			status = false;
		} else {
			ClientContact clientContact = clientContactRepository.findByEmailID(emailId);
			if (clientContact != null) {
				status = false;
			} else {
				status = true;
			}
		}
		//return (au != null) ? false : true;
		return status;
	}

	@Override
	public boolean isUniquePan(String pan) throws RuntimeException {
		try {
			ClientMaster cm = clientMasterRepository.getByPanAndActiveFlag(pan, "Y");
			return (cm != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
	
	//======unique PAN check respective to a particular Advisor========//
	@Override
	public boolean isUniquePanForFixedAdvisor(String pan, int advisorID) throws RuntimeException {
		try {
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorID);
			ClientMaster cm = clientMasterRepository.findByPanAndAdvisorUserAndActiveFlag(pan, advisorUser, "Y");
			return (cm != null) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public boolean isUniqueAadhar(String aadhar) throws RuntimeException {
		try {
			ClientMaster cm = clientMasterRepository.getByAadharAndActiveFlag(aadhar, "Y");
			return (cm != null) ? false : true;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}

	}
	
	//======unique AADHAR check respective to a particular Advisor========//
	public boolean isUniqueAadharForFixedAdvisor(String aadhar, int advisorID) throws RuntimeException {
		try {
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorID);
			ClientMaster cm = clientMasterRepository.findByAadharAndAdvisorUserAndActiveFlag(aadhar, advisorUser, "Y");
			return (cm != null) ? false : true;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}

	}

	@Override
	public boolean checkPanExists(String pan, int clientId) throws RuntimeException {
		// List<ClientMaster> listClientMaster =
		// clientMasterRepository.findAll();
		try {
			List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");
			boolean flag = true;
			for (ClientMaster clientMaster : listClientMaster) {
				if (clientMaster.getId() != clientId) {
					if (clientMaster.getPan().equals(pan)) {
						flag = false;
						break;
					}
				}
			}
			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	@Override
	public boolean checkAadharExists(long aadhar, int clientId) throws RuntimeException {
		try {
			List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");

			boolean flag = true;
			for (ClientMaster clientMaster : listClientMaster) {
				if (clientMaster.getId() != clientId) {
					if(clientMaster.getAadhar() != null) {
					if (clientMaster.getAadhar().equals(String.valueOf(aadhar))) {
						flag = false;
						break;
					}
				  }	
				}
			}
			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	@Override
	public ClientMaster deActivate(int clientId, HttpServletRequest request) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			clientMaster.setActiveFlag("N");
			clientMaster.setFinexaUser("Y");
			clientMaster = clientMasterRepository.save(clientMaster);
			//delete AUMCacheMap
			advisorService.deletetAUMCacheMap(clientMaster.getAdvisorUser().getId(),request);
			return clientMaster;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	@Override
	public List<ClientInfoDTO> searchClient(SearchClientDTO searchClientDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<ClientInfoDTO> clientIfoList = new ArrayList<ClientInfoDTO>();
			// Specification<ClientMaster> spec =
			// ClientMasterSpecification.findByCriteria(searchClientDTO);
			
			if(!searchClientDTO.getSearchName().isEmpty() || searchClientDTO.getSearchName() != null) {
				if(searchClientDTO.getSearchName().contains(" ")) {
					String[] searchNameArr = searchClientDTO.getSearchName().split(" ");
					searchClientDTO.setSearchNameArr(searchNameArr);
				}
			}
			
			if (!searchClientDTO.getSearchAadhar().isEmpty()) {
				// log.debug("aadhar is there");

				if (searchClientDTO.getSearchAadhar().matches("[0-9]+") == false) {
					// log.debug("aadhar is incorrect");
					clientIfoList.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
					return clientIfoList;
				}

			}

			if (!searchClientDTO.getSearchMobile().isEmpty()) {

				if (searchClientDTO.getSearchMobile().matches("[0-9]+") == false) {
					clientIfoList.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
					return clientIfoList;
				}
			}
			
			Specification<ClientMaster> spec = ClientMasterSpecification.findClientRecordsByCriteria(searchClientDTO);
			List<ClientMaster> list = clientMasterRepository.findAll(spec);
			for (ClientMaster client : list) {
				ClientInfoDTO dto = new ClientInfoDTO();
				dto.setId(client.getId());
				if (client.getClientContacts().size() > 0) {

				}

				dto.setFirstName(client.getFirstName());
				dto.setMiddleName(client.getMiddleName());
				dto.setLastName(client.getLastName());
				String name = null;
				name = client.getFirstName() + " " + client.getLastName();
				dto.setName(name);
				dto.setGender(client.getGender());
				dto.setSalutation(client.getSalutation());
				dto.setUserID(client.getAdvisorUser().getId());
				
				if (client.getClientContacts().size() > 0) {
					ClientContact cc = client.getClientContacts().get(0);
					dto.setEmailId(cc.getEmailID());
					dto.setMobile(cc.getMobile());
					if (cc.getPermanentAddressLine1() != null && !cc.getPermanentAddressLine1().isEmpty()) {
						dto.setAddress1(cc.getPermanentAddressLine1());
						dto.setAddress2(cc.getPermanentAddressLine2());
						dto.setAddress3(cc.getPermanentAddressLine3());
						dto.setCity(cc.getPermanentCity());
						dto.setState(cc.getPermanentState());
						if(cc.getLookupCountry2() != null) {
							dto.setCountry(cc.getLookupCountry2().getName());
						}
					} else if (cc.getOfficeAddressLine1() != null && !cc.getOfficeAddressLine1().isEmpty()) {
						dto.setAddress1(cc.getOfficeAddressLine1());
						dto.setAddress2(cc.getOfficeAddressLine2());
						dto.setAddress3(cc.getOfficeAddressLine3());
						dto.setCity(cc.getOfficeCity());
						dto.setState(cc.getOfficeState());
						if(cc.getLookupCountry1() != null) {
							dto.setCountry(cc.getLookupCountry1().getName());
						}
					} else if (cc.getCorrespondenceAddressLine1() != null
							&& !cc.getCorrespondenceAddressLine1().isEmpty()) {
						dto.setAddress1(cc.getCorrespondenceAddressLine1());
						dto.setAddress2(cc.getCorrespondenceAddressLine2());
						dto.setAddress3(cc.getCorrespondenceAddressLine3());
						dto.setCity(cc.getCorrespondenceCity());
						dto.setState(cc.getCorrespondenceState());
						if(cc.getLookupCountry3() != null) {
							dto.setCountry(cc.getLookupCountry3().getName());
						}
					}

				}

				clientIfoList.add(dto);
			}
			clientIfoList.sort(Comparator.comparing(ClientInfoDTO::getFirstName));
			return clientIfoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}

	}
	
	@Override
	public List<ClientInfoDTO> searchClientBusiness(SearchClientDTO searchClientDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<ClientInfoDTO> clientIfoList = new ArrayList<ClientInfoDTO>();
			// Specification<ClientMaster> spec =
			// ClientMasterSpecification.findByCriteria(searchClientDTO);
			if (searchClientDTO.getSearchAadhar() != null) {
				// log.debug("aadhar is there");

				if (searchClientDTO.getSearchAadhar().matches("[0-9]+") == false) {
					return clientIfoList;
				}

			}

			if (searchClientDTO.getSearchMobile() != null) {

				if (searchClientDTO.getSearchMobile().matches("[0-9]+") == false) {
					return clientIfoList;
				}
			}

			Specification<ClientMaster> spec = ClientMasterSpecification.findClientRecordsByCriteria(searchClientDTO);
			List<ClientMaster> list = clientMasterRepository.findAll(spec);
			for (ClientMaster client : list) {
				ClientInfoDTO dto = new ClientInfoDTO();
				dto.setId(client.getId());
				if (client.getClientContacts().size() > 0) {

				}

				dto.setFirstName(client.getFirstName());
				dto.setMiddleName(client.getMiddleName());
				dto.setLastName(client.getLastName());
				String name = null;
				name = client.getFirstName() + " " + client.getLastName();
				dto.setName(name);
				dto.setGender(client.getGender());
				dto.setSalutation(client.getSalutation());
				dto.setUserID(client.getAdvisorUser().getId());
				dto.setAge(new FinexaUtil().getAge(client));
				dto.setAlreadyRetired(client.getRetiredFlag());
				dto.setOrganization(client.getOrgName());
				
				LookupMaritalStatus lookupMaritalStatus = client.getLookupMaritalStatus();
				dto.setMaritalStatus(lookupMaritalStatus.getDescription());
				
				/*
				 * System.out.println("age " + getAge(client)); System.out.println("retired " +
				 * client.getRetiredFlag()); System.out.println("organization " +
				 * client.getOrgName()); System.out.println("m " +
				 * lookupMaritalStatus.getDescription());
				 */
				
				AdvisorUser advisorUser = client.getAdvisorUser();
				dto.setUser(advisorUser.getFirstName() + " " + (advisorUser.getMiddleName() == null ? " " : advisorUser.getMiddleName()) + " " + advisorUser.getLastName());
				dto.setUserLocation(advisorUser.getCity());

				if (client.getClientContacts().size() > 0) {
					ClientContact cc = client.getClientContacts().get(0);
					dto.setEmailId(cc.getEmailID());
					dto.setMobile(cc.getMobile());
					if (cc.getPermanentAddressLine1() != null && !cc.getPermanentAddressLine1().isEmpty()) {
						dto.setAddress1(cc.getPermanentAddressLine1());
						dto.setAddress2(cc.getPermanentAddressLine2());
						dto.setAddress3(cc.getPermanentAddressLine3());
						dto.setCity(cc.getPermanentCity());
						dto.setState(cc.getPermanentState());
						if (cc.getLookupCountry2() != null) {
							dto.setCountry(cc.getLookupCountry2().getName());
						}
					} else if (cc.getOfficeAddressLine1() != null && !cc.getOfficeAddressLine1().isEmpty()) {
						dto.setAddress1(cc.getOfficeAddressLine1());
						dto.setAddress2(cc.getOfficeAddressLine2());
						dto.setAddress3(cc.getOfficeAddressLine3());
						dto.setCity(cc.getOfficeCity());
						dto.setState(cc.getOfficeState());
						dto.setCountry(cc.getLookupCountry1().getName());
					} else if (cc.getCorrespondenceAddressLine1() != null
							&& !cc.getCorrespondenceAddressLine1().isEmpty()) {
						dto.setAddress1(cc.getCorrespondenceAddressLine1());
						dto.setAddress2(cc.getCorrespondenceAddressLine2());
						dto.setAddress3(cc.getCorrespondenceAddressLine3());
						dto.setCity(cc.getCorrespondenceCity());
						dto.setState(cc.getCorrespondenceState());
						dto.setCountry(cc.getLookupCountry3().getName());
					}

				}

				clientIfoList.add(dto);
			}

			return clientIfoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException();
		}

	}

	@Override
	public List<String> getAllOrganiations() throws RuntimeException {
		try {
			HashSet<String> retset = new HashSet<String>();
			for (ClientMaster client : clientMasterRepository.findByOrgNameIsNotNull()) {
				if (StringUtils.isNotEmpty(client.getOrgName())) {
					retset.add(client.getOrgName());
				}
			}
			return retset.stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
	
	@Override
	public List<String> getAllOrganiations(int advisorID) throws RuntimeException {
		try {
			HashSet<String> retset = new HashSet<String>();
			AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
			for (ClientMaster client : clientMasterRepository.findByAdvisorUserAndOrgNameIsNotNull(advisorUser)) {
				if (StringUtils.isNotEmpty(client.getOrgName())) {
					retset.add(client.getOrgName());
				}
			}
			return retset.stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public WritableWorkbook downloadClientTemplate(HttpServletResponse response) {
		String fileName = "ClientsTemplate" + ".xls";
		WritableWorkbook writableWorkbook = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Clients", 0);
			addClientHeader(excelOutputsheet);
			writableWorkbook.write();
			writableWorkbook.close();

		} catch (Exception e) {
			log.error("Error occured while creating Excel file", e);
		}
		return writableWorkbook;
	}

	private void addClientHeader(WritableSheet sheet) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		sheet.addCell(new Label(0, 0, "fundHouse"));
		sheet.addCell(new Label(1, 0, "schemeName"));
		sheet.addCell(new Label(2, 0, "amfiCode"));
		sheet.addCell(new Label(3, 0, "isin"));
		sheet.addCell(new Label(4, 0, "assetClassID"));
		sheet.addCell(new Label(5, 0, "subAssetClassID"));
		sheet.addCell(new Label(6, 0, "schemeOption"));
		sheet.addCell(new Label(7, 0, "closeEndedFlag"));
		sheet.addCell(new Label(8, 0, "SchemeInceptionDate"));
		sheet.addCell(new Label(9, 0, "regularDirectFlag"));
		sheet.addCell(new Label(10, 0, "schemeEndDate"));
		sheet.addCell(new Label(11, 0, "exitLoadAndPeriod"));
		sheet.addCell(new Label(12, 0, "" + "minInvestmentAmount"));
		sheet.addCell(new Label(13, 0, "fundManagerCode"));
		sheet.addCell(new Label(14, 0, "benchmarkIndex"));
	}

	@Override
	public List<ClientInfoDTO> findClientBirthdayByUserID(int userId, int timePeriod)
			throws ParseException, RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		String birthDate;
		Calendar cal = null;
		Date utilFromDate = null;
		Date utilTODate = null;
		List<ClientInfoDTO> listDTO = null;
		List<ClientMaster> clientMasterList;
		FinexaUtil finexaUtil;
		try {
		//  =================getUserList==============
		finexaUtil = new FinexaUtil();
		listDTO = new ArrayList<ClientInfoDTO>();
		AdvisorUser advisorUser = advisorUserRepository.findOne(userId);
		List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(advisorUser,
				advisorUserSupervisorMappingRepository, clientMasterRepository);
		//==============================================
	
			int year = DateTime.now(DateTimeZone.forID("Asia/Calcutta")).getYear();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String dt = dateFormat.format(date);
			int thisMonth = Integer.parseInt(dt.substring(5, 7));
			int thisday = Integer.parseInt(dt.substring(8, 10));
			
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();
			

			List<ClientMaster> list = null;
			
			if (timePeriod == 1) {
				cal.add(Calendar.DATE, 7);
			}
			
			if (timePeriod == 2) {
				cal.add(Calendar.DATE, 14);
			}
			if (timePeriod == 3) {
				cal.add(Calendar.MONTH, 1);
			}
			if (timePeriod == 4) {
				cal.add(Calendar.MONTH, 3);
			}
			if (timePeriod == 5) {
				cal.add(Calendar.MONTH, 6);
			}
			if (timePeriod == 6) {
				cal.add(Calendar.YEAR, 1);
			}
			utilTODate = cal.getTime();
			clientMasterList = new FinexaUtil().addedClientsBirthDayInlist(utilFromDate, utilTODate, clientMasterListTotal); 
			

			for (ClientMaster clientMaster : clientMasterList) {

				List<ClientContact> l = clientMaster.getClientContacts();
				ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);

				Date date2 = clientMaster.getBirthDate();
				String dob = dateFormat.format(date2);

				int monthDOB = Integer.parseInt(dob.substring(5, 7));
				int dayDOB = Integer.parseInt(dob.substring(8, 10));
				if (monthDOB == thisMonth && dayDOB == thisday) {

					clientInfoDTO.setAge(new FinexaUtil().getAge(clientMaster));

				} else {
					clientInfoDTO.setAge(new FinexaUtil().getAge(clientMaster) + 1);
				}

				int yearDOB = Integer.parseInt(dob.substring(0, 4));
				yearDOB = yearDOB + clientInfoDTO.getAge();

				if (monthDOB >= 1 && monthDOB <= 9) {
					birthDate = dayDOB + "/0" + monthDOB + "/" + yearDOB;
				} else {
					birthDate = dayDOB + "/" + monthDOB + "/" + yearDOB;
				}

				clientInfoDTO.setBirthDate(birthDate);
			
				Date DOB=new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
				

				if (l.size() > 0) {
					ClientContact c = l.get(0);
					clientInfoDTO.setEmailId(c.getEmailID());
					clientInfoDTO.setMobile(c.getMobile());

					listDTO.add(clientInfoDTO);
				}

			}
			
			Collections.sort(listDTO, new Comparator<ClientInfoDTO>() {
				public int compare(ClientInfoDTO o1, ClientInfoDTO o2) {
					return (o1.getBirthDate()).compareTo(o2.getBirthDate());
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listDTO;
	}
	
	

	@Override
	public List<ClientInfoDTO> findAllDashBoardByUserAndTimePeriod(int userId, int timePeriod) {

		// DateTime today = new DateTime();
		List<ClientMaster> addedList = new ArrayList<ClientMaster>();
		List<ClientInfoDTO> listDTO = null;
		Calendar cal = null;
		Date utilFromDate = null;
		Date utilTODate = null;
		try {
			AdvisorUser advUser = advisorUserRepository.findOne(userId);
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();

			FinexaUtil finexaUtil = new FinexaUtil();

			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(advUser,
					advisorUserSupervisorMappingRepository, clientMasterRepository);
			if (timePeriod == 1) {
				cal.add(Calendar.DATE, -7);

				// addedList = clientMasterRepository.getAddedClientsForLast1Week(userIds);

				/*
				 * Date lastDate=FinexaUtil.getPastDate(0,0,0,1);
				 * log.debug("lastDate "+lastDate); log.debug("todaye  "+today.toDate());
				 * addedList=clientMasterRepository.findByCreatedOnBetween(lastDate,
				 * today.toDate());
				 */

			}
			if (timePeriod == 2) {
				cal.add(Calendar.DATE, -14);
				// addedList = clientMasterRepository.getAddedClientsForLast1Fortnight(userIds);
				// Date lastDate=FinexaUtil.getPastDate(0,0,0,2);

			}
			if (timePeriod == 3) {
				cal.add(Calendar.MONTH, -1);
				// Date lastDate=FinexaUtil.getPastDate(0,1,0,0);
			}
			if (timePeriod == 4) {
				cal.add(Calendar.MONTH, -3);
				// Date lastDate=FinexaUtil.getPastDate(0,3,0,0);
			}
			if (timePeriod == 5) {
				cal.add(Calendar.MONTH, -6);
				// Date lastDate=FinexaUtil.getPastDate(0,6,0,0);

			}
			if (timePeriod == 6) {
				cal.add(Calendar.YEAR, -1);
				// Date lastDate=FinexaUtil.getPastDate(1,0,0,0);

			}
			if (timePeriod == 7) {
				AdvisorUser user = advisorUserRepository.findOne(userId);
				addedList = user.getClientMasters();
				// Date lastDate=FinexaUtil.getPastDate(1,0,0,0);

			}
		
				addedList = new FinexaUtil().addedClientsInlist(utilFromDate, utilTODate, clientMasterListTotal);

			listDTO = new ArrayList<ClientInfoDTO>();
			if (addedList.size() > 0) {
				for (ClientMaster clientMaster : addedList) {
					if (clientMaster.getActiveFlag().equals("Y")) {
						List<ClientContact> l = clientMaster.getClientContacts();
						ClientInfoDTO clientInfoDTO = mapper.map(clientMaster, ClientInfoDTO.class);

						if (l.size() > 0) {
							ClientContact c = l.get(0);
							clientInfoDTO.setEmailId(c.getEmailID());
							clientInfoDTO.setMobile(c.getMobile());

							listDTO.add(clientInfoDTO);
						}
					}
				}
			}
			Collections.sort(listDTO, new Comparator<ClientInfoDTO>() {
				public int compare(ClientInfoDTO o1, ClientInfoDTO o2) {
					return (o2.getCreatedOn()).compareTo(o1.getCreatedOn());
				}
			});

		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listDTO;
	}

	// ===================
	
	
	@Override
	public ClientMaster autoSave(ClientMasterDTO clientMasterDTO) throws RuntimeException, CustomFinexaException {

		ClientMaster clientMasterSave = null;
			try {
				ClientMaster clientMaster = mapper.map(clientMasterDTO, ClientMaster.class);
				clientMaster.setActiveFlag("Y");
				clientMaster.setAdvisorUser(advisorUserRepository.findOne(clientMasterDTO.getUserId()));
				LookupMaritalStatus lookupMaritalStatus = (clientMasterDTO.getMaritalStatus() > 0)
						? maritalStatusRepository.findOne(clientMasterDTO.getMaritalStatus()) : null;
				clientMaster.setLookupMaritalStatus(lookupMaritalStatus);
				LookupCountry lookupCountry = (clientMasterDTO.getCountryOfResidence() > 0)
						? lookupCountryRepository.findOne(clientMasterDTO.getCountryOfResidence()) : null;
				clientMaster.setLookupCountry(lookupCountry);
				LookupEducationalQualification lookupEducationalQualification = (clientMasterDTO.getEduQualification() > 0)
						? educationalQualificationRepository.findOne(clientMasterDTO.getEduQualification()) : null;
				clientMaster.setLookupEducationalQualification(lookupEducationalQualification);
				LookupEmploymentType lookupEmploymentType = (clientMasterDTO.getEmploymentType() > 0)
						? employmentTypeRepository.findOne(clientMasterDTO.getEmploymentType()) : null;
				clientMaster.setLookupEmploymentType(lookupEmploymentType);
				LookupResidentType lookupResidentType = (clientMasterDTO.getResidentType() > 0)
						? residentTypeRepository.findOne(clientMasterDTO.getResidentType()) : null;
				clientMaster.setLookupResidentType(lookupResidentType);

				if (clientMaster.getId() != 0) {
					if (clientMasterDTO.getRiskProfileScore() != null) {
						clientMaster.setRiskProfile(clientMasterDTO.getRiskProfileScore());
					}
				}

				clientMaster.setCreatedOn(new Date());
				
				if(!clientMasterDTO.getAadhar().equals(" ")) {
					clientMaster.setAadhar(clientMasterDTO.getAadhar());
				}
				String generatedString = RandomStringUtils.randomAlphabetic(6);
				clientMaster.setLoginPassword(generatedString);
				// clientMaster.setLookupEducationalQualification(lookupEducationalQualification);
				clientMaster.setFinexaUser("Y");
				clientMasterSave = clientMasterRepository.save(clientMaster);
			   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return clientMasterSave;

	}

	@Override
	public ClientLoginInfoDTO checkLoggedInOrNot(String username) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientFixedIncomeDTO> getAssetForMaturityRenewal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientInfoDTO> writeExcelMasterListForAdvisor(int userId, int value)
			throws ParseException, RuntimeException, CustomFinexaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkIfRelationAlreadyExists(int userId, byte relationID) {
		boolean status = false;
		
		ClientMaster clientMaster = clientMasterRepository.findOne(userId);
		System.out.println("CM : " + clientMaster.getFirstName());
		LookupRelation lookupRelation = lookupRelationshipRepository.findOne(relationID);
		System.out.println("LR : " + lookupRelation.getDescription());
		ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findByClientMasterAndLookupRelation(
				clientMaster, lookupRelation);
		
		if (clientFamilyMember != null) {
			System.out.println("not empty");
			status = true;
		} else {
			System.out.println("empty");
		}
		
		return status;
	}

	@Override
	public boolean groupFamilyMembers(int clientId1, int clientId2, byte relationID, String otherRelation) {
		//boolean status = false;
		try {
			ClientMaster clientMaster1 = clientMasterRepository.findOne(clientId1);	//first client
			ClientMaster clientMaster2 = clientMasterRepository.findOne(clientId2);	//second client
			LookupRelation lookupRelation = lookupRelationshipRepository.findOne(relationID);
			
			ClientFamilyMember oldClientFamilyMember = clientFamilyMemberRepository.
					findByClientMasterAndFirstName(clientMaster1, clientMaster1.getFirstName());
	//				clientMaster1, lookupRelation);
			ClientFamilyMember newClientFamilyMember = new ClientFamilyMember();
			
			//set the new family member
			//mapper.map(oldClientFamilyMember, newClientFamilyMember);
			newClientFamilyMember.setClientMaster(clientMaster2);
			newClientFamilyMember.setFirstName(oldClientFamilyMember.getFirstName());
			newClientFamilyMember.setMiddleName(oldClientFamilyMember.getMiddleName());
			newClientFamilyMember.setLastName(oldClientFamilyMember.getLastName());
			newClientFamilyMember.setBirthDate(oldClientFamilyMember.getBirthDate());
			newClientFamilyMember.setPan(oldClientFamilyMember.getPan());
			newClientFamilyMember.setAadhar(oldClientFamilyMember.getAadhar());
			newClientFamilyMember.setLookupRelation(lookupRelation);
			newClientFamilyMember.setOtherRelation((otherRelation != null) ? otherRelation : null);
			newClientFamilyMember.setIsFamilyHead("N");
			newClientFamilyMember.setDependentFlag(oldClientFamilyMember.getDependentFlag());
			newClientFamilyMember.setRetiredFlag(oldClientFamilyMember.getRetiredFlag());
			newClientFamilyMember.setRetirementAge(oldClientFamilyMember.getRetirementAge());
			newClientFamilyMember.setLifeExpectancy(oldClientFamilyMember.getLifeExpectancy());
			newClientFamilyMember.setIsProperBMI(oldClientFamilyMember.getIsProperBMI());
			newClientFamilyMember.setIsTobaccoUser(oldClientFamilyMember.getIsTobaccoUser());
			newClientFamilyMember.setHasDiseaseHistory(oldClientFamilyMember.getHasDiseaseHistory());
			newClientFamilyMember.setHasNormalBP(oldClientFamilyMember.getHasNormalBP());
			newClientFamilyMember.setCreatedOn(oldClientFamilyMember.getCreatedOn());
			newClientFamilyMember.setLastUpdatedOn(new Timestamp(new Date().getTime()));
			
			newClientFamilyMember = clientFamilyMemberRepository.save(newClientFamilyMember);
			
			//update client annuity
			List<ClientAnnuity> clientAnnuitys = clientMaster1.getClientAnnuities();
			if (clientAnnuitys.size() != 0) {
				for (ClientAnnuity annuity : clientAnnuitys) {
					annuity.setClientFamilyMember(newClientFamilyMember);
					annuity.setClientMaster(clientMaster2);
					clientAnnuityRepository.save(annuity);
				}
			}
			//update APY
			List<ClientAtalPensionYojana> atalPensionYojanas = clientMaster1.getClientAtalPensionYojanas();
			if (atalPensionYojanas.size() != 0) {
				for (ClientAtalPensionYojana atalPensionYojana : atalPensionYojanas) {
					atalPensionYojana.setClientFamilyMember(newClientFamilyMember);
					atalPensionYojana.setClientMaster(clientMaster2);
					clientAtalPensionYojanaRepository.save(atalPensionYojana);
				}
			}
			//update cash
			List<ClientCash> clientCashs = clientMaster1.getClientCashs();
			if (clientCashs.size() != 0) {
				for (ClientCash obj : clientCashs) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientCashRepository.save(obj);
				}
			}
			//update EPF
			List<ClientEPF> clientEPFs = clientMaster1.getClientEpfs();
			if (clientEPFs.size() != 0) {
				for (ClientEPF obj : clientEPFs) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientEpfRepository.save(obj);
				}
			}
			//update equity
			List<ClientEquity> clientEquities = clientMaster1.getClientEquities();
			if (clientEquities.size() != 0) {
				for (ClientEquity obj : clientEquities) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientEquityRepository.save(obj);
				}
			}
			//update family income
			List<ClientFamilyIncome> clientFamilyIncomes = clientMaster1.getClientFamilyIncomes();
			if (clientFamilyIncomes.size() != 0) {
				for (ClientFamilyIncome obj : clientFamilyIncomes) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientFamilyIncomeRepository.save(obj);
				}
			}
			//clientFixedIncome
			List<ClientFixedIncome> clientFixedIncomes = clientMaster1.getClientFixedIncomes();
			if (clientFixedIncomes.size() != 0) {
				for (ClientFixedIncome obj : clientFixedIncomes) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientFixedIncomeRepository.save(obj);
				}
			}
			//clientFloaterCover
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findByClientMasterAndFirstName(clientMaster1, clientMaster1.getFirstName());
			List<ClientFloaterCover> clientFloaterCovers = clientFloaterCoverRepository.findByClientFamilyMember(clientFamilyMember);
			if (clientFloaterCovers.size() != 0) {
				for (ClientFloaterCover obj : clientFloaterCovers) {
					obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientFloaterCoverRepository.save(obj);
				}
			}
			//clientGoal
			List<ClientGoal> clientGoals = clientMaster1.getClientGoals();
			if (clientGoals.size() != 0) {
				for (ClientGoal obj : clientGoals) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientGoalRepository.save(obj);
				}
			}
			//clientLifeInsurance
			List<ClientLifeInsurance> clientLifeInsurances = clientMaster1.getClientLifeInsurances();
			if (clientLifeInsurances.size() != 0) {
				for (ClientLifeInsurance obj : clientLifeInsurances) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientLifeInsuranceRepository.save(obj);
				}
			}
			//clientLoan
			List<ClientLoan> clientLoans = clientMaster1.getClientLoans();
			if (clientLoans.size() != 0) {
				for (ClientLoan obj : clientLoans) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientLoanRepository.save(obj);
				}
			}
			//clientMutualFund
			List<ClientMutualFund> clientMutualFunds = clientMaster1.getClientMutualFunds();
			if (clientMutualFunds.size() != 0) {
				for (ClientMutualFund obj : clientMutualFunds) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientMutualFundRepository.save(obj);
				}
			}
			//clientNonLifeInsurance
			List<ClientNonLifeInsurance> clientNonLifeInsurances = clientMaster1.getClientNonLifeInsurances();
			if (clientNonLifeInsurances.size() != 0) {
				for (ClientNonLifeInsurance obj : clientNonLifeInsurances) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientNonLifeInsuranceRepository.save(obj);
				}
			}
			//clientNPS
			List<ClientNPS> clientNPSs = clientMaster1.getClientNps();
			if (clientNPSs.size() != 0) {
				for (ClientNPS obj : clientNPSs) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientNPSRepository.save(obj);
				}
			}
			//clientOtherAlternateAsset
			List<ClientOtherAlternateAsset> alternateAssets = clientMaster1.getClientOtherAlternateAssets();
			if (alternateAssets.size() != 0) {
				for (ClientOtherAlternateAsset obj : alternateAssets) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientOtherAlternateAssetRepository.save(obj);
				}
			}
			//clientPPF
			List<ClientPPF> clientPPFs = clientMaster1.getClientPpfs();
			if (clientPPFs.size() != 0) {
				for (ClientPPF obj : clientPPFs) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientPpfRepository.save(obj);
				}
			}
			//clientPreciousMetal
			List<ClientPreciousMetal> clientPreciousMetals = clientMaster1.getClientPreciousMetals();
			if (clientPreciousMetals.size() != 0) {
				for (ClientPreciousMetal obj : clientPreciousMetals) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientPreciousMetalRepository.save(obj);
				}
			}
			//clientRealEstate
			List<ClientRealEstate> clientRealEstates = clientMaster1.getClientRealEstates();
			if (clientRealEstates.size() != 0) {
				for (ClientRealEstate obj : clientRealEstates) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientRealEstateRepository.save(obj);
				}
			}
			//clientSmallSaving
			List<ClientSmallSaving> clientSmallSavings = clientMaster1.getClientSmallSavings();
			if (clientSmallSavings.size() != 0) {
				for (ClientSmallSaving obj : clientSmallSavings) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientSmallSavingRepository.save(obj);
				}
			}
			//clientStructuredProduct
			List<ClientStructuredProduct> clientStructuredProducts = clientMaster1.getClientStructuredProducts();
			if (clientStructuredProducts.size() != 0) {
				for (ClientStructuredProduct obj : clientStructuredProducts) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientStructuredProductRepository.save(obj);
				}
			}
			//clientVehicle
			List<ClientVehicle> clientVehicles = clientMaster1.getClientVehicles();
			if (clientVehicles.size() != 0) {
				for (ClientVehicle obj : clientVehicles) {
					obj.setClientFamilyMember(newClientFamilyMember);
					obj.setClientMaster(clientMaster2);
					clientVehicleRepository.save(obj);
				}
			}
			
			/*******deletion*******/
	
			//update contact
			List<ClientContact> clientContacts = clientMaster1.getClientContacts();
			if (clientContacts.size() != 0) {
				for (ClientContact obj : clientContacts) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientContactRepository.delete(obj);
				}
			}
			//clientFatcaReport
			List<ClientFatcaReport> clientFatcaReports = clientMaster1.getClientFatcaReports();
			if (clientFatcaReports.size() != 0) {
				for (ClientFatcaReport obj : clientFatcaReports) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientFatcaRepository.delete(obj);
				}
			}
			//clientLumpsumInflow
			List<ClientLumpsumInflow> clientLumpsumInflows = clientMaster1.getClientLumpsumInflows();
			if (clientLumpsumInflows.size() != 0) {
				for (ClientLumpsumInflow obj : clientLumpsumInflows) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientLumpsumRepository.delete(obj);
				}
			}
			//clientMandateRegistration
			List<ClientMandateRegistration> clientMandateRegistrations = clientMaster1.getClientMandateRegistrations();
			if (clientMandateRegistrations.size() != 0) {
				for (ClientMandateRegistration obj : clientMandateRegistrations) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientMandateRepository.delete(obj);
				}
			}
			//clientGuardian
			List<ClientGuardian> clientGuardians = clientMaster1.getClientGuardians();
			if (clientGuardians.size() != 0) {
				for (ClientGuardian obj : clientGuardians) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					List<ClientGuardianContact> clientGuardianContacts = obj.getClientGuardianContacts();
					if (clientGuardianContacts.size() != 0) {
						for (ClientGuardianContact contact : clientGuardianContacts) {
							clientGuardianContactRepository.delete(contact);
						}
					}
					clientGuardianRepository.delete(obj);
				}
			}
			//clientAccessRight
			ClientAccessRight clientAccessRight = clientMaster1.getClientAccessRight();
			if (clientAccessRight != null) {
	
				// clientAccessRight.setClientFamilyMember(newClientFamilyMember);
				//clientAccessRight.setClientMaster(clientMaster2);
				clientAccessRightsRepository.delete(clientAccessRight);
	
			}
			//clientExpense
			List<ClientExpense> clientExpenses = clientMaster1.getClientExpenses();
			if (clientExpenses.size() != 0) {
				for (ClientExpense obj : clientExpenses) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientExpenseRepository.delete(obj);
				}
			}
			//clientLoginInfo
			List<ClientLoginInfo> clientLoginInfos = clientMaster1.getClientLoginInfo();
			if (clientLoginInfos.size() != 0) {
				for (ClientLoginInfo obj : clientLoginInfos) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientLoginInfoRepository.delete(obj);
				}
			}
			//clientRiskProfileResponse
			List<ClientRiskProfileResponse> clientRiskProfileResponses = clientMaster1.getClientRiskProfileResponses();
			if (clientRiskProfileResponses.size() != 0) {
				for (ClientRiskProfileResponse obj : clientRiskProfileResponses) {
					//obj.setClientFamilyMember(newClientFamilyMember);
					//obj.setClientMaster(clientMaster2);
					clientRiskProfileResponseRepository.delete(obj);
				}
			}
			//clientFamilyMember
			clientFamilyMemberRepository.delete(oldClientFamilyMember);
			//clientMaster
			clientMasterRepository.delete(clientMaster1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean checkIfFamilypresent(int clientID) {
		boolean status;
		List<ClientFamilyMember> clientFamilyMembers = clientMasterRepository.findOne(clientID).getClientFamilyMembers();
		if (clientFamilyMembers.size() > 1) {
			status = true;
		} else {
			status = false;
		}
		return status;
	}

//	@Override
//	public ClientLoginInfoDTO checkLoggedInOrNot(String username) throws RuntimeException {
//		ClientLoginInfo advisorUserLoginInfo;
//		ClientLoginInfoDTO clientLoginInfoDTO = new ClientLoginInfoDTO();
//		System.out.println(username);
//		AdvisorUser advisorUser = clientMasterRepository.findByLoginUsername(username);
//		System.out.println(advisorUserLoginInfoDTO);
//		System.out.println(advisorUser);
//		if(advisorUser.getLoggedInFlag().equalsIgnoreCase("N")) {
//			advisorUserLoginInfoDTO.setLoggedInflag("N");
//			return advisorUserLoginInfoDTO;
//		}else {
//			advisorUserLoginInfoDTO.setLoggedInflag("Y");
//			advisorUserLoginInfo = advisorUserLoginInfoRepository.findTopByAdvisorUserOrderByLoginTimeDesc(advisorUser);
//			advisorUserLoginInfoDTO.setToken(advisorUserLoginInfo.getToken());
//			return advisorUserLoginInfoDTO;
//		}
//	}

	@Override
	public boolean checkPasswordExists(String email, String password) throws RuntimeException {
		ClientMaster clientMaster;
		try {
			boolean flag = false;
			clientMaster = clientMasterRepository.findByLoginUsername(email);
			if (clientMaster != null && clientMaster.getActiveFlag().equalsIgnoreCase("y")) {
				if ((clientMaster.getLoginPassword()).equals(password)) {
					flag = true;
				}
			}
			return flag;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean changePasswordByEmailId(String password, String emailId) {
		// TODO Auto-generated method stub
		ClientMaster clientMaster = null;
		clientMaster =clientMasterRepository.findByLoginUsername(emailId);
		if (clientMaster != null) {
			clientMaster.setLoginPassword(password);
			clientMaster.setLastLoginTime(new Date());
			clientMaster.setFinexaUser("Y");
			clientMasterRepository.save(clientMaster);
			return true;
		} else {
			return false;
		}

	}
   

}
