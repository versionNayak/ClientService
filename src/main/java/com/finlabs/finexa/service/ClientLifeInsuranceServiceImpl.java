package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.ClientNonlifeInsuranceDTO;
import com.finlabs.finexa.dto.ClientTaskDTO;
import com.finlabs.finexa.dto.FinexaMessageDto;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.dto.LookupLifeInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.MasterInsuranceCompanyNameDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientLifeInsurance;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.model.ClientTask;
import com.finlabs.finexa.model.LookupInsurancePolicyType;
import com.finlabs.finexa.model.LookupInsuranceType;
import com.finlabs.finexa.model.MasterInsuranceCompanyName;
import com.finlabs.finexa.model.MasterInsurancePolicy;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientLifeInsuranceRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.ClientNonlifeInsuranceRepository;
import com.finlabs.finexa.repository.LookupInsurancePolicyTypeRepository;
import com.finlabs.finexa.repository.LookupInsuranceTypeRepository;
import com.finlabs.finexa.repository.MasterInsurancePolicyRepository;
import com.finlabs.finexa.repository.MasterInsuranceCompanyNameRepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientLifeInsuranceService")
@Transactional
public class ClientLifeInsuranceServiceImpl implements ClientLifeInsuranceService {

	private static Logger log = LoggerFactory.getLogger(ClientLifeInsuranceServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientLifeInsuranceRepository clientLifeInsuranceRepository;

	@Autowired
	private MasterInsurancePolicyRepository masterInsurancePolicyRepository;

	@Autowired
	private MasterInsuranceCompanyNameRepository masterInsuranceCompanyNameRepository;

	@Autowired
	private ClientMasterRepository clientMasterRespository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private LookupInsurancePolicyTypeRepository lookupInsurancePolicyTypeRepository;

	@Autowired
	private LookupInsuranceTypeRepository lookupInsuranceTypeRepository;

	@Autowired
	private FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;

	@Autowired
	private FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	private ClientNonlifeInsuranceRepository clientNonlifeInsuranceRepository;
	
	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	
	@Autowired
	private LookupInsurancePolicyTypeRepository insurancePolicyTypeRepository;
	
	@Autowired
	private AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;

	@Override
	public ClientLifeInsuranceDTO save(ClientLifeInsuranceDTO clientLifeInsuranceDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRespository.findOne(clientLifeInsuranceDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientLifeInsuranceDTO.getFamilyMemberID());
			ClientLifeInsurance clientLifeInsurance = mapper.map(clientLifeInsuranceDTO, ClientLifeInsurance.class);
			clientLifeInsurance.setClientMaster(cm);
			clientLifeInsurance.setClientFamilyMember(cfm);
			/*
			 * MasterInsurancePolicy insurancePolicy =
			 * masterInsurancePolicyRepository
			 * .findOne(clientLifeInsuranceDTO.getPolicyNameID());
			 * 
			 * clientLifeInsurance.setMasterInsurancePolicy(insurancePolicy);
			 */
			MasterInsuranceCompanyName insuranceCompanyName = masterInsuranceCompanyNameRepository
					.findOne(clientLifeInsuranceDTO.getCompanyNameID());

			//log.debug("Client Life Insurance Service >> Save() Company Name: " + insuranceCompanyName);

			clientLifeInsurance.setMasterInsuranceCompanyName(insuranceCompanyName);

			LookupInsurancePolicyType insurancePolicyType = lookupInsurancePolicyTypeRepository
					.findOne(clientLifeInsuranceDTO.getInsurancePolicyTypeID());
			//log.debug("Client Life Insurance Service >> Save() Policy Type: " + insurancePolicyType);
			clientLifeInsurance.setLookupInsurancePolicyType(insurancePolicyType);

			//log.debug("Client Life Insurance Service >> Save() Policy Name: " + clientLifeInsuranceDTO.getPolicyName());

			/*
			 * LookupInsurancePolicyType policyType =
			 * insurancePolicyTypeRepository
			 * .findOne(clientLifeInsuranceDTO.getInsurancePolicyTypeID());
			 * insurancePolicyType.setId(policyType); LookupInsuranceType
			 * lookupInsuranceType = lookupInsuranceTypeRepository
			 * .findOne(clientLifeInsuranceDTO.getInsuranceTypeID());
			 * //clientLifeInsurance.setLookupInsuranceType(lookupInsuranceType)
			 * ;
			 */
			clientLifeInsurance = clientLifeInsuranceRepository.save(clientLifeInsurance);

			return clientLifeInsuranceDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ClientLifeInsuranceDTO autoSave(ClientLifeInsuranceDTO clientLifeInsuranceDTO) throws RuntimeException {

		try {
			ClientLifeInsurance clientLifeInsurance = mapper.map(clientLifeInsuranceDTO, ClientLifeInsurance.class);
			
			ClientMaster clientMaster = clientMasterRespository.findOne(clientLifeInsuranceDTO.getClientID());
			clientLifeInsurance.setClientMaster(clientMaster);
			
			ClientFamilyMember cfmember = clientFamilyMemberRepository.findOne(clientLifeInsuranceDTO.getFamilyMemberID());
			clientLifeInsurance.setClientFamilyMember(cfmember);
			
			MasterInsuranceCompanyName insuranceCompanyName = masterInsuranceCompanyNameRepository.findOne(clientLifeInsuranceDTO.getCompanyNameID());
			clientLifeInsurance.setMasterInsuranceCompanyName(insuranceCompanyName);

			LookupInsurancePolicyType insurancePolicyType = insurancePolicyTypeRepository.findOne(clientLifeInsuranceDTO.getInsurancePolicyTypeID());
			clientLifeInsurance.setLookupInsurancePolicyType(insurancePolicyType);
			
			clientLifeInsurance = clientLifeInsuranceRepository.save(clientLifeInsurance);

			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return clientLifeInsuranceDTO;
	}

	@Override
	public List<ClientLifeInsuranceDTO> findAll() {
		// TODO Auto-generated method stub
		ClientLifeInsuranceDTO ClientLifeInsuranceDTO;
		List<ClientLifeInsurance> listClientLifeInsurance = clientLifeInsuranceRepository.findAll();

		List<ClientLifeInsuranceDTO> listDTO = new ArrayList<ClientLifeInsuranceDTO>();
		for (ClientLifeInsurance clientLifeInsurance : listClientLifeInsurance) {
			ClientLifeInsuranceDTO = mapper.map(clientLifeInsurance, ClientLifeInsuranceDTO.class);
			ClientLifeInsuranceDTO.setClientID(clientLifeInsurance.getClientMaster().getId());
			ClientLifeInsuranceDTO
					.setInsuranceCompanyName(clientLifeInsurance.getMasterInsuranceCompanyName().getDescription());
			// clientLifeInsurance.getMasterInsurancePolicy().getMasterinsurancecompanyname().getDescription());
			ClientLifeInsuranceDTO.setOwnerName(clientLifeInsurance.getClientMaster().getFirstName());
			listDTO.add(ClientLifeInsuranceDTO);
		}
		return listDTO;
	}

	@Override
	public ClientLifeInsuranceDTO update(ClientLifeInsuranceDTO clientLifeInsuranceDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRespository.findOne(clientLifeInsuranceDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientLifeInsuranceDTO.getFamilyMemberID());
			ClientLifeInsurance clientLifeInsurance = mapper.map(clientLifeInsuranceDTO, ClientLifeInsurance.class);
			clientLifeInsurance.setClientMaster(cm);
			clientLifeInsurance.setClientFamilyMember(cfm);
			/*
			 * MasterInsurancePolicy insurancePolicy =
			 * masterInsurancePolicyRepository
			 * .findOne(clientLifeInsuranceDTO.getPolicyNameID());
			 * clientLifeInsurance.setMasterInsurancePolicy(insurancePolicy);
			 * 
			 * LookupInsurancePolicyType policyType =
			 * insurancePolicyTypeRepository
			 * .findOne(clientLifeInsuranceDTO.getInsurancePolicyTypeID());
			 * insurancePolicy.setLookupinsurancepolicytype(policyType);
			 * LookupInsuranceType lookupInsuranceType =
			 * lookupInsuranceTypeRepository
			 * .findOne(clientLifeInsuranceDTO.getInsuranceTypeID());
			 * clientLifeInsurance.setLookupInsuranceType(lookupInsuranceType);
			 */

			MasterInsuranceCompanyName insuranceCompanyName = masterInsuranceCompanyNameRepository
					.findOne(clientLifeInsuranceDTO.getCompanyNameID());

			//log.debug("Client Life Insurance Service >> update() Company Name: " + insuranceCompanyName);

			clientLifeInsurance.setMasterInsuranceCompanyName(insuranceCompanyName);

			LookupInsurancePolicyType insurancePolicyType = lookupInsurancePolicyTypeRepository
					.findOne(clientLifeInsuranceDTO.getInsurancePolicyTypeID());
			//log.debug("Client Life Insurance Service >> Save() Policy Type: " + insurancePolicyType);
			clientLifeInsurance.setLookupInsurancePolicyType(insurancePolicyType);

			//log.debug("Client Life Insurance Service >> update() Policy Name: " + clientLifeInsuranceDTO.getPolicyName());
			clientLifeInsurance = clientLifeInsuranceRepository.save(clientLifeInsurance);
			// clientLifeInsuranceDTO = mapper.map(clientLifeInsurance,
			// ClientLifeInsuranceDTO.class);
			clientLifeInsuranceDTO = new ClientLifeInsuranceDTO();
			return clientLifeInsuranceDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientLifeInsuranceDTO findById(int id) throws RuntimeException {
		try {
			ClientLifeInsurance clientLifeInsurance = clientLifeInsuranceRepository.findOne(id);
			ClientLifeInsuranceDTO clientLifeInsuranceDTO = mapper.map(clientLifeInsurance,
					ClientLifeInsuranceDTO.class);
			clientLifeInsuranceDTO.setClientID(clientLifeInsurance.getClientMaster().getId());
			// clientLifeInsuranceDTO.setPolicyNameID(clientLifeInsurance.getMasterInsurancePolicy().getId());
			// clientLifeInsuranceDTO.setCompanyId(clientLifeInsurance.getInsurance().getMasterinsurancecompanyname().getId());
			// clientLifeInsuranceDTO.setCompanyId(clientLifeInsurance.getMasterInsuranceCompanyName().getId());
			clientLifeInsuranceDTO.setCompanyNameID(clientLifeInsurance.getMasterInsuranceCompanyName().getId());
			clientLifeInsuranceDTO.setInsurancePolicyTypeID(clientLifeInsurance.getLookupInsurancePolicyType().getId());

			clientLifeInsuranceDTO
					.setInsuranceCompanyName(clientLifeInsurance.getMasterInsuranceCompanyName().getDescription());

			// clientLifeInsuranceDTO.setInsuranceTypeID(clientLifeInsurance.getLookupInsurancePolicyType().getId());
			// clientLifeInsuranceDTO.setInsurancePolicyTypeID(clientLifeInsurance.getLookupInsurancePolicyType().getId());
			clientLifeInsuranceDTO
					.setLookupPolicyTypeDesc(clientLifeInsurance.getLookupInsurancePolicyType().getDescription());
			clientLifeInsuranceDTO.setFamilyMemberID(clientLifeInsurance.getClientFamilyMember().getId());
			return clientLifeInsuranceDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientLifeInsuranceDTO> findByClientId(int clientId) throws RuntimeException {
		try {
			//log.debug("ClientLifeInsuranceServiceImpl >> findByClientId()");
			List<ClientLifeInsuranceDTO> listDTO = new ArrayList<ClientLifeInsuranceDTO>();

			//log.debug("ClientMasterRepository >> findOne()");
			ClientMaster cm = clientMasterRespository.findOne(clientId);
			//log.debug("ClientMasterRepository << findOne()");

			for (ClientLifeInsurance clientLifeInsurance : cm.getClientLifeInsurances()) {
				ClientLifeInsuranceDTO dto = mapper.map(clientLifeInsurance, ClientLifeInsuranceDTO.class);
				dto.setOwnerName(clientLifeInsurance.getClientFamilyMember().getFirstName() + "  "
						+ (clientLifeInsurance.getClientFamilyMember().getMiddleName() == null ? ""
								: clientLifeInsurance.getClientFamilyMember().getMiddleName())
						+ "  " + clientLifeInsurance.getClientFamilyMember().getLastName());
				// dto.setCompanyNameID(clientLifeInsurance.getClientInsuranceCompanyName().getId());
				dto.setInsuranceCompanyName(clientLifeInsurance.getMasterInsuranceCompanyName().getDescription());
				// dto.setInsurancePolicyTypeID(clientLifeInsurance.getLookupInsurancePolicyType().getId());
				dto.setLookupPolicyTypeDesc(clientLifeInsurance.getLookupInsurancePolicyType().getDescription());
		       
				dto.setClientID(clientId);
				listDTO.add(dto);
			}
			//log.debug("ClientLifeInsuranceServiceImpl << findByClientId()");
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub

		try {
			clientLifeInsuranceRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<MasterInsuranceCompanyNameDTO> getClientLifeInsuranceCompanyList() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<MasterInsuranceCompanyName> listMasterInsuranceCompanyName = masterInsuranceCompanyNameRepository
					.getInsuranceCompanyNameList();

			List<MasterInsuranceCompanyNameDTO> listDTO = new ArrayList<MasterInsuranceCompanyNameDTO>();
			for (MasterInsuranceCompanyName masterInsuranceCompanyName : listMasterInsuranceCompanyName) {
				MasterInsuranceCompanyNameDTO dto = mapper.map(masterInsuranceCompanyName,
						MasterInsuranceCompanyNameDTO.class);
				dto.setId(masterInsuranceCompanyName.getId());
				dto.setDescription(masterInsuranceCompanyName.getDescription());
				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupLifeInsurancePolicyTypeDTO> getClientLifeInsurancePolicyTypeList() throws RuntimeException {
		// TODO Auto-generated method stub

		try {
			Byte insId = 1;
			List<LookupInsurancePolicyType> listInsurancePolicyType = lookupInsurancePolicyTypeRepository
					.getInsurancePolicyTypeList(insId);

			List<LookupLifeInsurancePolicyTypeDTO> listDTO = new ArrayList<LookupLifeInsurancePolicyTypeDTO>();

			for (LookupInsurancePolicyType insurancePolicyType : listInsurancePolicyType) {
				LookupLifeInsurancePolicyTypeDTO insurancePolicyTypeDTO = mapper.map(insurancePolicyType,
						LookupLifeInsurancePolicyTypeDTO.class);
				insurancePolicyTypeDTO.setId(insurancePolicyType.getId());
				insurancePolicyTypeDTO.setDescription(insurancePolicyType.getDescription());
				listDTO.add(insurancePolicyTypeDTO);
			}
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MasterInsurancePolicyDTO> ClientLifeInsuranceCompanyPolicyList(int companyId) {
		Byte insId = 1;
		List<MasterInsurancePolicy> listmasterInsurancePolicy = masterInsurancePolicyRepository
				.getInsurancePolicyTypeForCompanyId(insId, companyId);

		List<MasterInsurancePolicyDTO> listDTO = new ArrayList<MasterInsurancePolicyDTO>();
		for (MasterInsurancePolicy masterInsurancePolicy : listmasterInsurancePolicy) {
			MasterInsurancePolicyDTO masterInsurancePolicyDTO = new MasterInsurancePolicyDTO();
			masterInsurancePolicyDTO.setId(masterInsurancePolicy.getLookupInsurancePolicyType().getId());
			masterInsurancePolicyDTO
					.setInsurancePolicyType(masterInsurancePolicy.getLookupInsurancePolicyType().getDescription());
			listDTO.add(masterInsurancePolicyDTO);
		}
		return listDTO;
	}

	@Override
	public MasterInsurancePolicyDTO ClientLifeInsuranceCompanyPolicyName(int companyNameId, byte insurancePolicyType) {
		MasterInsurancePolicyDTO masterInsurancePolicyDTO;
		Byte insId = 1;
		MasterInsurancePolicy masterInsurancePolicy = masterInsurancePolicyRepository
				.getInsurancePolicyNameForCompanyIdAndPolicyTypeId(insId, companyNameId, insurancePolicyType);
		masterInsurancePolicyDTO = new MasterInsurancePolicyDTO();
		masterInsurancePolicyDTO.setId(masterInsurancePolicy.getId());
		masterInsurancePolicyDTO.setPolicyName(masterInsurancePolicy.getPolicyName());
		return masterInsurancePolicyDTO;
	}

	/*
	 * @Override public void deleteClientLifeInsurance(int id) {
	 * ClientLifeInsurance clientLifeInsurance =
	 * clientLifeInsuranceRepository.findOne(id); if (clientLifeInsurance !=
	 * null) { clientLifeInsuranceRepository.delete(id); } }
	 */

	public FinexaMessageDto checkAvailPolicyNumber(String policyNumber, int clientId)
			throws CustomFinexaException, RuntimeException {
		try {
			//log.debug("ClientLifeInsuranceServiceImpl >> checkAvailPolicyNumber()");

			//log.debug("ClientMasterRepository >> findOne()");
			ClientMaster clientMaster = clientMasterRespository.findOne(clientId);
			//log.debug("ClientMasterRepository << findOne()");

			if (clientMaster == null) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_LIFE_INSURANCE_CHECK_POLICY_NUMBER);
				throw new CustomFinexaException(FinexaConstant.CLIENT_LIFE_INSURANCE_MODULE,
						FinexaConstant.CLIENT_LIFE_INSURANCE_CHECK_POLICY_NUMBER,
						exception != null ? exception.getErrorMessage() : "");
			}

			//log.debug("ClientLifeInsuranceRepository >> findPolicyNumberByPolicyNumberAndClientMaster()");
			ClientLifeInsurance clientLifeInsurance = clientLifeInsuranceRepository
					.findPolicyNumberByPolicyNumberAndClientMaster(policyNumber, clientMaster);
			//log.debug("ClientLifeInsuranceRepository << findPolicyNumberByPolicyNumberAndClientMaster()");

			FinexaMessageDto finexaMessageDto = new FinexaMessageDto();
			if (clientLifeInsurance != null) {
				finexaMessageDto.setMessage(FinexaConstant.CLIENT_LIFE_POLICY_NUMBER_EXIST);
			} else {
				finexaMessageDto.setMessage(FinexaConstant.CLIENT_LIFE_POLICY_NUMBER_AVAIL);
			}
			//log.debug("ClientLifeInsuranceServiceImpl << checkAvailPolicyNumber()");
			return finexaMessageDto;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e );
		}
	}


	@Override
	public List<ClientLifeInsuranceDTO> getLockedUptoDate(int clientId, int timePeriod) {
		List<ClientLifeInsurance> clientLifeInsuranceList = null;
		List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = null;
		Date utilFromDate = null;
		Date utilTODate = null;
		ClientMaster clientMaster = null;
		Calendar cal = null;
		try {

			
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();

			clientMaster = clientMasterRespository.findOne(clientId);
			clientLifeInsuranceList = new ArrayList<ClientLifeInsurance>();
			clientLifeInsuranceDTOList = new ArrayList<ClientLifeInsuranceDTO>();
			if (timePeriod == 1) {
				// clientLifeInsuranceList =
				// clientLifeInsuranceRepository.getLockedUptoDateForNext1Week(clientId);
				cal.add(Calendar.DATE, 7);
			}
			if (timePeriod == 2) {
				cal.add(Calendar.MONTH, 1);
			}
			if (timePeriod == 3) {
				cal.add(Calendar.MONTH, 3);

			}
			utilTODate = cal.getTime();
			clientLifeInsuranceList = new FinexaUtil().addItemInLifeInsuranceList(utilFromDate, utilTODate, clientMaster);
		
			ClientLifeInsuranceDTO clientLifeInsuranceDTO;
			for (ClientLifeInsurance clientLifeInsurance : clientLifeInsuranceList) {
				//System.out.println("return clientLifeInsurance "+clientLifeInsurance);
				clientLifeInsuranceDTO = mapper.map(clientLifeInsurance, ClientLifeInsuranceDTO.class);
				clientLifeInsuranceDTO
						.setLookupPolicyTypeDesc(clientLifeInsurance.getLookupInsurancePolicyType().getDescription());
				clientLifeInsuranceDTOList.add(clientLifeInsuranceDTO);
			}
			Collections.sort(clientLifeInsuranceDTOList, new Comparator<ClientLifeInsuranceDTO>() {
				public int compare(ClientLifeInsuranceDTO o1, ClientLifeInsuranceDTO o2) {
					return (o2.getEndDate()).compareTo(o1.getEndDate());
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clientLifeInsuranceDTOList;

	}
	@Override
	public List<ClientLifeInsuranceDTO> getLockedUptoDateForAdvisor(int advisorUserID, int timePeriod) {
		List<ClientLifeInsurance> clientLifeInsuranceList = null;
		List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = null;
		String name = "";
		String ownerName = "";
		Date utilFromDate = null;
		Date utilTODate = null;

		try {
			
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorUserID);
			// ==== new get user List =======
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(advisorUser,
					advisorUserSupervisorMappingRepository, clientMasterRespository);
			// ===== end user list ====

			Calendar cal = null;
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();

			clientLifeInsuranceDTOList = new ArrayList<ClientLifeInsuranceDTO>();
			////System.out.println("utilFromDate "+utilFromDate);
	
				if (timePeriod == 1) {
					// clientLifeInsuranceList =
					// clientLifeInsuranceRepository.getLockedUptoDateForNext1Week(client.getId());
					cal.add(Calendar.DATE, 7);
				}
				if (timePeriod == 2) {
					// clientLifeInsuranceList =
					// clientLifeInsuranceRepository.getLockedUptoDateForNext1Fortnight(client.getId());
					cal.add(Calendar.DATE, 14);
				}
				if (timePeriod == 3) {
					// clientLifeInsuranceList =
					// clientLifeInsuranceRepository.getLockedUptoDateForNext1Month(client.getId());
					cal.add(Calendar.MONTH, 1);
				}
				if (timePeriod == 4) {
					// clientLifeInsuranceList =
					// clientLifeInsuranceRepository.getLockedUptoDateForNext3Month(client.getId());
					cal.add(Calendar.MONTH, 3);
				}
				if (timePeriod == 5) {
					// clientLifeInsuranceList =
					// clientLifeInsuranceRepository.getLockedUptoDateForNext6Month(client.getId());
					cal.add(Calendar.MONTH, 6);
				}
				if (timePeriod == 6) {
					/// clientLifeInsuranceList =
					/// clientLifeInsuranceRepository.getLockedUptoDateForNext1Year(client.getId());
					cal.add(Calendar.YEAR, 1);
				}
				utilTODate  = cal.getTime();
				for (ClientMaster client : clientMasterListTotal) {
				//System.out.println("utilTODate "+utilTODate);
				clientLifeInsuranceList = new FinexaUtil().addItemInLifeInsuranceList(utilFromDate, utilTODate, client);
				//System.out.println("client "+client.getId());
				//System.out.println("clientLifeInsuranceList "+clientLifeInsuranceList.size());
				ClientLifeInsuranceDTO clientLifeInsuranceDTO;
				for (ClientLifeInsurance clientLifeInsurance : clientLifeInsuranceList) {
					clientLifeInsuranceDTO = mapper.map(clientLifeInsurance, ClientLifeInsuranceDTO.class);
					clientLifeInsuranceDTO.setLookupPolicyTypeDesc(
							clientLifeInsurance.getLookupInsurancePolicyType().getDescription());

					name = client.getFirstName() + (client.getMiddleName() != "" ? " " + client.getMiddleName() : "")
							+ " " + client.getLastName();
					clientLifeInsuranceDTO.setClientName(name);
					ownerName = client.getFirstName()
							+ (clientLifeInsurance.getClientFamilyMember().getMiddleName() != ""
									? " " + clientLifeInsurance.getClientFamilyMember().getMiddleName()
									: "")
							+ " " + clientLifeInsurance.getClientFamilyMember().getLastName();
					clientLifeInsuranceDTO.setOwnerName(ownerName);
					clientLifeInsuranceDTO.setInsuranceCompanyName(
							clientLifeInsurance.getMasterInsuranceCompanyName().getDescription());
					clientLifeInsuranceDTO.setInsuranceType("Life");
					clientLifeInsuranceDTOList.add(clientLifeInsuranceDTO);
				}

			}
			
			//System.out.println("clientLifeInsuranceDTOList "+clientLifeInsuranceDTOList.size());
			Collections.sort(clientLifeInsuranceDTOList, new Comparator<ClientLifeInsuranceDTO>() {
				public int compare(ClientLifeInsuranceDTO o1, ClientLifeInsuranceDTO o2) {
					return (o1.getEndDate()).compareTo(o2.getEndDate());
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientLifeInsuranceDTOList;

	}


	@Override
	public List<ClientLifeInsuranceDTO> getLockedUptoDateTimePeriod(int clientId, int timePeriod) {
		
		List<ClientNonLifeInsurance> clientNonLifeInsuranceList = null;
		ClientLifeInsuranceDTO clientLifeInsuranceDTO = null;;
		List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList = null;
		List<ClientLifeInsurance> clientLifeInsuranceList = null;
		ClientMaster clientMaster = null;
		ClientLifeInsurance lifeInsurance = null;
		ClientNonLifeInsurance nonlifeInsurance = null;
		Date utilFromDate = null;
		Date utilTODate = null;
		try {
			clientLifeInsuranceList = new ArrayList<ClientLifeInsurance>();
			clientNonLifeInsuranceList = new ArrayList<ClientNonLifeInsurance>(); 
			clientLifeInsuranceDTOList = new ArrayList<ClientLifeInsuranceDTO>();
			
			Calendar cal = null;
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();
			
			clientMaster = clientMasterRespository.findOne(clientId);
			
			if (timePeriod == 1) {
				// clientLifeInsuranceList =
				// clientLifeInsuranceRepository.getLockedUptoDateForNext1Week(clientId);
				cal.add(Calendar.DATE, 7);
				//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateForNext1Week(clientId);
			}
			if (timePeriod == 2) {
				// clientLifeInsuranceList =
				// clientLifeInsuranceRepository.getLockedUptoDateForNext1Month(clientId);
				cal.add(Calendar.MONTH, 1);
				//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateForNext1Month(clientId);
			}
			if (timePeriod == 3) {
				// clientLifeInsuranceList =
				// clientLifeInsuranceRepository.getLockedUptoDateForNext3Month(clientId);
				cal.add(Calendar.MONTH, 3);
				//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateForNext3Month(clientId);
			}
			utilTODate = cal.getTime();
			clientLifeInsuranceList = new FinexaUtil().addItemInLifeInsuranceList(utilFromDate, utilTODate, clientMaster);
			//System.out.println("return lifeInsurance "+lifeInsurance);
			
			
			clientNonLifeInsuranceList = new FinexaUtil().addItemInNonLifeInsuranceList(utilFromDate, utilTODate, clientMaster);
			//System.out.println("return clientNonLifeInsuranceList "+clientNonLifeInsuranceList.size());
			
			for (ClientNonLifeInsurance clientNonLifeInsurance : clientNonLifeInsuranceList) {
				//System.out.println("return 555 nonlifeInsurance "+nonlifeInsurance);
				ClientNonlifeInsuranceDTO clientNonLifeInsuranceDTO = mapper.map(clientNonLifeInsurance,
						ClientNonlifeInsuranceDTO.class);

				clientLifeInsuranceDTO = mapper.map(clientNonLifeInsuranceDTO, ClientLifeInsuranceDTO.class);

				clientLifeInsuranceDTO.setLookupPolicyTypeDesc(
						clientNonLifeInsurance.getLookupInsurancePolicyType().getDescription());

				LookupInsuranceType lookupInsuranceType = lookupInsuranceTypeRepository
						.findOne(clientNonLifeInsurance.getInsuranceTypeID());
				clientLifeInsuranceDTO.setInsuranceType(lookupInsuranceType.getDescription());
				// log.debug("clientNonLifeInsurance.getPolicyEndDate() " +
				// clientNonLifeInsurance.getPolicyEndDate());

				clientLifeInsuranceDTO.setLockedUptoDate(clientNonLifeInsuranceDTO.getPolicyEndDate());
				// log.debug(" clientLifeInsuranceDTO.setLockedUptoDate() " +
				// clientLifeInsuranceDTO.getLockedUptoDate());

				clientLifeInsuranceDTOList.add(clientLifeInsuranceDTO);
			}

			for (ClientLifeInsurance clientLifeInsurance : clientLifeInsuranceList) {
				// log.debug("date1 " + clientLifeInsurance.getLockedUptoDate());
				//System.out.println("return 555 clientLifeInsurance "+clientLifeInsurance);
				clientLifeInsuranceDTO = mapper.map(clientLifeInsurance, ClientLifeInsuranceDTO.class);
				// log.debug("date2 " + clientLifeInsuranceDTO.getLockedUptoDate());
				clientLifeInsuranceDTO
						.setLookupPolicyTypeDesc(clientLifeInsurance.getLookupInsurancePolicyType().getDescription());

				clientLifeInsuranceDTO.setInsuranceType("Life");
				clientLifeInsuranceDTOList.add(clientLifeInsuranceDTO);
			}

			Collections.sort(clientLifeInsuranceDTOList, new Comparator<ClientLifeInsuranceDTO>() {
				public int compare(ClientLifeInsuranceDTO o1, ClientLifeInsuranceDTO o2) {
					return (o1.getEndDate()).compareTo(o2.getEndDate());
				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clientLifeInsuranceDTOList;

	}

	@Override
	public List<ClientLifeInsuranceDTO> getLockedUptoDateTimePeriodForAdvisor(int advisorUserID, int timePeriod) {
		List<ClientLifeInsurance> clientLifeInsuranceList = new ArrayList<ClientLifeInsurance>();
		List<ClientNonLifeInsurance> clientNonLifeInsuranceList = new ArrayList<ClientNonLifeInsurance>();;
		ClientLifeInsuranceDTO clientLifeInsuranceDTO;
		String name="";
	    String ownerName="";
		List<ClientLifeInsuranceDTO> clientLifeInsuranceDTOList;
		clientLifeInsuranceDTOList = new ArrayList<ClientLifeInsuranceDTO>();
		ClientMaster clientmaster;
		try {
			
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorUserID);
			//====  new get user List  =======
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(advisorUser,advisorUserSupervisorMappingRepository,clientMasterRespository);
			//===== end user list  ====
			
			
			    Date utilFromDate = null;
			    Date utilTODate = null;
			
				Calendar cal = null;
				cal=Calendar.getInstance();
				
				cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				
				utilFromDate = cal.getTime();
				
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
	             Date endDate = null;
				
	             for (ClientMaster client : clientMasterListTotal) {
				//for (ClientMaster client : advisorUser.getClientMasters()) {
					if(client.getActiveFlag().equals("Y")) {
					
					for (ClientLifeInsurance clientLifeInsurance : client.getClientLifeInsurances()) {
			        	 endDate = clientLifeInsurance.getEndDate();
	                    
	                    if(endDate != null) {
				        if ((endDate.after(utilFromDate))
								&& endDate.before(utilTODate)
								|| endDate.equals(utilFromDate)
								|| endDate.equals(utilTODate)) {
				        	  
				        	
				        	
				        	clientLifeInsuranceList.add(clientLifeInsurance);
				        	
				        	
				        }
				      }
				   }
					
					for (ClientNonLifeInsurance clientNonLifeInsurance : client.getClientNonLifeInsurances()) {
			        	 endDate = clientNonLifeInsurance.getPolicyEndDate();
	                  
	                    if(endDate != null) {
				        if ((endDate.after(utilFromDate))
								&& endDate.before(utilTODate)
								|| endDate.equals(utilFromDate)
								|| endDate.equals(utilTODate)) {
				        	  
				        	clientNonLifeInsuranceList.add(clientNonLifeInsurance);
				        	
				        	
				        }
				   }
				}	
		   
	     }
					
					
	   }
			
			
			
			
			for (ClientNonLifeInsurance clientNonLifeInsurance : clientNonLifeInsuranceList) {
				try {
					//ClientMaster client = clientNonLifeInsurance.getClientMaster();
					ClientNonlifeInsuranceDTO clientNonLifeInsuranceDTO = mapper.map(clientNonLifeInsurance,
							ClientNonlifeInsuranceDTO.class);
					clientLifeInsuranceDTO = mapper.map(clientNonLifeInsuranceDTO, ClientLifeInsuranceDTO.class);
					clientLifeInsuranceDTO
							.setLookupPolicyTypeDesc(clientNonLifeInsurance.getLookupInsurancePolicyType().getDescription());
					LookupInsuranceType lookupInsuranceType = lookupInsuranceTypeRepository
							.findOne(clientNonLifeInsurance.getInsuranceTypeID());
					clientLifeInsuranceDTO.setInsuranceType(lookupInsuranceType.getDescription());
					clientLifeInsuranceDTO.setEndDate(clientNonLifeInsuranceDTO.getPolicyEndDate());
					clientmaster=clientNonLifeInsurance.getClientMaster();
					name=clientmaster.getFirstName()+(clientmaster.getMiddleName()!=""?" "+clientmaster.getMiddleName():"")+" "+clientmaster.getLastName();
					clientLifeInsuranceDTO.setClientName(name);
					name=clientmaster.getFirstName()+(clientmaster.getMiddleName()!=null?" "+clientmaster.getMiddleName():"")+" "+clientmaster.getLastName();
					clientLifeInsuranceDTO.setClientName(name);
					ownerName=clientmaster.getFirstName()+(clientNonLifeInsurance.getClientFamilyMember().getMiddleName()!=null?" "+clientNonLifeInsurance.getClientFamilyMember().getMiddleName():"")+" "+clientNonLifeInsurance.getClientFamilyMember().getLastName();
					clientLifeInsuranceDTO.setOwnerName(ownerName);
					clientLifeInsuranceDTO.setInsuranceCompanyName(clientNonLifeInsurance.getMasterInsuranceCompanyName().getDescription());
					clientLifeInsuranceDTOList.add(clientLifeInsuranceDTO);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (ClientLifeInsurance clientLifeInsurance : clientLifeInsuranceList) {
				try {
					//ClientMaster client = clientLifeInsurance.getClientMaster();
					clientLifeInsuranceDTO = mapper.map(clientLifeInsurance, ClientLifeInsuranceDTO.class);
					clientLifeInsuranceDTO
							.setLookupPolicyTypeDesc(clientLifeInsurance.getLookupInsurancePolicyType().getDescription());

					clientLifeInsuranceDTO.setInsuranceType("Life");
					clientmaster=clientLifeInsurance.getClientMaster();
					name=clientmaster.getFirstName()+(clientmaster.getMiddleName()!=""?" "+clientmaster.getMiddleName():"")+" "+clientmaster.getLastName();
					clientLifeInsuranceDTO.setClientName(name);
					ownerName=clientmaster.getFirstName()+(clientLifeInsurance.getClientFamilyMember().getMiddleName()!=""?" "+clientLifeInsurance.getClientFamilyMember().getMiddleName():"")+" "+clientLifeInsurance.getClientFamilyMember().getLastName();
					clientLifeInsuranceDTO.setOwnerName(ownerName);
					clientLifeInsuranceDTO.setInsuranceCompanyName(clientLifeInsurance.getMasterInsuranceCompanyName().getDescription());
					clientLifeInsuranceDTOList.add(clientLifeInsuranceDTO);
				} catch (Exception e) {
					e.printStackTrace();
				}

			
			////System.out.println("clientLifeInsuranceDTOList size "+clientLifeInsuranceDTOList.size());
			
		}
			Collections.sort(clientLifeInsuranceDTOList, new Comparator<ClientLifeInsuranceDTO>() {
				public int compare(ClientLifeInsuranceDTO o1, ClientLifeInsuranceDTO o2) {
					return (o1.getEndDate()).compareTo(o2.getEndDate());
				}
			});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientLifeInsuranceDTOList;
	}
	
	
}