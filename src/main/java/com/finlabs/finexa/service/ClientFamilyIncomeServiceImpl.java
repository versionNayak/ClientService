package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientFamilyIncomeDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.LifeExpectancyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.LookupIncomeCategory;
import com.finlabs.finexa.model.LookupIncomeExpenseDuration;
import com.finlabs.finexa.model.LookupMonth;
import com.finlabs.finexa.repository.ClientFamilyIncomeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientGoalRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.IncomeCategoryRepository;
import com.finlabs.finexa.repository.IncomeExpenseDurationRepository;
import com.finlabs.finexa.repository.MonthRepository;
import com.finlabs.finexa.util.FinexaConstant;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientFamilyIncomeService")
@Transactional
public class ClientFamilyIncomeServiceImpl implements ClientFamilyIncomeService {
	private static Logger log = LoggerFactory.getLogger(ClientFamilyIncomeServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientFamilyIncomeRepository cientFamilyIncomeRepository;
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private FrequencyRepository frequencyRepository;

	@Autowired
	private MonthRepository monthRepository;

	@Autowired
	private IncomeExpenseDurationRepository incomeExpenseDurationRepository;

	@Autowired
	private ClientLifeExpectancyService ClientLifeExpectancyService;

	@Autowired
	private ClientFamilyMemberService clientFamilyMemberService;

	@Autowired
	private IncomeCategoryRepository incomeCategoryRepository;
	
	@Autowired
	AdvisorService advisorService;
	
	@Override
	public void save(List<ClientFamilyIncomeDTO> clientFamilyIncomeList, HttpServletRequest request)
			throws RuntimeException, CustomFinexaException {

		try {
			Byte inputType = 8;
			ClientFamilyMember clientFamilyMember = null;
			ClientFamilyIncome clientFamilyIncome = null;
			// int fid = 0;
			double totalIncomeAdded = 0.0;
			ClientMaster clientMaster = clientMasterRepository.findOne(clientFamilyIncomeList.get(0).getClientId());
			for (ClientFamilyIncomeDTO clientFamilyIncomeDTO : clientFamilyIncomeList) {

				// log.debug("frequency dto" + clientFamilyIncomeDTO.getIncomeFrequency());
				// log.debug("year dto" + clientFamilyIncomeDTO.getIncomeEndYear());
				// log.debug("month dto" + clientFamilyIncomeDTO.getReferenceMonth());
				clientFamilyIncome = mapper.map(clientFamilyIncomeDTO, ClientFamilyIncome.class);

				clientFamilyIncome.setClientMaster(clientMaster);
				if (clientFamilyIncomeDTO.getIncomeFrequency() != null) {
					LookupFrequency lookupFrequency = frequencyRepository
							.findOne(clientFamilyIncomeDTO.getIncomeFrequency());
					clientFamilyIncome.setLookupFrequency(lookupFrequency);
				}
				if (clientFamilyIncomeDTO.getReferenceMonth() != null) {
					LookupMonth lookupMonth = monthRepository.findOne(clientFamilyIncomeDTO.getReferenceMonth());
					clientFamilyIncome.setLookupMonth(lookupMonth);
				}
				if (clientFamilyIncomeDTO.getIncomeEndYear() != null) {
					LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
							.findOne(clientFamilyIncomeDTO.getIncomeEndYear());
					clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);
				}

				clientFamilyMember = clientFamilyMemberRepository.findOne(clientFamilyIncomeDTO.getFamilyMemberId());
				clientFamilyIncome.setClientFamilyMember(clientFamilyMember);

				/*
				 * if (clientFamilyIncomeDTO.getId() == 0) { if
				 * (clientFamilyIncomeDTO.getIncomeAmount().doubleValue() != 0.0) {
				 * cientFamilyIncomeRepository.save(clientFamilyIncome);
				 * 
				 * } } else{
				 */
				if ("I".equals(clientFamilyIncomeDTO.getOption())) {
					boolean b = checkIfTotalIncomeHeadPresent(clientFamilyIncomeDTO.getClientId(),
							clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					// log.debug("b " + b);
					if (b) {
						deleteTotalIncome(clientFamilyIncomeDTO.getClientId(),
								clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					}
				} else {
					// log.debug("aaaaa");
					boolean b = checkIfIndividualIncomeHeadPresent(clientFamilyIncomeDTO.getClientId(),
							clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					// log.debug("b " + b);
					if (b) {
						deleteIndividiualIncome(clientFamilyIncomeDTO.getClientId(),
								clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					}

					/*
					 * DateTime dt = new DateTime(); int month = dt.getMonthOfYear();
					 */

					LookupMonth lookupMonth1 = monthRepository.findOne((byte) 13);
					clientFamilyIncome.setLookupMonth(lookupMonth1);

				}

				if (clientFamilyIncomeDTO.getIncomeAmount() != null
						&& clientFamilyIncomeDTO.getIncomeAmount().doubleValue() != 0) {
					// log.debug("not zero");

					// log.debug("freq model" + clientFamilyIncome.getLookupFrequency().getId());
					// log.debug("month model" + clientFamilyIncome.getLookupMonth().getId());
					// log.debug("year model" +
					// clientFamilyIncome.getLookupIncomeExpenseDuration().getId());

					clientFamilyIncome = cientFamilyIncomeRepository.save(clientFamilyIncome);

					totalIncomeAdded = totalIncomeAdded + clientFamilyIncome.getIncomeAmount().doubleValue()
							* clientFamilyIncome.getLookupFrequency().getId();

					// log.debug("after edit size " +
					// clientFamilyMember.getClientFamilyIncomes().size());

				}

				else {
					// log.debug(" zero");
					deleteIncomeByIncometype(clientFamilyIncomeDTO.getClientId(),
							clientFamilyIncomeDTO.getFamilyMemberId(), clientFamilyIncomeDTO.getIncomeType());
					// log.debug(" deleted");
				}

			}

			// log.debug("total amount " + totalIncomeAdded);

			// log.debug("clientFamilyMember.getClientMaster().getId() " +
			// clientFamilyMember.getClientMaster().getId());
			// log.debug("clientFamilyMember.getId() " + clientFamilyMember.getId());
			if (clientFamilyMember.getLifeExpectancy() != null) {
				LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();

				// log.debug("totalIncomeAdded " + totalIncomeAdded);
				lifeExpectancyDTO.setAnnualIncome(totalIncomeAdded);
				/*
				 * List<ClientFamilyIncomeDTO>
				 * lstIncome=getAllIncomeForFamilyMember(clientFamilyMember.
				 * getClientMaster().getId(),clientFamilyMember.getId()); if
				 * (!lstIncome.isEmpty()) { int s=lstIncome.size(); log.debug("s "+s);
				 * log.debug("lstIncome.get(s-1).getTotal().doubleValue() "
				 * +lstIncome.get(s-1).getTotal().doubleValue());
				 * 
				 * 
				 * }
				 */

				lifeExpectancyDTO.setId(clientFamilyMember.getClientMaster().getId());
				lifeExpectancyDTO.setFamilyMemberId(clientFamilyMember.getId());
				lifeExpectancyDTO.setBirthDate(clientFamilyMember.getBirthDate());
				String gender = FinexaUtil.getGender(clientFamilyMember.getClientMaster(),
						clientFamilyMember.getLookupRelation());
				// log.debug("gender " + gender);
				lifeExpectancyDTO.setGender(gender);
				lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
				lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
				lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
				lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
				lifeExpectancyDTO = ClientLifeExpectancyService.calculateLifeExp(lifeExpectancyDTO);
				clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());

				ClientFamilyMemberDTO clientFamilyMemberDTO = mapper.map(clientFamilyMember,
						ClientFamilyMemberDTO.class);
				clientFamilyMemberDTO.setClientID(clientFamilyMember.getClientMaster().getId());
				clientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());
				clientFamilyMemberDTO.setTotalAmount(totalIncomeAdded);
				// log.debug("after added total income " +
				// clientFamilyMemberDTO.getTotalAmount());
				clientFamilyMemberService.save(clientFamilyMemberDTO, request);
			}
			advisorService.deletetAUMCacheMap(clientMaster.getAdvisorUser().getId(),request);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientFamilyIncomeDTO autoSave(ClientFamilyIncomeDTO clientFamilyIncomeDTO)
			throws RuntimeException, CustomFinexaException {

		try {
			
			ClientFamilyMember clientFamilyMember = null;
			ClientFamilyIncome clientFamilyIncome = null;
			ClientMaster clientMaster = null;
			
			clientFamilyIncome = mapper.map(clientFamilyIncomeDTO, ClientFamilyIncome.class);

			clientMaster = clientMasterRepository.findOne(clientFamilyIncomeDTO.getClientId());
			clientFamilyIncome.setClientMaster(clientMaster);


			int incometype = clientFamilyIncomeDTO.getIncomeType();
			clientFamilyIncome.setIncomeType((byte) incometype);
			
			clientFamilyIncome.setIncomeAmount(clientFamilyIncomeDTO.getIncomeAmount());

			LookupMonth lookupMonth = monthRepository.findOne(clientFamilyIncomeDTO.getReferenceMonth());
			clientFamilyIncome.setLookupMonth(lookupMonth);
			
			LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository.findOne(clientFamilyIncomeDTO.getIncomeEndYear());
			clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);

			LookupFrequency lookupFrequency = frequencyRepository.findOne(clientFamilyIncomeDTO.getIncomeFrequency());
			clientFamilyIncome.setLookupFrequency(lookupFrequency);

			clientFamilyMember = clientFamilyMemberRepository.findOne(clientFamilyIncomeDTO.getFamilyMemberId());
			clientFamilyIncome.setClientFamilyMember(clientFamilyMember);
			
			clientFamilyIncome = cientFamilyIncomeRepository.save(clientFamilyIncome);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return clientFamilyIncomeDTO;

	}

	@Override
	public void update(List<ClientFamilyIncomeDTO> clientFamilyIncomeList, HttpServletRequest request)
			throws RuntimeException, CustomFinexaException {
		try {
			Byte inputType = 8;
			ClientFamilyMember clientFamilyMember = null;
			ClientFamilyIncome clientFamilyIncome = null;
			// int fid = 0;
			double totalIncomeAdded = 0.0;
			ClientMaster clientMaster = clientMasterRepository.findOne(clientFamilyIncomeList.get(0).getClientId());
			for (ClientFamilyIncomeDTO clientFamilyIncomeDTO : clientFamilyIncomeList) {

				// log.debug("frequency dto" + clientFamilyIncomeDTO.getIncomeFrequency());
				// log.debug("year dto" + clientFamilyIncomeDTO.getIncomeEndYear());
				// log.debug("month dto" + clientFamilyIncomeDTO.getReferenceMonth());
				clientFamilyIncome = mapper.map(clientFamilyIncomeDTO, ClientFamilyIncome.class);

				// ClientMaster clientMaster =
				// clientMasterRepository.findOne(clientFamilyIncomeDTO.getClientId());
				clientFamilyIncome.setClientMaster(clientMaster);

				if (clientFamilyIncomeDTO.getIncomeFrequency() != null) {
					LookupFrequency lookupFrequency = frequencyRepository
							.findOne(clientFamilyIncomeDTO.getIncomeFrequency());
					clientFamilyIncome.setLookupFrequency(lookupFrequency);
				}
				if (clientFamilyIncomeDTO.getReferenceMonth() != null) {
					LookupMonth lookupMonth = monthRepository.findOne(clientFamilyIncomeDTO.getReferenceMonth());
					clientFamilyIncome.setLookupMonth(lookupMonth);
				}
				if (clientFamilyIncomeDTO.getIncomeEndYear() != null) {
					LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
							.findOne(clientFamilyIncomeDTO.getIncomeEndYear());
					clientFamilyIncome.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);
				}
				clientFamilyMember = clientFamilyMemberRepository.findOne(clientFamilyIncomeDTO.getFamilyMemberId());
				clientFamilyIncome.setClientFamilyMember(clientFamilyMember);

				// Integer inputType = clientFamilyIncomeDTO.getIncomeType();

				/*
				 * if (clientFamilyIncomeDTO.getId() == 0) { if
				 * (clientFamilyIncomeDTO.getIncomeAmount().doubleValue() != 0.0) {
				 * cientFamilyIncomeRepository.save(clientFamilyIncome);
				 * 
				 * } } else{
				 */
				if ("I".equals(clientFamilyIncomeDTO.getOption())) {
					boolean b = checkIfTotalIncomeHeadPresent(clientFamilyIncomeDTO.getClientId(),
							clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					// log.debug("b " + b);
					if (b) {
						deleteTotalIncome(clientFamilyIncomeDTO.getClientId(),
								clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					}
				} else {
					// log.debug("aaaaa");
					boolean b = checkIfIndividualIncomeHeadPresent(clientFamilyIncomeDTO.getClientId(),
							clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					// log.debug("b " + b);
					if (b) {
						// inputType = clientFamilyIncomeDTO.getIncomeType();
						deleteIndividiualIncome(clientFamilyIncomeDTO.getClientId(),
								clientFamilyIncomeDTO.getFamilyMemberId(), inputType);
					}

					/*
					 * DateTime dt = new DateTime(); int month = dt.getMonthOfYear();
					 */

					LookupMonth lookupMonth1 = monthRepository.findOne((byte) 13);
					clientFamilyIncome.setLookupMonth(lookupMonth1);

				}

				if (clientFamilyIncomeDTO.getIncomeAmount() != null
						&& clientFamilyIncomeDTO.getIncomeAmount().doubleValue() != 0) {
					// log.debug("not zero");

					// log.debug("freq model" + clientFamilyIncome.getLookupFrequency().getId());
					// log.debug("month model" + clientFamilyIncome.getLookupMonth().getId());
					// log.debug("year model" +
					// clientFamilyIncome.getLookupIncomeExpenseDuration().getId());

					clientFamilyIncome = cientFamilyIncomeRepository.save(clientFamilyIncome);

					totalIncomeAdded = totalIncomeAdded + clientFamilyIncome.getIncomeAmount().doubleValue()
							* clientFamilyIncome.getLookupFrequency().getId();

					// log.debug("after edit size " +
					// clientFamilyMember.getClientFamilyIncomes().size());

				}

				else {
					// log.debug(" zero");
					deleteIncomeByIncometype(clientFamilyIncomeDTO.getClientId(),
							clientFamilyIncomeDTO.getFamilyMemberId(), clientFamilyIncomeDTO.getIncomeType());
					// log.debug(" deleted");
				}

			}

			// log.debug("total amount " + totalIncomeAdded);

			// log.debug("clientFamilyMember.getClientMaster().getId() " +
			// clientFamilyMember.getClientMaster().getId());
			// log.debug("clientFamilyMember.getId() " + clientFamilyMember.getId());
			if (clientFamilyMember.getLifeExpectancy() != null) {
				LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();

				// log.debug("totalIncomeAdded " + totalIncomeAdded);
				lifeExpectancyDTO.setAnnualIncome(totalIncomeAdded);
				/*
				 * List<ClientFamilyIncomeDTO>
				 * lstIncome=getAllIncomeForFamilyMember(clientFamilyMember.
				 * getClientMaster().getId(),clientFamilyMember.getId()); if
				 * (!lstIncome.isEmpty()) { int s=lstIncome.size(); log.debug("s "+s);
				 * log.debug("lstIncome.get(s-1).getTotal().doubleValue() "
				 * +lstIncome.get(s-1).getTotal().doubleValue());
				 * 
				 * 
				 * }
				 */

				lifeExpectancyDTO.setId(clientFamilyMember.getClientMaster().getId());
				lifeExpectancyDTO.setFamilyMemberId(clientFamilyMember.getId());
				lifeExpectancyDTO.setBirthDate(clientFamilyMember.getBirthDate());
				String gender = FinexaUtil.getGender(clientFamilyMember.getClientMaster(),
						clientFamilyMember.getLookupRelation());
				// log.debug("gender " + gender);
				lifeExpectancyDTO.setGender(gender);
				lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
				lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
				lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
				lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
				lifeExpectancyDTO = ClientLifeExpectancyService.calculateLifeExp(lifeExpectancyDTO);
				clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO.getTotalLifeExpectancy());

				ClientFamilyMemberDTO clientFamilyMemberDTO = mapper.map(clientFamilyMember,
						ClientFamilyMemberDTO.class);
				clientFamilyMemberDTO.setClientID(clientFamilyMember.getClientMaster().getId());
				clientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());
				clientFamilyMemberDTO.setTotalAmount(totalIncomeAdded);
				// log.debug("after added total income " +
				// clientFamilyMemberDTO.getTotalAmount());
				clientFamilyMemberService.save(clientFamilyMemberDTO, request);		
			}
		 advisorService.deletetAUMCacheMap(clientMaster.getAdvisorUser().getId(),request);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientFamilyIncomeDTO> getAllFamilyIncome(Integer clientId) throws RuntimeException {

		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			List<ClientFamilyIncomeDTO> cfiDtoList = new ArrayList<ClientFamilyIncomeDTO>();

			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				// log.debug("aaaa " + clientFamilyMember.getId());
				// ClientFamilyMember clientFamilyMember =
				// clientFamilyMemberRepository.findOne(clientFamilyMember1.getId());
				ClientFamilyIncomeDTO clientFamilyIncomeDTO = null;
				double TotalIncome = 0;
				// log.debug("length income " +
				// clientFamilyMember.getClientFamilyIncomes().size());
				if (clientFamilyMember.getClientFamilyIncomes().size() != 0) {
					for (ClientFamilyIncome clientFamilyIncome : clientFamilyMember.getClientFamilyIncomes()) {
						// log.debug("aaaa income " + clientFamilyIncome.getId());
						TotalIncome = TotalIncome + clientFamilyIncome.getIncomeAmount().doubleValue()
								* clientFamilyIncome.getLookupFrequency().getId();

						// log.debug("total Income " + TotalIncome);

						clientFamilyIncomeDTO = mapper.map(clientFamilyIncome, ClientFamilyIncomeDTO.class);

						clientFamilyIncomeDTO.setTotal(BigDecimal.valueOf(TotalIncome));
						clientFamilyIncomeDTO.setFirstName(clientFamilyMember.getFirstName());
						clientFamilyIncomeDTO.setMiddleName(clientFamilyMember.getMiddleName());
						clientFamilyIncomeDTO.setLastName(clientFamilyMember.getLastName());
						clientFamilyIncomeDTO.setClientId(clientId);
						clientFamilyIncomeDTO.setFamilyMemberId(clientFamilyMember.getId());
						clientFamilyIncomeDTO.setIncomeFrequency(clientFamilyIncome.getLookupFrequency().getId());
						clientFamilyIncomeDTO
								.setIncomeEndYear(clientFamilyIncome.getLookupIncomeExpenseDuration().getId());
						clientFamilyIncomeDTO.setReferenceMonth(clientFamilyIncome.getLookupMonth().getId());

						// log.debug("month " + clientFamilyIncomeDTO.getReferenceMonth());
						// log.debug("year " + clientFamilyIncomeDTO.getReferenceMonth());
						// log.debug("freq " + clientFamilyIncomeDTO.getIncomeFrequency());

						if (clientFamilyIncomeDTO.getIncomeType() >= 1 && clientFamilyIncomeDTO.getIncomeType() <= 7)
							clientFamilyIncomeDTO.setOption("I");
						else
							clientFamilyIncomeDTO.setOption("T");

					}
					// if(clientFamilyIncomeDTO.getTotal().doubleValue()!=0){
					cfiDtoList.add(clientFamilyIncomeDTO);
					// }

				}
			}
			// log.debug("income list" + cfiDtoList.size());
			return cfiDtoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	/*
	 * @Override public List<ClientFamilyIncomeDTO> getAllFamilyIncome1(int
	 * clientId){
	 * 
	 * List<ClientFamilyIncome>
	 * cfiList=cientFamilyIncomeRepository.getAllFamilyIncome(clientId);
	 * 
	 * List<ClientFamilyIncomeDTO> cfiDtoList=new
	 * ArrayList<ClientFamilyIncomeDTO>(); ClientFamilyIncomeDTO
	 * clientFamilyIncomeDTO; for (ClientFamilyIncome clientFamilyIncome : cfiList)
	 * { clientFamilyIncomeDTO = mapper.map(clientFamilyIncome,
	 * ClientFamilyIncomeDTO.class);
	 * clientFamilyIncomeDTO.setIncomeAmount(clientFamilyIncome.getIncomeAmount( ));
	 * cfiDtoList.add(clientFamilyIncomeDTO); }
	 * 
	 * return cfiDtoList;
	 * 
	 * 
	 * }
	 */
	@Override
	public boolean checkIfIncomePresent(Integer clientId) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

		List<ClientFamilyIncome> ClientFamilyIncomeList = clientMaster.getClientFamilyIncomes();
		if (ClientFamilyIncomeList.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<ClientFamilyMemberDTO> checkIfIncomePresentForAll(Integer clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMember> ClientFamilyMembers = clientMaster.getClientFamilyMembers();

			List<ClientFamilyIncome> ClientFamilyIncomes = clientMaster.getClientFamilyIncomes();

			List<ClientFamilyMember> ClientFamilyMembers1 = new ArrayList<ClientFamilyMember>();

			for (ClientFamilyIncome cf : ClientFamilyIncomes) {

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

			return cfmDtoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<ClientFamilyIncomeDTO> getAllIncomeForFamilyMember(Integer clientId, Integer familyMemberId)
			throws RuntimeException {
		try {
			double total = 0;
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(familyMemberId);

			List<ClientFamilyIncomeDTO> listDTO = new ArrayList<ClientFamilyIncomeDTO>();
			ClientFamilyIncomeDTO dto = null;
			// log.debug("size income " + clientFamilyMember.getClientFamilyIncomes());
			for (ClientFamilyIncome clientFamilyIncome : clientFamilyMember.getClientFamilyIncomes()) {
				total = total + clientFamilyIncome.getIncomeAmount().doubleValue()
						* clientFamilyIncome.getLookupFrequency().getId();
				dto = mapper.map(clientFamilyIncome, ClientFamilyIncomeDTO.class);

				if (dto.getIncomeType() >= 1 && dto.getIncomeType() <= 7)
					dto.setOption("I");
				else
					dto.setOption("T");

				dto.setTotal(BigDecimal.valueOf(total));
				dto.setClientId(clientId);
				dto.setFamilyMemberId(familyMemberId);
				dto.setFirstName(clientFamilyMember.getFirstName());
				dto.setMiddleName(clientFamilyMember.getMiddleName());
				dto.setLastName(clientFamilyMember.getLastName());
				// log.debug("clientFamilyMember.getClientMaster().getGender() "
				// + clientFamilyMember.getClientMaster().getGender());
				dto.setGender(FinexaUtil.getGender(clientFamilyMember.getClientMaster(),
						clientFamilyMember.getLookupRelation()));
				// log.debug("relation " + clientFamilyMember.getLookupRelation().getId());
				dto.setRelationId(clientFamilyMember.getLookupRelation().getId());
				dto.setRelationName(clientFamilyMember.getLookupRelation().getDescription());
				dto.setIncomeFrequency(clientFamilyIncome.getLookupFrequency().getId());
				dto.setIncomeEndYear(clientFamilyIncome.getLookupIncomeExpenseDuration().getId());
				dto.setReferenceMonth(clientFamilyIncome.getLookupMonth().getId());

				/*
				 * log.debug("month "+dto.getReferenceMonth());
				 * log.debug("year "+dto.getReferenceMonth());
				 * log.debug("freq "+dto.getIncomeFrequency());
				 */

				listDTO.add(dto);

			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	public boolean checkIfTotalIncomeHeadPresent(int inClientId, int familyMemberId, int incomeType)
			throws RuntimeException, CustomFinexaException {
		try {
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(familyMemberId);

			boolean flag = false;

			for (ClientFamilyIncome clientFamilyIncome : clientFamilyMember.getClientFamilyIncomes()) {
				if (clientFamilyIncome.getIncomeType() == incomeType)
					flag = true;

			}

			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Family Income", "Nothing Specific",
					"Failed to check if Total Income Head Present", e);
		}
	}

	public boolean checkIfIndividualIncomeHeadPresent(int clientId, int familyMemberId, int incomeType)
			throws RuntimeException, CustomFinexaException {
		try {
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(familyMemberId);

			boolean flag = false;

			for (ClientFamilyIncome clientFamilyIncome : clientFamilyMember.getClientFamilyIncomes()) {
				if (clientFamilyIncome.getIncomeType() != incomeType)
					flag = true;

			}

			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Family Income", "Nothing Specific",
					"Failed to check if Individual Income Head Present", e);
		}

	}
	/*
	 * @Override public LookupIncomeCategory findByDescriptionContaining(String
	 * description){ LookupIncomeCategory
	 * lookupIncomeCategory=incomeCategoryRepository.findByDescriptionContaining
	 * (description); return lookupIncomeCategory;
	 * 
	 * }
	 */

	public void deleteIndividiualIncome(Integer clientID, Integer memberID, Byte incomeType)
			throws RuntimeException, CustomFinexaException {
		// log.debug("delete111");
		try {
			cientFamilyIncomeRepository.deleteIndividiualIncome(clientID, memberID, incomeType);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Family Income", "Nothing Specific",
					"Failed to delete individual income.", e);
		}
		// log.debug("delete");
	}

	@Override
	public void deleteIncomeForMember(Integer clientId, Integer familyMemberId, HttpServletRequest request)
			throws RuntimeException, CustomFinexaException {

		try {
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(familyMemberId);
			cientFamilyIncomeRepository.deleteIncomeForMember(clientId, familyMemberId);
			// log.debug("clientFamilyMember.getLifeExpectancy() " +
			// clientFamilyMember.getLifeExpectancy());
			if (clientFamilyMember.getLifeExpectancy() != null) {
				// log.debug("calculate life exp after delete " +
				// clientFamilyMember.getLifeExpectancy());
				ClientFamilyMemberDTO clientFamilyMemberDTO = mapper.map(clientFamilyMember,
						ClientFamilyMemberDTO.class);
				clientFamilyMemberDTO.setClientID(clientId);
				clientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());
				clientFamilyMemberService.save(clientFamilyMemberDTO, request);
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public void deleteTotalIncome(Integer clientId, Integer familyMemberId, Byte incomeType)
			throws RuntimeException, CustomFinexaException {
		// log.debug("delete 5");
		try {
			cientFamilyIncomeRepository.deleteTotalIncome(clientId, familyMemberId, incomeType.byteValue());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Family Income", "Nothing Specific",
					"Failed to delete total income.", e);
		}
		// log.debug("delete 6");
	}

	public void deleteIncomeByIncometype(Integer clientId, Integer familyMemberId, Integer incomeType)
			throws RuntimeException, CustomFinexaException {
		// log.debug("delete 3");
		try {
			cientFamilyIncomeRepository.deleteTotalIncome(clientId, familyMemberId, incomeType.byteValue());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Expense", "Nothing Specific", "Failed to delete total income.", e);
		}
		// log.debug("delete 4");
	}

	@Override
	public void delete(Integer id) {
		cientFamilyIncomeRepository.delete(id);
	}

	@Override
	public void saveFamilyIncomeAfterChangeInLifeExp(LifeExpectancyDTO lifeExpectancyDTO) throws RuntimeException {
		// TODO Auto-generated method stub

		try {
			log.debug("lifeExpectancyDTO.getAnnualIncome() " + lifeExpectancyDTO.getAnnualIncome());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(lifeExpectancyDTO.getFamilyMemberId());

			ClientMaster cm = cfm.getClientMaster();
			ClientFamilyIncome familyIncomeObj = cientFamilyIncomeRepository
					.checkIfExists(lifeExpectancyDTO.getFamilyMemberId(), FinexaConstant.LOOKUP_INCOME_TOTAL_ID);
			if (familyIncomeObj == null) {
				familyIncomeObj = new ClientFamilyIncome();
			}

			// LifeExpectancyDTO lifeExpectancyDTO = mapper.map(familyIncomeObj,
			// LifeExpectancyDTO.class);
			familyIncomeObj.setClientMaster(cm);
			familyIncomeObj.setClientFamilyMember(cfm);
			familyIncomeObj.setIncomeType((byte) FinexaConstant.LOOKUP_INCOME_TOTAL_ID);
			// log.debug("annual income: "
			// +lifeExpectancyDTO.getAnnualIncome());
			familyIncomeObj.setIncomeAmount(
					BigDecimal.valueOf(lifeExpectancyDTO.getAnnualIncome() / FinexaConstant.FREQUENCY_MONTHLY_ID));

			LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
					.findOne(FinexaConstant.INCOME_END_YEAR_LIFE_EXPECTANCY);
			familyIncomeObj.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);

			/*
			 * DateTime dt = new DateTime(); int month = dt.getMonthOfYear();
			 */

			LookupMonth lookupMonth = monthRepository.findOne(FinexaConstant.FREQUENCY_NA_ID);
			familyIncomeObj.setLookupMonth(lookupMonth);

			LookupFrequency lf = frequencyRepository.findOne(FinexaConstant.FREQUENCY_MONTHLY_ID);
			familyIncomeObj.setLookupFrequency(lf);

			cientFamilyIncomeRepository.save(familyIncomeObj);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public boolean getIncomeByClientID(Integer cliendID) throws CustomFinexaException {
		try {
			boolean present = false;
			int clientFamilyMemberID = cientFamilyIncomeRepository.fetchMemberID(cliendID);
			List<Integer> expenseList = cientFamilyIncomeRepository
					.fetchExpenseTypeByClientIDAndFamilyMemberID(cliendID, clientFamilyMemberID);
			for (Object expense : expenseList) {
				if (expense.toString().equals("2")) {
					present = true;
					break;
				}
			}
			return present;
		} catch (RuntimeException e) {
			throw new CustomFinexaException("Client Expense", "Nothing Specific", "Failed to fetch income", e);
		}
	}

}
