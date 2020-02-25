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

import com.finlabs.finexa.dto.ClientEpfDTO;
import com.finlabs.finexa.dto.ClientFamilyMemberDTO;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.MasterEPFInterestRate;
import com.finlabs.finexa.model.MasterIncomeGrowthRate;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.repository.ClientEpfRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.MasterEPFInterestRateRepository;
import com.finlabs.finexa.repository.MasterIncomeGrowthRateRepository;
import com.finlabs.finexa.repository.MasterProductClassificationRepository;
import com.finlabs.finexa.util.FinexaUtil;


@Service("ClientEPFService")
@Transactional
public class ClientEPFServiceImpl implements ClientEPFService {
	private static Logger log = LoggerFactory.getLogger(ClientEPFServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private ClientEpfRepository clientEPFRepository;

	@Autowired
	private ClientAnnuityService clientAnnuityService;

	@Autowired
	private MasterProductClassificationRepository masterProductClassificationRepository;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private MasterEPFInterestRateRepository masterEPFInterestRateRepository;

	@Autowired
	private MasterIncomeGrowthRateRepository masterIncomeGrowthRateRepository;
	@Autowired
	AdvisorService advisorService;
	
	@Override
	public ClientEpfDTO save(ClientEpfDTO clientEPFDTO, HttpServletRequest request) throws RuntimeException {
		try {
			log.debug("ClientEPFServiceImpl <<");
			ClientMaster cm = clientMasterRepository.findOne(clientEPFDTO.getClientID());
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientEPFDTO.getFamilyMemberID());
			ClientEPF clientEPF = mapper.map(clientEPFDTO, ClientEPF.class);
			clientEPF.setClientMaster(cm);
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientEPFDTO.getFinancialAssetType());
			clientEPF.setMasterProductClassification(masterProductClassification);
			clientEPF.setClientFamilyMember(fam);
			double epfoInterestRate = clientEPFDTO.getEpfoInterestRate().doubleValue() / 100;
			clientEPF.setEpfoInterestRate(new BigDecimal(epfoInterestRate));
			double expIncreasePerc = clientEPFDTO.getAnnualContributionIncrease().doubleValue() / 100;
			clientEPF.setAnnualContributionIncrease((new BigDecimal(expIncreasePerc)));
			clientEPF = clientEPFRepository.save(clientEPF);
			log.debug("ClientEPFServiceImpl << EPF saved successfully");
			clientAnnuityService.saveEPSAnnuity(clientEPF);
			log.debug("ClientEPFServiceImpl << EPS saved successfully");
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			System.out.println(clientEPFDTO.getEpfoInterestRate());
			
			return clientEPFDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientEpfDTO update(ClientEpfDTO clientEPFDTO, HttpServletRequest request) throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientEPFDTO.getClientID());
			ClientFamilyMember fam = clientFamilyMemberRepository.findOne(clientEPFDTO.getFamilyMemberID());
			ClientEPF clientEPF = mapper.map(clientEPFDTO, ClientEPF.class);
			clientEPF.setClientMaster(cm);
			MasterProductClassification masterProductClassification = masterProductClassificationRepository
					.findOne(clientEPFDTO.getFinancialAssetType());
			clientEPF.setMasterProductClassification(masterProductClassification);
			clientEPF.setClientFamilyMember(fam);
			double epfoInterestRate = clientEPFDTO.getEpfoInterestRate().doubleValue() / 100;
			clientEPF.setEpfoInterestRate(new BigDecimal(epfoInterestRate));
			double expIncreasePerc = clientEPFDTO.getAnnualContributionIncrease().doubleValue() / 100;
			clientEPF.setAnnualContributionIncrease((new BigDecimal(expIncreasePerc)));
			log.debug("in between epf and eps annuity");
			clientEPF = clientEPFRepository.save(clientEPF);
			clientAnnuityService.updateEPSAnnuity(clientEPF);
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
			System.out.println(clientEPFDTO.getEpfoInterestRate());
			return clientEPFDTO;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientEpfDTO> findAll() {
		// TODO Auto-generated method stub
		ClientEpfDTO ClientEpfDTO;
		List<ClientEPF> listClientEPF = clientEPFRepository.findAll();

		List<ClientEpfDTO> listDTO = new ArrayList<ClientEpfDTO>();
		for (ClientEPF clientEPF : listClientEPF) {
			ClientEpfDTO = mapper.map(clientEPF, ClientEpfDTO.class);
			ClientEpfDTO.setClientID(clientEPF.getClientMaster().getId());
			listDTO.add(ClientEpfDTO);
		}

		return listDTO;
	}

	@Override
	public List<ClientEpfDTO> findByClientId(int clientId) throws RuntimeException {

		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			ClientEpfDTO clientEPFDTO;
			List<ClientEPF> clientEPFList = clientMaster.getClientEpfs();
			List<ClientEpfDTO> clientEPFDTOList = new ArrayList<ClientEpfDTO>();

			for (ClientEPF obj : clientEPFList) {

				clientEPFDTO = mapper.map(obj, ClientEpfDTO.class);
				clientEPFDTO.setClientID(obj.getClientMaster().getId());
				clientEPFDTOList.add(clientEPFDTO);
			}

			return clientEPFDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientEPFRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientEpfDTO findById(int id) throws RuntimeException {
		try {
			ClientEPF clientEpf = clientEPFRepository.findOne(id);
			ClientEpfDTO clientEpfDTO = mapper.map(clientEpf, ClientEpfDTO.class);
			clientEpfDTO.setClientID(clientEPFRepository.findOne(id).getClientMaster().getId());
			clientEpfDTO.setFamilyMemberID(clientEpf.getClientFamilyMember().getId());
			clientEpfDTO.setFinancialAssetType(clientEpf.getMasterProductClassification().getId());
			clientEpfDTO.setFirstName(clientEpf.getClientFamilyMember().getFirstName());
			clientEpfDTO.setRelationId(clientEpf.getClientFamilyMember().getLookupRelation().getId());
			clientEpfDTO.setRelationName(clientEpf.getClientFamilyMember().getLookupRelation().getDescription());
			clientEpfDTO.setGender(FinexaUtil.getGender(clientEpf.getClientFamilyMember().getClientMaster(),
					clientEpf.getClientFamilyMember().getLookupRelation()));
			double expIncreasePerc = clientEpf.getAnnualContributionIncrease().doubleValue() * 100;
			clientEpfDTO.setAnnualContributionIncrease(new BigDecimal(expIncreasePerc));
			double epfInterestRate = clientEpf.getEpfoInterestRate().doubleValue() * 100;
			clientEpfDTO.setEpfoInterestRate(new BigDecimal(epfInterestRate));
			
			
			System.out.println(clientEpfDTO.getEpfoInterestRate());
			
			return clientEpfDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getInterestRate(Date dt) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			double interestRate = 0.0;
			List<MasterEPFInterestRate> masterEPFInterestRateList = masterEPFInterestRateRepository.findAll();
			for (MasterEPFInterestRate obj : masterEPFInterestRateList) {
				if (obj.getFromDate().before(dt) && dt.before(obj.getToDate())) {
					interestRate = obj.getInterestRate().doubleValue();
				}
			}
			return interestRate;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getCagr(Date dt1) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			double cagr = 0.0;
			List<MasterIncomeGrowthRate> masterIncomeGrowthRateList = masterIncomeGrowthRateRepository.findAll();
			for (MasterIncomeGrowthRate obj : masterIncomeGrowthRateList) {
				if (obj.getFromDate().before(dt1) && dt1.before(obj.getToDate())) {
					cagr = obj.getCagr().doubleValue();
				}
			}
			return cagr;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int existAnnuity(int id) {
		// TODO Auto-generated method stub
		int i = 0;
		ClientEPF clientEPF = clientEPFRepository.findOne(id);
		
		if (clientEPF.getClientAnnuities().size() != 0) {
			i = 1;
		}
		return i;
	}

	@Override
	public int checkIfEpfPresent(int clientId) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

		List<ClientEPF> ClientEpfList = clientMaster.getClientEpfs();
		if (ClientEpfList.size() > 0)
			return 1;
		else
			return 0;
	}

	@Override
	public List<ClientFamilyMemberDTO> checkIfEpfPresentForAll(Integer clientId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);

			List<ClientFamilyMember> ClientFamilyMembers = clientMaster.getClientFamilyMembers();

			List<ClientEPF> ClientEpfs = clientMaster.getClientEpfs();

			List<ClientFamilyMember> ClientFamilyMembers1 = new ArrayList<ClientFamilyMember>();

			for (ClientEPF cf : ClientEpfs) {

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
					clientFamilyMemberDTO.setSelfCheck(true);
				} /*else {
					clientFamilyMemberDTO.setSelfCheck(false);
					if(clientFamilyMember.getLookupRelation().getId() == 1) {
						clientFamilyMemberDTO.setSpouseCheck(true);
					} else if(clientFamilyMember.getLookupRelation().getId() == 2) {
						clientFamilyMemberDTO.setSonCheck(true);
					} else if(clientFamilyMember.getLookupRelation().getId() == 3) {
						clientFamilyMemberDTO.setDaughterCheck(true);
					} else if(clientFamilyMember.getLookupRelation().getId() == 4) {
						clientFamilyMemberDTO.setFatherCheck(true);
					} else if(clientFamilyMember.getLookupRelation().getId() == 5) {
						clientFamilyMemberDTO.setMotherCheck(true);
					} else if(clientFamilyMember.getLookupRelation().getId() == 6) {
						clientFamilyMemberDTO.setBrotherCheck(true);
					} else if(clientFamilyMember.getLookupRelation().getId() == 7) {
						clientFamilyMemberDTO.setSisterCheck(true);
					} else if(clientFamilyMember.getLookupRelation().getId() == 8) {
						clientFamilyMemberDTO.setOtherCheck(true);
					}
				}*/
				
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