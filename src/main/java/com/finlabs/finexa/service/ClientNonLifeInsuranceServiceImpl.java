package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientFloaterCoverDTO;
import com.finlabs.finexa.dto.ClientLifeInsuranceDTO;
import com.finlabs.finexa.dto.ClientNonlifeInsuranceDTO;
import com.finlabs.finexa.dto.FinexaMessageDto;
import com.finlabs.finexa.dto.MasterInsuranceCompanyNameDTO;
import com.finlabs.finexa.dto.LookupHealthInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.LookupNonLifeInsurancePolicyTypeDTO;
import com.finlabs.finexa.dto.LookupNonlifeInsuranceTypeDTO;
import com.finlabs.finexa.dto.MasterInsurancePolicyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFloaterCover;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.model.FinexaBusinessSubmodule;
import com.finlabs.finexa.model.FinexaExceptionHandling;
import com.finlabs.finexa.model.LookupInsurancePolicyType;
import com.finlabs.finexa.model.LookupInsuranceType;
import com.finlabs.finexa.model.MasterInsurancePolicy;
import com.finlabs.finexa.model.MasterInsuranceCompanyName;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientFloaterCoverRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientNonlifeInsuranceRepository;
import com.finlabs.finexa.repository.FinexaBusinessSubmoduleRepository;
import com.finlabs.finexa.repository.FinexaExceptionHandlingRepository;
import com.finlabs.finexa.repository.LookupInsurancePolicyTypeRepository;
import com.finlabs.finexa.repository.LookupInsuranceTypeRepository;
import com.finlabs.finexa.repository.MasterInsurancePolicyRepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.repository.MasterInsuranceCompanyNameRepository;

@Service("ClientNonLifeInsuranceService")
@Transactional

public class ClientNonLifeInsuranceServiceImpl implements ClientNonLifeInsuranceService {
	private static Logger log = LoggerFactory.getLogger(ClientNonLifeInsuranceServiceImpl.class);

	@Autowired
	private Mapper mapper;
	@Autowired
	private ClientMasterRepository clientMasterRespository;
	@Autowired
	private ClientNonlifeInsuranceRepository clientNonlifeInsuranceRepository;
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	@Autowired
	private LookupInsurancePolicyTypeRepository lookupInsurancePolicyTypeRepository;
	@Autowired
	private MasterInsurancePolicyRepository masterInsurancePolicyRepository;
	// Bug Fix CIUAT-271 Company Name and Policy Type foreign keys changed to
	// point to respective master tables in DB
	@Autowired
	private MasterInsuranceCompanyNameRepository masterInsuranceCompanyNameRepository;
	// End Bug Fix CIUAT-271

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	@Autowired
	private LookupInsurancePolicyTypeRepository insurancePolicyTypeRepository;
	@Autowired
	private FinexaExceptionHandlingRepository finexaExceptionHandlingRepository;
	@Autowired
	private FinexaBusinessSubmoduleRepository finexaBusinessSubmoduleRepository;
	@Autowired
	private ClientFloaterCoverRepository clientFloaterCoverRepository;
	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	@Autowired
	private LookupInsuranceTypeRepository lookupInsuranceTypeRepository;
	@Autowired
	private AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;

	@Override
	public ClientNonlifeInsuranceDTO save(ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO) throws RuntimeException {

		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientNonlifeInsuranceDTO.getClientID());

			for (Integer s : clientNonlifeInsuranceDTO.getCheckedFamilyMemberID()) {
				log.debug("member id " + s);
			}
			ClientFamilyMember cfmember = clientFamilyMemberRepository
					.findOne(clientNonlifeInsuranceDTO.getFamilyMemberID());
			// ClientFloaterCover cfloaterCover = clientFloaterCoverRepository
			// .findOne(clientNonlifeInsuranceDTO.get);
			ClientNonLifeInsurance clientNonLifeInsurance = mapper.map(clientNonlifeInsuranceDTO,
					ClientNonLifeInsurance.class);

			clientNonLifeInsurance.setClientMaster(clientMaster);
			clientNonLifeInsurance.setClientFamilyMember(cfmember);

			// Bug Fix CIUAT-271 Company Name and Policy Type foreign keys
			// changed
			// to point to respective master tables in DB

			/*
			 * MasterInsurancePolicy insurancePolicy =
			 * masterInsurancePolicyRepository
			 * .findOne(clientNonlifeInsuranceDTO.getPolicyNameID());
			 * clientNonLifeInsurance.setMasterInsurancePolicy(insurancePolicy);
			 * 
			 * LookupInsurancePolicyType policyType =
			 * insurancePolicyTypeRepository
			 * .findOne(clientNonlifeInsuranceDTO.getInsurancePolicyTypeID());
			 * insurancePolicy.setLookupinsurancepolicytype(policyType);
			 */
			MasterInsuranceCompanyName insuranceCompanyName = masterInsuranceCompanyNameRepository
					.findOne(clientNonlifeInsuranceDTO.getCompanyNameID());

			log.debug("Client Non Life Insurance Service >> Save() Company Name: " + insuranceCompanyName);

			clientNonLifeInsurance.setMasterInsuranceCompanyName(insuranceCompanyName);

			LookupInsurancePolicyType insurancePolicyType = insurancePolicyTypeRepository
					.findOne(clientNonlifeInsuranceDTO.getInsurancePolicyTypeID());
			log.debug("Client Non Life Insurance Service >> Save() Policy Type: " + insurancePolicyType);
			clientNonLifeInsurance.setLookupInsurancePolicyType(insurancePolicyType);
			/*
			 * LookupInsuranceType lookupInsuranceType =
			 * lookupInsuranceTypeRepository
			 * .findOne(clientNonlifeInsuranceDTO.getInsuranceTypeID());
			 * clientNonLifeInsurance.setLookupInsuranceType(lookupInsuranceType
			 * );g
			 */
			// End Bug Fix CIUAT-271
			clientNonLifeInsurance.setCoverFlag(clientNonlifeInsuranceDTO.getCoverFlag());

			// ClientFloaterCover floaterCover = new ClientFloaterCover();
			clientNonLifeInsurance = clientNonlifeInsuranceRepository.save(clientNonLifeInsurance);
			// log.debug("ddddddddddddddd");
			ClientFloaterCoverDTO clientFloaterCoverDTO = mapper.map(clientNonlifeInsuranceDTO,
					ClientFloaterCoverDTO.class);

			// log.debug("ggggggggggggg");
			for (int memberId : clientFloaterCoverDTO.getCheckedFamilyMemberID()) {
				// log.debug("memberId uuuu "+memberId);
				////System.out.println("memberID :" + memberId);
				ClientFloaterCover clientFloaterCover = mapper.map(clientFloaterCoverDTO, ClientFloaterCover.class);
				clientFloaterCover.setClientFamilyMember(clientFamilyMemberRepository.findOne(memberId));
				clientFloaterCover.setClientNonLifeInsurance(
						clientNonlifeInsuranceRepository.findOne((int) clientNonLifeInsurance.getId()));

				clientFloaterCoverRepository.save(clientFloaterCover);
			}
			return clientNonlifeInsuranceDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ClientFloaterCoverDTO saveFloaterCover(ClientFloaterCoverDTO clientFloaterCoverDTO) throws RuntimeException {

//		try {
//			ClientFloaterCover clientFloaterCover = mapper.map(clientFloaterCoverDTO, ClientFloaterCover.class);
//			
//			ClientFamilyMember cfmember = clientFamilyMemberRepository.findOne(clientFloaterCoverDTO.getFamilyMemberID());
//			clientFloaterCover.setClientFamilyMember(cfmember);
//			
//			ClientNonLifeInsurance clientNonLifeInsurance = clientNonlifeInsuranceRepository.findOne((int) clientFloaterCoverDTO.getInsuranceID());
//			clientFloaterCover.setClientNonLifeInsurance(clientNonLifeInsurance);
//			
//			clientFloaterCover = clientFloaterCoverRepository.save(clientFloaterCover);
//			
//		} catch(RuntimeException e) {
			//throw new RuntimeException();
		//}
		//return clientFloaterCoverDTO;
		return null;
	}
	
	@Override
	public ClientNonlifeInsuranceDTO autoSave(ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO, ClientFloaterCoverDTO clientFloaterCoverDTO) throws RuntimeException {

		try {
			ClientNonLifeInsurance clientNonLifeInsurance = mapper.map(clientNonlifeInsuranceDTO, ClientNonLifeInsurance.class);
			
			ClientMaster clientMaster = clientMasterRepository.findOne(clientNonlifeInsuranceDTO.getClientID());
			clientNonLifeInsurance.setClientMaster(clientMaster);
			
			ClientFamilyMember cfmember = clientFamilyMemberRepository.findOne(clientNonlifeInsuranceDTO.getFamilyMemberID());
			clientNonLifeInsurance.setClientFamilyMember(cfmember);
			
			MasterInsuranceCompanyName insuranceCompanyName = masterInsuranceCompanyNameRepository.findOne(clientNonlifeInsuranceDTO.getCompanyNameID());
			clientNonLifeInsurance.setMasterInsuranceCompanyName(insuranceCompanyName);

			LookupInsurancePolicyType insurancePolicyType = insurancePolicyTypeRepository.findOne(clientNonlifeInsuranceDTO.getInsurancePolicyTypeID());
			clientNonLifeInsurance.setLookupInsurancePolicyType(insurancePolicyType);
			
			//LookupInsuranceType insuranceType = lookupInsuranceTypeRepository.findOne(clientNonlifeInsuranceDTO.getInsuranceTypeID());
			clientNonLifeInsurance.setInsuranceTypeID(clientNonlifeInsuranceDTO.getInsuranceTypeID());
			
			clientNonLifeInsurance.setCoverFlag(clientNonlifeInsuranceDTO.getCoverFlag());
			clientNonLifeInsurance.setPremiumAmount(clientNonlifeInsuranceDTO.getPremiumAmount());
			clientNonLifeInsurance.setOtherPolicyType(clientNonlifeInsuranceDTO.getOtherPolicyType());
			clientNonLifeInsurance.setPolicyName(clientNonlifeInsuranceDTO.getPolicyName());
			clientNonLifeInsurance.setPolicyNumber(clientNonlifeInsuranceDTO.getPolicyNumber());
			clientNonLifeInsurance.setPolicyEndDate(clientNonlifeInsuranceDTO.getPolicyEndDate());
			clientNonLifeInsurance.setPolicyStartDate(clientNonlifeInsuranceDTO.getPolicyStartDate());
			clientNonLifeInsurance.setSumInsured(clientNonlifeInsuranceDTO.getSumInsured());
			
			clientNonLifeInsurance = clientNonlifeInsuranceRepository.save(clientNonLifeInsurance);
			
			//ClientFloaterCoverDTO clientFloaterCoverDTO = mapper.map(clientNonlifeInsuranceDTO,ClientFloaterCoverDTO.class);

			// log.debug("ggggggggggggg");
			for (int memberId : clientFloaterCoverDTO.getCheckedFamilyMemberID()) {
				// log.debug("memberId uuuu "+memberId);
				////System.out.println("memberID :" + memberId);
				ClientFloaterCover clientFloaterCover = mapper.map(clientFloaterCoverDTO, ClientFloaterCover.class);
				clientFloaterCover.setClientFamilyMember(clientFamilyMemberRepository.findOne(memberId));
				clientFloaterCover.setClientNonLifeInsurance(
						clientNonlifeInsuranceRepository.findOne((int) clientNonLifeInsurance.getId()));

				clientFloaterCoverRepository.save(clientFloaterCover);
			}

			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return clientNonlifeInsuranceDTO;
	}

	/*
	 * @Override public ClientFloaterCoverDTO
	 * saveFloaterCover(ClientFloaterCoverDTO clientFloaterCoverDTO) { // TODO
	 * Auto-generated method stub LookupInsurancePolicyType
	 * lookupInsurancePolicyType =
	 * lookupInsurancePolicyTypeRepository.findOne((byte)13);
	 * ClientNonLifeInsurance clientNonLifeInsurance =
	 * clientNonlifeInsuranceRepository.
	 * findByLookupInsurancePolicyTypeAndInsuranceTypeID(
	 * lookupInsurancePolicyType, clientFloaterCoverDTO.getInsuranceID());
	 * ClientFloaterCover clientFloaterCover = mapper.map(clientFloaterCoverDTO,
	 * ClientFloaterCover.class);
	 * clientFloaterCover.setClientNonLifeInsurance(clientNonLifeInsurance);
	 * ClientFamilyMember chkdFamMember =
	 * clientFamilyMemberRepository.findOne(clientFloaterCoverDTO.
	 * getCheckedFamilyMemberID()); //ClientFamilyMember chngdFamMember =
	 * clientFamilyMemberRepository.findOne(clientFloaterCoverDTO.
	 * getChangedFamilyMemberID());
	 * clientFloaterCover.setClientFamilyMember(chkdFamMember);
	 * //clientFloaterCover.setClientFamilyMember(chngdFamMember);
	 * clientFloaterCover =
	 * clientFloaterCoverRepository.save(clientFloaterCover); return
	 * clientFloaterCoverDTO; }
	 */

	@Override
	public ClientNonlifeInsuranceDTO update(ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO)
			throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientNonlifeInsuranceDTO.getClientID());
			ClientNonLifeInsurance clientNonLifeInsurance = mapper.map(clientNonlifeInsuranceDTO,
					ClientNonLifeInsurance.class);
			ClientFamilyMember cfmember = clientFamilyMemberRepository
					.findOne(clientNonlifeInsuranceDTO.getFamilyMemberID());
			clientNonLifeInsurance.setClientMaster(clientMaster);
			clientNonLifeInsurance.setClientFamilyMember(cfmember);

			// Bug Fix CIUAT-271 Company Name and Policy Type foreign keys
			// changed
			// to point to respective master tables in DB
			/*
			 * MasterInsurancePolicy insurancePolicy =
			 * masterInsurancePolicyRepository
			 * .findOne(clientNonlifeInsuranceDTO.getPolicyNameID());
			 * clientNonLifeInsurance.setMasterInsurancePolicy(insurancePolicy);
			 */

			MasterInsuranceCompanyName insuranceCompanyName = masterInsuranceCompanyNameRepository
					.findOne(clientNonlifeInsuranceDTO.getCompanyNameID());

			log.debug("Client Non Life Insurance Service >> Update() Company Name: " + insuranceCompanyName);

			clientNonLifeInsurance.setMasterInsuranceCompanyName(insuranceCompanyName);

			LookupInsurancePolicyType policyType = insurancePolicyTypeRepository
					.findOne(clientNonlifeInsuranceDTO.getInsurancePolicyTypeID());
			clientNonLifeInsurance.setLookupInsurancePolicyType(policyType);
			/*
			 * LookupInsuranceType lookupInsuranceType =
			 * lookupInsuranceTypeRepository
			 * .findOne(clientNonlifeInsuranceDTO.getInsuranceTypeID());
			 * //clientNonLifeInsurance.setLookupInsuranceType(
			 * lookupInsuranceType);
			 */
			// End Bug Fix CIUAT-271
			clientNonLifeInsurance.setCoverFlag(clientNonlifeInsuranceDTO.getCoverFlag());

			// ClientFloaterCover floaterCover = new ClientFloaterCover();
			clientNonLifeInsurance = clientNonlifeInsuranceRepository.save(clientNonLifeInsurance);
			// log.debug("ddddddddddddddd");
			ClientFloaterCoverDTO clientFloaterCoverDTO = mapper.map(clientNonlifeInsuranceDTO,
					ClientFloaterCoverDTO.class);

			// log.debug("ggggggggggggg");
			for (int memberId : clientFloaterCoverDTO.getCheckedFamilyMemberID()) {
				// log.debug("memberId uuuu "+memberId);
				ClientFloaterCover clientFloaterCover = mapper.map(clientFloaterCoverDTO, ClientFloaterCover.class);
				clientFloaterCover.setClientFamilyMember(clientFamilyMemberRepository.findOne(memberId));
				clientFloaterCover.setClientNonLifeInsurance(
						clientNonlifeInsuranceRepository.findOne((int) clientNonLifeInsurance.getId()));

				clientFloaterCoverRepository.save(clientFloaterCover);
			}
			return clientNonlifeInsuranceDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientNonlifeInsuranceDTO findById(int id) throws RuntimeException {
		try {
			ClientNonLifeInsurance clientNonLifeInsurance = clientNonlifeInsuranceRepository.findOne(id);
			ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO = mapper.map(clientNonLifeInsurance,
					ClientNonlifeInsuranceDTO.class);
			clientNonlifeInsuranceDTO.setClientID(clientNonLifeInsurance.getClientMaster().getId());
			////////////// done by saheli////////////////////////
			// ClientFloaterCoverDTO clientFloaterCoverDTO =
			////////////// mapper.map(clientNonlifeInsuranceDTO,ClientFloaterCoverDTO.class);
			// ClientFloaterCover clientFloaterCover=
			////////////// mapper.map(clientFloaterCoverDTO,ClientFloaterCover.class);
			////////////// done by saheli////////////////////////
			// Bug Fix CIUAT-271 Company Name and Policy Type foreign keys
			////////////// changed
			// to point to respective master tables in DB

			// clientNonlifeInsuranceDTO.setPolicyNameID(clientNonLifeInsurance.getMasterInsurancePolicy().getId());
			// clientNonlifeInsuranceDTO.setCompanyId(
			// clientNonLifeInsurance.getMasterInsurancePolicy().getMasterinsurancecompanyname().getId());
			// clientNonlifeInsuranceDTO.setInsuranceTypeID(clientNonLifeInsurance.getLookupInsuranceType().getId());
			// clientNonlifeInsuranceDTO.setInsurancePolicyTypeID(
			// clientNonLifeInsurance.getMasterInsurancePolicy().getLookupinsurancepolicytype().getId());
			clientNonlifeInsuranceDTO.setCompanyNameID(clientNonLifeInsurance.getMasterInsuranceCompanyName().getId());
			clientNonlifeInsuranceDTO
					.setInsurancePolicyTypeID(clientNonLifeInsurance.getLookupInsurancePolicyType().getId());

			clientNonlifeInsuranceDTO
					.setInsuranceCompanyName(clientNonLifeInsurance.getMasterInsuranceCompanyName().getDescription());

			// clientNonlifeInsuranceDTO.setInsuranceTypeID(clientNonLifeInsurance.getInsuranceTypeID());
			// clientLifeInsuranceDTO.setInsurancePolicyTypeID(clientLifeInsurance.getLookupInsurancePolicyType().getId());
			clientNonlifeInsuranceDTO
					.setLookupPolicyTypeDesc(clientNonLifeInsurance.getLookupInsurancePolicyType().getDescription());
			// End Bug Fix CIUAT-271

			clientNonlifeInsuranceDTO.setFamilyMemberID(clientNonLifeInsurance.getClientFamilyMember().getId());

			List<ClientFloaterCover> clientFloaterCover = clientFloaterCoverRepository
					.findClientFamilyMemberByClientNonLifeInsurance(clientNonLifeInsurance);
			//log.debug("clientFloaterCover: " + clientFloaterCover.get(0).getClientFamilyMember().getId());
			//log.debug("clientFloaterCover: " + clientFloaterCover.get(1).getClientFamilyMember().getId());

			for (ClientFloaterCover cfc : clientNonLifeInsurance.getClientFloaterCovers()) {
				clientNonlifeInsuranceDTO.getCheckedFamilyMemberID().add(cfc.getClientFamilyMember().getId());
			}

			clientNonlifeInsuranceDTO.setCheckedFamilyMemberID(clientNonlifeInsuranceDTO.getCheckedFamilyMemberID());
			
			log.debug("non life insurance dto: " +  clientNonlifeInsuranceDTO);

			return clientNonlifeInsuranceDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientNonlifeInsuranceDTO findPolicyNumber(String policyNumber, int clientId) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
		ClientNonLifeInsurance clientNonLifeInsurance = clientNonlifeInsuranceRepository
				.findPolicyNumberByPolicyNumberAndClientMaster(policyNumber, clientMaster);
		ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO = new ClientNonlifeInsuranceDTO();
		if (clientNonLifeInsurance != null) {
			clientNonlifeInsuranceDTO.setPolicyNumber(policyNumber);
		}
		return clientNonlifeInsuranceDTO;
	}

	public FinexaMessageDto checkAvailPolicyNumber(String policyNumber, int clientId)
			throws CustomFinexaException, RuntimeException {
		try {
			log.debug("ClientNonLifeInsuranceServiceImpl >> checkAvailPolicyNumber()");

			log.debug("ClientMasterRepository >> findOne()");
			ClientMaster clientMaster = clientMasterRespository.findOne(clientId);
			log.debug("ClientMasterRepository << findOne()");

			if (clientMaster == null) {
				FinexaBusinessSubmodule subModule = finexaBusinessSubmoduleRepository
						.findByCode(FinexaConstant.MY_CLIENT_CLIENT_INFORMATION_INSURANCE);
				FinexaExceptionHandling exception = finexaExceptionHandlingRepository
						.findByFinexaBusinessSubmoduleAndErrorCode(subModule,
								FinexaConstant.CLIENT_NON_LIFE_INSURANCE_CHECK_POLICY_TYPE_LIST_ERROR);
				throw new CustomFinexaException(FinexaConstant.CLIENT_NON_LIFE_INSURANCE_MODULE,
						FinexaConstant.CLIENT_NON_LIFE_INSURANCE_CHECK_POLICY_TYPE_LIST_ERROR,
						exception != null ? exception.getErrorMessage() : "");
			}

			log.debug("ClientNonLifeInsuranceRepository >> findPolicyNumberByPolicyNumberAndClientMaster()");
			ClientNonLifeInsurance clientNonLifeInsurance = clientNonlifeInsuranceRepository
					.findPolicyNumberByPolicyNumberAndClientMaster(policyNumber, clientMaster);
			log.debug("ClientNonLifeInsuranceRepository << findPolicyNumberByPolicyNumberAndClientMaster()");

			FinexaMessageDto finexaMessageDto = new FinexaMessageDto();
			if (clientNonLifeInsurance != null) {
				finexaMessageDto.setMessage(FinexaConstant.CLIENT_NONLIFE_POLICY_NUMBER_EXIST);
			} else {
				finexaMessageDto.setMessage(FinexaConstant.CLIENT_NONLIFE_POLICY_NUMBER_AVAIL);
			}
			log.debug("ClientNonLifeInsuranceServiceImpl << checkAvailPolicyNumber()");
			return finexaMessageDto;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {
		try {
			clientFloaterCoverRepository.deleteClientFloaterCover(id);
			clientNonlifeInsuranceRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientNonlifeInsuranceDTO> findAll() {
		// TODO Auto-generated method stub
		ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO;
		List<ClientNonLifeInsurance> listClientNonlifeInsurance = clientNonlifeInsuranceRepository.findAll();

		List<ClientNonlifeInsuranceDTO> listDTO = new ArrayList<ClientNonlifeInsuranceDTO>();
		for (ClientNonLifeInsurance clientNonLifeInsurance : listClientNonlifeInsurance) {
			clientNonlifeInsuranceDTO = mapper.map(clientNonLifeInsurance, ClientNonlifeInsuranceDTO.class);
			// clientNonlifeInsuranceDTO.setClientID(clientNonLifeInsurance.getClientMaster().getId());
			clientNonlifeInsuranceDTO
					.setInsuranceCompanyName(clientNonLifeInsurance.getMasterInsuranceCompanyName().getDescription());
			// clientLifeInsurance.getMasterInsurancePolicy().getMasterinsurancecompanyname().getDescription());
			listDTO.add(clientNonlifeInsuranceDTO);
		}

		return listDTO;
	}

	@Override
	public List<ClientNonlifeInsuranceDTO> findByClientId(int clientId) throws RuntimeException {
		try {
			log.debug("Client Non Life Insurance Service >> findByClientId(int clientId)");
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientNonlifeInsuranceDTO> listDTO = new ArrayList<ClientNonlifeInsuranceDTO>();
			for (ClientNonLifeInsurance clientNonLifeInsurance : cm.getClientNonLifeInsurances()) {
				ClientNonlifeInsuranceDTO dto = mapper.map(clientNonLifeInsurance, ClientNonlifeInsuranceDTO.class);
				log.debug("Client Non Life Insurance Service >> findByClientId Company Name ID: "
						+ clientNonLifeInsurance.getMasterInsuranceCompanyName().getId());
				dto.setClientID(clientId);
				dto.setOwnerName(clientNonLifeInsurance.getClientFamilyMember().getFirstName() + "  "
						+ (clientNonLifeInsurance.getClientFamilyMember().getMiddleName() == null ? ""
								: clientNonLifeInsurance.getClientFamilyMember().getMiddleName())
						+ "  " + clientNonLifeInsurance.getClientFamilyMember().getLastName());
				// dto.setInsuranceCompanyName(
				// clientNonLifeInsurance.getMasterInsurancePolicy().getMasterinsurancecompanyname().getDescription());
				dto.setInsuranceCompanyName(clientNonLifeInsurance.getMasterInsuranceCompanyName().getDescription());
				dto.setLookupPolicyTypeDesc(clientNonLifeInsurance.getLookupInsurancePolicyType().getDescription());
				listDTO.add(dto);
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupNonlifeInsuranceTypeDTO> findAllNonInsuranceTypeList() {
		// LookupNonlifeInsuranceTypeDTO clientNonlifeInsuranceDTO;
		/*
		 * List<LookupNonLifeInsuranceType> listClientNonlifeInsuranceList =
		 * nonlifeInsurancePolicyTypeRepository .findAll();
		 */

		List<LookupNonlifeInsuranceTypeDTO> lookupNonLifeInsuranceTypeList = new ArrayList<LookupNonlifeInsuranceTypeDTO>();
		/*
		 * for (LookupNonLifeInsuranceType lookupNonLifeInsuranceType :
		 * listClientNonlifeInsuranceList) { clientNonlifeInsuranceDTO =
		 * mapper.map(lookupNonLifeInsuranceType,
		 * LookupNonlifeInsuranceTypeDTO.class);
		 * lookupNonLifeInsuranceTypeList.add(clientNonlifeInsuranceDTO); }
		 */

		return lookupNonLifeInsuranceTypeList;
	}

	@Override
	public Set<LookupNonLifeInsurancePolicyTypeDTO> findAllNonInsurancePolicyTypeList(byte insTypeId)
			throws RuntimeException {

		try {
			List<LookupInsurancePolicyType> listLookupInsurancePolicyType = lookupInsurancePolicyTypeRepository
					.getInsurancePolicyTypeList(insTypeId);

			Set<LookupNonLifeInsurancePolicyTypeDTO> lookupGeneralInsurancePolicyTypeDTOList = new HashSet<LookupNonLifeInsurancePolicyTypeDTO>();

			for (LookupInsurancePolicyType lookupInsurancePolicyType : listLookupInsurancePolicyType) {
				LookupNonLifeInsurancePolicyTypeDTO lookupGeneralInsurancePolicyTypeDTO = new LookupNonLifeInsurancePolicyTypeDTO();
				lookupGeneralInsurancePolicyTypeDTO.setId(lookupInsurancePolicyType.getId());
				lookupGeneralInsurancePolicyTypeDTO.setDescription(lookupInsurancePolicyType.getDescription());
				lookupGeneralInsurancePolicyTypeDTOList.add(lookupGeneralInsurancePolicyTypeDTO);
			}

			return lookupGeneralInsurancePolicyTypeDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupHealthInsurancePolicyTypeDTO> findAllHealthInsurancePolicyTypeList() {

		/*
		 * List<LookupHealthInsurancePolicyType>
		 * lookupHealthInsurancePolicyTypeList =
		 * lookupHealthInsurancePolicyTypeRepository .findAll();
		 */

		List<LookupHealthInsurancePolicyTypeDTO> lokupHealthInsurancePolicyTypeDTOList = new ArrayList<LookupHealthInsurancePolicyTypeDTO>();
		/*
		 * for (LookupHealthInsurancePolicyType lookupHealthInsurancePolicyType
		 * : lookupHealthInsurancePolicyTypeList) {
		 * LookupHealthInsurancePolicyTypeDTO
		 * lookupGeneralInsurancePolicyTypeDTO = mapper
		 * .map(lookupHealthInsurancePolicyType,
		 * LookupHealthInsurancePolicyTypeDTO.class);
		 * lokupHealthInsurancePolicyTypeDTOList.add(
		 * lookupGeneralInsurancePolicyTypeDTO); }
		 */

		return lokupHealthInsurancePolicyTypeDTOList;
	}

	@Override
	public MasterInsurancePolicyDTO ClientNonLifeInsuranceCompanyPolicyName(byte insTypeId, int companyId,
			byte insurancePolicyTypeId) {
		MasterInsurancePolicyDTO masterInsurancePolicyDTO;
		MasterInsurancePolicy masterInsurancePolicy = masterInsurancePolicyRepository
				.getInsurancePolicyNameForCompanyIdAndPolicyTypeId(insTypeId, companyId, insurancePolicyTypeId);
		masterInsurancePolicyDTO = new MasterInsurancePolicyDTO();
		masterInsurancePolicyDTO.setId(masterInsurancePolicy.getId());
		masterInsurancePolicyDTO.setPolicyName(masterInsurancePolicy.getPolicyName());
		return masterInsurancePolicyDTO;
	}

	@Override
	public List<MasterInsuranceCompanyNameDTO> getClientInsuranceCompanyList() throws RuntimeException {
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
	public List<ClientNonlifeInsuranceDTO> getPolicyEndDate(int clientId, int timePeriod, int insuranceType) throws RuntimeException{
		List<ClientNonLifeInsurance> clientNonLifeInsuranceList = null;
		List<ClientNonlifeInsuranceDTO> clientNonLifeInsuranceDTOList =null;
		Calendar cal = null;
		ClientMaster clientMaster;
		Date utilFromDate = null;
		Date utilTODate = null;
		try {
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();
			
			clientNonLifeInsuranceDTOList = new ArrayList<ClientNonlifeInsuranceDTO>();
			clientNonLifeInsuranceList = new ArrayList<ClientNonLifeInsurance>();
			clientMaster = clientMasterRespository.findOne(clientId);
			
			if (timePeriod == 1) {
				//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateForNext1Week(clientId,
				//		insuranceType);
			}
			if (timePeriod == 2) {
				//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateNext1Month(clientId,
				//		insuranceType);
			}
			if (timePeriod == 3) {
				//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateForNext3Month(clientId,
				//		insuranceType);
			}
			utilTODate = cal.getTime();
			clientNonLifeInsuranceList =new FinexaUtil().addItemInNonLifeInsuranceListByInsuranceType(utilFromDate, utilTODate, clientMaster, insuranceType);
			
			ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO;
			for (ClientNonLifeInsurance clientNonLifeInsurance : clientNonLifeInsuranceList) {
				clientNonlifeInsuranceDTO = mapper.map(clientNonLifeInsurance, ClientNonlifeInsuranceDTO.class);
				clientNonlifeInsuranceDTO
						.setLookupPolicyTypeDesc(clientNonLifeInsurance.getLookupInsurancePolicyType().getDescription());
				clientNonLifeInsuranceDTOList.add(clientNonlifeInsuranceDTO);
			}
			
			 Collections.sort(clientNonLifeInsuranceDTOList, new Comparator<ClientNonlifeInsuranceDTO>() {
					public int compare(ClientNonlifeInsuranceDTO o1, ClientNonlifeInsuranceDTO o2) {
						return (o1.getPolicyEndDate()).compareTo(o2.getPolicyEndDate());
					}
			});
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

		return clientNonLifeInsuranceDTOList;

	}
	@Override
	public List<ClientNonlifeInsuranceDTO> getPolicyEndDateForadvisor(int advisorUserID, int timePeriod,int insuranceType) throws RuntimeException{
		List<ClientNonLifeInsurance> clientNonLifeInsuranceList = null;
		List<ClientNonlifeInsuranceDTO> clientNonLifeInsuranceDTOList = null;
		Calendar cal = null;
		Date utilFromDate = null;
		Date utilTODate = null;
		try {
			clientNonLifeInsuranceDTOList = new ArrayList<ClientNonlifeInsuranceDTO>();
			clientNonLifeInsuranceList = new ArrayList<ClientNonLifeInsurance>();
			
			cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			utilFromDate = cal.getTime();
			
			AdvisorUser advisorUser = advisorUserRepository.findOne(advisorUserID);
			//====  new get master List  =======
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterListTotal = finexaUtil.findAllClientByUserHierarchy(advisorUser,advisorUserSupervisorMappingRepository,clientMasterRespository);
			//===== end master list  ====
			
				if (timePeriod == 1) {
					cal.add(Calendar.DATE, 7);
					//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateForNext1Week(client.getId(),insuranceType);
				}
				if (timePeriod == 2) {
					cal.add(Calendar.DATE, 14);
					//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateNext1FortNight(client.getId(),insuranceType);
				}
				if (timePeriod == 3) {
					cal.add(Calendar.MONTH, 1);
					//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateNext1Month(client.getId(),insuranceType);
				}
				
				if (timePeriod == 4) {
					cal.add(Calendar.MONTH, 3);
					//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateForNext3Month(client.getId(),insuranceType);
				}
				if (timePeriod == 5) {
					cal.add(Calendar.MONTH, 6);
					//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateNext6Month(client.getId(),insuranceType);
				}
				if (timePeriod == 6) {
					cal.add(Calendar.YEAR, 1);
					//clientNonLifeInsuranceList = clientNonlifeInsuranceRepository.getPolicyEndDateNext1YEAR(client.getId(),insuranceType);
				}
				utilTODate = cal.getTime();
				for (ClientMaster client : clientMasterListTotal) {
				clientNonLifeInsuranceList =new FinexaUtil().addItemInNonLifeInsuranceListByInsuranceType(utilFromDate, utilTODate, client, insuranceType);
			
				ClientNonlifeInsuranceDTO clientNonlifeInsuranceDTO;
				for (ClientNonLifeInsurance clientNonLifeInsurance : clientNonLifeInsuranceList) {
					clientNonlifeInsuranceDTO = mapper.map(clientNonLifeInsurance, ClientNonlifeInsuranceDTO.class);
					LookupInsuranceType lookupInsuranceType = lookupInsuranceTypeRepository.findOne(clientNonLifeInsurance.getInsuranceTypeID());
					clientNonlifeInsuranceDTO.setInsuranceType(lookupInsuranceType.getDescription());
					clientNonlifeInsuranceDTO.setLookupPolicyTypeDesc(clientNonLifeInsurance.getLookupInsurancePolicyType().getDescription());
					clientNonlifeInsuranceDTO.setClientName(client.getFirstName()+(client.getMiddleName()!=null?" "+client.getMiddleName():""+" "+client.getLastName()));
					clientNonlifeInsuranceDTO.setOwnerName(client.getFirstName()+(clientNonLifeInsurance.getClientFamilyMember().getMiddleName()!=null?" "+clientNonLifeInsurance.getClientFamilyMember().getMiddleName():""+" "+clientNonLifeInsurance.getClientFamilyMember().getLastName()));
					clientNonlifeInsuranceDTO.setInsuranceCompanyName(clientNonLifeInsurance.getMasterInsuranceCompanyName().getDescription());
					clientNonLifeInsuranceDTOList.add(clientNonlifeInsuranceDTO);
				}

				
			}
			 Collections.sort(clientNonLifeInsuranceDTOList, new Comparator<ClientNonlifeInsuranceDTO>() {
					public int compare(ClientNonlifeInsuranceDTO o1, ClientNonlifeInsuranceDTO o2) {
						return (o1.getPolicyEndDate()).compareTo(o2.getPolicyEndDate());
					}
				});
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return clientNonLifeInsuranceDTOList;
	}

	


}