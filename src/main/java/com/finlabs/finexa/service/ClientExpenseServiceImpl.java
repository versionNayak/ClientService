package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.math.BigInteger;
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

import com.finlabs.finexa.dto.ClientExpenseDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupFrequency;
import com.finlabs.finexa.model.LookupIncomeExpenseDuration;
import com.finlabs.finexa.model.LookupMonth;
import com.finlabs.finexa.repository.ClientExpenseRepository;
import com.finlabs.finexa.repository.ClientGoalRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.FrequencyRepository;
import com.finlabs.finexa.repository.IncomeExpenseDurationRepository;
import com.finlabs.finexa.repository.MonthRepository;

@Service("ClientExpenseService")
@Transactional
public class ClientExpenseServiceImpl implements ClientExpenseService {
	
	private static Logger log = LoggerFactory.getLogger(ClientExpenseServiceImpl.class);
	
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientExpenseRepository cientExpenseRepository;
	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private FrequencyRepository frequencyRepository;

	@Autowired
	private MonthRepository monthRepository;

	@Autowired
	private IncomeExpenseDurationRepository incomeExpenseDurationRepository;
	
	@Autowired
	private ClientGoalRepository clientGoalRepository;
	
	@Autowired
	AdvisorService advisorService;

	@Override
	public void save(List<ClientExpenseDTO> clientExpenseList, HttpServletRequest request) throws RuntimeException, CustomFinexaException {

		/*
		 * for (ClientExpenseDTO clientExpenseDTO : clientExpenseList) {
		 * ClientExpense clientExpense = mapper.map(clientExpenseDTO,
		 * ClientExpense.class);
		 * 
		 * ClientMaster clientMaster =
		 * clientMasterRepository.findOne(clientExpenseDTO.getClientId());
		 * clientExpense.setClientMaster(clientMaster);
		 * 
		 * if (clientExpenseDTO.getId() == 0) { if
		 * (clientExpenseDTO.getExpenseAmount() != 0.0) {
		 * cientExpenseRepository.save(clientExpense);
		 * 
		 * } } else{ if("I".equals(clientExpenseDTO.getOption())){ boolean b =
		 * checkIfTotalExpenseHeadPresent(clientExpenseDTO.getClientId(),12); if
		 * (b){ deleteTotalExpense(clientExpenseDTO.getClientId(),12); } }else{
		 * boolean b =
		 * checkIfIndividualExpenseHeadPresent(clientExpenseDTO.getClientId(),12
		 * ); if (b){
		 * deleteIndividiualExpense(clientExpenseDTO.getClientId(),12); } }
		 * log.debug("amount "+clientExpenseDTO.getExpenseAmount()); if
		 * (clientExpenseDTO.getExpenseAmount() != 0.0) {
		 * cientExpenseRepository.save(clientExpense);
		 * 
		 * } else{ deleteExpenseByExpensetype(clientExpenseDTO.getClientId(),
		 * clientExpenseDTO.getExpenseType()); }
		 * 
		 * } }
		 */

		try {
			//Integer inputType = 12;
			log.debug("in service " + clientExpenseList.size());
			//double totalExpenseAdded = 0.0;
			for (ClientExpenseDTO clientExpenseDTO : clientExpenseList) {
				// log.debug("in service 99999");
				// log.debug("freq dto" + clientExpenseDTO.getFrequency());
				// log.debug("year dto" + clientExpenseDTO.getEndYear());
				// log.debug("month dto" +
				// clientExpenseDTO.getReferenceMonth());
				// log.debug("amount " + clientExpenseDTO.getExpenseAmount());
				// log.debug("type " + clientExpenseDTO.getExpenseType());
				ClientExpense clientExpense = mapper.map(clientExpenseDTO, ClientExpense.class);

				ClientMaster clientMaster = clientMasterRepository.findOne(clientExpenseDTO.getClientId());
				clientExpense.setClientMaster(clientMaster);

				LookupFrequency lookupFrequency = frequencyRepository.findOne(clientExpenseDTO.getFrequency());
				clientExpense.setLookupFrequency(lookupFrequency);

				if (clientExpenseDTO.getReferenceMonth() != null) {
					LookupMonth lookupMonth = monthRepository.findOne(clientExpenseDTO.getReferenceMonth());
					clientExpense.setLookupMonth(lookupMonth);
				}

				LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
						.findOne(clientExpenseDTO.getEndYear());
				clientExpense.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);

				if ("I".equals(clientExpenseDTO.getOption())) {
					boolean b = checkIfTotalExpenseHeadPresent(clientExpenseDTO.getClientId(), 12);
					// log.debug("b " + b);
					if (b) {
						deleteTotalExpense(clientExpenseDTO.getClientId(), 12);
					}
				} else {
					// log.debug("aaaaa");
					boolean b = checkIfIndividualExpenseHeadPresent(clientExpenseDTO.getClientId(), 12);
					if (b) {
						deleteIndividiualExpense(clientExpenseDTO.getClientId(), 12);
					}
					/*DateTime dt = new DateTime();
					int month = dt.getMonthOfYear();
*/
					LookupMonth lookupMonth1 = monthRepository.findOne((byte)13);
					clientExpense.setLookupMonth(lookupMonth1);
				}
				// log.debug("amount " + clientExpenseDTO.getExpenseAmount());
				if (clientExpenseDTO.getExpenseAmount() != null 
						&& clientExpenseDTO.getExpenseAmount() != 0) {
					// log.debug("not zero");
					// log.debug("freq model" +
					// clientExpense.getLookupFrequency().getId());
					// log.debug("year model" +
					// clientExpense.getLookupIncomeExpenseDuration().getId());
					// log.debug("month model" +
					// clientExpense.getLookupMonth().getId());
					cientExpenseRepository.save(clientExpense);
					advisorService.deletetAUMCacheMap(clientMaster.getAdvisorUser().getId(),request);
					/*totalExpenseAdded = totalExpenseAdded + clientExpense.getExpenseAmount().doubleValue()
							+ clientExpense.getLookupFrequency().getId();*/
					

				} else {
					// log.debug(" zero");
					deleteExpenseByExpensetype(clientExpenseDTO.getClientId(), clientExpenseDTO.getExpenseType());
					// log.debug(" deleted");
				}

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public ClientExpenseDTO autoSave(ClientExpenseDTO clientExpenseDTO) throws RuntimeException, CustomFinexaException {

		try {
				int expenseType = clientExpenseDTO.getExpenseType();
				double expenseAmount = clientExpenseDTO.getExpenseAmount();
				
				ClientExpense clientExpense = mapper.map(clientExpenseDTO, ClientExpense.class);

				ClientMaster clientMaster = clientMasterRepository.findOne(clientExpenseDTO.getClientId());
				clientExpense.setClientMaster(clientMaster);
				
				clientExpense.setExpenseType((byte) expenseType);
				clientExpense.setExpenseAmount(BigDecimal.valueOf(expenseAmount));
				
				LookupFrequency lookupFrequency = frequencyRepository.findOne(clientExpenseDTO.getFrequency());
				clientExpense.setLookupFrequency(lookupFrequency);

				LookupMonth lookupMonth = monthRepository.findOne(clientExpenseDTO.getReferenceMonth());
				clientExpense.setLookupMonth(lookupMonth);

				LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
						.findOne(clientExpenseDTO.getEndYear());
				clientExpense.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);

				clientExpense = cientExpenseRepository.save(clientExpense);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return clientExpenseDTO;
	}

	@Override
	public void update(List<ClientExpenseDTO> clientExpenseList, HttpServletRequest request) throws RuntimeException, CustomFinexaException {
		try {
			//Integer inputType = 12;
			log.debug("in service " + clientExpenseList.size());
			double totalExpenseAdded = 0.0;
			for (ClientExpenseDTO clientExpenseDTO : clientExpenseList) {
				
				ClientExpense clientExpense = mapper.map(clientExpenseDTO, ClientExpense.class);

				ClientMaster clientMaster = clientMasterRepository.findOne(clientExpenseDTO.getClientId());
				clientExpense.setClientMaster(clientMaster);

				LookupFrequency lookupFrequency = frequencyRepository.findOne(clientExpenseDTO.getFrequency());
				clientExpense.setLookupFrequency(lookupFrequency);

				if (clientExpenseDTO.getReferenceMonth() != null) {
					LookupMonth lookupMonth = monthRepository.findOne(clientExpenseDTO.getReferenceMonth());
					clientExpense.setLookupMonth(lookupMonth);
				}

				LookupIncomeExpenseDuration lookupIncomeExpenseDuration = incomeExpenseDurationRepository
						.findOne(clientExpenseDTO.getEndYear());
				clientExpense.setLookupIncomeExpenseDuration(lookupIncomeExpenseDuration);

				if ("I".equals(clientExpenseDTO.getOption())) {
					boolean b = checkIfTotalExpenseHeadPresent(clientExpenseDTO.getClientId(), 12);
					// log.debug("b " + b);
					if (b) {
						deleteTotalExpense(clientExpenseDTO.getClientId(), 12);
					}
				} else {
					// log.debug("aaaaa");
					boolean b = checkIfIndividualExpenseHeadPresent(clientExpenseDTO.getClientId(), 12);
					if (b) {
						deleteIndividiualExpense(clientExpenseDTO.getClientId(), 12);
					}
					/*DateTime dt = new DateTime();
					int month = dt.getMonthOfYear();*/

					LookupMonth lookupMonth1 = monthRepository.findOne((byte) 13);
					clientExpense.setLookupMonth(lookupMonth1);
				}
				// log.debug("amount " + clientExpenseDTO.getExpenseAmount());
				if (clientExpenseDTO.getExpenseAmount() != null 
						&& clientExpenseDTO.getExpenseAmount() != 0) {
					
					cientExpenseRepository.save(clientExpense);
					advisorService.deletetAUMCacheMap(clientMaster.getAdvisorUser().getId(),request);
					//clientGoalService.updateClientGoal(clientExpense);
					
					totalExpenseAdded = totalExpenseAdded + clientExpense.getExpenseAmount().doubleValue()
							* clientExpense.getLookupFrequency().getId();

				}

				else {
					// log.debug(" zero");
					deleteExpenseByExpensetype(clientExpenseDTO.getClientId(), clientExpenseDTO.getExpenseType());
					// log.debug(" deleted");
				}

			}
			//removed as per issue Id 829
			/*
			 * ClientMaster clientMaster =
			 * clientMasterRepository.findOne(clientExpenseList.get(0).getClientId());
			 * List<ClientGoal> ClientGoalList = clientMaster.getClientGoals();
			 * 
			 * for(ClientGoal clientGoal:ClientGoalList) {
			 * if(clientGoal.getLookupGoalType().getId() == 8) {
			 * clientGoal.setPostRetirementAnnualExpense(totalExpenseAdded);
			 * clientGoalRepository.save(clientGoal); } }
			 */
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientExpenseDTO getAllExpense(Integer clientId) throws RuntimeException {
		try {
			// log.debug("id cl" + clientId);
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			ClientExpenseDTO clientExpenseDTO = null;
			double TotalExpense = 0;
			if (clientMaster.getClientExpenses().size() != 0) {
				for (ClientExpense clientExpense : clientMaster.getClientExpenses()) {
					TotalExpense = TotalExpense + clientExpense.getExpenseAmount().doubleValue()
							* clientExpense.getLookupFrequency().getId();
					clientExpenseDTO = mapper.map(clientExpense, ClientExpenseDTO.class);

					
					
					clientExpenseDTO.setExpenseAmount(TotalExpense);
					
					/*
					 * if(clientExpenseDTO.getExpenseType()>=1 &&
					 * clientExpenseDTO.getExpenseType()<=12)
					 * clientExpenseDTO.setOption("I"); else
					 * clientExpenseDTO.setOption("T");
					 */

					/*
					 * clientExpenseDTO.setFirstName(clientMaster.getFirstName()
					 * );
					 * clientExpenseDTO.setLastName(clientMaster.getLastName());
					 * clientExpenseDTO.setMiddleName(clientMaster.getMiddleName
					 * ());
					 */

				}
				//clientExpenseDTO.setExpenseAmount(TotalExpense);
				
			}
			return clientExpenseDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<ClientExpenseDTO> viewAllExpense(Integer clientId) throws RuntimeException {
		// log.debug("id cl" + clientId);
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			List<ClientExpenseDTO> cfiDtoList = new ArrayList<ClientExpenseDTO>();

			ClientExpenseDTO clientExpenseDTO = null;
			double TotalExpense = 0;
			for (ClientExpense clientExpense : clientMaster.getClientExpenses()) {
				TotalExpense = TotalExpense
						+ clientExpense.getExpenseAmount().doubleValue() * clientExpense.getLookupFrequency().getId();
				clientExpenseDTO = mapper.map(clientExpense, ClientExpenseDTO.class);

				clientExpenseDTO.setTotalexpense(TotalExpense);
				clientExpenseDTO.setClientId(clientId);

				clientExpenseDTO.setFrequency(clientExpense.getLookupFrequency().getId());
				clientExpenseDTO.setEndYear(clientExpense.getLookupIncomeExpenseDuration().getId());
				clientExpenseDTO.setReferenceMonth(clientExpense.getLookupMonth().getId());

				if (clientExpenseDTO.getExpenseType() >= 1 && clientExpenseDTO.getExpenseType() <= 11)
					clientExpenseDTO.setOption("I");
				else
					clientExpenseDTO.setOption("T");

				cfiDtoList.add(clientExpenseDTO);

			}
			return cfiDtoList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	/*
	 * @Override public List<ClientExpenseDTO> getAllExpense1(int clientId){
	 * 
	 * List<ClientExpense>
	 * cfiList=cientExpenseRepository.getAllExpense(clientId);
	 * 
	 * List<ClientExpenseDTO> cfiDtoList=new ArrayList<ClientExpenseDTO>();
	 * ClientExpenseDTO clientExpenseDTO; for (ClientExpense clientExpense :
	 * cfiList) { clientExpenseDTO = mapper.map(clientExpense,
	 * ClientExpenseDTO.class);
	 * clientExpenseDTO.setExpenseAmount(clientExpense.getExpenseAmount());
	 * cfiDtoList.add(clientExpenseDTO); }
	 * 
	 * return cfiDtoList;
	 * 
	 * 
	 * }
	 */
	@Override
	public boolean checkIfExpensePresent(Integer clientId) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

		List<ClientExpense> ClientExpenseList = clientMaster.getClientExpenses();
		if (ClientExpenseList.size() > 0)
			return true;
		else
			return false;
	}

	public boolean checkIfTotalExpenseHeadPresent(Integer clientId, Integer expenseType) throws CustomFinexaException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			boolean flag = false;

			for (ClientExpense clientExpense : clientMaster.getClientExpenses()) {
				if (clientExpense.getExpenseType() == expenseType)
					flag = true;

			}

			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Expense", "Nothing Specific",
					"Failed to check if Total Expense head present.", e);
		}
	}

	public boolean checkIfIndividualExpenseHeadPresent(int clientId, Integer ExpenseType) throws CustomFinexaException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			boolean flag = false;

			for (ClientExpense clientExpense : clientMaster.getClientExpenses()) {
				if (clientExpense.getExpenseType() != ExpenseType)
					flag = true;
			}
			return flag;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Expense", "Nothing Specific",
					"Failed to check if Individual Expense head present.", e);
		}

	}
	/*
	 * @Override public LookupExpenseCategory findByDescriptionContaining(String
	 * description){ LookupExpenseCategory
	 * lookupExpenseCategory=ExpenseCategoryRepository.
	 * findByDescriptionContaining(description); return lookupExpenseCategory;
	 * 
	 * }
	 */

	public void deleteIndividiualExpense(Integer clientID, Integer ExpenseType) throws CustomFinexaException {
		try {
			cientExpenseRepository.deleteIndividiualExpense(clientID, ExpenseType.byteValue());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Expense", "Nothing Specific",
					"Failed to delete Individual Expense.", e);
		}
	}

	@Override
	public int deleteClientExpense(Integer expenceID) throws RuntimeException {
		try {
			log.debug("expense");
			cientExpenseRepository.delete(expenceID);
			log.debug("expense after");
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	public void deleteTotalExpense(int clientId, Integer ExpenseType) throws CustomFinexaException {
		try {
			cientExpenseRepository.deleteTotalExpense(clientId, ExpenseType.byteValue());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Expense", "Nothing Specific",
					"Failed to delete Total Expense.", e);
		}
	}

	public void deleteExpenseByExpensetype(Integer clientId, Integer ExpenseType) throws CustomFinexaException {
		try {
			cientExpenseRepository.deleteTotalExpense(clientId, ExpenseType.byteValue());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException("Client Expense", "Nothing Specific",
					"Failed to delete Expense By Expense Type.", e);
		}
	}

	public boolean getExpenceByClientID(Integer cliendID) throws CustomFinexaException {
		try {
			boolean present = false;
			List<Integer> expenseList = cientExpenseRepository.fetchExpenseTypeByClientID(cliendID);
			for (Object expense : expenseList) {
				if (expense.toString().equals("2")) {
					present = true;
					break;
				}
			}
			return present;
		} catch (RuntimeException e) {
			throw new CustomFinexaException("Client Expense", "Nothing Specific", "Failed to fetche xpensece", e);
		}
	}
}
