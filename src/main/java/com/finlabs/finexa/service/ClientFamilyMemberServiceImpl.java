package com.finlabs.finexa.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientFamilyIncomeDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.LifeExpectancyDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupRelation;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.LookupRelationshipRepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("ClientFamilyMemberService")
@Transactional
public class ClientFamilyMemberServiceImpl implements ClientFamilyMemberService {
	private static Logger log = LoggerFactory.getLogger(ClientFamilyMemberServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private LookupRelationshipRepository relationsRepository;

	@Autowired
	private ClientLifeExpectancyService ClientLifeExpectancyService;

	@Autowired
	private ClientFamilyIncomeService clientFamilyIncomeService;
	@Autowired
	AdvisorService advisorService;
	// int lifeExpectancy;

	@Override
	public ClientFamilyMemberDTO save(ClientFamilyMemberDTO clientFamilyMemberDTO, HttpServletRequest request)
			throws RuntimeException, CustomFinexaException {
		ClientMaster cm = null;
		try {
			log.debug("inside save");
			double totalIncomeAmount = 0.0;
			if (clientFamilyMemberDTO.getBirthDate() != null) {
			    cm = clientMasterRepository.findOne(clientFamilyMemberDTO.getClientID());
				LookupRelation rel = relationsRepository.findOne(clientFamilyMemberDTO.getRelationID());

				ClientFamilyMember clientFamilyMember = mapper.map(clientFamilyMemberDTO, ClientFamilyMember.class);

				clientFamilyMember.setClientMaster(cm);
				clientFamilyMember.setLookupRelation(rel);

				String hasDiseaseHistory = (clientFamilyMemberDTO.getHasDiseaseHistory() != null)
						? clientFamilyMemberDTO.getHasDiseaseHistory()
						: "Y";
				String hasNormalBP = (clientFamilyMemberDTO.getHasNormalBP() != null)
						? clientFamilyMemberDTO.getHasNormalBP()
						: "Y";
				String isProperBMI = (clientFamilyMemberDTO.getIsProperBMI() != null)
						? clientFamilyMemberDTO.getIsProperBMI()
						: "Y";
				String isTobaccoUser = (clientFamilyMemberDTO.getIsTobaccoUser() != null)
						? clientFamilyMemberDTO.getIsTobaccoUser()
						: "N";

				clientFamilyMember.setHasDiseaseHistory(hasDiseaseHistory);
				clientFamilyMember.setHasNormalBP(hasNormalBP);
				clientFamilyMember.setIsProperBMI(isProperBMI);
				clientFamilyMember.setIsTobaccoUser(isTobaccoUser);

				if (clientFamilyMember.getLifeExpectancy() != null) {
					LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
					lifeExpectancyDTO.setId(clientFamilyMember.getClientMaster().getId());
					lifeExpectancyDTO.setFamilyMemberId(clientFamilyMember.getId());
					lifeExpectancyDTO.setBirthDate(clientFamilyMemberDTO.getBirthDate());
					String gender = FinexaUtil.getGender(clientFamilyMember.getClientMaster(),
							clientFamilyMember.getLookupRelation());
					lifeExpectancyDTO.setGender(gender);
					List<ClientFamilyIncomeDTO> lstIncome = clientFamilyIncomeService.getAllIncomeForFamilyMember(
							clientFamilyMemberDTO.getClientID(), clientFamilyMemberDTO.getId());
					if (lstIncome.size() > 0) {
						int s = lstIncome.size();
						lifeExpectancyDTO.setAnnualIncome(lstIncome.get(s - 1).getTotal().doubleValue());

					} else {
						lifeExpectancyDTO.setAnnualIncome(0);
					}

					totalIncomeAmount = clientFamilyMemberDTO.getTotalAmount();
					if (totalIncomeAmount != 0.0) {
						lifeExpectancyDTO.setAnnualIncome(totalIncomeAmount);
					}
					lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
					lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
					lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
					lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
					LifeExpectancyDTO lifeExpectancyDTO1 = ClientLifeExpectancyService
							.calculateLifeExp(lifeExpectancyDTO);
					clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO1.getTotalLifeExpectancy());

					clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);
				} else {
					clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);
				}
				clientFamilyMemberDTO = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
				clientFamilyMemberDTO.setClientID(clientFamilyMember.getClientMaster().getId());
				clientFamilyMemberDTO.setRelationID(rel.getId());
				clientFamilyMemberDTO.setRelationName(rel.getDescription());
			}
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientFamilyMemberDTO;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	// No need for Update as sepearte service Method Save will do
	@Override
	public ClientFamilyMemberDTO update(ClientFamilyMemberDTO clientFamilyMemberDTO, HttpServletRequest request)
			throws RuntimeException, CustomFinexaException {
		ClientMaster cm = null;
		try {
			double totalIncomeAmount = 0.0;
			if (clientFamilyMemberDTO.getBirthDate() != null) {
				cm = clientMasterRepository.findOne(clientFamilyMemberDTO.getClientID());
				LookupRelation rel = relationsRepository.findOne(clientFamilyMemberDTO.getRelationID());

				ClientFamilyMember clientFamilyMember = mapper.map(clientFamilyMemberDTO, ClientFamilyMember.class);

				clientFamilyMember.setClientMaster(cm);
				clientFamilyMember.setLookupRelation(rel);

				String hasDiseaseHistory = (clientFamilyMemberDTO.getHasDiseaseHistory() != null)
						? clientFamilyMemberDTO.getHasDiseaseHistory()
						: "Y";
				String hasNormalBP = (clientFamilyMemberDTO.getHasNormalBP() != null)
						? clientFamilyMemberDTO.getHasNormalBP()
						: "Y";
				String isProperBMI = (clientFamilyMemberDTO.getIsProperBMI() != null)
						? clientFamilyMemberDTO.getIsProperBMI()
						: "Y";
				String isTobaccoUser = (clientFamilyMemberDTO.getIsTobaccoUser() != null)
						? clientFamilyMemberDTO.getIsTobaccoUser()
						: "N";

				clientFamilyMember.setHasDiseaseHistory(hasDiseaseHistory);
				clientFamilyMember.setHasNormalBP(hasNormalBP);
				clientFamilyMember.setIsProperBMI(isProperBMI);
				clientFamilyMember.setIsTobaccoUser(isTobaccoUser);

				// log.debug("HasDiseaseHistory(): before" +
				// clientFamilyMember.getHasDiseaseHistory());
				// log.debug("HasNormalBP: before" +
				// clientFamilyMember.getHasNormalBP());
				// log.debug("IsProperBMI(): before" +
				// clientFamilyMember.getIsProperBMI());
				// log.debug("IsTobaccoUser: before" +
				// clientFamilyMember.getIsTobaccoUser());

				// log.debug("member id"+clientFamilyMember.getId());
				// log.debug("member
				// lf"+clientFamilyMember.getLifeExpectancy());
				if (clientFamilyMember.getLifeExpectancy() != null) {
					LifeExpectancyDTO lifeExpectancyDTO = new LifeExpectancyDTO();
					lifeExpectancyDTO.setId(clientFamilyMember.getClientMaster().getId());
					lifeExpectancyDTO.setFamilyMemberId(clientFamilyMember.getId());
					lifeExpectancyDTO.setBirthDate(clientFamilyMemberDTO.getBirthDate());
					String gender = FinexaUtil.getGender(clientFamilyMember.getClientMaster(),
							clientFamilyMember.getLookupRelation());
					lifeExpectancyDTO.setGender(gender);
					List<ClientFamilyIncomeDTO> lstIncome = clientFamilyIncomeService.getAllIncomeForFamilyMember(
							clientFamilyMemberDTO.getClientID(), clientFamilyMemberDTO.getId());
					if (lstIncome.size() > 0) {
						int s = lstIncome.size();
						// log.debug("s "+s);
						// log.debug("lstIncome.get(s-1).getTotal().doubleValue()
						// "+lstIncome.get(s-1).getTotal().doubleValue());
						lifeExpectancyDTO.setAnnualIncome(lstIncome.get(s - 1).getTotal().doubleValue());

					} else {
						lifeExpectancyDTO.setAnnualIncome(0);
					}
					// log.debug("after added in family total income
					// "+clientFamilyMemberDTO.getTotalAmount());
					totalIncomeAmount = clientFamilyMemberDTO.getTotalAmount();
					if (totalIncomeAmount != 0.0) {
						lifeExpectancyDTO.setAnnualIncome(totalIncomeAmount);
					}
					// log.debug("lifeExpectancyDTO.setAnnualIncome
					// "+lifeExpectancyDTO.getAnnualIncome());
					lifeExpectancyDTO.setHasNormalBP(clientFamilyMember.getHasNormalBP());
					lifeExpectancyDTO.setHasDiseaseHistory(clientFamilyMember.getHasDiseaseHistory());
					lifeExpectancyDTO.setIsTobaccoUser(clientFamilyMember.getIsTobaccoUser());
					lifeExpectancyDTO.setIsProperBMI(clientFamilyMember.getIsProperBMI());
					LifeExpectancyDTO lifeExpectancyDTO1 = ClientLifeExpectancyService
							.calculateLifeExp(lifeExpectancyDTO);
					clientFamilyMember.setLifeExpectancy(lifeExpectancyDTO1.getTotalLifeExpectancy());

					// log.debug("HasDiseaseHistory(): after" +
					// clientFamilyMember.getHasDiseaseHistory());
					// log.debug("HasNormalBP: after" +
					// clientFamilyMember.getHasNormalBP());
					// log.debug("IsProperBMI(): after" +
					// clientFamilyMember.getIsProperBMI());
					// log.debug("IsTobaccoUser: after" +
					// clientFamilyMember.getIsTobaccoUser());

					clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);

					// log.debug("clientFamilyMember
					// id"+clientFamilyMember.getId());
					// log.debug("clientFamilyMember
					// dob"+clientFamilyMember.getBirthDate());
					// log.debug("life exp
					// "+clientFamilyMember.getLifeExpectancy());

				} else {
					clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);
				}
				clientFamilyMemberDTO = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
				clientFamilyMemberDTO.setClientID(clientFamilyMember.getClientMaster().getId());
				clientFamilyMemberDTO.setRelationID(rel.getId());
				clientFamilyMemberDTO.setRelationName(rel.getDescription());
			}
		    advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			return clientFamilyMemberDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientFamilyMemberRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<ClientFamilyMemberDTO> findByClientId(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			List<ClientFamilyMemberDTO> listDTO = new ArrayList<ClientFamilyMemberDTO>();
			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				ClientFamilyMemberDTO dto = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
				/*
				 * if (clientFamilyMember.getLookupRelation().getId() == 0) {
				 * dto.setGender(clientMaster.getGender()); } if
				 * (clientFamilyMember.getLookupRelation().getId() == 1) { if
				 * (clientMaster.getGender().equals("M")) { dto.setGender("F"); } else {
				 * dto.setGender("M"); } }
				 */

				dto.setGender(FinexaUtil.getGender(clientMaster, clientFamilyMember.getLookupRelation()));
				dto.setRelationName(clientFamilyMember.getLookupRelation().getDescription());
				dto.setRelationID(clientFamilyMember.getLookupRelation().getId());
				dto.setClientID(clientId);
				if (clientFamilyMember.getLookupRelation().getId() != 0) {
					listDTO.add(dto);
				}
			}
			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientFamilyMemberDTO findClientByClientId(int clientId) throws RuntimeException {
		ClientFamilyMemberDTO dto1 = null;
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			List<ClientFamilyMemberDTO> listDTO = new ArrayList<ClientFamilyMemberDTO>();
			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				ClientFamilyMemberDTO dto = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
				/*
				 * if (clientFamilyMember.getLookupRelation().getId() == 0) {
				 * dto.setGender(clientMaster.getGender()); } if
				 * (clientFamilyMember.getLookupRelation().getId() == 1) { if
				 * (clientMaster.getGender().equals("M")) { dto.setGender("F"); } else {
				 * dto.setGender("M"); } }
				 */

				dto.setGender(FinexaUtil.getGender(clientMaster, clientFamilyMember.getLookupRelation()));
				dto.setRelationName(clientFamilyMember.getLookupRelation().getDescription());
				dto.setRelationID(clientFamilyMember.getLookupRelation().getId());
				dto.setClientID(clientId);
				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					// listDTO.add(dto);
					dto1 = dto;
					break;
				}
			}

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return dto1;
	}

	@Override
	public ClientFamilyMemberDTO findById(int id) throws RuntimeException {
		try {
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(id);
			ClientFamilyMemberDTO clientFamilyMemberDTO = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
			clientFamilyMemberDTO.setClientID(clientFamilyMember.getClientMaster().getId());
			clientFamilyMemberDTO.setGender(
					FinexaUtil.getGender(clientFamilyMember.getClientMaster(), clientFamilyMember.getLookupRelation()));
			clientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());
			return clientFamilyMemberDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int existassetOrLoan(int id) throws RuntimeException {
		try {
			int i = 0;
			ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(id);
			if (clientFamilyMember.getClientAnnuities().size() != 0
					|| clientFamilyMember.getClientAtalPensionYojanas().size() != 0
					|| clientFamilyMember.getClientCashs().size() != 0 || clientFamilyMember.getClientEpfs().size() != 0
					|| clientFamilyMember.getClientEquities().size() != 0
					|| clientFamilyMember.getClientFamilyIncomes().size() != 0
					|| clientFamilyMember.getClientFixedIncomes().size() != 0
					|| clientFamilyMember.getClientLifeInsurances().size() != 0
					|| clientFamilyMember.getClientLoans().size() != 0
					|| clientFamilyMember.getClientMutualFunds().size() != 0
					|| clientFamilyMember.getClientNonLifeInsurances().size() != 0
					|| clientFamilyMember.getClientNps().size() != 0
					|| clientFamilyMember.getClientOtherAlternateAssets().size() != 0
					|| clientFamilyMember.getClientPpfs().size() != 0
					|| clientFamilyMember.getClientPreciousMetals().size() != 0
					|| clientFamilyMember.getClientRealEstates().size() != 0
					|| clientFamilyMember.getClientSmallSavings().size() != 0
					|| clientFamilyMember.getClientStructuredProducts().size() != 0
					|| clientFamilyMember.getClientVehicles().size() != 0) {
				i = 1;
			}
			return i;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientFamilyMemberDTO> findAll() {
		ClientFamilyMemberDTO ClientFamilyMemberDTO;
		List<ClientFamilyMember> listClientFamilyMember = clientFamilyMemberRepository.findAll();

		List<ClientFamilyMemberDTO> listDTO = new ArrayList<ClientFamilyMemberDTO>();
		for (ClientFamilyMember clientFamilyMember : listClientFamilyMember) {
			ClientFamilyMemberDTO = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
			ClientFamilyMemberDTO.setClientID(clientFamilyMember.getClientMaster().getId());
			ClientFamilyMemberDTO.setRelationID(clientFamilyMember.getLookupRelation().getId());
			listDTO.add(ClientFamilyMemberDTO);
		}

		return listDTO;
	}

	// Don't edit this method as it is used in global family member image logic
	@Override
	public List<ClientFamilyMemberDTO> getClientFamilyMemberImageByClient(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMemberDTO> familyMemberlistDTO = new ArrayList<ClientFamilyMemberDTO>();

			for (ClientFamilyMember clientFamilyMember : clientMaster.getClientFamilyMembers()) {
				ClientFamilyMemberDTO dto = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
				// System.out.println(clientFamilyMember.getClientMaster().getGender());
				if (clientFamilyMember.getLookupRelation().getId() == 0) {
					dto.setGender(clientMaster.getGender());
				} else {
					if (clientFamilyMember.getLookupRelation().getId() == 1) {
						if (clientMaster.getGender().equals("M")) {
							System.out.println("inside if");
							dto.setGender("F");
						} else {
							dto.setGender("M");
						}
					}
				}

				// System.out.println("relation id: " +
				// clientFamilyMember.getLookupRelation().getId());
				// System.out.println("master gender: " + clientMaster.getGender());

				dto.setRelationName(clientFamilyMember.getLookupRelation().getDescription());
				dto.setRelationID(clientFamilyMember.getLookupRelation().getId());
				familyMemberlistDTO.add(dto);
			}

			Collections.sort(familyMemberlistDTO, new Comparator<ClientFamilyMemberDTO>() {
				public int compare(ClientFamilyMemberDTO o1, ClientFamilyMemberDTO o2) {
					return (o1.getRelationID()).compareTo(o2.getRelationID());
				}
			});
			return familyMemberlistDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteClientFamilyMember(int id) {
		ClientFamilyMember clientFamilyMember = clientFamilyMemberRepository.findOne(id);
		if (clientFamilyMember != null) {
			clientFamilyMemberRepository.delete(id);
		}
	}
	
	@Override
	public ClientFamilyMemberDTO autoSave(ClientFamilyMemberDTO clientFamilyMemberDTO)
			throws RuntimeException, CustomFinexaException {
		try {
			log.debug("inside save");
			if (clientFamilyMemberDTO.getBirthDate() != null) {
				ClientMaster cm = clientMasterRepository.findOne(clientFamilyMemberDTO.getClientID());
				LookupRelation rel = relationsRepository.findOne(clientFamilyMemberDTO.getRelationID());

				ClientFamilyMember clientFamilyMember = mapper.map(clientFamilyMemberDTO, ClientFamilyMember.class);

				clientFamilyMember.setClientMaster(cm);
				clientFamilyMember.setLookupRelation(rel);

				String hasDiseaseHistory = (clientFamilyMemberDTO.getHasDiseaseHistory() != null)
						? clientFamilyMemberDTO.getHasDiseaseHistory()
						: "Y";
				String hasNormalBP = (clientFamilyMemberDTO.getHasNormalBP() != null)
						? clientFamilyMemberDTO.getHasNormalBP()
						: "Y";
				String isProperBMI = (clientFamilyMemberDTO.getIsProperBMI() != null)
						? clientFamilyMemberDTO.getIsProperBMI()
						: "Y";
				String isTobaccoUser = (clientFamilyMemberDTO.getIsTobaccoUser() != null)
						? clientFamilyMemberDTO.getIsTobaccoUser()
						: "N";

				clientFamilyMember.setHasDiseaseHistory(hasDiseaseHistory);
				clientFamilyMember.setHasNormalBP(hasNormalBP);
				clientFamilyMember.setIsProperBMI(isProperBMI);
				clientFamilyMember.setIsTobaccoUser(isTobaccoUser);
				clientFamilyMember = clientFamilyMemberRepository.save(clientFamilyMember);
				System.out.println("id "+clientFamilyMember.getId());
				clientFamilyMemberDTO = mapper.map(clientFamilyMember, ClientFamilyMemberDTO.class);
			}

			return clientFamilyMemberDTO;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
