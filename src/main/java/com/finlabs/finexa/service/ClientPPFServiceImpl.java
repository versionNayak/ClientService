package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientPpfDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.MasterEPFInterestRate;
import com.finlabs.finexa.model.MasterPPFInterestRate;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientPpfRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.MasterPPFInterestRateRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.resources.model.PPFFixedAmountDeposit;
import com.finlabs.finexa.resources.service.PPFFixedAmountService;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientPPFService")
@Transactional
public class ClientPPFServiceImpl implements ClientPPFService {
	private static Logger log = LoggerFactory.getLogger(ClientPPFServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private FrequencyRepository lookupFreqRepo;

	@Autowired
	private MasterProductClassificationRepository masterProdRepository;

	@Autowired
	private ClientPpfRepository clientPPFRepository;

	@Autowired
	private MasterPPFInterestRateRepository masterPPFRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public ClientPpfDTO save(ClientPpfDTO clientPPFDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientPPFDTO.getClientID());
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientPPFDTO.getFamilyMemberID());
			MasterProductClassification masterProd = masterProdRepository.findOne(clientPPFDTO.getFinancialAssetType());
			LookupFrequency depositFreq = lookupFreqRepo.findOne(clientPPFDTO.getAmountDepositFrequency());

			if (clientPPFDTO.getExtensionFlag() == null) {
				clientPPFDTO.setExtensionFlag("N");
				clientPPFDTO.setExtTypeFlag("N");
			}

			ClientPPF clientPPF = mapper.map(clientPPFDTO, ClientPPF.class);
			clientPPF.setClientMaster(cm);
			clientPPF.setClientFamilyMember(fam);
			clientPPF.setMasterProductClassification(masterProd);
			clientPPF.setLookupFrequency1(depositFreq);
			log.info("Entered interest rate: " + clientPPFDTO.getInterestRate().doubleValue());
			double ppfInterestRate = clientPPFDTO.getInterestRate().doubleValue() / 100;
			log.info("Converted interest rate: " + ppfInterestRate);
			clientPPF.setInterestRate(new BigDecimal(ppfInterestRate));

			if (clientPPFDTO.getCompoundingFrequency() != null) {
				LookupFrequency compoundFreq = lookupFreqRepo.findOne(clientPPFDTO.getCompoundingFrequency());
				clientPPF.setLookupFrequency2(compoundFreq);
			}

			if (clientPPFDTO.getExtensionFlag().equals("Y")) {
				double ppfExtensionInterestRate = clientPPFDTO.getExtensionInterestRate().doubleValue() / 100;
				clientPPF.setExtensionInterestRate(new BigDecimal(ppfExtensionInterestRate));
				clientPPF.setExtensionTenure(clientPPFDTO.getExtensionTenure());
			}

			clientPPF = clientPPFRepository.save(clientPPF);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientPPFDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientPpfDTO update(ClientPpfDTO clientPPFDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientPPFDTO.getClientID());
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientPPFDTO.getFamilyMemberID());
			MasterProductClassification masterProd = masterProdRepository.findOne(clientPPFDTO.getFinancialAssetType());
			LookupFrequency depositFreq = lookupFreqRepo.findOne(clientPPFDTO.getAmountDepositFrequency());

			if (clientPPFDTO.getExtensionFlag() == null) {
				clientPPFDTO.setExtensionFlag("N");
				clientPPFDTO.setExtTypeFlag("N");
			}

			ClientPPF clientPPF = mapper.map(clientPPFDTO, ClientPPF.class);
			clientPPF.setClientMaster(cm);
			clientPPF.setClientFamilyMember(fam);
			clientPPF.setMasterProductClassification(masterProd);
			clientPPF.setLookupFrequency1(depositFreq);
			log.info("Entered interest rate: " + clientPPFDTO.getInterestRate().doubleValue());
			double ppfInterestRate = clientPPFDTO.getInterestRate().doubleValue() / 100;
			log.info("Converted interest rate: " + ppfInterestRate);
			clientPPF.setInterestRate(new BigDecimal(ppfInterestRate));

			if (clientPPFDTO.getCompoundingFrequency() != null) {
				LookupFrequency compoundFreq = lookupFreqRepo.findOne(clientPPFDTO.getCompoundingFrequency());
				clientPPF.setLookupFrequency2(compoundFreq);
			}

			if (clientPPFDTO.getExtensionFlag().equals("Y")) {
				double ppfExtensionInterestRate = clientPPFDTO.getExtensionInterestRate().doubleValue() / 100;
				clientPPF.setExtensionInterestRate(new BigDecimal(ppfExtensionInterestRate));
				clientPPF.setExtensionTenure(clientPPFDTO.getExtensionTenure());
			}

			clientPPF = clientPPFRepository.save(clientPPF);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientPPFDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientPpfDTO> findByClientId(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			ClientPpfDTO clientPPFDTO;
			List<ClientPPF> clientPPFList = clientMaster.getClientPpfs();
			List<ClientPpfDTO> clientPPFDTOList = new ArrayList<ClientPpfDTO>();

			for (ClientPPF obj : clientPPFList) {

				clientPPFDTO = mapper.map(obj, ClientPpfDTO.class);
				clientPPFDTO.setClientID(obj.getClientMaster().getId());
				clientPPFDTOList.add(clientPPFDTO);
			}

			return clientPPFDTOList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {
		
			try {
				clientPPFRepository.delete(id);
				return 1;
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
		
	}

	@Override
	public ClientPpfDTO findById(int id) throws RuntimeException {

		try {
			ClientPPF clientPPf = clientPPFRepository.findOne(id);
			ClientPpfDTO clientPPFDTO = mapper.map(clientPPFRepository.findOne(id), ClientPpfDTO.class);
			clientPPFDTO.setClientID(clientPPFRepository.findOne(id).getClientMaster().getId());
			clientPPFDTO.setFamilyMemberID(clientPPf.getClientFamilyMember().getId());
			clientPPFDTO.setFirstName(clientPPf.getClientFamilyMember().getFirstName());
			clientPPFDTO.setRelationId(clientPPf.getClientFamilyMember().getLookupRelation().getId());
			clientPPFDTO.setRelationName(clientPPf.getClientFamilyMember().getLookupRelation().getDescription());
			clientPPFDTO.setGender(FinexaUtil.getGender(clientPPf.getClientFamilyMember().getClientMaster(),
					clientPPf.getClientFamilyMember().getLookupRelation()));
			clientPPFDTO.setFinancialAssetType(clientPPf.getMasterProductClassification().getId());
			clientPPFDTO.setAmountDepositFrequency(clientPPf.getLookupFrequency1().getId());
			clientPPFDTO.setCompoundingFrequency(clientPPf.getLookupFrequency2().getId());
			if (clientPPf.getExtensionFlag().equals("Y")) {
				clientPPFDTO.setExtensionTenure(clientPPf.getExtensionTenure());
			}
			return clientPPFDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientPpfDTO> findAll() {
		// TODO Auto-generated method stub
		ClientPpfDTO ClientPpfDTO;
		List<ClientPPF> listClientPPF = clientPPFRepository.findAll();

		List<ClientPpfDTO> listDTO = new ArrayList<ClientPpfDTO>();
		for (ClientPPF clientPPF : listClientPPF) {
			ClientPpfDTO = mapper.map(clientPPF, ClientPpfDTO.class);
			ClientPpfDTO.setClientID(clientPPF.getClientMaster().getId());
			listDTO.add(ClientPpfDTO);
		}

		return listDTO;
	}

	/*
	 * @Override public double getInterestRate(Date dt) { // TODO Auto-generated
	 * method stub System.out.println("Date" + dt); double interestRate = 0.0;
	 * List<MasterPPFInterestRate> masterPPFInterestRateList =
	 * masterPPFRepository.findAll(); for(MasterPPFInterestRate obj :
	 * masterPPFInterestRateList) { if (obj.getValidFromDate().before(dt) &&
	 * dt.before(obj.getValidToDate())) { interestRate =
	 * obj.getInterestRate().doubleValue(); } } return interestRate; }
	 */
	@Override
	public double getInterestRate(Date ppfDate) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			System.out.println("Date" + ppfDate);
			Double interestRate = null;
			List<MasterPPFInterestRate> masterPPFInterestRateList = masterPPFRepository.findAll();
			
			for (MasterPPFInterestRate obj : masterPPFInterestRateList) {
				
					if (ppfDate.after(obj.getValidFromDate()) &&  ppfDate.before(obj.getValidToDate())) {
						interestRate = obj.getInterestRate().doubleValue();
					} 
				
			}
			
			if (interestRate == null) {
				interestRate = masterPPFInterestRateList.get((masterPPFInterestRateList.size() - 1)).getInterestRate().doubleValue();
			}
				
			System.out.println("interestRate: " + interestRate);
			return interestRate;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	/*@Override
	public double getInterestRate(Date dt) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			double interestRate = 0.0;
			List<MasterPPFInterestRate> masterPPFInterestRate = masterPPFRepository.findAll();
			for (MasterPPFInterestRate obj : masterPPFInterestRate) {
				if (obj.getValidFromDate().before(dt) && dt.before(obj.getValidToDate())) {
					interestRate = obj.getInterestRate().doubleValue();
				}
			}
			return interestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}*/
	
	@Override
	public boolean checkIfPpfPresent(Integer clientId) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

		List<ClientPPF> ClientPpfList = clientMaster.getClientPpfs();
		if (ClientPpfList.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<ClientFamilyMemberDTO> checkIfPpfPresentForAll(Integer clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMember> ClientFamilyMembers = clientMaster.getClientFamilyMembers();

			List<ClientPPF> ClientPpfs = clientMaster.getClientPpfs();

			List<ClientFamilyMember> ClientFamilyMembers1 = new ArrayList<ClientFamilyMember>();

			for (ClientPPF cf : ClientPpfs) {

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

	@Override
	public ClientPpfDTO calculatePPFMaturityDate(ClientPpfDTO clientPpfDTO) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			log.debug("ClientPpfDTO =  " + clientPpfDTO);
			
			SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			PPFFixedAmountService ppfFixedAmountService = new PPFFixedAmountService();
			
			if (clientPpfDTO.getCurrentBalance() == null) {
				clientPpfDTO.setCurrentBalance(new BigDecimal("0"));
			}
			
			if (clientPpfDTO.getAmountDepositFrequency() == null) {
				clientPpfDTO.setAmountDepositFrequency((byte)1);
			}
			
			PPFFixedAmountDeposit ppfCalculator = ppfFixedAmountService.getPPFFixedAmountCalculationDetails(
					(clientPpfDTO.getCurrentBalance().doubleValue()),
					(clientPpfDTO.getPlannedDepositAmount() == null ? 0 : clientPpfDTO.getPlannedDepositAmount().doubleValue()), 
					"", 
					clientPpfDTO.getPpfTenure(), 
					clientPpfDTO.getAmountDepositFrequency(), 
					clientPpfDTO.getCompoundingFrequency(), 
					clientPpfDTO.getStartDate());
			
			clientPpfDTO.setDisplayDate(displayDateFormat.format(ppfCalculator.getMaturityDate()));
			
			return clientPpfDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void autoSave(ClientPpfDTO clientPPFDTO) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientPPFDTO.getClientID());
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientPPFDTO.getFamilyMemberID());
			MasterProductClassification masterProd = masterProdRepository.findOne(clientPPFDTO.getFinancialAssetType());
			LookupFrequency depositFreq = lookupFreqRepo.findOne(clientPPFDTO.getAmountDepositFrequency());

			if (clientPPFDTO.getExtensionFlag() == null) {
				clientPPFDTO.setExtensionFlag("N");
				clientPPFDTO.setExtTypeFlag("N");
			}

			ClientPPF clientPPF = mapper.map(clientPPFDTO, ClientPPF.class);
			clientPPF.setClientMaster(cm);
			clientPPF.setClientFamilyMember(fam);
			clientPPF.setMasterProductClassification(masterProd);
			clientPPF.setLookupFrequency1(depositFreq);
			log.info("Entered interest rate: " + clientPPFDTO.getInterestRate().doubleValue());
			double ppfInterestRate = clientPPFDTO.getInterestRate().doubleValue() / 100;
			log.info("Converted interest rate: " + ppfInterestRate);
			clientPPF.setInterestRate(new BigDecimal(ppfInterestRate));

			if (clientPPFDTO.getCompoundingFrequency() != null) {
				LookupFrequency compoundFreq = lookupFreqRepo.findOne(clientPPFDTO.getCompoundingFrequency());
				clientPPF.setLookupFrequency2(compoundFreq);
			}

			if (clientPPFDTO.getExtensionFlag().equals("Y")) {
				double ppfExtensionInterestRate = clientPPFDTO.getExtensionInterestRate().doubleValue() / 100;
				clientPPF.setExtensionInterestRate(new BigDecimal(ppfExtensionInterestRate));
				clientPPF.setExtensionTenure(clientPPFDTO.getExtensionTenure());
			}

			clientPPF = clientPPFRepository.save(clientPPF);
			//return clientPPFDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

}
