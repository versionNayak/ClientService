package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientFixedIncomeDTO;

import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupBondType;
import com.finlabs.finexa.model.LookupFixedDepositType;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.MasterCash;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientFixedIncomeRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.LookupBondTypeRepository;
import com.finlabs.finexa.repository.LookupFixedDepositTypeRepository;
import com.finlabs.finexa.repository.MasterCashRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientFixedIncomeService")
@Transactional
public class ClientFixedIncomeServiceImpl implements ClientFixedIncomeService {
	private static Logger log = LoggerFactory.getLogger(ClientFixedIncomeServiceImpl.class);
	@Autowired
	private Mapper mapper;
	@Autowired
	private ClientFixedIncomeRepository clientFixedIncomeRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	@Autowired
	private LookupFixedDepositTypeRepository lookupFixedDepositTypeRepository;
	@Autowired
	private FrequencyRepository frequencyRepository;

	@Autowired
	private MasterCashRepository masterCashRepository;

	@Autowired
	private LookupBondTypeRepository lookupBondTypeRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public void autoSave(ClientFixedIncomeDTO clientFixedIncomeDTO) throws RuntimeException {

		try {
			
			ClientFixedIncome clientFixedIncome = mapper.map(clientFixedIncomeDTO, ClientFixedIncome.class);

			ClientMaster cm = clientMasterRepository.findOne(clientFixedIncomeDTO.getClientID());
			clientFixedIncome.setClientMaster(cm);
			
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(clientFixedIncomeDTO.getFamilyMemberID());
			clientFixedIncome.setClientFamilyMember(clientFamilyMember);
			
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientFixedIncomeDTO.getFinancialAssetType());
			clientFixedIncome.setMasterProductClassification(masterProductClassification);
			
			LookupFrequency compoundingFrequency = frequencyRepository.findOne(clientFixedIncomeDTO.getCompoundingFrequency());
			clientFixedIncome.setLookupFrequency1(compoundingFrequency);
			
			LookupFixedDepositType fixedDepositType = lookupFixedDepositTypeRepository.findOne(clientFixedIncomeDTO.getFixedDepositType());
			clientFixedIncome.setLookupFixedDepositType(fixedDepositType);
			
			clientFixedIncome = clientFixedIncomeRepository.save(clientFixedIncome);

		//	return clientFixedIncomeDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientFixedIncomeDTO save(ClientFixedIncomeDTO clientFixedIncomeDTO, HttpServletRequest request) throws RuntimeException {

		try {
			
			ClientFixedIncome clientFixedIncome = mapper.map(clientFixedIncomeDTO, ClientFixedIncome.class);

			ClientMaster cm = clientMasterRepository.findOne(clientFixedIncomeDTO.getClientID());
			clientFixedIncome.setClientMaster(cm);
			// log.debug("inr rate
			// "+clientFixedIncomeDTO.getInterestCouponRate());
			
			if (clientFixedIncomeDTO.getInterestCouponRate() != null) {
				double interestRate = clientFixedIncomeDTO.getInterestCouponRate().doubleValue() / 100;
				//System.out.println("interest coupon rate: " + BigDecimal.valueOf(interestRate));
				clientFixedIncome.setInterestCouponRate(BigDecimal.valueOf(interestRate));
			}else {
				BigDecimal v = BigDecimal.valueOf(0);
				clientFixedIncome.setInterestCouponRate(v);
			}
			
			/*if (clientFixedIncomeDTO.getInterestCouponRate() != null) {
				double interestRate = clientFixedIncomeDTO.getInterestCouponRate().doubleValue() / 100;
				// log.debug("ss "+interestRate);
				clientFixedIncomeDTO.setInterestCouponRate(BigDecimal.valueOf(interestRate));
			} else {
				BigDecimal v = BigDecimal.valueOf(0);
				clientFixedIncomeDTO.setInterestCouponRate(v);
			}*/
			if (clientFixedIncomeDTO.getTenureYearsDays().equalsIgnoreCase("M")) {
				clientFixedIncomeDTO.setTenureRDMonths(clientFixedIncome.getTenure());
			}

			// log.debug("save ");
			// log.debug("member id"+clientFixedIncomeDTO.getFamilyMemberID());
			// log.debug("bank id"+clientFixedIncomeDTO.getBankIssuerId());
			// log.debug("start
			// date"+clientFixedIncomeDTO.getInvestmentDepositDate());
			// log.debug("Amount"+clientFixedIncomeDTO.getAmount());
			// log.debug("coupon
			// rate"+clientFixedIncomeDTO.getInterestCouponRate());
			// log.debug("compound
			// freq"+clientFixedIncomeDTO.getCompoundingFrequency());
			// log.debug("years
			// days"+clientFixedIncomeDTO.getTenureYearsDays());
			// log.debug("tenure "+clientFixedIncomeDTO.getTenure());
			// log.debug("interest
			// freq"+clientFixedIncomeDTO.getPayoutFrequency());
			// log.debug("matu date"+clientFixedIncomeDTO.getMaturityDate());

			

			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientFixedIncomeDTO.getFinancialAssetType());
			clientFixedIncome.setMasterProductClassification(masterProductClassification);

			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository
					.findOne(clientFixedIncomeDTO.getFamilyMemberID());
			clientFixedIncome.setClientFamilyMember(clientFamilyMember);

			// log.debug("aaaaa");
			if (clientFixedIncomeDTO.getFixedDepositType() != 0) {
				LookupFixedDepositType lookupFixedDepositType = lookupFixedDepositTypeRepository
						.findOne(clientFixedIncomeDTO.getFixedDepositType());
				clientFixedIncome.setLookupFixedDepositType(lookupFixedDepositType);
			}
			// log.debug("bbbb");
			if (clientFixedIncomeDTO.getCompoundingFrequency() != 0) {
				LookupFrequency lookupFrequency1 = frequencyRepository
						.findOne(clientFixedIncomeDTO.getCompoundingFrequency());
				clientFixedIncome.setLookupFrequency1(lookupFrequency1);
			}
			// log.debug("payout "+clientFixedIncomeDTO.getPayoutFrequency());
			// clientFixedIncomeDTO.setPayoutFrequency((byte)2);
			// log.debug("cccc ");
			// log.debug("cccc "+clientFixedIncomeDTO.getPayoutFrequency());

			if (clientFixedIncomeDTO.getPayoutFrequency() != 0) {
				// log.debug("cc11111");
				LookupFrequency lookupFrequency2 = frequencyRepository
						.findOne(clientFixedIncomeDTO.getPayoutFrequency());
				// log.debug("payouttt "+lookupFrequency2);
				clientFixedIncome.setLookupFrequency2(lookupFrequency2);
			}

			// log.debug("ddddd");
			// log.debug("recurring
			// "+clientFixedIncomeDTO.getRecurringDepositFrequency());
			if (clientFixedIncomeDTO.getRecurringDepositFrequency() != 0) {
				LookupFrequency lookupFrequency3 = frequencyRepository
						.findOne(clientFixedIncomeDTO.getRecurringDepositFrequency());
				// log.debug("recurringggg "+lookupFrequency3.getId());
				clientFixedIncome.setLookupFrequency3(lookupFrequency3);
			}

			// log.debug("eeee");
			if (clientFixedIncomeDTO.getBondType() != 0 || clientFixedIncomeDTO.getBondType() != 0) {
				LookupBondType LookupBondType = lookupBondTypeRepository.findOne(clientFixedIncomeDTO.getBondType());
				// log.debug("recurringggg "+LookupBondType.getId());
				clientFixedIncome.setLookupBondType(LookupBondType);
			}
			
			/*if (clientFixedIncomeDTO.getBondType() == 2) {
				if (clientFixedIncomeDTO.getMaturityDate() != null) {
					clientFixedIncome.setMaturityDate(clientFixedIncomeDTO.getMaturityDate());
				} else {
					clientFixedIncome.setMaturityDate(new Date(0));
				}
			}*/
			
			/*MasterCash masterCash;
			if (clientFixedIncomeDTO.getBankIssuerId() != null) {
				masterCash = masterCashRepository.findOne(clientFixedIncomeDTO.getBankIssuerId());
				clientFixedIncome.setMasterCash(masterCash);
			}*/

			if (clientFixedIncomeDTO.getFinancialAssetType() == 27) {
				clientFixedIncomeDTO.setTenureYearsDays("D");
			}
			if (clientFixedIncomeDTO.getFinancialAssetType() == 23) {
				clientFixedIncomeDTO.setTenure(clientFixedIncome.getTenure());
				
			}
			// log.debug("id "+clientFixedIncome.getId());

			// log.debug("member
			// id"+clientFixedIncome.getClientFamilyMember().getId());
			
			//by saheli
			/*if (clientFixedIncome.getMasterCash() != null) {
				// log.debug("bank
				// id"+clientFixedIncome.getMasterCash().getId());
			}*/
			
			// log.debug("start
			// date"+clientFixedIncome.getInvestmentDepositDate());
			// log.debug("Amount"+clientFixedIncome.getAmount());
			// log.debug("coupon
			// rate"+clientFixedIncome.getInterestCouponRate());
			// log.debug("compound
			// freq"+clientFixedIncome.getLookupFrequency1());
			// log.debug("years days"+clientFixedIncome.getTenureYearsDays());
			// log.debug("tenure "+clientFixedIncome.getTenure());

			// log.debug("interest
			// freq"+clientFixedIncome.getLookupFrequency2());
			// log.debug("recurring interest
			// freq"+clientFixedIncome.getLookupFrequency3());
			// log.debug("matu date"+clientFixedIncome.getMaturityDate());
			
			clientFixedIncome = clientFixedIncomeRepository.save(clientFixedIncome);

			// log.debug("save "+clientFixedIncome.getId());

			// clientFixedIncomeDTO = mapper.map(clientFixedIncome,
			// ClientFixedIncomeDTO.class);
			// clientFixedIncomeDTO.setClientID(clientFixedIncome.getClientMaster().getId());

			// clientFixedIncomeDTO.setFinancialAssetType(clientFixedIncome.getMasterProductClassification().getId());
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientFixedIncomeDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<ClientFixedIncomeDTO> findByClientId(int clientId) throws RuntimeException {

		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			List<ClientFixedIncome> clientFixedIncomeList = clientMaster.getClientFixedIncomes();
			ClientFixedIncomeDTO clientFixedIncomeDTO;

			List<ClientFixedIncomeDTO> clientFIDTOList = new ArrayList<ClientFixedIncomeDTO>();

			for (ClientFixedIncome obj : clientFixedIncomeList) {
				
				clientFixedIncomeDTO = mapper.map(obj, ClientFixedIncomeDTO.class);
				
				double interestRate = obj.getInterestCouponRate().doubleValue() * 100;
				clientFixedIncomeDTO.setInterestCouponRate(new BigDecimal(interestRate));
				
				
				clientFixedIncomeDTO.setClientID(obj.getClientMaster().getId());
				clientFixedIncomeDTO.setFamilyMemberID(obj.getClientFamilyMember().getId());
				
				
				
				String bankIdString = clientFixedIncomeDTO.getBankIssuerName();
				try {
					int bankId = Integer.parseInt(bankIdString);
					MasterCash masterCash = masterCashRepository.findOne(bankId);
					clientFixedIncomeDTO.setBankIssuerName(masterCash.getName());
				} catch (Exception e) {
					// bankIssuerName is a String , no action needs to be taken
				}
				
				
				/*if (obj.getMasterCash() != null) {
				 * 
					clientFixedIncomeDTO.setBankIssuerId(obj.getMasterCash().getId());
					clientFixedIncomeDTO.setBankIssuerName(obj.getMasterCash().getName());
				} else {
					clientFixedIncomeDTO.setBankIssuerName("");
				}
*/
				/*if (obj.getBankIssuerName() != null) {
					clientFixedIncomeDTO.setBankIssuerName(obj.getBankIssuerName());
				} else {
					clientFixedIncomeDTO.setBankIssuerName("");
				}*/
				
				clientFixedIncomeDTO.setFinancialAssetType(obj.getMasterProductClassification().getId());
				clientFixedIncomeDTO.setFinancialAssetTypeName(obj.getMasterProductClassification().getProductName());

				if (obj.getLookupFrequency1() != null) {
					clientFixedIncomeDTO.setCompoundingFrequency(obj.getLookupFrequency1().getId());
				}
				if (obj.getLookupFrequency2() != null) {
					clientFixedIncomeDTO.setPayoutFrequency(obj.getLookupFrequency2().getId());
				}
				if (obj.getLookupFrequency3() != null) {
					clientFixedIncomeDTO.setRecurringDepositFrequency(obj.getLookupFrequency3().getId());
				}
				if (obj.getLookupFixedDepositType() != null) {
					clientFixedIncomeDTO.setFixedDepositType(obj.getLookupFixedDepositType().getId());
				}
				ClientFamilyMember obj1 = obj.getClientFamilyMember();
				String ownerName = obj1.getFirstName() + " "
						+ (obj1.getMiddleName() == null ? "" : obj1.getMiddleName()) + " " + obj1.getLastName();
				clientFixedIncomeDTO.setOwnerName(ownerName);
								clientFIDTOList.add(clientFixedIncomeDTO);
			}
			
			
			return clientFIDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientFixedIncomeRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientFixedIncomeDTO findById(int id) throws RuntimeException {
		try {
			ClientFixedIncome clientFixedIncome = clientFixedIncomeRepository.findOne(id);
			
			ClientFixedIncomeDTO clientFixedIncomeDTO = mapper.map(clientFixedIncomeRepository.findOne(id),
					ClientFixedIncomeDTO.class);

			clientFixedIncomeDTO.setClientID(clientFixedIncome.getClientMaster().getId());
			clientFixedIncomeDTO.setFamilyMemberID(clientFixedIncome.getClientFamilyMember().getId());
			clientFixedIncomeDTO.setFinancialAssetType(clientFixedIncome.getMasterProductClassification().getId());
			clientFixedIncomeDTO
					.setFinancialAssetTypeName(clientFixedIncome.getMasterProductClassification().getProductName());
		
			double interestRate = clientFixedIncome.getInterestCouponRate().doubleValue() * 100;
			clientFixedIncomeDTO.setInterestCouponRate(new BigDecimal(interestRate));
			
			
			if (clientFixedIncomeDTO.getFinancialAssetType() == 23) {
				clientFixedIncomeDTO.setTenure(clientFixedIncome.getTenure());
				long[] data = new FinexaUtil().getYearCountByDay(clientFixedIncome.getInvestmentDepositDate(), clientFixedIncome.getTenure());
				clientFixedIncomeDTO.setTenureD((int)data[0]);
				clientFixedIncomeDTO.setTenureRDMonths((int)data[1]);
			}
			
			/*if (clientFixedIncome.getBankIssuerName() != null) {
				clientFixedIncomeDTO.setBankIssuerName(clientFixedIncome.getBankIssuerName());
			} else {
				clientFixedIncomeDTO.setBankIssuerName("");
			}*/
			if (clientFixedIncome.getLookupFixedDepositType() != null) {
				clientFixedIncomeDTO.setFixedDepositType(clientFixedIncome.getLookupFixedDepositType().getId());
			}
			if (clientFixedIncome.getLookupFrequency1() != null) {
				clientFixedIncomeDTO.setCompoundingFrequency(clientFixedIncome.getLookupFrequency1().getId());
			}
			if (clientFixedIncome.getLookupFrequency2() != null) {
				clientFixedIncomeDTO.setPayoutFrequency(clientFixedIncome.getLookupFrequency2().getId());
			}
			if (clientFixedIncome.getLookupFrequency3() != null) {
				
				clientFixedIncomeDTO.setRecurringDepositFrequency(clientFixedIncome.getLookupFrequency3().getId());
			}
			if (clientFixedIncome.getLookupBondType() != null) {
				clientFixedIncomeDTO.setBondType(clientFixedIncome.getLookupBondType().getId());
			}
			String bankIdString = clientFixedIncomeDTO.getBankIssuerName();
			try {
				int bankId = Integer.parseInt(bankIdString);
				MasterCash masterCash = masterCashRepository.findOne(bankId);
				clientFixedIncomeDTO.setBankIssuerName(masterCash.getName());
			} catch (Exception e) {
				// bankIssuerName is a String , no action needs to be taken
			}
			return clientFixedIncomeDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientFixedIncomeDTO update(ClientFixedIncomeDTO clientFixedIncomeDTO, HttpServletRequest request) throws RuntimeException {
		try {
			ClientFixedIncome clientFixedIncome = mapper.map(clientFixedIncomeDTO, ClientFixedIncome.class);

			ClientMaster cm = clientMasterRepository.findOne(clientFixedIncomeDTO.getClientID());
			clientFixedIncome.setClientMaster(cm);
			
			if (clientFixedIncomeDTO.getInterestCouponRate() != null) {
				double interestRate = clientFixedIncomeDTO.getInterestCouponRate().doubleValue() / 100;
				//System.out.println("interest coupon rate: " + BigDecimal.valueOf(interestRate));
				clientFixedIncome.setInterestCouponRate(BigDecimal.valueOf(interestRate));
			}else {
				BigDecimal v = BigDecimal.valueOf(0);
				clientFixedIncome.setInterestCouponRate(v);
			}
			
			if (clientFixedIncomeDTO.getTenureYearsDays().equalsIgnoreCase("M")) {
				clientFixedIncomeDTO.setTenureRDMonths(clientFixedIncome.getTenure());
			}
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientFixedIncomeDTO.getFinancialAssetType());
			clientFixedIncome.setMasterProductClassification(masterProductClassification);

			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository
					.findOne(clientFixedIncomeDTO.getFamilyMemberID());
			clientFixedIncome.setClientFamilyMember(clientFamilyMember);

			if (clientFixedIncomeDTO.getFixedDepositType() != 0) {
				LookupFixedDepositType lookupFixedDepositType = lookupFixedDepositTypeRepository
						.findOne(clientFixedIncomeDTO.getFixedDepositType());
				clientFixedIncome.setLookupFixedDepositType(lookupFixedDepositType);
			}

			if (clientFixedIncomeDTO.getCompoundingFrequency() != 0) {
				LookupFrequency lookupFrequency1 = frequencyRepository
						.findOne(clientFixedIncomeDTO.getCompoundingFrequency());
				clientFixedIncome.setLookupFrequency1(lookupFrequency1);
			}

			if (clientFixedIncomeDTO.getPayoutFrequency() != 0) {

				LookupFrequency lookupFrequency2 = frequencyRepository
						.findOne(clientFixedIncomeDTO.getPayoutFrequency());

				clientFixedIncome.setLookupFrequency2(lookupFrequency2);
			}

			if (clientFixedIncomeDTO.getRecurringDepositFrequency() != 0) {
				LookupFrequency lookupFrequency3 = frequencyRepository
						.findOne(clientFixedIncomeDTO.getRecurringDepositFrequency());

				clientFixedIncome.setLookupFrequency3(lookupFrequency3);
			}

			if (clientFixedIncomeDTO.getBondType() != 0 || clientFixedIncomeDTO.getBondType() != 0) {
				LookupBondType LookupBondType = lookupBondTypeRepository.findOne(clientFixedIncomeDTO.getBondType());

				clientFixedIncome.setLookupBondType(LookupBondType);
			}
			/*if (clientFixedIncomeDTO.getBondType() == 2) {
				if (clientFixedIncomeDTO.getMaturityDate() != null) {
					clientFixedIncome.setMaturityDate(clientFixedIncomeDTO.getMaturityDate());
				} else {
					clientFixedIncome.setMaturityDate(new Date(0));
				}
			}*/
			
			/*MasterCash masterCash;
			if (clientFixedIncomeDTO.getBankIssuerId() != null) {
				masterCash = masterCashRepository.findOne(clientFixedIncomeDTO.getBankIssuerId());
				clientFixedIncome.setMasterCash(masterCash);
			}*/
			
//			clientFixedIncomeDTO.setBankIssuerName(clientFixedIncome.getBankIssuerName());

			if (clientFixedIncomeDTO.getFinancialAssetType() == 27) {
				clientFixedIncomeDTO.setTenureYearsDays("D");
			}
			if (clientFixedIncomeDTO.getFinancialAssetType() == 23) {
				
				//System.out.println("days: " + clientFixedIncome.getTenure());
				
				clientFixedIncomeDTO.setTenure(clientFixedIncome.getTenure());
				
				
			}
			
			String bankIdString = clientFixedIncomeDTO.getBankIssuerName();
			try {
				int bankId = Integer.parseInt(bankIdString);
				MasterCash masterCash = masterCashRepository.findOne(bankId);
				clientFixedIncomeDTO.setBankIssuerName(masterCash.getName());
			} catch (Exception e) {
				// bankIssuerName is a String , no action needs to be taken
			}
			clientFixedIncome = clientFixedIncomeRepository.save(clientFixedIncome);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientFixedIncomeDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientFixedIncomeDTO> findAll() {
		List<ClientFixedIncome> listClientFixedIncome = clientFixedIncomeRepository.findAll();

		List<ClientFixedIncomeDTO> listDTO = new ArrayList<ClientFixedIncomeDTO>();
		for (ClientFixedIncome clientFixedIncome : listClientFixedIncome) {
			ClientFixedIncomeDTO dto = mapper.map(clientFixedIncome, ClientFixedIncomeDTO.class);
			String bankIdString = dto.getBankIssuerName();
			try {
				int bankId = Integer.parseInt(bankIdString);
				MasterCash masterCash = masterCashRepository.findOne(bankId);
				dto.setBankIssuerName(masterCash.getName());
			} catch (Exception e) {
				// bankIssuerName is a String , no action needs to be taken
			}
			dto.setClientID(clientFixedIncome.getClientMaster().getId());
			listDTO.add(dto);
		}

		return listDTO;
	}

}
