package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientAnnuityDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.LookupAnnuityTypeDTO;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;

import com.finlabs.finexa.model.LookupAnnuityType;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientAnnuityRepository;

import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.LookupAnnuityTypeRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientAnnuityService")
@Transactional
public class ClientAnnuityServiceImpl implements ClientAnnuityService {
	
	private static Logger log = LoggerFactory.getLogger(ClientAnnuityServiceImpl.class);
	
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientAnnuityRepository clientAnnuityRepository;
	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;
	@Autowired
	private LookupAnnuityTypeRepository lookupAnnuityTypeRepository;
	@Autowired
	private FrequencyRepository frequencyRepository;
	@Autowired
	private LookupAnnuityTypeRepository annuityTypeRepo;
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientAnnuityDTO save(ClientAnnuityDTO clientAnnuityDTO,HttpServletRequest request) throws RuntimeException {

		try {
			log.debug("inside save");
			ClientMaster cm = clientMasterRepository.findOne(clientAnnuityDTO.getClientID());

			ClientAnnuity clientAnnuity = mapper.map(clientAnnuityDTO, ClientAnnuity.class);
			clientAnnuity.setClientMaster(cm);
			LookupAnnuityType lookupAnnuityType = lookupAnnuityTypeRepository
					.findOne(clientAnnuityDTO.getAnnuityType());
			clientAnnuity.setLookupAnnuityType(lookupAnnuityType);

			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientAnnuityDTO.getFinancialAssetType());
			clientAnnuity.setMasterProductClassification(masterProductClassification);
			
			if (clientAnnuityDTO.getAnnuityType() != 6) {
				LookupFrequency lookupFrequency = frequencyRepository.findOne(clientAnnuityDTO.getPayoutFrequency());
				clientAnnuity.setLookupFrequency(lookupFrequency);
			}
			
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientAnnuityDTO.getFamilyMemberID());
			clientAnnuity.setClientFamilyMember(fam);
			if (clientAnnuityDTO.getAnnuityRate() != null) {
				double annuityRate = clientAnnuityDTO.getAnnuityRate().doubleValue() / 100;
				clientAnnuity.setAnnuityRate(new BigDecimal(annuityRate));
			}
			if (clientAnnuityDTO.getGrowthRate() != null) {
				double annuityGrowthRate = clientAnnuityDTO.getGrowthRate().doubleValue() / 100;
				clientAnnuity.setGrowthRate((new BigDecimal(annuityGrowthRate)));
			}
			if (clientAnnuityDTO.getAnnuityType() == 6) {
				clientAnnuity.setMonthlyBasicDA(clientAnnuityDTO.getAnnuityMonthlyBasicDA());
				clientAnnuity.setServiceYears(clientAnnuityDTO.getAnnuityServiceYears());
				//double annualContribIncr = clientAnnuityDTO.getAnnuityAnnualContributionIncrease().doubleValue() / 100;
				//clientAnnuity.setAnnualContributionIncrease((new BigDecimal(annualContribIncr)));
				clientAnnuity.setLookupFrequency(frequencyRepository.findOne((byte) 12));
			}
			clientAnnuity = clientAnnuityRepository.save(clientAnnuity);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientAnnuityDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientAnnuityDTO update(ClientAnnuityDTO clientAnnuityDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientAnnuityDTO.getClientID());

			ClientAnnuity clientAnnuity = mapper.map(clientAnnuityDTO, ClientAnnuity.class);
			clientAnnuity.setClientMaster(cm);
			LookupAnnuityType lookupAnnuityType = lookupAnnuityTypeRepository
					.findOne(clientAnnuityDTO.getAnnuityType());
			clientAnnuity.setLookupAnnuityType(lookupAnnuityType);

			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientAnnuityDTO.getFinancialAssetType());
			clientAnnuity.setMasterProductClassification(masterProductClassification);

			if (clientAnnuityDTO.getAnnuityType() != 6) {
				LookupFrequency lookupFrequency = frequencyRepository.findOne(clientAnnuityDTO.getPayoutFrequency());
				clientAnnuity.setLookupFrequency(lookupFrequency);
			}
			
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientAnnuityDTO.getFamilyMemberID());
			clientAnnuity.setClientFamilyMember(fam);
			if (clientAnnuityDTO.getAnnuityRate() != null) {
				double annuityRate = clientAnnuityDTO.getAnnuityRate().doubleValue() / 100;
				clientAnnuity.setAnnuityRate(new BigDecimal(annuityRate));
			}
			if (clientAnnuityDTO.getGrowthRate() != null) {
				double annuityGrowthRate = clientAnnuityDTO.getGrowthRate().doubleValue() / 100;
				clientAnnuity.setGrowthRate((new BigDecimal(annuityGrowthRate)));
			}
			if (clientAnnuityDTO.getAnnuityType() == 6) {
				clientAnnuity.setMonthlyBasicDA(clientAnnuityDTO.getAnnuityMonthlyBasicDA());
				clientAnnuity.setServiceYears(clientAnnuityDTO.getAnnuityServiceYears());
				//double annualContribIncr = clientAnnuityDTO.getAnnuityAnnualContributionIncrease().doubleValue() / 100;
				//clientAnnuity.setAnnualContributionIncrease((new BigDecimal(annualContribIncr)));
			}
			clientAnnuity = clientAnnuityRepository.save(clientAnnuity);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientAnnuityDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientAnnuityDTO> findAll() {
		// TODO Auto-generated method stub
		ClientAnnuityDTO ClientAnnuityDTO;
		List<ClientAnnuity> listClientAnnuity = clientAnnuityRepository.findAll();

		List<ClientAnnuityDTO> listDTO = new ArrayList<ClientAnnuityDTO>();
		for (ClientAnnuity clientAnnuity : listClientAnnuity) {
			ClientAnnuityDTO = mapper.map(clientAnnuity, ClientAnnuityDTO.class);
			ClientAnnuityDTO.setClientID(clientAnnuity.getClientMaster().getId());
			listDTO.add(ClientAnnuityDTO);
		}

		return listDTO;
	}

	@Override
	public List<ClientAnnuityDTO> findByClientId(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			ClientAnnuityDTO clientAnnuityDTO;
			List<ClientAnnuity> clientAnnuityList = clientMaster.getClientAnnuities();
			List<ClientAnnuityDTO> clientAnnuityDTOList = new ArrayList<ClientAnnuityDTO>();

			for (ClientAnnuity obj : clientAnnuityList) {

				clientAnnuityDTO = mapper.map(obj, ClientAnnuityDTO.class);
				clientAnnuityDTO.setClientID(obj.getClientMaster().getId());
				clientAnnuityDTO.setAnnuityType(obj.getLookupAnnuityType().getId());
				clientAnnuityDTOList.add(clientAnnuityDTO);
			}

			return clientAnnuityDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientAnnuityRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

	}

	/*
	 * @Override public int deleteSpecial(int epfId) { // TODO Auto-generated
	 * method stub if (epfId != 0){ clientAnnuityRepository.delete(epfId);
	 * return 1; }else { return 0; } }
	 */

	@Override
	public ClientAnnuityDTO findById(int id) throws RuntimeException {

		try {
			ClientAnnuity clientAnnuity = clientAnnuityRepository.findOne(id);
			ClientAnnuityDTO clientAnnuityDTO = mapper.map(clientAnnuity, ClientAnnuityDTO.class);
			clientAnnuityDTO.setClientID(clientAnnuityRepository.findOne(id).getClientMaster().getId());
			clientAnnuityDTO.setFamilyMemberID(clientAnnuity.getClientFamilyMember().getId());
			clientAnnuityDTO.setFinancialAssetType(clientAnnuity.getMasterProductClassification().getId());
			clientAnnuityDTO.setAnnuityType(clientAnnuity.getLookupAnnuityType().getId());
			if (clientAnnuity.getLookupFrequency() != null) {
				clientAnnuityDTO.setPayoutFrequency(clientAnnuity.getLookupFrequency().getId());
			}
			if (clientAnnuity.getClientEpf() != null) {
				clientAnnuityDTO.setClientEPFID(clientAnnuity.getClientEpf().getId());
			}
			clientAnnuityDTO.setFirstName(clientAnnuity.getClientFamilyMember().getFirstName());
			clientAnnuityDTO.setRelationId(clientAnnuity.getClientFamilyMember().getLookupRelation().getId());
			clientAnnuityDTO.setRelationName(clientAnnuity.getClientFamilyMember().getLookupRelation().getDescription());
			clientAnnuityDTO.setGender(FinexaUtil.getGender(clientAnnuity.getClientFamilyMember().getClientMaster(),
					clientAnnuity.getClientFamilyMember().getLookupRelation()));
			clientAnnuityDTO.setAnnuityMonthlyBasicDA(clientAnnuity.getMonthlyBasicDA());
			clientAnnuityDTO.setAnnuityServiceYears(clientAnnuity.getServiceYears());
			clientAnnuityDTO.setAnnuityAnnualContributionIncrease(clientAnnuity.getAnnualContributionIncrease());
			return clientAnnuityDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LookupAnnuityTypeDTO> getAllAnnuityTypes() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupAnnuityType> annuityList = annuityTypeRepo.findAll();
			List<LookupAnnuityTypeDTO> annuityTypeDTOList = new ArrayList<>();
			for (LookupAnnuityType obj : annuityList) {
				LookupAnnuityTypeDTO lookupAnnuityTypeDTO = mapper.map(obj, LookupAnnuityTypeDTO.class);
				annuityTypeDTOList.add(lookupAnnuityTypeDTO);
			}
			return annuityTypeDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void saveEPSAnnuity(ClientEPF clientEPF) {
		// TODO Auto-generated method stub

		ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientEPF.getClientFamilyMember().getId());
		ClientMaster cm = cfm.getClientMaster();

		ClientAnnuity annuityObj = new ClientAnnuity();
		annuityObj.setClientMaster(cm);
		annuityObj.setClientFamilyMember(cfm);

		MasterProductClassification mpc = masterProductClassificationRepository
				.findOne(FinexaConstant.MASTER_PRODUCT_CLASSIFICATION_ANNUITY_ID);
		annuityObj.setMasterProductClassification(mpc);

		LookupFrequency lf = frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID);
		annuityObj.setLookupFrequency(lf);

		LookupAnnuityType lat = annuityTypeRepo.findOne(FinexaConstant.LOOKUP_ANNUITY_TYPE_EPS_ID);
		annuityObj.setLookupAnnuityType(lat);

		int retAge = cfm.getRetirementAge();
		Date dob = cfm.getBirthDate();
		Calendar startDate = getDateOfRetirement(retAge, dob);

		annuityObj.setAnnuityStartDate(startDate.getTime());
		annuityObj.setGrowthRate(new BigDecimal(0.0));
		annuityObj.setClientEpf(clientEPF);
		annuityObj.setMonthlyBasicDA(clientEPF.getMonthlyBasicDA());
		annuityObj.setServiceYears(clientEPF.getServiceYears());
		annuityObj.setAnnualContributionIncrease(clientEPF.getAnnualContributionIncrease());
		clientAnnuityRepository.save(annuityObj);

	}

	@Override
	public void updateEPSAnnuity(ClientEPF clientEPF) {
		// TODO Auto-generated method stub

		ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientEPF.getClientFamilyMember().getId());
		ClientMaster cm = cfm.getClientMaster();
		ClientAnnuity annuityObj = new ClientAnnuity();
		annuityObj.setClientMaster(cm);
		annuityObj.setClientFamilyMember(cfm);

		MasterProductClassification mpc = masterProductClassificationRepository
				.findOne(FinexaConstant.MASTER_PRODUCT_CLASSIFICATION_ANNUITY_ID);
		annuityObj.setMasterProductClassification(mpc);

		LookupFrequency lf = frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID);
		annuityObj.setLookupFrequency(lf);

		LookupAnnuityType lat = annuityTypeRepo.findOne(FinexaConstant.LOOKUP_ANNUITY_TYPE_EPS_ID);
		annuityObj.setLookupAnnuityType(lat);

		int retAge = cfm.getRetirementAge();
		Date dob = cfm.getBirthDate();

		Calendar startDate = getDateOfRetirement(retAge, dob);
		annuityObj.setAnnuityStartDate(startDate.getTime());
		annuityObj.setGrowthRate(new BigDecimal(0.0));

		clientAnnuityRepository.updateClientFamilyMember(clientEPF.getId(), clientEPF.getClientFamilyMember().getId());
		clientAnnuityRepository.updateMonthlyBasicDA(clientEPF.getId(), clientEPF.getMonthlyBasicDA());
		clientAnnuityRepository.updateServiceYears(clientEPF.getId(), clientEPF.getServiceYears());
		clientAnnuityRepository.updateAnnualContributionIncrease(clientEPF.getId(),
				clientEPF.getAnnualContributionIncrease());

	}

	public Calendar getDateOfRetirement(int years, Date dob) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(dob);
		cal.add(Calendar.MONTH, years * 12);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal;

	}

	@Override
	public List<ClientFamilyMemberDTO> checkIfAnnuityPresentForAll(Integer clientId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMember> ClientFamilyMembers = clientMaster.getClientFamilyMembers();

			List<ClientAnnuity> ClientAnnuities = clientMaster.getClientAnnuities();

			List<ClientFamilyMember> ClientFamilyMembers1 = new ArrayList<ClientFamilyMember>();

			for (ClientAnnuity cf : ClientAnnuities) {

				ClientFamilyMembers1.add(cf.getClientFamilyMember());
			}

			ClientFamilyMembers.removeAll(ClientFamilyMembers1);

			for (ClientFamilyMember cf : ClientFamilyMembers) {

				log.debug("not income member " + cf.getId());
			}

			List<ClientFamilyMemberDTO> cfmDtoList = new ArrayList<ClientFamilyMemberDTO>();

			for (ClientFamilyMember clientFamilyMember : ClientFamilyMembers) {
				ClientFamilyMemberDTO clientFamilyMemberDTO = mapper.map(clientFamilyMember,
						ClientFamilyMemberDTO.class);

				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					clientFamilyMemberDTO.setGender(clientMaster.getGender());
				}
				if (clientFamilyMember.getLookupRelation().getId() == 1) {
					if (clientMaster.getGender().equals("M")) {
						clientFamilyMemberDTO.setGender("F");
					} else {
						clientFamilyMemberDTO.setGender("M");
					}
				}
				clientFamilyMemberDTO.setRelationName(clientFamilyMember.getLookupRelation().getDescription());
				clientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());

				log.debug("f name " + clientFamilyMemberDTO.getFirstName());
				log.debug("relation " + clientFamilyMemberDTO.getRelationName());
				cfmDtoList.add(clientFamilyMemberDTO);
			}
			Collections.sort(cfmDtoList, new Comparator<ClientFamilyMemberDTO>() {

				@Override
				public int compare(ClientFamilyMemberDTO relation1, ClientFamilyMemberDTO relation2) {
					// TODO Auto-generated method stub
					return relation1.getRelationID().compareTo(relation2.getRelationID());
				}
			});

			return cfmDtoList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

}
