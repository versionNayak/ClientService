package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientNPSAssetClassReturnDTO;
import com.finlabs.finexa.dto.ClientNpsDTO;
import com.finlabs.finexa.dto.LookupNPSPlanTypeDTO;

import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientNPS;
import com.finlabs.finexa.model.LookupNPSPlanType;
import com.finlabs.finexa.model.MasterIncomeGrowthRate;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.model.MasterNPSAssetClassExpectedReturn;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientNpsRepository;
import com.finlabs.finexa.repository.LookupNPSPlanTypeRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.util.FinexaUtil;
import com.finlabs.finexa.repository.MasterIncomeGrowthRateRepository;
import com.finlabs.finexa.repository.MasterNPSAssetClassExpectedReturnRepository;

@Service("ClientNPSService")
@Transactional
public class ClientNPSServiceImpl implements ClientNPSService {
	private static Logger log = LoggerFactory.getLogger(ClientNPSServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepo;

	@Autowired
	private ClientNpsRepository clientNPSRepository;

	@Autowired
	private LookupNPSPlanTypeRepository npsPlanTypeRepo;

	@Autowired
	private MasterIncomeGrowthRateRepository masterIncomeGrowthRateRepository;

	@Autowired
	private MasterNPSAssetClassExpectedReturnRepository masterNPSAssetClassExpectedReturnRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientNpsDTO save(ClientNpsDTO clientNPSDTO, HttpServletRequest request) throws RuntimeException {
		try {
			log.info("ClientNPSServiceImpl >> save");
			ClientMaster cm = clientMasterRepository.findOne(clientNPSDTO.getClientID());
			ClientNPS clientNPS = mapper.map(clientNPSDTO, ClientNPS.class);
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientNPSDTO.getFinancialAssetType());
			ClientFamilyMember fam = clientFamilyMemberRepo.findOne(clientNPSDTO.getFamilyMemberID());
			clientNPS.setClientFamilyMember(fam);
			clientNPS.setClientMaster(cm);
			clientNPS.setMasterProductClassification(masterProductClassification);
			double expectedAnnualIncrease = clientNPSDTO.getExpectedAnnualIncrease().doubleValue() / 100;
			clientNPS.setExpectedAnnualIncrease(new BigDecimal(expectedAnnualIncrease));
			if (clientNPSDTO.getPlanType() != null) {
				if (clientNPSDTO.getPlanType() == 1) {
					// for Auto Mode
					if (clientNPS.getAutoPlanReturns() != null) {
						log.info("Auto Return: " + clientNPS.getAutoPlanReturns().doubleValue());
						clientNPS
								.setAutoPlanReturns(new BigDecimal(clientNPS.getAutoPlanReturns().doubleValue() / 100));
					}
				} else if (clientNPSDTO.getPlanType() == 2) {
					// For Active Mode
					if (clientNPS.getAssetClassEAllocation() != null) {
						log.info("AssetClassEAllocation: " + clientNPS.getAssetClassEAllocation().doubleValue());
						clientNPS.setAssetClassEAllocation(
								new BigDecimal(clientNPS.getAssetClassEAllocation().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassCAllocation() != null) {
						log.info("AssetClassCAllocation: " + clientNPS.getAssetClassCAllocation().doubleValue());
						clientNPS.setAssetClassCAllocation(
								new BigDecimal(clientNPS.getAssetClassCAllocation().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassGAllocation() != null) {
						log.info("AssetClassGAllocation: " + clientNPS.getAssetClassGAllocation().doubleValue());
						clientNPS.setAssetClassGAllocation(
								new BigDecimal(clientNPS.getAssetClassGAllocation().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassCReturns() != null) {
						log.info("AssetClassCReturn: " + clientNPS.getAssetClassCReturns().doubleValue());
						clientNPS.setAssetClassCReturns(
								new BigDecimal(clientNPS.getAssetClassCReturns().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassEReturns() != null) {
						log.info("AssetClassEReturn: " + clientNPS.getAssetClassEReturns().doubleValue());
						clientNPS.setAssetClassEReturns(
								new BigDecimal(clientNPS.getAssetClassEReturns().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassGReturns() != null) {
						log.info("AssetClassGReturn: " + clientNPS.getAssetClassGReturns().doubleValue());
						clientNPS.setAssetClassGReturns(
								new BigDecimal(clientNPS.getAssetClassGReturns().doubleValue() / 100));
					}
				}
			}
			clientNPS = clientNPSRepository.save(clientNPS);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			log.info("ClientNPSServiceImpl << save");
			return clientNPSDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientNpsDTO update(ClientNpsDTO clientNPSDTO, HttpServletRequest request) throws RuntimeException {

		try {
			log.info("ClientNPSServiceImpl >> save");
			ClientMaster cm = clientMasterRepository.findOne(clientNPSDTO.getClientID());
			ClientNPS clientNPS = mapper.map(clientNPSDTO, ClientNPS.class);
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientNPSDTO.getFinancialAssetType());
			ClientFamilyMember fam = clientFamilyMemberRepo.findOne(clientNPSDTO.getFamilyMemberID());
			clientNPS.setClientFamilyMember(fam);
			clientNPS.setClientMaster(cm);
			clientNPS.setMasterProductClassification(masterProductClassification);
			double expectedAnnualIncrease = clientNPSDTO.getExpectedAnnualIncrease().doubleValue() / 100;
			clientNPS.setExpectedAnnualIncrease(new BigDecimal(expectedAnnualIncrease));
			if (clientNPSDTO.getPlanType() != null) {
				if (clientNPSDTO.getPlanType() == 1) {
					// for Auto Mode
					if (clientNPS.getAutoPlanReturns() != null) {
						log.info("Auto Return: " + clientNPS.getAutoPlanReturns().doubleValue());
						clientNPS
								.setAutoPlanReturns(new BigDecimal(clientNPS.getAutoPlanReturns().doubleValue() / 100));
					}
				} else if (clientNPSDTO.getPlanType() == 2) {
					// For Active Mode
					if (clientNPS.getAssetClassEAllocation() != null) {
						log.info("AssetClassEAllocation: " + clientNPS.getAssetClassEAllocation().doubleValue());
						clientNPS.setAssetClassEAllocation(
								new BigDecimal(clientNPS.getAssetClassEAllocation().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassCAllocation() != null) {
						log.info("AssetClassCAllocation: " + clientNPS.getAssetClassCAllocation().doubleValue());
						clientNPS.setAssetClassCAllocation(
								new BigDecimal(clientNPS.getAssetClassCAllocation().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassGAllocation() != null) {
						log.info("AssetClassGAllocation: " + clientNPS.getAssetClassGAllocation().doubleValue());
						clientNPS.setAssetClassGAllocation(
								new BigDecimal(clientNPS.getAssetClassGAllocation().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassCReturns() != null) {
						log.info("AssetClassCReturn: " + clientNPS.getAssetClassCReturns().doubleValue());
						clientNPS.setAssetClassCReturns(
								new BigDecimal(clientNPS.getAssetClassCReturns().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassEReturns() != null) {
						log.info("AssetClassEReturn: " + clientNPS.getAssetClassEReturns().doubleValue());
						clientNPS.setAssetClassEReturns(
								new BigDecimal(clientNPS.getAssetClassEReturns().doubleValue() / 100));
					}
					if (clientNPS.getAssetClassGReturns() != null) {
						log.info("AssetClassGReturn: " + clientNPS.getAssetClassGReturns().doubleValue());
						clientNPS.setAssetClassGReturns(
								new BigDecimal(clientNPS.getAssetClassGReturns().doubleValue() / 100));
					}
				}
			}
			clientNPS = clientNPSRepository.save(clientNPS);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			log.info("ClientNPSServiceImpl << save");
			return clientNPSDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientNpsDTO> findAll() {
		// TODO Auto-generated method stub
		ClientNpsDTO ClientNpDTO;
		List<ClientNPS> listClientNPS = clientNPSRepository.findAll();

		List<ClientNpsDTO> listDTO = new ArrayList<ClientNpsDTO>();
		for (ClientNPS clientNPS : listClientNPS) {
			ClientNpDTO = mapper.map(clientNPS, ClientNpsDTO.class);
			ClientNpDTO.setClientID(clientNPS.getClientMaster().getId());
			listDTO.add(ClientNpDTO);
		}

		return listDTO;
	}

	@Override
	public List<ClientNpsDTO> findByClientId(int clientId) throws RuntimeException {

		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			ClientNpsDTO clientNPSDTO;
			List<ClientNPS> clientNPSList = clientMaster.getClientNps();
			List<ClientNpsDTO> clientNPSDTOList = new ArrayList<ClientNpsDTO>();

			for (ClientNPS obj : clientNPSList) {

				clientNPSDTO = mapper.map(obj, ClientNpsDTO.class);
				clientNPSDTO.setClientID(obj.getClientMaster().getId());
				clientNPSDTOList.add(clientNPSDTO);
			}

			return clientNPSDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientNPSRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			throw new RuntimeException();
		}

	}

	@Override
	public ClientNpsDTO findById(int id) throws RuntimeException {

		try {
			ClientNPS clientNPS = clientNPSRepository.findOne(id);
			ClientNpsDTO clientNPSDTO = mapper.map(clientNPS, ClientNpsDTO.class);
			clientNPSDTO.setClientID(clientNPSRepository.findOne(id).getClientMaster().getId());
			clientNPSDTO.setFamilyMemberID(clientNPS.getClientFamilyMember().getId());
			clientNPSDTO.setFirstName(clientNPS.getClientFamilyMember().getFirstName());
			clientNPSDTO.setRelationId(clientNPS.getClientFamilyMember().getLookupRelation().getId());
			clientNPSDTO.setRelationName(clientNPS.getClientFamilyMember().getLookupRelation().getDescription());
			clientNPSDTO.setGender(FinexaUtil.getGender(clientNPS.getClientFamilyMember().getClientMaster(),
					clientNPS.getClientFamilyMember().getLookupRelation()));
			clientNPSDTO.setFinancialAssetType(clientNPS.getMasterProductClassification().getId());
			return clientNPSDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException();
		}
	}

	@Override
	public List<LookupNPSPlanTypeDTO> findAllNPSType() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<LookupNPSPlanTypeDTO> lookupPlanTypeDTOList = new ArrayList<>();
			List<LookupNPSPlanType> lookupPlanTypeList = npsPlanTypeRepo.findAll();
			for (LookupNPSPlanType obj : lookupPlanTypeList) {
				LookupNPSPlanTypeDTO lookupNPSPlanTypeDTO = new LookupNPSPlanTypeDTO();
				lookupNPSPlanTypeDTO = mapper.map(obj, LookupNPSPlanTypeDTO.class);
				lookupPlanTypeDTOList.add(lookupNPSPlanTypeDTO);
			}
			return lookupPlanTypeDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getCAGRByIncomeType(int incomeType) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("ClientNPSServiceImpl >> getCAGRByIncomeType()");
			Date dt = new Date();
			double cagr = 0.0;
			List<MasterIncomeGrowthRate> masterIncomeGrowthRateList = masterIncomeGrowthRateRepository.findAll();
			for (MasterIncomeGrowthRate obj : masterIncomeGrowthRateList) {
				log.info("Todays Date: " + dt);
				log.info("From Date: " + obj.getFromDate() + " To Date: " + obj.getToDate());
				if (obj.getFromDate().before(dt) && dt.before(obj.getToDate())) {
					if (obj.getId() == incomeType) // Salary Income
						cagr = obj.getCagr().doubleValue();
				}
			}

			return cagr;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public ClientNPSAssetClassReturnDTO getAssetClassReturn() throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("ClientNPSServiceImpl >> getAssetClassReturn()");
			Date dt = new Date();
			ClientNPSAssetClassReturnDTO assetClassReturn = new ClientNPSAssetClassReturnDTO();
			List<MasterNPSAssetClassExpectedReturn> masterAssetClassExpectedReturn = masterNPSAssetClassExpectedReturnRepository
					.findAll();
			for (MasterNPSAssetClassExpectedReturn obj : masterAssetClassExpectedReturn) {
				log.info("Todays Date: " + dt);
				log.info("From Date: " + obj.getFromDate() + " To Date: " + obj.getToDate());
				if (obj.getFromDate().before(dt) && dt.before(obj.getToDate())) {
					log.debug("Asset Class Type: " + obj.getAssetClassType());
					if (obj.getAssetClassType().equals("Asset Class C")) {
						log.debug("Asset Class C Return: " + obj.getAssetClassEExpectedReturns());
						assetClassReturn.setAssetClassCReturn(obj.getAssetClassEExpectedReturns());
					}
					if (obj.getAssetClassType().equals("Asset Class E")) {
						log.debug("Asset Class E Return: " + obj.getAssetClassEExpectedReturns());
						assetClassReturn.setAssetClassEReturn(obj.getAssetClassEExpectedReturns());
					}
					if (obj.getAssetClassType().equals("Asset Class G")) {
						log.debug("Asset Class G Return: " + obj.getAssetClassEExpectedReturns());
						assetClassReturn.setAssetClassGReturn(obj.getAssetClassEExpectedReturns());
					}
					if (obj.getAssetClassType().equals("Auto Plan")) {
						log.debug("Auto Plan Return: " + obj.getAssetClassEExpectedReturns());
						assetClassReturn.setAutoPlanReturn(obj.getAssetClassEExpectedReturns());
					}
				}
			}
			log.debug("ClientNPSServiceImpl << getAssetClassReturn()");
			return assetClassReturn;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkIfNpsPresent(Integer clientId) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

		List<ClientNPS> ClientEpfList = clientMaster.getClientNps();
		if (ClientEpfList.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<ClientFamilyMemberDTO> checkIfNpsPresentForAll(Integer clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMember> ClientFamilyMembers = clientMaster.getClientFamilyMembers();

			List<ClientNPS> ClientNps = clientMaster.getClientNps();

			List<ClientFamilyMember> ClientFamilyMembers1 = new ArrayList<ClientFamilyMember>();

			for (ClientNPS cf : ClientNps) {

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
