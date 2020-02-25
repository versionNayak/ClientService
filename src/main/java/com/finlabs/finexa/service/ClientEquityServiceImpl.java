package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientEquityDTO;
import com.finlabs.finexa.dto.MasterDirectEquityDTO;
import com.finlabs.finexa.model.ClientEquity;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterEquityDailyPrice;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientEquityRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterDirectEquityRepository;
import com.finlabs.finexa.repository.MasterEquityDailyPriceRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.resources.model.EquityCalculator;
import com.finlabs.finexa.resources.service.EquityCalculatorService;


@Service("ClientEquityService")
@Transactional
public class ClientEquityServiceImpl implements ClientEquityService {
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	private static Logger log = LoggerFactory.getLogger(ClientEquityServiceImpl.class);
	
	@Autowired
	private Mapper mapper;
	@Autowired
	private ClientEquityRepository clientEquityRepository;
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	@Autowired
	private MasterDirectEquityRepository masterDirectEquityRepository;
	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;
	@Autowired
	private MasterEquityDailyPriceRepository masterEquityDailyPriceRepository;
	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientEquityDTO save(ClientEquityDTO clientEquityDTO, HttpServletRequest request) throws RuntimeException {

		try {
			log.debug("in save");
			ClientMaster cm = clientMasterRepository.findOne(clientEquityDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientEquityDTO.getFamilyMemberId());
			ClientEquity clientEquity = mapper.map(clientEquityDTO, ClientEquity.class);
			if (clientEquityDTO.getListedFlag() != null) {
				if (clientEquityDTO.getListedFlag().equals("Y")) {
					MasterDirectEquity mde = new MasterDirectEquity();
					mde.setIsin(clientEquityDTO.getIsin());
					clientEquity.setMasterDirectEquity(mde);
				} else {
					clientEquity.setUnlistedStockName(clientEquityDTO.getUnlistedStockNameText());
				}
			} else {
				clientEquity.setUnlistedStockName(clientEquityDTO.getUnlistedStockNameText());
			}

			clientEquity.setClientMaster(cm);
			clientEquity.setClientFamilyMember(cfm);

			MasterDirectEquity masterDirectEquity = masterDirectEquityRepository.findOne(clientEquityDTO.getIsin());
			clientEquity.setMasterDirectEquity(masterDirectEquity);
			
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientEquityDTO.getFinancialAssetType());
			clientEquity.setMasterProductClassification(masterProductClassification);

			ClientEquity retModel = clientEquityRepository.save(clientEquity);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			ClientEquityDTO retDTO = mapper.map(retModel, ClientEquityDTO.class);

			return retDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void autoSave(ClientEquityDTO clientEquityDTO) throws RuntimeException {

		try {
			log.debug("in save");
			ClientMaster cm = clientMasterRepository.findOne(clientEquityDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientEquityDTO.getFamilyMemberId());
			ClientEquity clientEquity = mapper.map(clientEquityDTO, ClientEquity.class);
			if (clientEquityDTO.getListedFlag() != null) {
				if (clientEquityDTO.getListedFlag().equals("Y")) {
					MasterDirectEquity mde = new MasterDirectEquity();
					mde.setIsin(clientEquityDTO.getIsin());
					clientEquity.setMasterDirectEquity(mde);
				} else {
					clientEquity.setUnlistedStockName(clientEquityDTO.getUnlistedStockNameText());
				}
			} else {
				clientEquity.setUnlistedStockName(clientEquityDTO.getUnlistedStockNameText());
			}

			clientEquity.setClientMaster(cm);
			clientEquity.setClientFamilyMember(cfm);

			MasterDirectEquity masterDirectEquity = masterDirectEquityRepository.findOne(clientEquityDTO.getIsin());
			clientEquity.setMasterDirectEquity(masterDirectEquity);
			
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientEquityDTO.getFinancialAssetType());
			clientEquity.setMasterProductClassification(masterProductClassification);

			ClientEquity retModel = clientEquityRepository.save(clientEquity);
			//ClientEquityDTO retDTO = mapper.map(retModel, ClientEquityDTO.class);

			//return retModel;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientEquityDTO update(ClientEquityDTO clientEquityDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientEquityDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientEquityDTO.getFamilyMemberId());
			ClientEquity clientEquity = mapper.map(clientEquityDTO, ClientEquity.class);
			if (clientEquityDTO.getListedFlag() != null) {
				if (clientEquityDTO.getListedFlag().equals("Y")) {
					MasterDirectEquity mde = new MasterDirectEquity();
					mde.setIsin(clientEquityDTO.getIsin());
					clientEquity.setMasterDirectEquity(mde);
				} else {
					clientEquity.setUnlistedStockName(clientEquityDTO.getUnlistedStockNameText());
				}
			} else {
				clientEquity.setUnlistedStockName(clientEquityDTO.getUnlistedStockNameText());
			}

			clientEquity.setClientMaster(cm);
			clientEquity.setClientFamilyMember(cfm);

			MasterDirectEquity masterDirectEquity = masterDirectEquityRepository.findOne(clientEquityDTO.getIsin());
			clientEquity.setMasterDirectEquity(masterDirectEquity);

			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientEquityDTO.getFinancialAssetType());
			clientEquity.setMasterProductClassification(masterProductClassification);

			clientEquity = clientEquityRepository.save(clientEquity);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientEquityDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientEquityDTO> findAll() {
		ClientEquityDTO clientEquityDTO;
		List<ClientEquity> clientEquityList = clientEquityRepository.findAll();

		List<ClientEquityDTO> clientEquityDTOList = new ArrayList<ClientEquityDTO>();
		for (ClientEquity obj : clientEquityList) {
			clientEquityDTO = mapper.map(obj, ClientEquityDTO.class);
			clientEquityDTO.setClientID(obj.getClientMaster().getId());
			clientEquityDTOList.add(clientEquityDTO);
		}

		return clientEquityDTOList;
	}

	@Override
	public List<ClientEquityDTO> findByClientId(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			ClientEquityDTO clientEquityDTO;
			List<ClientEquity> clientEquityList = clientMaster.getClientEquities();
			
			clientEquityList.sort(Comparator.comparing(ClientEquity::getCreatedOn).reversed());
			
			List<ClientEquityDTO> clientEquityDTOList = new ArrayList<ClientEquityDTO>();

			for (ClientEquity clientEquity : clientEquityList) {

				clientEquityDTO = mapper.map(clientEquity, ClientEquityDTO.class);
				if (clientEquity.getListedFlag() != null) {
					if (clientEquity.getListedFlag().equals("Y")) {
						clientEquityDTO.setUnlistedStockNameList(clientEquity.getMasterDirectEquity().getStockName());
						clientEquityDTO.setIsin(clientEquity.getMasterDirectEquity().getIsin());
					} else {
						clientEquityDTO.setUnlistedStockNameText(clientEquity.getUnlistedStockName());
					}
				} else {
					clientEquityDTO.setUnlistedStockNameText(clientEquity.getUnlistedStockName());
				}
				clientEquityDTO.setClientID(clientEquity.getClientMaster().getId());
				clientEquityDTOList.add(clientEquityDTO);
				clientEquityDTO.setOwnerName(clientEquity.getClientFamilyMember().getFirstName() + " "
						+ (clientEquity.getClientFamilyMember().getMiddleName() == null ? " "
								: clientEquity.getClientFamilyMember().getMiddleName())
						+ " " + clientEquity.getClientFamilyMember().getLastName());
				clientEquityDTO.setFinancialAssetTypeName(clientEquity.getMasterProductClassification().getProductName());

				// clientEquityDTO.setEsopVestingDate(obj.getEsopVestingDate());

			}

			return clientEquityDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientEquityDTO findById(int id) throws RuntimeException {
		// ClientEquityDTO clientEquityDTO =new ClientEquityDTO();
		try {
			ClientEquity clientEquity = clientEquityRepository.findOne(id);
			ClientEquityDTO clientEquityDTO = mapper.map(clientEquity, ClientEquityDTO.class);
			clientEquityDTO = getClosingPrice(clientEquityDTO);
			clientEquityDTO.setClientID(clientEquity.getClientMaster().getId());
			// clientEquityDTO.setListedFlag(clientEquity.getListedFlag());

			if (clientEquity.getListedFlag() != null) {
				if (clientEquity.getListedFlag().equals("Y")) {
					clientEquityDTO.setUnlistedStockNameList(clientEquity.getMasterDirectEquity().getStockName());
					clientEquityDTO.setIsin(clientEquity.getMasterDirectEquity().getIsin());

				} else {
					clientEquityDTO.setUnlistedStockNameText(clientEquity.getUnlistedStockName());
					clientEquityDTO.setCurrentMarketValue(clientEquity.getCurrentMarketValue());
				}
			} else {
				clientEquityDTO.setUnlistedStockNameText(clientEquity.getUnlistedStockName());
				clientEquityDTO.setCurrentMarketValue(clientEquity.getCurrentMarketValue());
			}
			String date2 = formatter.format(clientEquity.getPurchaseDate());
			try {
				clientEquityDTO.setPurchaseDate(formatter.parse(date2));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (clientEquity.getMasterProductClassification().getId() == 3) {
				String date3 = formatter.format(clientEquity.getEsopVestingDate());
				try {
					clientEquityDTO.setEsopVestingDate(formatter.parse(date3));

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			clientEquityDTO.setFamilyMemberId(clientEquity.getClientFamilyMember().getId());
			clientEquityDTO.setFinancialAssetType(clientEquity.getMasterProductClassification().getId());
			clientEquityDTO.setInvestmentAmount(clientEquity.getInvestmentAmount());
			clientEquityDTO.setQuantity(clientEquity.getQuantity());

			return clientEquityDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientEquityRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<MasterDirectEquityDTO> securityNameList() throws RuntimeException {
		try {
			MasterDirectEquityDTO masterDirectEquityDTO;
			List<MasterDirectEquity> masterDirectEquityList = masterDirectEquityRepository.findAll();

			List<MasterDirectEquityDTO> masterDirectEquityList1 = new ArrayList<MasterDirectEquityDTO>();
			for (MasterDirectEquity obj : masterDirectEquityList) {
				masterDirectEquityDTO = mapper.map(obj, MasterDirectEquityDTO.class);
				masterDirectEquityList1.add(masterDirectEquityDTO);
			}
			//sorting to alphabetical order
			masterDirectEquityList1.sort(Comparator.comparing(MasterDirectEquityDTO::getStockName));
			return masterDirectEquityList1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientEquityDTO calculateEquityCurrentValue(ClientEquityDTO clientEquityDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			log.debug("ClientEquityDTO =  " + clientEquityDTO);
			
			EquityCalculatorService equityCalculatorService = new EquityCalculatorService();
			
			if (clientEquityDTO.getIsin() != "" || clientEquityDTO.getIsin() != null || clientEquityDTO.getQuantity() != null) {
				EquityCalculator equityCalculator = equityCalculatorService.getEquityCalculation(
						clientEquityDTO.getPurchaseDate(), 
						clientEquityDTO.getInvestmentAmount().doubleValue(), 
						clientEquityDTO.getQuantity(), 
						clientEquityDTO.getIsin());
				log.debug("equity calculator: " + new BigDecimal(equityCalculator.getPortfolioValue()));
				clientEquityDTO.setCurrentMarketValue(new BigDecimal(equityCalculator.getPortfolioValue()));
			} 
			
			return clientEquityDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ClientEquityDTO getClosingPrice(ClientEquityDTO clientEquityDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		int i = 1;
		try {
			System.out.println("isin: " + clientEquityDTO.getIsin());
			//System.out.println("purchaseDate: " + clientEquityDTO.getPurchaseDate());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH,-1);
			Date oneDayBefore= cal.getTime();
			
			//System.out.println("purchaseDate one day before: " + oneDayBefore);
			
			MasterEquityDailyPrice equityDailyPrice = masterEquityDailyPriceRepository.findClosingPrice(clientEquityDTO.getIsin(), oneDayBefore);
			
			if (equityDailyPrice != null) {
				//System.out.println("closingPrice: " + equityDailyPrice.getClosingPrice());
				clientEquityDTO.setCurrentMarketValue(BigDecimal.valueOf(equityDailyPrice.getClosingPrice() * clientEquityDTO.getQuantity()));
			} else {
			
				while (true) {
					cal.add(Calendar.DAY_OF_MONTH,-1);
					Date oneDayBefore1= cal.getTime();
					
					//System.out.println("purchaseDate one day before: " + oneDayBefore);
					
					equityDailyPrice = masterEquityDailyPriceRepository.findClosingPrice(clientEquityDTO.getIsin(), oneDayBefore1);
					if (equityDailyPrice != null) {
						clientEquityDTO.setCurrentMarketValue(new BigDecimal(equityDailyPrice.getClosingPrice() * clientEquityDTO.getQuantity()));
						break;
					} 
					if(i==7) {
						break;
					}
					i++;
				}
			}
			if(i==7) {
			
			clientEquityDTO.setErrorMessage("No closing balance for this Security Name.");
					
			}
			return clientEquityDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
