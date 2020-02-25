package com.finlabs.finexa.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientSmallSavingsDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientSmallSavingRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.util.FinexaConstant;


@Service("ClientSmallSavingsService")
@Transactional
public class ClientSmallSavingsServiceImpl implements ClientSmallSavingsService {

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	private static Logger log = LoggerFactory.getLogger(ClientSmallSavingsServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientSmallSavingRepository clientSSRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Autowired
	private FrequencyRepository frequencyRepository;
	@Resource(name = "exceptionmap")
	private Map<String, String> exceptionmap;
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	@Autowired
	AdvisorService advisorService;
	
	@Override
	public void autoSave(ClientSmallSavingsDTO clientSSDTO) throws RuntimeException {


		try {
			ClientMaster cm = clientMasterRepository.findOne(clientSSDTO.getClientID());
			log.debug("cm: " + cm);
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientSSDTO.getFamilyMemberID());

			ClientSmallSaving clientSmallSaving = mapper.map(clientSSDTO, ClientSmallSaving.class);
			clientSmallSaving.setClientMaster(cm);
			clientSmallSaving.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientSSDTO.getClientID());
			log.debug("Id: " + clientSSDTO.getId());

			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientSSDTO.getFinancialAssetType());
			clientSmallSaving.setMasterProductClassification(master);
			
			LookupFrequency compoundingFrequency = frequencyRepository.findOne(clientSSDTO.getCompoundingFrequencySelect());
			clientSmallSaving.setLookupFrequency1(compoundingFrequency);

			LookupFrequency interestPayoutFrequency = frequencyRepository.findOne(clientSSDTO.getInterestPayoutFrequencySelect());
			clientSmallSaving.setLookupFrequency2(interestPayoutFrequency);

			LookupFrequency depositFrequency = frequencyRepository.findOne(clientSSDTO.getDepositFrequency());
			clientSmallSaving.setLookupFrequency3(depositFrequency);
			
			if (clientSSDTO.getFinancialAssetType() == 25 || clientSSDTO.getFinancialAssetType() == 26 || clientSSDTO.getFinancialAssetType() == 28 || clientSSDTO.getFinancialAssetType() == 32) {
				//NSC or KVP or PORD or SSS
				LookupFrequency compoundingFrequencyNSCorKVPorPORDorSSS = frequencyRepository.findOne(clientSSDTO.getCompoundingFrequency());
				clientSmallSaving.setLookupFrequency1(compoundingFrequencyNSCorKVPorPORDorSSS);
			}
			
			if (clientSSDTO.getFinancialAssetType() == 30 || clientSSDTO.getFinancialAssetType() == 31) {
				//POMIS or SCSS
				LookupFrequency interestPayoutFrequencySCSSorPOMIS = frequencyRepository.findOne(clientSSDTO.getInterestPayoutFrequency());
				clientSmallSaving.setLookupFrequency2(interestPayoutFrequencySCSSorPOMIS);
			}
			
			if (clientSSDTO.getFinancialAssetType() == 32) {
				clientSmallSaving.setDepositTenure(FinexaConstant.SUKANYA_DEPOSIT_TENURE);
			}
		
			clientSmallSaving = clientSSRepository.save(clientSmallSaving);
			
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	
	}
	 

	@Override
	public ClientSmallSavingsDTO save(ClientSmallSavingsDTO clientSSDTO, HttpServletRequest request)
			throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientSSDTO.getClientID());
			log.debug("cm: " + cm);
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientSSDTO.getFamilyMemberID());

			ClientSmallSaving clientSmallSaving = mapper.map(clientSSDTO, ClientSmallSaving.class);
			clientSmallSaving.setClientMaster(cm);
			clientSmallSaving.setClientFamilyMember(cfm);
			log.debug("ClientId: " + clientSSDTO.getClientID());
			log.debug("Id: " + clientSSDTO.getId());

			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientSSDTO.getFinancialAssetType());
			clientSmallSaving.setMasterProductClassification(master);
			
			LookupFrequency compoundingFrequency = frequencyRepository.findOne(clientSSDTO.getCompoundingFrequencySelect());
			clientSmallSaving.setLookupFrequency1(compoundingFrequency);

			LookupFrequency interestPayoutFrequency = frequencyRepository.findOne(clientSSDTO.getInterestPayoutFrequencySelect());
			clientSmallSaving.setLookupFrequency2(interestPayoutFrequency);

			LookupFrequency depositFrequency = frequencyRepository.findOne(clientSSDTO.getDepositFrequency());
			clientSmallSaving.setLookupFrequency3(depositFrequency);
			
			if (clientSSDTO.getFinancialAssetType() == 25 || clientSSDTO.getFinancialAssetType() == 26 || clientSSDTO.getFinancialAssetType() == 28 || clientSSDTO.getFinancialAssetType() == 32) {
				//NSC or KVP or PORD or SSS
				LookupFrequency compoundingFrequencyNSCorKVPorPORDorSSS = frequencyRepository.findOne(clientSSDTO.getCompoundingFrequency());
				clientSmallSaving.setLookupFrequency1(compoundingFrequencyNSCorKVPorPORDorSSS);
			}
			
			if (clientSSDTO.getFinancialAssetType() == 30 || clientSSDTO.getFinancialAssetType() == 31) {
				//POMIS or SCSS
				LookupFrequency interestPayoutFrequencySCSSorPOMIS = frequencyRepository.findOne(clientSSDTO.getInterestPayoutFrequency());
				clientSmallSaving.setLookupFrequency2(interestPayoutFrequencySCSSorPOMIS);
			}
			
			if (clientSSDTO.getFinancialAssetType() == 32) {
				clientSmallSaving.setDepositTenure(FinexaConstant.SUKANYA_DEPOSIT_TENURE);
			}
		
			clientSmallSaving = clientSSRepository.save(clientSmallSaving);
			log.debug("clientSmallSaving: " + clientSmallSaving);

			clientSSDTO = mapper.map(clientSmallSaving, ClientSmallSavingsDTO.class);
			log.debug("clientSSDTO: " + clientSSDTO);
			clientSSDTO.setClientID(clientSmallSaving.getClientMaster().getId());
			log.debug("after setting clientID: " + clientSSDTO);
			clientSSDTO.setFinancialAssetType(clientSmallSaving.getMasterProductClassification().getId());
			log.debug("after setting financialAssetType: " + clientSSDTO);
			if (clientSSDTO.getFinancialAssetType() == 25 || clientSSDTO.getFinancialAssetType() == 26) {
				log.debug("NSC or KVP");
				if(clientSmallSaving.getLookupFrequency1() != null){
					clientSSDTO.setCompoundingFrequency(clientSmallSaving.getLookupFrequency1().getId());
				}
			} else {
				if (clientSSDTO.getFinancialAssetType() == 29) {
					log.debug("POTD");
					clientSSDTO.setCompoundingFrequencySelect(clientSmallSaving.getLookupFrequency1().getId());
					clientSSDTO.setInterestPayoutFrequencySelect(clientSmallSaving.getLookupFrequency2().getId());
				} else {
					if (clientSSDTO.getFinancialAssetType() == 30 || clientSSDTO.getFinancialAssetType() == 31) {
						log.debug("POMIS or SCSS");
						clientSSDTO.setInterestPayoutFrequency(clientSmallSaving.getLookupFrequency2().getId());
					} else {
						if (clientSSDTO.getFinancialAssetType() == 28 || clientSSDTO.getFinancialAssetType() == 32) {
							log.debug("PORD or Sukanya");
							clientSSDTO.setDepositFrequency(clientSmallSaving.getLookupFrequency3().getId());
							clientSSDTO.setCompoundingFrequency(clientSmallSaving.getLookupFrequency1().getId());
						} 
					}
				}
			}

			log.debug("Id: " + clientSSDTO.getId() + " clientId: " + clientSSDTO.getClientID());
			log.debug("clientSSDTO: " + clientSSDTO);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientSSDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientSmallSavingsDTO update(ClientSmallSavingsDTO clientSSDTO, HttpServletRequest request) throws RuntimeException{
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientSSDTO.getClientID());
			log.debug("Client ID: " + cm);
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientSSDTO.getFamilyMemberID());
			ClientSmallSaving clientSmallSaving = mapper.map(clientSSDTO, ClientSmallSaving.class);
			clientSmallSaving.setClientMaster(cm);
			clientSmallSaving.setClientFamilyMember(cfm);
			log.debug("Asset Type: " + clientSSDTO.getFinancialAssetType());
			log.debug("Id: " + clientSSDTO.getId());
			log.debug("Start Date: " + clientSSDTO.getStartDate());
			log.debug("Investment Amount: " + clientSSDTO.getInvestmentAmount());
			log.debug("Interest Rate: " + clientSSDTO.getInterestRate());
			log.debug("Maturity Date: " + clientSSDTO.getMaturityDate());

			MasterProductClassification master = masterProductClassificationRepository
					.findOne(clientSSDTO.getFinancialAssetType());
			clientSmallSaving.setMasterProductClassification(master);
			log.debug("Product Classification ID: " + master);

			LookupFrequency compoundingFrequency = frequencyRepository.findOne(clientSSDTO.getCompoundingFrequencySelect());
			clientSmallSaving.setLookupFrequency1(compoundingFrequency);
			log.debug("After setting compounding frequency: ");

			LookupFrequency interestPayoutFrequency = frequencyRepository.findOne(clientSSDTO.getInterestPayoutFrequencySelect());
			clientSmallSaving.setLookupFrequency2(interestPayoutFrequency);
			log.debug("After setting interest payout frequency: ");

			LookupFrequency depositFrequency = frequencyRepository.findOne(clientSSDTO.getDepositFrequency());
			clientSmallSaving.setLookupFrequency3(depositFrequency);
			log.debug("After setting deposit frequency: ");
			
			if (clientSSDTO.getFinancialAssetType() == 25 || clientSSDTO.getFinancialAssetType() == 26 || clientSSDTO.getFinancialAssetType() == 28 || clientSSDTO.getFinancialAssetType() == 32) {
				//NSC or KVP or PORD or SSS
				LookupFrequency compoundingFrequencyNSCorKVPorPORDorSSS = frequencyRepository.findOne(clientSSDTO.getCompoundingFrequency());
				clientSmallSaving.setLookupFrequency1(compoundingFrequencyNSCorKVPorPORDorSSS);
			}
			
			if (clientSSDTO.getFinancialAssetType() == 30 || clientSSDTO.getFinancialAssetType() == 31) {
				//POMIS or SCSS
				LookupFrequency interestPayoutFrequencyPOMISorSCSS = frequencyRepository.findOne(clientSSDTO.getInterestPayoutFrequency());
				clientSmallSaving.setLookupFrequency2(interestPayoutFrequencyPOMISorSCSS);
			}
			
			if (clientSSDTO.getFinancialAssetType() == 32) {
				clientSmallSaving.setDepositTenure(FinexaConstant.SUKANYA_DEPOSIT_TENURE);
			}
			
			clientSmallSaving = clientSSRepository.save(clientSmallSaving);
			log.debug("After Save: " + clientSmallSaving);

			clientSSDTO = mapper.map(clientSmallSaving, ClientSmallSavingsDTO.class);
			log.debug("clientSSDTO: " + clientSSDTO);
			clientSSDTO.setClientID(clientSmallSaving.getClientMaster().getId());
			log.debug("after setting clientID: " + clientSSDTO);
			clientSSDTO.setFinancialAssetType(clientSmallSaving.getMasterProductClassification().getId());
			log.debug("Asset Type DTO: " + clientSSDTO.getFinancialAssetType());
			// log.debug("after setting financialAssetType: " + clientSSDTO);
			if (clientSSDTO.getFinancialAssetType() == 25 || clientSSDTO.getFinancialAssetType() == 26) {
				log.debug("NSC or KVP");
				if(clientSmallSaving.getLookupFrequency1() != null){
					clientSSDTO.setCompoundingFrequency(clientSmallSaving.getLookupFrequency1().getId());
				}
			} else {
				if (clientSSDTO.getFinancialAssetType() == 29) {
					log.debug("POTD");
					clientSSDTO.setCompoundingFrequencySelect(clientSmallSaving.getLookupFrequency1().getId());
					clientSSDTO.setInterestPayoutFrequencySelect(clientSmallSaving.getLookupFrequency2().getId());
				} else {
					if (clientSSDTO.getFinancialAssetType() == 30 || clientSSDTO.getFinancialAssetType() == 31) {
						log.debug("POMIS or SCSS");
						clientSSDTO.setInterestPayoutFrequency(clientSmallSaving.getLookupFrequency2().getId());
					} else {
						if (clientSSDTO.getFinancialAssetType() == 28 || clientSSDTO.getFinancialAssetType() == 32) {
							log.debug("PORD or Sukanya");
							clientSSDTO.setDepositFrequency(clientSmallSaving.getLookupFrequency3().getId());
							log.debug("compounding frequency: " + clientSmallSaving.getLookupFrequency1().getId());
							clientSSDTO.setCompoundingFrequency(clientSmallSaving.getLookupFrequency1().getId());
						} 
					}
				}
			}

			log.debug("Id: " + clientSSDTO.getId() + " clientId: " + clientSSDTO.getClientID());
			// log.debug("clientSSDTO: " + clientSSDTO);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientSSDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientSmallSavingsDTO> findByClientId(int clientId) throws RuntimeException{
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientSmallSavingsDTO> listDTO = new ArrayList<ClientSmallSavingsDTO>();

			for (ClientSmallSaving clientSmallSaving : cm.getClientSmallSavings()) {
				ClientSmallSavingsDTO dto = mapper.map(clientSmallSaving, ClientSmallSavingsDTO.class);
				dto.setClientID(clientSmallSaving.getClientMaster().getId());
				MasterProductClassification mpc = masterProductClassificationRepository
						.findOne(clientSmallSaving.getMasterProductClassification().getId());
				dto.setFinancialAssetName(mpc.getProductName());
				dto.setOwnerName(clientSmallSaving.getClientFamilyMember().getFirstName() + " "
						+ (clientSmallSaving.getClientFamilyMember().getMiddleName() == null ? " "
								: clientSmallSaving.getClientFamilyMember().getMiddleName())
						+ " " + clientSmallSaving.getClientFamilyMember().getLastName());
				listDTO.add(dto);
			}
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException{
		
			try {
				clientSSRepository.delete(id);
				return 1;
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		
	}

	@Override
	public ClientSmallSavingsDTO findById(int id) throws RuntimeException{

		try {
			ClientSmallSavingsDTO clientSSDTO = mapper.map(clientSSRepository.findOne(id), ClientSmallSavingsDTO.class);
			clientSSDTO.setClientID(clientSSRepository.findOne(id).getClientMaster().getId());
			clientSSDTO.setFamilyMemberID(clientSSRepository.findOne(id).getClientFamilyMember().getId());
			clientSSDTO.setFinancialAssetType(clientSSRepository.findOne(id).getMasterProductClassification().getId());
			if (clientSSDTO.getFinancialAssetType() == 25 || clientSSDTO.getFinancialAssetType() == 26) {
				log.debug("NSC or KVP");
				if(clientSSRepository.findOne(id).getLookupFrequency1() != null){
					clientSSDTO.setCompoundingFrequency(clientSSRepository.findOne(id).getLookupFrequency1().getId());
				}
			} else {
				if (clientSSDTO.getFinancialAssetType() == 29) {
					log.debug("POTD");
					clientSSDTO.setCompoundingFrequencySelect(clientSSRepository.findOne(id).getLookupFrequency1().getId());
					clientSSDTO.setInterestPayoutFrequencySelect(clientSSRepository.findOne(id).getLookupFrequency2().getId());
				} else {
					if (clientSSDTO.getFinancialAssetType() == 30 || clientSSDTO.getFinancialAssetType() == 31) {
						log.debug("POMIS or SCSS");
						clientSSDTO.setInterestPayoutFrequency(clientSSRepository.findOne(id).getLookupFrequency2().getId());
					} else {
						if (clientSSDTO.getFinancialAssetType() == 28 || clientSSDTO.getFinancialAssetType() == 32) {
							log.debug("PORD or Sukanya");
							clientSSDTO.setDepositFrequency(clientSSRepository.findOne(id).getLookupFrequency3().getId());
							clientSSDTO.setCompoundingFrequency(clientSSRepository.findOne(id).getLookupFrequency1().getId());
						} 
					}
				}
			}

			String dsd = formatter.format(clientSSDTO.getStartDate());
			try {
				clientSSDTO.setStartDate(formatter.parse(dsd));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			String dmd = formatter.format(clientSSDTO.getMaturityDate());
			try {
				clientSSDTO.setMaturityDate(formatter.parse(dmd));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return clientSSDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientSmallSavingsDTO> findAll() {

		ClientSmallSavingsDTO ClientSmallSavingsDTO;
		List<ClientSmallSaving> listSmallSaving = clientSSRepository.findAll();

		List<ClientSmallSavingsDTO> listDTO = new ArrayList<ClientSmallSavingsDTO>();
		for (ClientSmallSaving clientSmallSaving : listSmallSaving) {
			ClientSmallSavingsDTO = mapper.map(clientSmallSaving, ClientSmallSavingsDTO.class);
			ClientSmallSavingsDTO.setClientID(clientSmallSaving.getClientMaster().getId());
			ClientSmallSavingsDTO.setFinancialAssetType(clientSmallSaving.getMasterProductClassification().getId());
			if (ClientSmallSavingsDTO.getFinancialAssetType() == 25
					|| ClientSmallSavingsDTO.getFinancialAssetType() == 26) {
				log.debug("NSC or KVP");
				ClientSmallSavingsDTO.setCompoundingFrequency(clientSmallSaving.getLookupFrequency1().getId());
			} else {
				if (ClientSmallSavingsDTO.getFinancialAssetType() == 29) {
					log.debug("POTD");
					ClientSmallSavingsDTO.setCompoundingFrequencySelect(clientSmallSaving.getLookupFrequency1().getId());
					ClientSmallSavingsDTO.setInterestPayoutFrequencySelect(clientSmallSaving.getLookupFrequency2().getId());
				} else {
					if (ClientSmallSavingsDTO.getFinancialAssetType() == 30
							|| ClientSmallSavingsDTO.getFinancialAssetType() == 31) {
						log.debug("POMIS or SCSS");
						ClientSmallSavingsDTO
								.setInterestPayoutFrequencySelect(clientSmallSaving.getLookupFrequency2().getId());
					} else {
						if (ClientSmallSavingsDTO.getFinancialAssetType() == 28
								|| ClientSmallSavingsDTO.getFinancialAssetType() == 32) {
							log.debug("PORD or Sukanya");
							ClientSmallSavingsDTO.setDepositFrequency(clientSmallSaving.getLookupFrequency3().getId());
							ClientSmallSavingsDTO
									.setCompoundingFrequencySelect(clientSmallSaving.getLookupFrequency1().getId());
						}
					}
				}
			}

			listDTO.add(ClientSmallSavingsDTO);
		}
		return listDTO;
	}
	

}
