package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.ClientGoalDTO;
import com.finlabs.finexa.dto.MasterGoalInflationRateDTO;
import com.finlabs.finexa.dto.MasterSubAssetClassReturnDTO;
import com.finlabs.finexa.model.AdvisorProductReco;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupAssetSubClass;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.LookupGoalCorpusUtilizationFrequency;
import com.finlabs.finexa.model.LookupGoalType;
import com.finlabs.finexa.model.MasterGoalInflationRate;
import com.finlabs.finexa.model.MasterSubAssetClassReturn;
import com.finlabs.finexa.repository.AdvisorProductRecoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AdvisorUserSupervisorMappingRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientGoalRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.GoalFrequencyRepository;
import com.finlabs.finexa.repository.GoalTypeRepository;
import com.finlabs.finexa.repository.LookupAssetSubClassRepository;
import com.finlabs.finexa.repository.MasterGoalInflationRateRepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientGoalService")
@Transactional
public class ClientGoalServiceImpl implements ClientGoalService {
	private static Logger log = LoggerFactory.getLogger(ClientGoalServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientGoalRepository clientGoalRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;
	@Autowired
	private GoalTypeRepository goalTyperRepository;

	@Autowired
	private GoalFrequencyRepository goalFrequencyRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	/*@Autowired
	private FrequencyRepository frequencyRepository;*/

	@Autowired
	private LookupAssetSubClassRepository lookupAssetSubClassrepository;

	@Autowired
	private MasterGoalInflationRateRepository masterGoalInflationRateRepository;

	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	
	@Autowired
	private AdvisorUserSupervisorMappingRepository advisorUserSupervisorMappingRepository;
	
	@Autowired
	private ClientMasterRepository clientMasterRespository;
	
	@Autowired
	private AdvisorProductRecoRepository advisorProductRecoRepository;
	
	@Override
	public ClientGoalDTO save(ClientGoalDTO clientGoalDTO) throws RuntimeException {
		try {
			if (clientGoalDTO.getLookupGoalTypeId() == 2 || clientGoalDTO.getLookupGoalTypeId() == 3
					|| clientGoalDTO.getLookupGoalTypeId() == 4 || clientGoalDTO.getLookupGoalTypeId() == 5) {

				clientGoalDTO.setRecurringFlag("N");

			}

			if (clientGoalDTO.getLookupGoalTypeId() == 8) {
				clientGoalDTO.setDescription("Retirement Goal");
				clientGoalDTO.setRecurringFlag("Y");
				clientGoalDTO.setLoanRequiredFlag("N");

			}
			if (clientGoalDTO.getExpectedInflationRate() != null) {
				double ExpectedInflationRate = clientGoalDTO.getExpectedInflationRate() / 100;
				clientGoalDTO.setExpectedInflationRate(ExpectedInflationRate);
			}
			if (clientGoalDTO.getExpectedReturnOnCorpus() != null) {
				double returnOncorpus = clientGoalDTO.getExpectedReturnOnCorpus() / 100;
				clientGoalDTO.setExpectedReturnOnCorpus(returnOncorpus);
			}

			ClientGoal clientGoal = mapper.map(clientGoalDTO, ClientGoal.class);
			ClientMaster clientMaster = clientMasterRepository.findOne(clientGoalDTO.getClientId());
			clientGoal.setClientMaster(clientMaster);
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository
					.findOne(clientGoalDTO.getClientFamilyMemberId());
			clientGoal.setClientFamilyMember(clientFamilyMember);
			LookupGoalType goalType = goalTyperRepository.findOne(clientGoalDTO.getLookupGoalTypeId());
			clientGoal.setLookupGoalType(goalType);

			/*LookupFrequency lookupFrequency;*/
			LookupGoalCorpusUtilizationFrequency goalCorpusUtilizationFrequency;
			//System.out.println("clientGoalDTO.getLookupGoalTypeId() "+clientGoalDTO.getLookupGoalTypeId());
			if (clientGoalDTO.getLookupGoalTypeId() == 8) {
				goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne(clientGoalDTO.getPostRetirementExpectedPayoutFrequency());
				/*** commented by debolina as due to this code corpus frequency was getting saved as Monthly***/
				//goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne((byte) 1);
				/****** end of code modification ****************/
				clientGoalDTO.setLoanRequiredFlag("N");
				clientGoal.setEstimatedCostOfGoal(BigDecimal.valueOf(0.0));

			} else {
				goalCorpusUtilizationFrequency = null;
				goalCorpusUtilizationFrequency = goalFrequencyRepository
						.findOne(clientGoalDTO.getLookupGoalCorpusUtilizationFrequencyId());
				if (clientGoalDTO.getLookupGoalTypeId() == 1 || clientGoalDTO.getLookupGoalTypeId() == 6
						|| clientGoalDTO.getLookupGoalTypeId() == 7 || clientGoalDTO.getLookupGoalTypeId() == 9) {
					if (clientGoalDTO.getRecurringFlag().equalsIgnoreCase("N")) {
						goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne((byte) 5);
						clientGoal.setYearsToGoal((byte) 1); // for recurring goal made non recurring from UI, years to
																// Goal must be 1
					}
				}

				if (clientGoalDTO.getLoanRequiredFlag().equalsIgnoreCase("Y")) {
					double loanpercent = clientGoalDTO.getLoanPercent() / 100;
					clientGoal.setLoanPercent(Double.valueOf(loanpercent));
					double interestRate = clientGoalDTO.getLoanInterestRate() / 100;
					clientGoal.setLoanInterestRate(Double.valueOf(interestRate));
				} else {
					if (clientGoal.getId() != 0) {
						clientGoal.setLoanPercent(null);
						clientGoal.setLoanTenure(null);
						clientGoal.setLoanInterestRate(null);
					}
				}

			}
			/*clientGoal.setLookupFrequency(lookupFrequency);*/
			clientGoal.setLookupGoalCorpusUtilizationFrequencyForPayoutFrequency(goalCorpusUtilizationFrequency);
			clientGoal.setLookupGoalCorpusUtilizationFrequency(goalCorpusUtilizationFrequency);
			ClientGoal model = clientGoalRepository.save(clientGoal);
			ClientGoalDTO retDTO = mapper.map(model, ClientGoalDTO.class);
			retDTO.setClientId(model.getClientMaster().getId());
			return clientGoalDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ClientGoalDTO autoSave(ClientGoalDTO clientGoalDTO) throws RuntimeException {
//		try {
//			if (clientGoalDTO.getLookupGoalTypeId() == 2 || clientGoalDTO.getLookupGoalTypeId() == 3
//					|| clientGoalDTO.getLookupGoalTypeId() == 4 || clientGoalDTO.getLookupGoalTypeId() == 5) {
//
//				clientGoalDTO.setRecurringFlag("N");
//
//			}
//
//			if (clientGoalDTO.getLookupGoalTypeId() == 8) {
//				clientGoalDTO.setDescription("Retirement Goal");
//				clientGoalDTO.setRecurringFlag("Y");
//				clientGoalDTO.setLoanRequiredFlag("N");
//
//			}
//			if (clientGoalDTO.getExpectedInflationRate() != null) {
//				double ExpectedInflationRate = clientGoalDTO.getExpectedInflationRate() / 100;
//				clientGoalDTO.setExpectedInflationRate(ExpectedInflationRate);
//			}
//			if (clientGoalDTO.getExpectedReturnOnCorpus() != null) {
//				double returnOncorpus = clientGoalDTO.getExpectedReturnOnCorpus() / 100;
//				clientGoalDTO.setExpectedReturnOnCorpus(returnOncorpus);
//			}
//
//			ClientGoal clientGoal = mapper.map(clientGoalDTO, ClientGoal.class);
//			ClientMaster clientMaster = clientMasterRepository.findOne(clientGoalDTO.getClientId());
//			clientGoal.setClientMaster(clientMaster);
//			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository
//					.findOne(clientGoalDTO.getClientFamilyMemberId());
//			clientGoal.setClientFamilyMember(clientFamilyMember);
//			LookupGoalType goalType = goalTyperRepository.findOne(clientGoalDTO.getLookupGoalTypeId());
//			clientGoal.setLookupGoalType(goalType);
//
//			/*LookupFrequency lookupFrequency;*/
//			LookupGoalCorpusUtilizationFrequency goalCorpusUtilizationFrequency;
//			//System.out.println("clientGoalDTO.getLookupGoalTypeId() "+clientGoalDTO.getLookupGoalTypeId());
//			if (clientGoalDTO.getLookupGoalTypeId() == 8) {
//				goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne(clientGoalDTO.getPostRetirementExpectedPayoutFrequency());
//				clientGoalDTO.setLoanRequiredFlag("N");
//				clientGoal.setEstimatedCostOfGoal(BigDecimal.valueOf(0.0));
//
//			} else {
//				goalCorpusUtilizationFrequency = null;
//				//System.out.println("type "+clientGoalDTO.getLookupGoalTypeId());
//				//System.out.println("clientGoalDTO.getLookupGoalCorpusUtilizationFrequencyId() "+clientGoalDTO.getLookupGoalCorpusUtilizationFrequencyId());
//				goalCorpusUtilizationFrequency = goalFrequencyRepository
//						.findOne(clientGoalDTO.getLookupGoalCorpusUtilizationFrequencyId());
//				if (clientGoalDTO.getLookupGoalTypeId() == 1 || clientGoalDTO.getLookupGoalTypeId() == 6
//						|| clientGoalDTO.getLookupGoalTypeId() == 7 || clientGoalDTO.getLookupGoalTypeId() == 9) {
//					if (clientGoalDTO.getRecurringFlag().equalsIgnoreCase("N")) {
//						goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne((byte) 5);
//						clientGoal.setYearsToGoal((byte) 1); // for recurring goal made non recurring from UI, years to
//																// Goal must be 1
//					}
//				}
//
//				if (clientGoalDTO.getLoanRequiredFlag().equalsIgnoreCase("Y")) {
//					double loanpercent = clientGoalDTO.getLoanPercent() / 100;
//					clientGoal.setLoanPercent(Double.valueOf(loanpercent));
//					double interestRate = clientGoalDTO.getLoanInterestRate() / 100;
//					clientGoal.setLoanInterestRate(Double.valueOf(interestRate));
//				} else {
//					if (clientGoal.getId() != 0) {
//						clientGoal.setLoanPercent(null);
//						clientGoal.setLoanTenure(null);
//						clientGoal.setLoanInterestRate(null);
//					}
//				}
//
//			}
//			/*clientGoal.setLookupFrequency(lookupFrequency);*/
//			//System.out.println("type "+clientGoalDTO.getLookupGoalTypeId());
//			//System.out.println("goalCorpusUtilizationFrequency "+goalCorpusUtilizationFrequency);
//			clientGoal.setLookupGoalCorpusUtilizationFrequencyForPayoutFrequency(goalCorpusUtilizationFrequency);
//			clientGoal.setLookupGoalCorpusUtilizationFrequency(goalCorpusUtilizationFrequency);
//			clientGoal = clientGoalRepository.save(clientGoal);
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		}
//		return clientGoalDTO;	
		return null;
	}

	@Override
	public ClientGoalDTO update(ClientGoalDTO clientGoalDTO) throws RuntimeException {
		try {

			/*
			 * ClientMaster cm =
			 * clientMasterRepository.findOne(clientGoalDTO.getClientId());
			 * ClientFamilyMember fam =
			 * clientFamilyMemberRepository.findOne(clientGoalDTO.getClientFamilyMemberId())
			 * ; ClientGoal clientGoal = mapper.map(clientGoalDTO, ClientGoal.class);
			 * clientGoal.setClientMaster(cm); clientGoal.setClientFamilyMember(fam);
			 */
			// log.debug("save in service");
			if (clientGoalDTO.getLookupGoalTypeId() == 2 || clientGoalDTO.getLookupGoalTypeId() == 3
					|| clientGoalDTO.getLookupGoalTypeId() == 4 || clientGoalDTO.getLookupGoalTypeId() == 5) {

				clientGoalDTO.setRecurringFlag("N");

			}
			if (clientGoalDTO.getLookupGoalTypeId() == 8) {
				clientGoalDTO.setDescription("Retirement Goal");
				clientGoalDTO.setRecurringFlag("Y");
				clientGoalDTO.setLoanRequiredFlag("N");

				// byte cfi = 8;
				// clientGoalDTO.setLookupGoalCorpusUtilizationFrequencyId(cfi);

			}
			// log.debug("save in10");
			if (clientGoalDTO.getExpectedInflationRate() != null) {
				// log.debug("save in11
				// "+clientGoalDTO.getExpectedInflationRate());
				double ExpectedInflationRate = clientGoalDTO.getExpectedInflationRate() / 100;
				// log.debug("save in9 "+ExpectedInflationRate);
				clientGoalDTO.setExpectedInflationRate(ExpectedInflationRate);
			}
			if (clientGoalDTO.getExpectedReturnOnCorpus() != null) {
				double returnOncorpus = clientGoalDTO.getExpectedReturnOnCorpus() / 100;
				// log.debug("ss "+returnOncorpus);
				clientGoalDTO.setExpectedReturnOnCorpus(returnOncorpus);
			}
			// log.debug("save in8");

			ClientGoal clientGoal = mapper.map(clientGoalDTO, ClientGoal.class);
			// log.debug("save in6");
			ClientMaster clientMaster = clientMasterRepository.findOne(clientGoalDTO.getClientId());
			clientGoal.setClientMaster(clientMaster);
			// log.debug("save in5");
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository
					.findOne(clientGoalDTO.getClientFamilyMemberId());
			clientGoal.setClientFamilyMember(clientFamilyMember);
			// log.debug("save in4");
			LookupGoalType goalType = goalTyperRepository.findOne(clientGoalDTO.getLookupGoalTypeId());
			clientGoal.setLookupGoalType(goalType);
			// log.debug("save in3");

			// log.debug("save in2");
			//LookupFrequency lookupFrequency;
			LookupGoalCorpusUtilizationFrequency goalCorpusUtilizationFrequency;
			if (clientGoalDTO.getLookupGoalTypeId() == 8) {
				goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne(clientGoalDTO.getPostRetirementExpectedPayoutFrequency());
				/*** commented by debolina as due to this code corpus frequency was getting saved as Monthly***/
				//goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne((byte) 1);
				/****** end of code modification ****************/
				clientGoalDTO.setLoanRequiredFlag("N");
				clientGoal.setEstimatedCostOfGoal(BigDecimal.valueOf(0.0));
			} else {
				// lookupFrequency = frequencyRepository.findOne((byte) 12);
				goalCorpusUtilizationFrequency = null;
				goalCorpusUtilizationFrequency = goalFrequencyRepository
						.findOne(clientGoalDTO.getLookupGoalCorpusUtilizationFrequencyId());
				if (clientGoalDTO.getLookupGoalTypeId() == 1 || clientGoalDTO.getLookupGoalTypeId() == 6
						|| clientGoalDTO.getLookupGoalTypeId() == 7 || clientGoalDTO.getLookupGoalTypeId() == 9) {
					if (clientGoalDTO.getRecurringFlag().equalsIgnoreCase("N")) {
						goalCorpusUtilizationFrequency = goalFrequencyRepository.findOne((byte) 5);
						clientGoal.setYearsToGoal((byte) 1); // for recurring goal made non recurring from UI, years to
																// Goal must be 1
					}
				}
				/*
				 * goalCorpusUtilizationFrequency = goalFrequencyRepository
				 * .findOne(clientGoalDTO.getLookupGoalCorpusUtilizationFrequencyId());
				 */
				if (clientGoalDTO.getLoanRequiredFlag().equalsIgnoreCase("Y")) {
					double loanpercent = clientGoalDTO.getLoanPercent() / 100;
					clientGoal.setLoanPercent(Double.valueOf(loanpercent));
					// log.debug("save in7");
					double interestRate = clientGoalDTO.getLoanInterestRate() / 100;
					// log.debug("ss "+interestRate);
					clientGoal.setLoanInterestRate(Double.valueOf(interestRate));
				} else {
					if (clientGoal.getId() != 0) {
						clientGoal.setLoanPercent(null);
						clientGoal.setLoanTenure(null);
						clientGoal.setLoanInterestRate(null);
					}
				}

			}
			clientGoal.setLookupGoalCorpusUtilizationFrequencyForPayoutFrequency(goalCorpusUtilizationFrequency);
			clientGoal.setLookupGoalCorpusUtilizationFrequency(goalCorpusUtilizationFrequency);

			// log.debug("save in1 "+clientGoal.getLoanTenure());
			ClientGoal model = clientGoalRepository.save(clientGoal);

			ClientGoalDTO retDTO = mapper.map(model, ClientGoalDTO.class);
			retDTO.setClientId(model.getClientMaster().getId());
			return clientGoalDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientGoalDTO UpdatePriority(ClientGoalDTO clientGoalDTO) throws RuntimeException {
		try {
			ClientGoal clientGoal = clientGoalRepository.findOne(clientGoalDTO.getId());
			clientGoal.setPriority(clientGoalDTO.getPriority());
			ClientGoal model = clientGoalRepository.save(clientGoal);
			ClientGoalDTO retDTO = mapper.map(model, ClientGoalDTO.class);
			retDTO.setClientId(model.getClientMaster().getId());
			return retDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
/*	
 * Delete It
 * @Override
	public List<ClientGoalDTO> checkIfRetirementGoalPresentForAll(Integer clientId, Integer goalType)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {

			List<ClientGoal> clientGoalList = clientGoalRepository.getRetirementGoalForClient(clientId, goalType);

			List<ClientGoalDTO> goalDtoList = new ArrayList<ClientGoalDTO>();

			for (ClientGoal clientGoal : clientGoalList) {
				ClientGoalDTO clientGoalDTO = mapper.map(clientGoal, ClientGoalDTO.class);

				goalDtoList.add(clientGoalDTO);
			}

			log.debug("goalDtoList size: " + goalDtoList.size());

			return goalDtoList;

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	*/
	
	@Override
	public List<ClientGoalDTO> fetchExpiredGoals(int clientId) throws RuntimeException {
		//TODO: use Java to get Current date and then check if the current date is less than the goal startMonthYear, 
		Date currentDate = new Date();
		Calendar calenderFormatOfDate = Calendar.getInstance();
		calenderFormatOfDate.setTime(currentDate);
		
		String requiredDate = new String();
		
		/**
		 *  requiredDate is formatted in 'yyyy-mm-dd'
		 *  
		 */
		
		if( ((calenderFormatOfDate.get(Calendar.MONTH)+1) < 10 ) && ( (calenderFormatOfDate.get(Calendar.DAY_OF_MONTH)) < 10 ) ) {
			requiredDate = calenderFormatOfDate.get(Calendar.YEAR) + "-0" + (calenderFormatOfDate.get(Calendar.MONTH)+1) + "-0" + calenderFormatOfDate.get(Calendar.DAY_OF_MONTH);
		} else if( ((calenderFormatOfDate.get(Calendar.MONTH)+1) <10 ) && ( (calenderFormatOfDate.get(Calendar.DAY_OF_MONTH)) >= 10 ) ) {
			requiredDate = calenderFormatOfDate.get(Calendar.YEAR) + "-0" + (calenderFormatOfDate.get(Calendar.MONTH)+1) + "-" + calenderFormatOfDate.get(Calendar.DAY_OF_MONTH);
		} else if( ((calenderFormatOfDate.get(Calendar.MONTH)+1) >= 10 ) && ( (calenderFormatOfDate.get(Calendar.DAY_OF_MONTH)) < 10 ) ) {
			requiredDate = calenderFormatOfDate.get(Calendar.YEAR) + "-" + (calenderFormatOfDate.get(Calendar.MONTH)+1) + "-0" + calenderFormatOfDate.get(Calendar.DAY_OF_MONTH);
		} else {
			requiredDate = calenderFormatOfDate.get(Calendar.YEAR) + "-" + (calenderFormatOfDate.get(Calendar.MONTH)+1) + "-" + calenderFormatOfDate.get(Calendar.DAY_OF_MONTH);
		}
		
		ClientGoalDTO clientGoalDTO = null;
		List<ClientGoalDTO> clientGoalDTOList = new ArrayList<>();
		
		try {
			ClientMaster master = clientMasterRepository.findOne(clientId);
			List<ClientGoal> clientGoalList = clientGoalRepository.findByClientMasterAndStartMonthYearBefore(master, requiredDate);
			if (clientGoalList != null && clientGoalList.size() > 0) {
				for (ClientGoal clientGoal : clientGoalList) {
					clientGoalDTO = mapper.map(clientGoal, ClientGoalDTO.class);
					
					clientGoalDTOList.add(clientGoalDTO);
				}
			}
			
			return clientGoalDTOList;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		
	}	
	@Override
	public int delete(int id) throws RuntimeException {

		try {
			//first delete from advisorProductReco
			ClientGoal clientGoal = clientGoalRepository.findOne(id);
			List<AdvisorProductReco> advisorProductRecos = advisorProductRecoRepository.findByClientGoal(clientGoal);
			for (AdvisorProductReco obj : advisorProductRecos) {
				advisorProductRecoRepository.delete(obj);
			}
			//then from ClientGoal
			List<AdvisorProductReco> objList = advisorProductRecoRepository.findByClientGoal(clientGoal);
			if (objList == null) {
				clientGoalRepository.delete(id);
			} else {
				// delete the recommendations first and then delete
				for (AdvisorProductReco deleteObj : objList) {
					advisorProductRecoRepository.delete(deleteObj);
				}
				// Now delete the goal
				clientGoalRepository.delete(id);
			}
			
			
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientGoalDTO findById(int id) throws RuntimeException {
		try {
			ClientGoalDTO clientGoalDTO = mapper.map(clientGoalRepository.findOne(id), ClientGoalDTO.class);
			clientGoalDTO.setClientId(clientGoalRepository.findOne(id).getClientMaster().getId());
			clientGoalDTO.setClientFamilyMemberId(clientGoalRepository.findOne(id).getClientFamilyMember().getId());
			clientGoalDTO.setLookupGoalTypeId(clientGoalRepository.findOne(id).getLookupGoalType().getId());
			clientGoalDTO.setLookupGoalCorpusUtilizationFrequencyId(
					clientGoalRepository.findOne(id).getLookupGoalCorpusUtilizationFrequency().getId());
			/*if (clientGoalDTO.getLookupGoalTypeId() != 8) {
				clientGoalDTO.setLookupGoalCorpusUtilizationFrequencyId(
						clientGoalRepository.findOne(id).getLookupGoalCorpusUtilizationFrequency().getId());
			}*/
			//if (clientGoalDTO.getLookupGoalTypeId() == 8) {
				/*clientGoalDTO.setPostRetirementExpectedPayoutFrequency(
						clientGoalRepository.findOne(id).getLookupFrequency().getId());*/
				
				
			//}
			
		
			return clientGoalDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientGoalDTO> findByClientId(int clientId) throws RuntimeException {

		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			List<ClientGoalDTO> cfiDtoList = new ArrayList<ClientGoalDTO>();

			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				log.debug("aaaa " + clientFamilyMember.getId());
				// ClientFamilyMember clientFamilyMember =
				// clientFamilyMemberRepository.findOne(clientFamilyMember1.getId());
				ClientGoalDTO clientGoalDTO = null;

				log.debug("length  " + clientFamilyMember.getClientGoals().size());
				if (clientFamilyMember.getClientGoals().size() != 0) {
					for (ClientGoal clientGoal : clientFamilyMember.getClientGoals()) {
						log.debug("aaaa  " + clientGoal.getId());

						clientGoalDTO = mapper.map(clientGoal, ClientGoalDTO.class);

						clientGoalDTO.setLookupGoalTypeId(clientGoal.getLookupGoalType().getId());
						clientGoalDTO.setLookupGoalTypeName(clientGoal.getLookupGoalType().getDescription());
						clientGoalDTO.setClientId(clientGoal.getClientMaster().getId());
						clientGoalDTO.setClientFamilyMemberId(clientGoal.getClientFamilyMember().getId());

						BigDecimal lumpsum = clientGoalRepository.getLumpsumInvestmentForGoalPlanning(
								clientGoal.getClientMaster().getId(), clientGoal.getId(), "G");
						if (lumpsum != null) {
							clientGoalDTO.setLumpsum(lumpsum.doubleValue());
						}
						BigDecimal sip = clientGoalRepository.getSipInvestmentForGoalPlanning(
								clientGoal.getClientMaster().getId(), clientGoal.getId(), "G");
						if (sip != null) {
							clientGoalDTO.setSip(sip.doubleValue());
						}

						log.debug("goal type" + clientGoalDTO.getLookupGoalTypeId());
						if (clientGoalDTO.getLookupGoalTypeId() != 0) {
							cfiDtoList.add(clientGoalDTO);
							log.debug("size " + cfiDtoList.size());
						}
					
						BigDecimal corpusReqdAtGoalStartOutput = clientGoalRepository.getCorpusReqdAtGoalStartOutput(
								clientGoal.getClientMaster().getId(), clientGoal.getId());
						if (corpusReqdAtGoalStartOutput != null) {
							clientGoalDTO.setCorpusReqdAtGoalStartOutput(corpusReqdAtGoalStartOutput.doubleValue());
						}

					}
				}

				log.debug("size list" + cfiDtoList.size());
			}
			return cfiDtoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public int getMaxPriority(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			int priority = 0;
			if (clientMaster.getClientGoals().size() != 0) {
				for (ClientGoal clientGoal : clientMaster.getClientGoals()) {
					log.debug("aaaa  " + clientGoal.getPriority());
					if (clientGoal.getPriority() > priority) {
						priority = clientGoal.getPriority();
					}
				}
			}

			return (priority + 1);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getLifeExpectency(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			int lifeExpectencyforClient = 0, lifeExpectencyforSpouse = 0;
			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					lifeExpectencyforClient = clientFamilyMember.getLifeExpectancy();
				}
				if (clientFamilyMember.getLookupRelation().getId() == 1) {
					if (clientFamilyMember.getLifeExpectancy() != null) {
						lifeExpectencyforSpouse = clientFamilyMember.getLifeExpectancy();
					} else {
						lifeExpectencyforSpouse = 0;
					}

				}
			}
			if (lifeExpectencyforClient > lifeExpectencyforSpouse)
				return lifeExpectencyforClient;
			else
				return lifeExpectencyforSpouse;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getAge(int familymenberId) throws RuntimeException {
		try {
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(familymenberId);
			Date dob1 = clientFamilyMember.getBirthDate();

			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dob = formatter.format(dob1);

			// TAKE SUBSTRINGS OF THE DOB SO SPLIT OUT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			int yearDOB = Integer.parseInt(dob.substring(0, 4));
			int monthDOB = Integer.parseInt(dob.substring(5, 7));
			int dayDOB = Integer.parseInt(dob.substring(8, 10));

			// CALCULATE THE CURRENT YEAR, MONTH AND DAY
			// INTO SEPERATE VARIABLES
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			java.util.Date date = new java.util.Date();
			int thisYear = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("MM");
			date = new java.util.Date();
			int thisMonth = Integer.parseInt(dateFormat.format(date));

			dateFormat = new SimpleDateFormat("dd");
			date = new java.util.Date();
			int thisDay = Integer.parseInt(dateFormat.format(date));

			// CREATE AN AGE VARIABLE TO HOLD THE CALCULATED AGE
			// TO START WILL SET THE AGE EQUEL TO THE CURRENT YEAR MINUS THE
			// YEAR
			// OF THE DOB
			int age = thisYear - yearDOB;

			// IF THE CURRENT MONTH IS LESS THAN THE DOB MONTH
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth < monthDOB) {
				age = age - 1;
			}

			// IF THE MONTH IN THE DOB IS EQUAL TO THE CURRENT MONTH
			// THEN CHECK THE DAY TO FIND OUT IF THEY HAVE HAD THEIR
			// BIRTHDAY YET. IF THE CURRENT DAY IS LESS THAN THE DAY OF THE DOB
			// THEN REDUCE THE DOB BY 1 AS THEY HAVE NOT HAD THEIR
			// BIRTHDAY YET THIS YEAR
			if (thisMonth == monthDOB && thisDay < dayDOB) {
				age = age - 1;
			}
			return age;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientFamilyMemberDTO getDate(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			DateFormat df;
			Date clientDate1 = null, spouseDate3 = null, returnedDate = null;
			int lifeExpectencyforClient = 0, lifeExpectencyforMember = 0;
			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				log.debug("rid " + clientFamilyMember.getLookupRelation().getId());
				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					lifeExpectencyforClient = clientFamilyMember.getLifeExpectancy();
					Date birthDate = clientFamilyMember.getBirthDate();
					lifeExpectencyforClient = clientFamilyMember.getLifeExpectancy();

					log.debug("lf " + lifeExpectencyforClient);
					df = new SimpleDateFormat("yyyy-MM-dd");
					String bdate = df.format(birthDate);
					String year1 = bdate.substring(0, 4);
					String month = bdate.substring(5, 7);
					String day = bdate.substring(8, 10);
					int year = Integer.parseInt(year1);
					int year2 = year + lifeExpectencyforClient;
					log.debug("client year " + year2);
					String clientDate = year2 + "-" + month + "-" + day;
					log.debug("clientDate " + clientDate);
					try {
						clientDate1 = df.parse(clientDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.debug("clientDate1 " + clientDate1);

				}
				if (clientFamilyMember.getLookupRelation().getId() == 1) {

					Date birthDate = clientFamilyMember.getBirthDate();
					if (clientFamilyMember.getLifeExpectancy() != null) {
						lifeExpectencyforMember = clientFamilyMember.getLifeExpectancy();
					} else {
						lifeExpectencyforMember = 0;
					}

					log.debug("lf " + lifeExpectencyforMember);
					df = new SimpleDateFormat("yyyy-MM-dd");
					String bdate = df.format(birthDate);
					String year1 = bdate.substring(0, 4);
					String month = bdate.substring(5, 7);
					String day = bdate.substring(8, 10);
					int year = Integer.parseInt(year1);
					int year2 = year + lifeExpectencyforMember;
					log.debug("spouse year " + year2);
					String spouseDate11 = year2 + "-" + month + "-" + day;
					log.debug("spouse " + spouseDate11);
					try {
						spouseDate3 = df.parse(spouseDate11);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.debug("spouse " + spouseDate3);

				}

			}
			if (spouseDate3 == null) {
				returnedDate = clientDate1;
			}
			if (spouseDate3 != null) {
				if (clientDate1.after(spouseDate3)) {
					returnedDate = clientDate1;
				}

				if (clientDate1.before(spouseDate3)) {
					returnedDate = spouseDate3;
				}

				if (clientDate1.equals(spouseDate3)) {
					returnedDate = spouseDate3;
				}
			}

			log.debug("returnedDate " + returnedDate);

			ClientFamilyMemberDTO clientFamilyMemberDTO = new ClientFamilyMemberDTO();
			clientFamilyMemberDTO.setBirthDate(returnedDate);
			log.debug("returnedDate " + clientFamilyMemberDTO.getBirthDate());
			return clientFamilyMemberDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientGoalDTO getAgePlusRetirementAge(int familyMemberId) throws RuntimeException {
		try {
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(familyMemberId);
			DateFormat df;
			// Date agePlusRetirementAge = null;
			Byte retirementAge = 0;

			retirementAge = clientFamilyMember.getRetirementAge();

			if (retirementAge != null) {
				Date birthDate = clientFamilyMember.getBirthDate();

				log.debug("lf " + retirementAge);
				df = new SimpleDateFormat("yyyy-MM-dd");
				String bdate = df.format(birthDate);
				String year1 = bdate.substring(0, 4);
				String month = bdate.substring(5, 7);
				String day = "01";

				int year = Integer.parseInt(year1);
				int year2 = year + retirementAge;
				log.debug("client year " + year2);

				String agePlusRetirementAgeString = year2 + "-" + month + "-" + day;
				ClientGoalDTO clientGoalDTO = new ClientGoalDTO();
				clientGoalDTO.setStartMonthYear(agePlusRetirementAgeString);
				log.debug("agePlusRetirementAge " + clientGoalDTO.getDt());
				return clientGoalDTO;
			} else {
				log.debug("Client is retired!");
				return null;
			}

			/*
			 * log.debug("clientDate "+agePlusRetirementAge);
			 * 
			 * try { agePlusRetirementAge = df.parse(agePlusRetirementAgeString); } catch
			 * (ParseException e) { // TODO Auto-generated catch block e.printStackTrace();
			 * } log.debug("clientDate1 "+agePlusRetirementAge);
			 */
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public MasterGoalInflationRateDTO getInflationRate(byte goalTypeId, Date date) throws RuntimeException {

		try {
			MasterGoalInflationRate masterGoalInflationRate = masterGoalInflationRateRepository
					.findByGoalType(goalTypeId, date);

			MasterGoalInflationRateDTO retDTO = mapper.map(masterGoalInflationRate, MasterGoalInflationRateDTO.class);

			log.debug("rate " + retDTO.getInflationRate());

			return retDTO;
		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public MasterSubAssetClassReturnDTO getCorpusPostGoalStart() throws RuntimeException {

		try {
			LookupAssetSubClass lookupAssetSubClass = lookupAssetSubClassrepository.findByDescription("Cash/Liquid");

			MasterSubAssetClassReturn masterSubAssetClassReturn = lookupAssetSubClass.getMasterSubAssetClassReturns();

			MasterSubAssetClassReturnDTO retDTO = mapper.map(masterSubAssetClassReturn,
					MasterSubAssetClassReturnDTO.class);
			retDTO.setLookupAssetSubClassId(masterSubAssetClassReturn.getLookupAssetSubClass().getId());
			log.debug("rate " + retDTO.getMlr());

			return retDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<ClientGoalDTO> findAllUpcomingGoalsByClientID(int userId, int timeperiod) {
		// TODO Auto-generated method stub
		List<ClientGoal> upcaomingGoalList = null;
		List<ClientGoalDTO> upcaomingGoalListDTO = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date utilFromDate = null;
			Date utilTODate = null;
			Date goalDate = null;

			AdvisorUser user = advisorUserRepository.findOne(userId);
			//====  new get client List  =======
			FinexaUtil finexaUtil = new FinexaUtil();
			List<ClientMaster> clientMasterList = finexaUtil.findAllClientByUserHierarchy(user,advisorUserSupervisorMappingRepository,clientMasterRespository);
			//===== end client list  ====
			
			upcaomingGoalList = new ArrayList<ClientGoal>();
			upcaomingGoalListDTO = new ArrayList<ClientGoalDTO>();
			
			Calendar cal = null;
			cal=Calendar.getInstance();
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			
			utilFromDate = cal.getTime();
		
			
		     //in below line of code, date is in which which you want to add 7 number of days
	        
			//List<ClientMaster> clientMasterList = user.getClientMasters();
			if (timeperiod == 7) {	
		         cal.add(Calendar.DATE, 7);
		  }		
			if (timeperiod == 8) {	
		         cal.add(Calendar.DATE, 14);
		  }
			if (timeperiod == 9) {
		        cal.add(Calendar.MONTH, 1);
		  }
			if (timeperiod == 10) {		
		        cal.add(Calendar.MONTH, 3);	   
		  }
			if (timeperiod == 11) {
		        cal.add(Calendar.MONTH, 6);
			 }
		  
			if (timeperiod == 12) {
				cal.add(Calendar.YEAR, 1);
			}
		  

			    utilTODate = cal.getTime();
				for (ClientMaster c : clientMasterList) {
					if(c.getActiveFlag().equals("Y")) {
			        List<ClientGoal> clientGoalList = c.getClientGoals();
			        for(ClientGoal goal:clientGoalList) {
			         if(goal.getStartMonthYear() != null) {
			          goalDate = dateFormat.parse(goal.getStartMonthYear());
			         
					//upcaomingGoalList.addAll(clientGoalRepository.getUpcomingGoalsForNext1Week(c.getId()));
			        if ((goalDate.after(utilFromDate))
							&& goalDate.before(utilTODate)
							|| goalDate.equals(utilFromDate)
							|| goalDate.equals(utilTODate)) {
			        	  
			        	upcaomingGoalList.add(goal);
			          }
			        }
				  }
			    }
		      }
			//log.debug("size " + upcaomingGoalList.size());

			for (ClientGoal clientGoal : upcaomingGoalList) {
				ClientGoalDTO retDTO = mapper.map(clientGoal, ClientGoalDTO.class);
				retDTO.setClientId(clientGoal.getClientMaster().getId());

				log.debug("clientGoal.getClientMaster().getId() " + clientGoal.getClientMaster().getId());
				/*
				 * BigDecimal lumpsum = clientGoalRepository
				 * .getLumpsumInvestmentForGoalPlanning(clientGoal.getClientMaster().getId(),
				 * clientGoal.getId(), "G"); retDTO.setLumpsum(lumpsum.doubleValue());
				 */
				retDTO.setName(
						clientGoal.getClientMaster().getFirstName() + " " + clientGoal.getClientMaster().getMiddleName()
								+ " " + clientGoal.getClientMaster().getLastName());
				retDTO.setLookupGoalTypeName(clientGoal.getLookupGoalType().getDescription());

				BigDecimal lumpsum = clientGoalRepository.getLumpsumInvestmentForGoalPlanning(
						clientGoal.getClientMaster().getId(), clientGoal.getId(), "G");
				if (lumpsum != null) {
					retDTO.setLumpsum(lumpsum.doubleValue());
				}
				BigDecimal sip = clientGoalRepository
						.getSipInvestmentForGoalPlanning(clientGoal.getClientMaster().getId(), clientGoal.getId(), "G");
				if (sip != null) {
					retDTO.setSip(sip.doubleValue());
				}
				BigDecimal corpusReqdAtGoalStartOutput = clientGoalRepository.getCorpusReqdAtGoalStartOutput(
						clientGoal.getClientMaster().getId(), clientGoal.getId());
				if (corpusReqdAtGoalStartOutput != null) {
					retDTO.setCorpusReqdAtGoalStartOutput(corpusReqdAtGoalStartOutput.doubleValue());
				}
				upcaomingGoalListDTO.add(retDTO);

			}

			Collections.sort(upcaomingGoalListDTO, new Comparator<ClientGoalDTO>() {
				public int compare(ClientGoalDTO o1, ClientGoalDTO o2) {
					return (o1.getStartMonthYear()).compareTo(o2.getStartMonthYear());
				}
			});
			return upcaomingGoalListDTO;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		

	}

	@Override
	public List<ClientGoalDTO> checkIfRetirementGoalPresentForAll(Integer clientId, Integer goalType)
			throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster clientMaster = null;
			LookupGoalType lookupGoalType = null;
			List<ClientGoalDTO> goalDtoList = new ArrayList<ClientGoalDTO>();
			
			try {
				clientMaster = clientMasterRepository.findOne(clientId);
				lookupGoalType = goalTyperRepository.findOne((byte) 8);
				List<ClientGoal> clientGoalList = clientGoalRepository.findByClientMasterAndLookupGoalType(clientMaster, lookupGoalType);
				

				for (ClientGoal clientGoal : clientGoalList) {
					ClientGoalDTO clientGoalDTO = mapper.map(clientGoal, ClientGoalDTO.class);

					goalDtoList.add(clientGoalDTO);
				}

				log.debug("goalDtoList size: " + goalDtoList.size());

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return goalDtoList;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateClientGoal(ClientExpense clientExpense) {

		clientGoalRepository.updateRetirementGoalExpense(FinexaConstant.GOAL_TYPE_RETIREMENT,
				clientExpense.getClientMaster().getId(),
				clientExpense.getExpenseAmount().doubleValue() * FinexaConstant.FREQUENCY_MONTHLY_ID);

	}

	@Override
	public boolean checkIfRetirementGoalExists(int clientID) throws RuntimeException {

		long x = 0;
		ClientMaster clientMaster;
		LookupGoalType lookupGoalType;
		// int
		// x=clientGoalRepository.getRetirementGoal(clientID)==null?0:clientGoalRepository.getRetirementGoal(clientID);
		//int x = clientGoalRepository.getRetirementGoal(clientID);
		try {
			clientMaster = clientMasterRepository.findOne(clientID);
			lookupGoalType = goalTyperRepository.findOne((byte) 8);
			x = clientGoalRepository.countByClientMasterAndLookupGoalType(clientMaster, lookupGoalType);
			//System.out.println("value : " + x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (x > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void updateGoalStartMonthYear(ClientMaster clientMaster) {
		// TODO Auto-generated method stub
		DateFormat df;

		Byte retirementAge = 0;

		retirementAge = clientMaster.getRetirementAge();

		Date dob = clientMaster.getBirthDate();
		df = new SimpleDateFormat("yyyy-MM-dd");

		String bdate = df.format(dob);
		String year1 = bdate.substring(0, 4);
		String month = bdate.substring(5, 7);
		String day = "01";

		int year = Integer.parseInt(year1);
		int year2 = year + retirementAge;

		String agePlusRetirementAgeString = year2 + "-" + month + "-" + day;
		
		clientGoalRepository.updateGoalStartMonthYear(agePlusRetirementAgeString, clientMaster.getId());

	}

}
