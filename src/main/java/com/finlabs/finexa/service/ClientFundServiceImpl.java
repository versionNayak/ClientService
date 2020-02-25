package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientMutualFundDTO;
import com.finlabs.finexa.model.AccordSchemeIsinMaster;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.LookupFundCategory;
import com.finlabs.finexa.model.LookupFundInvestmentMode;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.AccordSchemeISINMasterRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientMutualFundRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.LookupFundCategoryRepository;
import com.finlabs.finexa.repository.LookupFundInvestmentModeRepository;
import com.finlabs.finexa.repository.MasterMFDailyNAVRepository;
import com.finlabs.finexa.repository.MasterMutualFundETFRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.resources.model.MutualFundLumpsumSipLookup;
import com.finlabs.finexa.resources.service.MutualFundLumpsumSipService;

@Service("ClientFundService")
@Transactional
public class ClientFundServiceImpl implements ClientFundService {

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	private static Logger log = LoggerFactory.getLogger(ClientFundServiceImpl.class);
	@Autowired
	private Mapper mapper;
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	@Autowired
	private ClientMutualFundRepository clientMFRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Autowired
	private MasterMutualFundETFRepository mastermutualFundETFRepository;

	@Autowired
	private LookupFundCategoryRepository lookupFundCategoryRepository;

	@Autowired
	private LookupFundInvestmentModeRepository lookupFundInvestmentModeRepository;

	@Autowired
	private MasterMFDailyNAVRepository masterMFDailyNAVRepository;	

	@Autowired
	private LookupAssetSubClassRepository lookupAssetSubClassRepository;

	@Autowired
	private MasterMutualFundETFRepository masterMutualFundETFRepository;
	
	@Autowired
	private AccordSchemeISINMasterRepository accordSchemeISINMasterRepository;
	
	@Autowired
	AdvisorService advisorService;
	
	@Override
	public ClientMutualFundDTO save(ClientMutualFundDTO clientMutualFundDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientMutualFundDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientMutualFundDTO.getFamilyMemberId());	
			ClientMutualFund clientMutualFund = mapper.map(clientMutualFundDTO, ClientMutualFund.class);
			clientMutualFund.setClientMaster(cm);
			clientMutualFund.setClientFamilyMember(cfm);
			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientMutualFundDTO.getFundTypeID());
			
			clientMutualFund.setMasterProductClassification(master);

			LookupFundCategory lookupFundCategory = lookupFundCategoryRepository
					.findOne(clientMutualFundDTO.getFundCategoryID());
			
			clientMutualFund.setLookupFundCategory(lookupFundCategory);

			if (clientMutualFundDTO.getFundTypeID() == 19) {
				
				LookupFundInvestmentMode lookupFundInvestmentMode = lookupFundInvestmentModeRepository
						.findOne(clientMutualFundDTO.getInvestmentModeID());
				
				clientMutualFund.setLookupFundInvestmentMode(lookupFundInvestmentMode);
			} else {
				if (clientMutualFundDTO.getFundTypeID() == 20) {
					
					LookupFundInvestmentMode lookupFundInvestmentModeETF = lookupFundInvestmentModeRepository
							.findOne(clientMutualFundDTO.getInvestmentModeIDETF());
					
					clientMutualFund.setLookupFundInvestmentMode(lookupFundInvestmentModeETF);
				}
			}

			if (clientMutualFundDTO.getFundTypeID() == 19 || clientMutualFundDTO.getFundTypeID() == 20) {
				/*log.debug("mastermutualFundETFRepository >> findAmfiCodeByFundHouseAndSchemeName()");
				MasterMutualFundETF mmfETF = mastermutualFundETFRepository.findByFundHouseAndSchemeName(
						clientMutualFundDTO.getFundHouse(), clientMutualFundDTO.getSchemeName());
				log.debug("mastermutualFundETFRepository << findAmfiCodeByFundHouseAndSchemeName()");*/
				clientMutualFund.setIsin(clientMutualFundDTO.getIsin());

			}

			if (clientMutualFundDTO.getFundTypeID() == 21) {
				/*log.debug("masterPMSRepository >> findByProviderNameAndSchemeName()");
				MasterPM mPMS = masterPMSRepository.findByProviderNameAndSchemeName(clientMutualFundDTO.getProviderName(),
						clientMutualFundDTO.getSchemeNamePMS());
				log.debug("masterPMSRepository << findByProviderNameAndSchemeName()");
				clientMutualFund.setPmsID(mPMS.getId());*/
				clientMutualFund.setIsin(null);
				clientMutualFund.setPmsProvider(clientMutualFundDTO.getProviderName());
				clientMutualFund.setPmsSchemeName(clientMutualFundDTO.getSchemeNamePMS());
				LookupAssetSubClass assetSubClass = lookupAssetSubClassRepository.findOne(clientMutualFundDTO.getSubAssetID());
				clientMutualFund.setLookupAssetSubClass(assetSubClass);


			}

			if (clientMutualFundDTO.getFundTypeID() == 20 || clientMutualFundDTO.getFundTypeID() == 21) {

				clientMutualFundDTO.setCloseEndedFlag("N");
			}

			
			clientMutualFund = clientMFRepository.save(clientMutualFund);
	
			clientMutualFundDTO = mapper.map(clientMutualFund, ClientMutualFundDTO.class);
			clientMutualFundDTO.setClientID(clientMutualFund.getClientMaster().getId());
			clientMutualFundDTO.setFundTypeID(clientMutualFund.getMasterProductClassification().getId());
			if (clientMutualFundDTO.getFundTypeID() == 19 || clientMutualFundDTO.getFundTypeID() == 20) {
				clientMutualFundDTO.setIsin(clientMutualFund.getIsin());
			}
			if (clientMutualFundDTO.getFundTypeID() == 21) {
				//clientMutualFundDTO.setPmsID(clientMutualFund.getPmsID());
				clientMutualFundDTO.setIsin(null);
				clientMutualFundDTO.setProviderName(clientMutualFund.getPmsProvider());
				clientMutualFundDTO.setSchemeNamePMS(clientMutualFund.getPmsSchemeName());
				clientMutualFundDTO.setSubAssetID(clientMutualFund.getLookupAssetSubClass().getId());
			}
			clientMutualFundDTO.setFundCategoryID(clientMutualFund.getLookupFundCategory().getId());
			if (clientMutualFundDTO.getFundTypeID() == 19) {
				clientMutualFundDTO.setInvestmentModeID(clientMutualFund.getLookupFundInvestmentMode().getId());
			} else {
				if (clientMutualFundDTO.getFundTypeID() == 20) {
					clientMutualFundDTO.setInvestmentModeIDETF(clientMutualFund.getLookupFundInvestmentMode().getId());
				}
			}
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
		
			return clientMutualFundDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientMutualFundDTO> findAll() {
		ClientMutualFundDTO clientMutualFundDTO;

		List<ClientMutualFund> clientMFList = clientMFRepository.findAll();

		List<ClientMutualFundDTO> clientMFDTOList = new ArrayList<ClientMutualFundDTO>();
		for (ClientMutualFund obj : clientMFList) {
			clientMutualFundDTO = mapper.map(obj, ClientMutualFundDTO.class);
			clientMutualFundDTO.setClientID(obj.getClientMaster().getId());
			clientMutualFundDTO
			.setOwnerName(obj.getClientMaster().getFirstName() + " " + obj.getClientMaster().getLastName());
			clientMutualFundDTO.setFundTypeID(obj.getMasterProductClassification().getId());
			if (clientMutualFundDTO.getFundTypeID() == 19) {
				clientMutualFundDTO.setIsin(obj.getIsin());
				clientMutualFundDTO.setFundCategoryID(obj.getLookupFundCategory().getId());
				clientMutualFundDTO.setCloseEndedFlag(obj.getCloseEndedFlag());
				clientMutualFundDTO.setInvestmentModeID(obj.getLookupFundInvestmentMode().getId());
				clientMutualFundDTO.setInvestmentStartDate(obj.getInvestmentStartDate());
				clientMutualFundDTO.setInvestmentAmount(obj.getInvestmentAmount());
				if (clientMutualFundDTO.getInvestmentModeID() == 1) {
					clientMutualFundDTO.setLumpsumUnitsPurchased(obj.getLumpsumUnitsPurchased());
					clientMutualFundDTO.setMfLumpsumLockedInDate(obj.getMfLumpsumLockedInDate());
				} else {
					if (clientMutualFundDTO.getInvestmentModeID() == 2) {
						clientMutualFundDTO.setSipFrequency(obj.getSipFrequency());
						clientMutualFundDTO.setSipInstalments(obj.getSipInstalments());
					}
				}

			} else {
				if (clientMutualFundDTO.getFundTypeID() == 20) {

					clientMutualFundDTO.setIsin(obj.getIsin());
					clientMutualFundDTO.setFundCategoryID(obj.getLookupFundCategory().getId());
					clientMutualFundDTO.setCloseEndedFlag(obj.getCloseEndedFlag());
					clientMutualFundDTO.setInvestmentModeID(obj.getLookupFundInvestmentMode().getId());
					clientMutualFundDTO.setInvestmentStartDate(obj.getInvestmentStartDate());
					clientMutualFundDTO.setInvestmentAmount(obj.getInvestmentAmount());
					if (clientMutualFundDTO.getInvestmentModeID() == 1) {
						clientMutualFundDTO.setLumpsumUnitsPurchased(obj.getLumpsumUnitsPurchased());
					} else {
						if (clientMutualFundDTO.getInvestmentModeID() == 2) {
							clientMutualFundDTO.setSipFrequency(obj.getSipFrequency());
							clientMutualFundDTO.setSipInstalments(obj.getSipInstalments());
						}
					}
				} else {
					if (clientMutualFundDTO.getFundTypeID() == 21) {
						log.debug("PMS");
						//clientMutualFundDTO.setPmsID(obj.getPmsID());
						clientMutualFundDTO.setProviderName(obj.getPmsProvider());
						clientMutualFundDTO.setSchemeNamePMS(obj.getPmsSchemeName());
						clientMutualFundDTO.setFundCategoryID(obj.getLookupFundCategory().getId());
						clientMutualFundDTO.setInvestmentStartDate(obj.getInvestmentStartDate());
						clientMutualFundDTO.setInvestmentAmount(obj.getInvestmentAmount());
						clientMutualFundDTO.setCurrentMarketValue(obj.getCurrentMarketValue());
					} else {
						// clientMutualFundDTO.getFundTypeID() ==
					}
				}
			}

			clientMFDTOList.add(clientMutualFundDTO);

		}

		return clientMFDTOList;
	}

	@Override
	public ClientMutualFundDTO update(ClientMutualFundDTO clientMutualFundDTO, HttpServletRequest request) throws RuntimeException {
		// TODO Auto-generated method stub

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientMutualFundDTO.getClientID());
			
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientMutualFundDTO.getFamilyMemberId());
			ClientMutualFund clientMutualFund = mapper.map(clientMutualFundDTO, ClientMutualFund.class);
			clientMutualFund.setClientMaster(cm);
			log.debug("setting family member during update");
			clientMutualFund.setClientFamilyMember(cfm);

			log.debug("MasterProductClassificationRepository >> findOne()");
			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientMutualFundDTO.getFundTypeID());
			log.debug("MasterProductClassificationRepository << findOne()");
			clientMutualFund.setMasterProductClassification(master);
			log.debug("MF Type Model: " + clientMutualFund.getMasterProductClassification());

			log.debug("LookupFundCategoryRepository >> findOne()");
			LookupFundCategory lookupFundCategory = lookupFundCategoryRepository
					.findOne(clientMutualFundDTO.getFundCategoryID());
			log.debug("LookupFundCategoryRepository << findOne()");
			clientMutualFund.setLookupFundCategory(lookupFundCategory);

			if (clientMutualFundDTO.getFundTypeID() == 19) {
				log.debug("lookupFundInvestmentModeRepository >> findOne()");
				LookupFundInvestmentMode lookupFundInvestmentMode = lookupFundInvestmentModeRepository
						.findOne(clientMutualFundDTO.getInvestmentModeID());
				log.debug("lookupFundInvestmentModeRepository << findOne()");
				clientMutualFund.setLookupFundInvestmentMode(lookupFundInvestmentMode);
			} else {
				if (clientMutualFundDTO.getFundTypeID() == 20) {
					log.debug("lookupFundInvestmentModeRepository >> findOne()");
					LookupFundInvestmentMode lookupFundInvestmentModeETF = lookupFundInvestmentModeRepository
							.findOne(clientMutualFundDTO.getInvestmentModeIDETF());
					log.debug("lookupFundInvestmentModeRepository << findOne()");
					clientMutualFund.setLookupFundInvestmentMode(lookupFundInvestmentModeETF);
				}
			}

			if (clientMutualFundDTO.getFundTypeID() == 19 || clientMutualFundDTO.getFundTypeID() == 20) {
				/*log.debug("mastermutualFundETFRepository >> findAmfiCodeByFundHouseAndSchemeName()");
				MasterMutualFundETF mmfETF = mastermutualFundETFRepository.findByFundHouseAndSchemeName(
						clientMutualFundDTO.getFundHouse(), clientMutualFundDTO.getSchemeName());
				log.debug("mastermutualFundETFRepository << findAmfiCodeByFundHouseAndSchemeName()");*/
				clientMutualFund.setIsin(clientMutualFundDTO.getIsin());

			}

			if (clientMutualFundDTO.getFundTypeID() == 21) {
				/*log.debug("masterPMSRepository >> findByProviderNameAndSchemeName()");
				MasterPM mPMS = masterPMSRepository.findByProviderNameAndSchemeName(clientMutualFundDTO.getProviderName(),
						clientMutualFundDTO.getSchemeNamePMS());
				log.debug("masterPMSRepository << findByProviderNameAndSchemeName()");
				clientMutualFund.setPmsID(mPMS.getId());*/
				clientMutualFund.setIsin(null);
				clientMutualFund.setPmsProvider(clientMutualFundDTO.getProviderName());
				clientMutualFund.setPmsSchemeName(clientMutualFundDTO.getSchemeNamePMS());
				LookupAssetSubClass assetSubClass = lookupAssetSubClassRepository.findOne(clientMutualFundDTO.getSubAssetID());
				clientMutualFund.setLookupAssetSubClass(assetSubClass);

			}

			if (clientMutualFundDTO.getFundTypeID() == 20 || clientMutualFundDTO.getFundTypeID() == 21) {

				clientMutualFundDTO.setCloseEndedFlag("N");
			}

			log.debug("clientMFRepository >> save()");
			clientMutualFund = clientMFRepository.save(clientMutualFund);
			log.debug("clientMFRepository << save()");

			clientMutualFundDTO = mapper.map(clientMutualFund, ClientMutualFundDTO.class);
			clientMutualFundDTO.setClientID(clientMutualFund.getClientMaster().getId());
			clientMutualFundDTO.setFundTypeID(clientMutualFund.getMasterProductClassification().getId());
			if (clientMutualFundDTO.getFundTypeID() == 19 || clientMutualFundDTO.getFundTypeID() == 20) {
				clientMutualFundDTO.setIsin(clientMutualFund.getIsin());
			}
			if (clientMutualFundDTO.getFundTypeID() == 21) {
				//clientMutualFundDTO.setPmsID(clientMutualFund.getPmsID());
				clientMutualFundDTO.setIsin(null);
				clientMutualFundDTO.setProviderName(clientMutualFund.getPmsProvider());
				clientMutualFundDTO.setSchemeNamePMS(clientMutualFund.getPmsSchemeName());
				clientMutualFundDTO.setSubAssetID(clientMutualFund.getLookupAssetSubClass().getId());
			}
			clientMutualFundDTO.setFundCategoryID(clientMutualFund.getLookupFundCategory().getId());
			if (clientMutualFundDTO.getFundTypeID() == 19) {
				clientMutualFundDTO.setInvestmentModeID(clientMutualFund.getLookupFundInvestmentMode().getId());
			} else {
				if (clientMutualFundDTO.getFundTypeID() == 20) {
					clientMutualFundDTO.setInvestmentModeIDETF(clientMutualFund.getLookupFundInvestmentMode().getId());
				}
			}
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			log.debug("ClientFundServiceImpl << save()");
			return clientMutualFundDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public LinkedList<ClientMutualFundDTO> findByClientId(int clientId) throws RuntimeException {
		// log.debug("start of service");
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			LinkedList<ClientMutualFundDTO> clientMFDTOList = new LinkedList<ClientMutualFundDTO>();
			
			List<ClientMutualFund> clientMutualFundList = clientMaster.getClientMutualFunds();
			
			clientMutualFundList.sort(Comparator.comparing(ClientMutualFund::getCreatedOn));

			for (ClientMutualFund obj : clientMutualFundList) {
				ClientMutualFundDTO clientMutualFundDTO = mapper.map(obj, ClientMutualFundDTO.class);
				clientMutualFundDTO.setClientID(obj.getClientMaster().getId());
				clientMutualFundDTO.setOwnerName(obj.getClientFamilyMember().getFirstName() + " "
						+ (obj.getClientFamilyMember().getMiddleName() == null ? " "
								: obj.getClientFamilyMember().getMiddleName())
						+ " " + obj.getClientFamilyMember().getLastName());
				clientMutualFundDTO.setFundTypeID(obj.getMasterProductClassification().getId());
				MasterProductClassification mpc = masterProductClassificationRepository
						.findOne(obj.getMasterProductClassification().getId());
				clientMutualFundDTO.setFundTypeName(mpc.getProductName());

				clientMutualFundDTO.setFundCategoryID(obj.getLookupFundCategory().getId());

				if (obj.getLookupFundInvestmentMode() != null) {
					// log.debug("Id = " +
					// obj.getLookupFundInvestmentMode().getId());
					if (obj.getMasterProductClassification().getId() == 19) {
						clientMutualFundDTO.setInvestmentModeID(obj.getLookupFundInvestmentMode().getId());
						LookupFundInvestmentMode lfim = lookupFundInvestmentModeRepository
								.findOne(obj.getLookupFundInvestmentMode().getId());
						clientMutualFundDTO.setInvestmentModeName(lfim.getDescription());
					} else {
						if (obj.getMasterProductClassification().getId() == 20) {
							clientMutualFundDTO.setInvestmentModeIDETF(obj.getLookupFundInvestmentMode().getId());
							LookupFundInvestmentMode lfim = lookupFundInvestmentModeRepository
									.findOne(obj.getLookupFundInvestmentMode().getId());
							clientMutualFundDTO.setInvestmentModeName(lfim.getDescription());
						}
					}

				} else {
					clientMutualFundDTO.setInvestmentModeName(null);
				}

				if (obj.getIsin() != null) {
					MasterMutualFundETF mmfETF = mastermutualFundETFRepository.findOne(obj.getIsin());
					List<AccordSchemeIsinMaster> isinMasterList = accordSchemeISINMasterRepository.findByIdIsin(obj.getIsin());
					System.out.println("size "+isinMasterList.size());
					AccordSchemeIsinMaster acd = isinMasterList.get(0);
					clientMutualFundDTO.setFundHouse(mmfETF.getFundHouse());
					clientMutualFundDTO.setSchemeName(mmfETF.getSchemeName());
					String descriptiveSchemeName = clientMutualFundDTO.getSchemeName() + "-" + acd.getSeries();
					clientMutualFundDTO.setDescriptiveSchemeName(descriptiveSchemeName);
					
				} else {
					clientMutualFundDTO.setFundHouse("");
					clientMutualFundDTO.setSchemeName("");
				}

				if (obj.getMasterProductClassification().getId() == 21) {
					//For PMS
					clientMutualFundDTO.setProviderName(obj.getPmsProvider());
					clientMutualFundDTO.setSchemeNamePMS(obj.getPmsSchemeName());
					clientMutualFundDTO.setCurrentMarketValue(obj.getCurrentMarketValue());
				} else {
					clientMutualFundDTO.setProviderName("");
					clientMutualFundDTO.setSchemeNamePMS("");
				}
				
				// finding current value For Lumpsum & SIP For Mutual Fund and ETF
				if (obj.getMasterProductClassification().getId() == 19 || obj.getMasterProductClassification().getId() == 20) {
					MutualFundLumpsumSipService mutualFundLumpsumSipService = new MutualFundLumpsumSipService();
					double lastValue = 0.0;
					Calendar variableCal = Calendar.getInstance();
					Calendar currentDate = Calendar.getInstance();
					if (obj.getLookupFundInvestmentMode().getId() == 1 && obj.getLumpsumUnitsPurchased() != null) {

					/*	MutualFundLumpsumSip mutualFundLumpsumSip = mutualFundLumpsumSipService.getMutualFundLumpsumCalculation(
								obj.getInvestmentAmount().doubleValue(),
								obj.getIsin(), obj.getInvestmentStartDate(),
								obj.getLumpsumUnitsPurchased().intValue());
						//It is already being multiplied with 100 in product calculator
							for (MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup : mutualFundLumpsumSip
								.getMutualFundLumpsumSipList()) {
							variableCal.setTime(mutualFundLumpsumSipLookup.getRefDate());

							if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
									&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
								if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
										.get(Calendar.DAY_OF_MONTH)) {
									clientMutualFundDTO.setCurrentMarketValue(new BigDecimal(mutualFundLumpsumSipLookup.getPortfolioValue()));
								} else {
									clientMutualFundDTO.setCurrentMarketValue(new BigDecimal(lastValue));
								}
								break;
							}
							lastValue = mutualFundLumpsumSipLookup.getPortfolioValue();
						}*/
                   //new code
							
							MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup=mutualFundLumpsumSipService.getMutualFundLumpsumCalculationClient(obj.getInvestmentAmount().doubleValue(),
									obj.getIsin(), obj.getInvestmentStartDate(),
									obj.getLumpsumUnitsPurchased().intValue());
							clientMutualFundDTO.setCurrentMarketValue(new BigDecimal(mutualFundLumpsumSipLookup.getCurrentportfoliovalue()));	
					} else {
						if(obj.getSipInstalments() != null) {/*
							MutualFundLumpsumSip mutualFundLumpsumSip = mutualFundLumpsumSipService.getMutualFundSIPCalculation(
									obj.getInvestmentAmount().doubleValue(),
									obj.getIsin(), obj.getInvestmentStartDate(),
									obj.getSipInstalments(), obj.getSipFrequency());
							for (MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup : mutualFundLumpsumSip
									.getMutualFundLumpsumSipList()) {
								variableCal.setTime(mutualFundLumpsumSipLookup.getRefDate());
								if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
										&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
									if ((variableCal.get(Calendar.DAY_OF_MONTH) < currentDate
											.get(Calendar.DAY_OF_MONTH))) {
										clientMutualFundDTO.setCurrentMarketValue(new BigDecimal(mutualFundLumpsumSipLookup.getPortfolioValue()));
									} else {
										clientMutualFundDTO.setCurrentMarketValue(new BigDecimal(lastValue));
									}

									break;
								}
								lastValue = mutualFundLumpsumSipLookup.getPortfolioValue();
							}
						*/
							
						//new code 
							MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup = mutualFundLumpsumSipService.getMutualFundSIPCalculationClient(obj.getInvestmentAmount().doubleValue(),
									obj.getIsin(), obj.getInvestmentStartDate(),
									obj.getSipInstalments(), obj.getSipFrequency());
							if(mutualFundLumpsumSipLookup != null) {
							//System.out.println("mutualFundLumpsumSipLookup.getAmountInvested() "+mutualFundLumpsumSipLookup.getAmountInvested());
							clientMutualFundDTO.setCurrentMarketValue(new BigDecimal(mutualFundLumpsumSipLookup.getPortfolioValue()));
						}
					}
					
				  }
				}
				
				clientMutualFundDTO.setCreatedOn(obj.getCreatedOn());
				
				clientMFDTOList.add(clientMutualFundDTO);
			}
			log.debug("ClientFundServiceImpl << findByClientId()");
			return clientMFDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int deleteClientMutualFund(int id) throws RuntimeException {
		try {
			
			clientMFRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientMutualFundDTO findById(int id) throws RuntimeException {

		try {
			ClientMutualFundDTO clientMutualFundDTO = mapper.map(clientMFRepository.findOne(id), ClientMutualFundDTO.class);

			MasterMutualFundETF masterMutualFundETF = new MasterMutualFundETF();

			if (clientMutualFundDTO.getIsin() != null) {
				masterMutualFundETF = mastermutualFundETFRepository
						.findOne(clientMutualFundDTO.getIsin());
			}
			/*log.debug("masterPMSRepository >> findOne()");
			MasterPM masterPMS = masterPMSRepository.findOne(clientMutualFundDTO.getPmsID());
			log.debug("masterPMSRepository << findOne()");*/

			log.debug("clientMFRepository >> findOne()");
			clientMutualFundDTO.setClientID(clientMFRepository.findOne(id).getClientMaster().getId());
			clientMutualFundDTO.setFamilyMemberId(clientMFRepository.findOne(id).getClientFamilyMember().getId());
			clientMutualFundDTO.setOwnerName(clientMFRepository.findOne(id).getClientMaster().getFirstName() + " "
					+ clientMFRepository.findOne(id).getClientMaster().getLastName());
			clientMutualFundDTO.setFundTypeID(clientMFRepository.findOne(id).getMasterProductClassification().getId());
			if (clientMutualFundDTO.getFundTypeID() == 19) {
				log.debug("MF");
				clientMutualFundDTO.setFundHouse(masterMutualFundETF.getFundHouse());
				clientMutualFundDTO.setSchemeName(masterMutualFundETF.getSchemeName());
				clientMutualFundDTO.setIsin(clientMFRepository.findOne(id).getIsin());
				clientMutualFundDTO.setFundCategoryID(clientMFRepository.findOne(id).getLookupFundCategory().getId());
				clientMutualFundDTO.setCloseEndedFlag(clientMFRepository.findOne(id).getCloseEndedFlag());
				clientMutualFundDTO
				.setInvestmentModeID(clientMFRepository.findOne(id).getLookupFundInvestmentMode().getId());
				clientMutualFundDTO.setInvestmentStartDate(clientMFRepository.findOne(id).getInvestmentStartDate());
				clientMutualFundDTO.setInvestmentAmount(clientMFRepository.findOne(id).getInvestmentAmount());
				if (clientMutualFundDTO.getInvestmentModeID() == 1) {
					clientMutualFundDTO.setLumpsumUnitsPurchased(clientMFRepository.findOne(id).getLumpsumUnitsPurchased());
					clientMutualFundDTO.setMfLumpsumLockedInDate(clientMFRepository.findOne(id).getMfLumpsumLockedInDate());
					if (clientMutualFundDTO.getMfLumpsumLockedInDate() != null) {
						String date2 = formatter.format(clientMutualFundDTO.getMfLumpsumLockedInDate());
						try {
							clientMutualFundDTO.setMfLumpsumLockedInDate(formatter.parse(date2));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

				} else {
					if (clientMutualFundDTO.getInvestmentModeID() == 2) {
						clientMutualFundDTO.setSipFrequency(clientMFRepository.findOne(id).getSipFrequency());
						clientMutualFundDTO.setSipInstalments(clientMFRepository.findOne(id).getSipInstalments());
					}
				}

			} else {
				if (clientMutualFundDTO.getFundTypeID() == 20) {
					log.debug("ETF");
					clientMutualFundDTO.setFundHouse(masterMutualFundETF.getFundHouse());
					clientMutualFundDTO.setSchemeName(masterMutualFundETF.getSchemeName());
					clientMutualFundDTO.setIsin(clientMFRepository.findOne(id).getIsin());
					clientMutualFundDTO.setFundCategoryID(clientMFRepository.findOne(id).getLookupFundCategory().getId());
					// clientMutualFundDTO.setCloseEndedFlag(clientMFRepository.findOne(id).getCloseEndedFlag());
					clientMutualFundDTO
					.setInvestmentModeIDETF(clientMFRepository.findOne(id).getLookupFundInvestmentMode().getId());
					clientMutualFundDTO.setInvestmentStartDate(clientMFRepository.findOne(id).getInvestmentStartDate());
					clientMutualFundDTO.setInvestmentAmount(clientMFRepository.findOne(id).getInvestmentAmount());
					if (clientMutualFundDTO.getInvestmentModeIDETF() == 1) {
						clientMutualFundDTO
						.setLumpsumUnitsPurchased(clientMFRepository.findOne(id).getLumpsumUnitsPurchased());
					} else {
						if (clientMutualFundDTO.getInvestmentModeIDETF() == 2) {
							clientMutualFundDTO.setSipFrequency(clientMFRepository.findOne(id).getSipFrequency());
							clientMutualFundDTO.setSipInstalments(clientMFRepository.findOne(id).getSipInstalments());
						}
					}
				} else {
					if (clientMutualFundDTO.getFundTypeID() == 21) {
						log.debug("PMS");
						clientMutualFundDTO.setProviderName(clientMFRepository.findOne(id).getPmsProvider());
						clientMutualFundDTO.setSchemeNamePMS(clientMFRepository.findOne(id).getPmsSchemeName());
						//clientMutualFundDTO.setPmsID(clientMFRepository.findOne(id).getPmsID());
						clientMutualFundDTO
						.setFundCategoryID(clientMFRepository.findOne(id).getLookupFundCategory().getId());
						clientMutualFundDTO.setInvestmentStartDate(clientMFRepository.findOne(id).getInvestmentStartDate());
						clientMutualFundDTO.setInvestmentAmount(clientMFRepository.findOne(id).getInvestmentAmount());
						clientMutualFundDTO.setCurrentMarketValue(clientMFRepository.findOne(id).getCurrentMarketValue());
						clientMutualFundDTO.setSubAssetID(clientMFRepository.findOne(id).getLookupAssetSubClass().getId());
					}
				}
			}

			log.debug("clientMFRepository << findOne()");
			String date = formatter.format(clientMutualFundDTO.getInvestmentStartDate());
			try {
				clientMutualFundDTO.setInvestmentStartDate(formatter.parse(date));
				// date =
				// formatter.format(clientMutualFundDTO.getMfLumpsumLockedInDate());
				// clientMutualFundDTO.setMfLumpsumLockedInDate(formatter.parse(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.debug("ClientFundServiceImpl << findById()");
			return clientMutualFundDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	public Double getNAV(Date investmentDate, String isin) throws RuntimeException{
		try {
			double nav = 0.0;
			//log.info("Scheme Name: " + schemeName + " and Investment Date " + investmentDate);
			/*Calendar portfolioValueDate = Calendar.getInstance();
			portfolioValueDate.set(Calendar.MILLISECOND, 0);
			portfolioValueDate.set(Calendar.SECOND, 0);
			portfolioValueDate.set(Calendar.MINUTE, 0);
			portfolioValueDate.set(Calendar.HOUR, 0);*/

			/*List<MasterMFDailyNAV> mfNAVList = masterMFDailyNAVRepository.findNAV(isin, investmentDate, portfolioValueDate.getTime());*/

			MasterMFDailyNAV navObject = masterMFDailyNAVRepository.findNAV(isin, investmentDate);
			if(navObject == null) {
				/*Date newDate = masterMFDailyNAVRepository.findMaxDate(isin, investmentDate);
				MasterMFDailyNAV newNavObj = masterMFDailyNAVRepository.findNAV(isin, newDate);
				nav = newNavObj.getNav().doubleValue();*/
				nav = 0.0;
			} else {
				nav = navObject.getNav().doubleValue();
			}


			/*for (MasterMFDailyNAV navObj: mfNAVList) {
				if (navObj.getId().getDate().equals(investmentDate))		
					nav = navObj.getNav().doubleValue();
			}*/	
			//log.info("NAV: " + nav);
			System.out.println("NAV: " + nav);
			return nav;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public Double getLatestNAV(String isin) throws RuntimeException{
		try {
			double nav;
			MasterMFDailyNAV mfNAVlist = masterMFDailyNAVRepository.findTopByIdMasterMutualFundEtfIsinOrderByIdDateDesc(isin);
			nav = mfNAVlist.getNav();
			return nav;
		} catch (RuntimeException e){
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	@Override
	public void autoSave(ClientMutualFundDTO clientMutualFundDTO) throws RuntimeException {

		try {
			log.debug("ClientFundServiceImpl >> save()");

			log.debug("ClientMasterRepository >> findOne()");
			ClientMaster cm = clientMasterRepository.findOne(clientMutualFundDTO.getClientID());
			log.debug("ClientMasterRepository << findOne()");

			log.debug("ClientFamilyMemberRepository >> findOne()");
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientMutualFundDTO.getFamilyMemberId());
			log.debug("ClientFamilyMemberRepository << findOne()");

			ClientMutualFund clientMutualFund = mapper.map(clientMutualFundDTO, ClientMutualFund.class);
			clientMutualFund.setClientMaster(cm);
			log.debug("setting family member during insert");
			clientMutualFund.setClientFamilyMember(cfm);

			log.debug("MasterProductClassificationRepository >> findOne()");
			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientMutualFundDTO.getFundTypeID());
			log.debug("MasterProductClassificationRepository << findOne()");
			clientMutualFund.setMasterProductClassification(master);

			log.debug("LookupFundCategoryRepository >> findOne()");
			LookupFundCategory lookupFundCategory = lookupFundCategoryRepository
					.findOne(clientMutualFundDTO.getFundCategoryID());
			log.debug("LookupFundCategoryRepository << findOne()");
			clientMutualFund.setLookupFundCategory(lookupFundCategory);

			if (clientMutualFundDTO.getFundTypeID() == 19) {
				log.debug("lookupFundInvestmentModeRepository >> findOne()");
				LookupFundInvestmentMode lookupFundInvestmentMode = lookupFundInvestmentModeRepository
						.findOne(clientMutualFundDTO.getInvestmentModeID());
				log.debug("lookupFundInvestmentModeRepository << findOne()");
				clientMutualFund.setLookupFundInvestmentMode(lookupFundInvestmentMode);
			} else {
				if (clientMutualFundDTO.getFundTypeID() == 20) {
					log.debug("lookupFundInvestmentModeRepository >> findOne()");
					LookupFundInvestmentMode lookupFundInvestmentModeETF = lookupFundInvestmentModeRepository
							.findOne(clientMutualFundDTO.getInvestmentModeIDETF());
					log.debug("lookupFundInvestmentModeRepository << findOne()");
					clientMutualFund.setLookupFundInvestmentMode(lookupFundInvestmentModeETF);
				}
			}

			if (clientMutualFundDTO.getFundTypeID() == 19 || clientMutualFundDTO.getFundTypeID() == 20) {
				/*log.debug("mastermutualFundETFRepository >> findAmfiCodeByFundHouseAndSchemeName()");
				MasterMutualFundETF mmfETF = mastermutualFundETFRepository.findByFundHouseAndSchemeName(
						clientMutualFundDTO.getFundHouse(), clientMutualFundDTO.getSchemeName());
				log.debug("mastermutualFundETFRepository << findAmfiCodeByFundHouseAndSchemeName()");*/
				clientMutualFund.setIsin(clientMutualFundDTO.getIsin());

			}

			if (clientMutualFundDTO.getFundTypeID() == 21) {
				/*log.debug("masterPMSRepository >> findByProviderNameAndSchemeName()");
				MasterPM mPMS = masterPMSRepository.findByProviderNameAndSchemeName(clientMutualFundDTO.getProviderName(),
						clientMutualFundDTO.getSchemeNamePMS());
				log.debug("masterPMSRepository << findByProviderNameAndSchemeName()");
				clientMutualFund.setPmsID(mPMS.getId());*/
				clientMutualFund.setIsin(null);
				clientMutualFund.setPmsProvider(clientMutualFundDTO.getProviderName());
				clientMutualFund.setPmsSchemeName(clientMutualFundDTO.getSchemeNamePMS());
				LookupAssetSubClass assetSubClass = lookupAssetSubClassRepository.findOne(clientMutualFundDTO.getSubAssetID());
				clientMutualFund.setLookupAssetSubClass(assetSubClass);


			}

			if (clientMutualFundDTO.getFundTypeID() == 20 || clientMutualFundDTO.getFundTypeID() == 21) {

				clientMutualFundDTO.setCloseEndedFlag("N");
			}

			log.debug("clientMFRepository >> save()");
			clientMutualFund = clientMFRepository.save(clientMutualFund);
			log.debug("clientMFRepository << save()");
			//return clientMutualFundDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
