package com.finlabs.finexa.service;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.finlabs.finexa.dto.RiskProfileMasterDTO;
import com.finlabs.finexa.dto.RiskProfileQuestionnaireDTO;
import com.finlabs.finexa.dto.RiskProfileResponseBasedScoreDTO;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.RiskProfileMaster;
import com.finlabs.finexa.model.RiskProfileQuestionnaire;
import com.finlabs.finexa.model.RiskProfileResponseBasedScore;
import com.finlabs.finexa.model.ViewRiskProfileSumMinMaxScore;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.ClientRiskProfileResponseRepository;
import com.finlabs.finexa.repository.RiskProfileMasterRepository;
import com.finlabs.finexa.repository.RiskProfileQuestionnaireRepository;
import com.finlabs.finexa.repository.RiskProfileResponseBasedScoreRepository;
import com.finlabs.finexa.repository.ViewriskprofilesumminmaxscoreRepository;

@Service("AdvisorRiskProfileService")
@Transactional
public class AdvisorRiskProfileServiceImpl implements AdvisorRiskProfileService {
	private static Logger log = LoggerFactory.getLogger(AdvisorRiskProfileServiceImpl.class);

	@Autowired
	RiskProfileMasterRepository riskProfileMasterRepository;

	@Autowired
	AdvisorMasterRepository advisorMasterRepository;

	@Autowired
	RiskProfileResponseBasedScoreRepository riskProfileResponseBasedScoreRepository;

	@Autowired
	RiskProfileQuestionnaireRepository riskProfileQuestionnaireRepository;

	@Autowired
	private Mapper mapper;

	@Autowired
	private ViewriskprofilesumminmaxscoreRepository viewriskprofilesumminmaxscoreRepository;

	@Autowired
	private ClientRiskProfileResponseRepository clientRiskProfileResponseRepository;
	
	@Autowired
	private ClientRiskProfileService clientRiskProfileService;

	@Override
	public void saveRiskprofileName(List<RiskProfileMasterDTO> riskProfileMasterList) throws RuntimeException {
		try {
			int i = 1;
			riskProfileMasterRepository.deleteByadvisorId(riskProfileMasterList.get(0).getAdvisorID());
			for (RiskProfileMasterDTO riskProfileMasterDTO : riskProfileMasterList) {

				AdvisorMaster advisorMaster = advisorMasterRepository.findOne(riskProfileMasterDTO.getAdvisorID());
				
				RiskProfileMaster riskProfileMaster = mapper.map(riskProfileMasterDTO, RiskProfileMaster.class);
				riskProfileMaster.setAdvisorMaster(advisorMaster);
				riskProfileMaster.setRiskprofileID(i);
				riskProfileMasterRepository.save(riskProfileMaster);
				i++;

			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public void saveQuestion(List<RiskProfileQuestionnaireDTO> RiskProfileQuestionnaireDTOList)
			throws RuntimeException {
      //  System.out.println("RiskProfileQuestionnaireDTOList "+RiskProfileQuestionnaireDTOList.size());
		try {
			for (RiskProfileQuestionnaireDTO riskProfileQuestionnaireDTO : RiskProfileQuestionnaireDTOList) {

				AdvisorMaster advisorMaster = advisorMasterRepository
						.findOne(riskProfileQuestionnaireDTO.getAdvisorId());

				riskProfileQuestionnaireDTO.setQuestion(riskProfileQuestionnaireDTO.getQuestion());
				RiskProfileQuestionnaire riskProfileQuestionnaire = mapper.map(riskProfileQuestionnaireDTO,
						RiskProfileQuestionnaire.class);

				riskProfileQuestionnaire.setAdvisorMaster(advisorMaster);
				log.debug("id " + riskProfileQuestionnaire.getId() + "question  " + riskProfileQuestionnaire.getId());
				riskProfileQuestionnaireRepository.save(riskProfileQuestionnaire);
			    
				for (RiskProfileResponseBasedScoreDTO riskProfileResponseBasedScoreDTO : riskProfileQuestionnaireDTO
						.getRiskProfileResponseBasedScoresDTO()) {
					log.debug("riskProfileResponseBasedScoreDTO.getResponseText() " + riskProfileResponseBasedScoreDTO.getResponseText());					
					if (riskProfileResponseBasedScoreDTO.getResponseText().length() != 0) {
						RiskProfileResponseBasedScore riskProfileResponseBasedScore = mapper
								.map(riskProfileResponseBasedScoreDTO, RiskProfileResponseBasedScore.class);
						riskProfileResponseBasedScore.setAdvisorMaster(advisorMaster);						
						riskProfileResponseBasedScore.setRiskProfileQuestionnaire(riskProfileQuestionnaire);
						riskProfileResponseBasedScoreRepository.save(riskProfileResponseBasedScore);
					} else {
						if (riskProfileResponseBasedScoreDTO.getId() != 0) {
							RiskProfileResponseBasedScore riskProfileResponseBasedScore = riskProfileResponseBasedScoreRepository
									.findOne(riskProfileResponseBasedScoreDTO.getId());
							
							riskProfileResponseBasedScoreRepository.delete(riskProfileResponseBasedScore.getId());
						}
					}

				}

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<RiskProfileMasterDTO> getRiskProfileNameList(int advisorID) throws RuntimeException {
		AdvisorMaster advisorMaster = null;
		try {
			List<RiskProfileMasterDTO> riskProfileMasterListDTO = new ArrayList<RiskProfileMasterDTO>();
			ViewRiskProfileSumMinMaxScore viewRiskProfileSumMinMaxScore = viewriskprofilesumminmaxscoreRepository
					.findOne(advisorID);			
		 if (viewRiskProfileSumMinMaxScore != null) {
			double minScore = (double) viewRiskProfileSumMinMaxScore.getTotalMinScore();
			double maxScore = (double) viewRiskProfileSumMinMaxScore.getTotalMaxScore();
			Double scoreInterval = viewRiskProfileSumMinMaxScore.getScoreInterval();
			List<Double> minScoreList = new ArrayList<Double>();
			List<Double> maxScoreList = new ArrayList<Double>();
			minScoreList.add(minScore);
			maxScoreList.add(minScore + scoreInterval);

			advisorMaster = advisorMasterRepository.findOne(advisorID);
			List<RiskProfileMaster> RiskProfileMasterList = advisorMaster.getRiskProfileMasters();
			for (int i = 0; i < RiskProfileMasterList.size(); i++) {
				RiskProfileMasterDTO riskProfileMasterDTO = mapper.map(RiskProfileMasterList.get(i),
						RiskProfileMasterDTO.class);
    
				if (i != 0) {
					//minScoreList.add(maxScoreList.get(i - 1) + .1);
					minScoreList.add(maxScoreList.get(i - 1) + 1);
					maxScoreList.add(minScoreList.get(i) + scoreInterval);
				}

				riskProfileMasterDTO.setAdvisorID(advisorID);
				riskProfileMasterDTO.setScoreFrom(minScoreList.get(i));
				riskProfileMasterDTO.setScoreTo(maxScoreList.get(i));
				riskProfileMasterDTO.setMaxScore(maxScore);
				riskProfileMasterDTO.setMinScore(minScore);
				riskProfileMasterDTO.setScoreInterval(scoreInterval);
				riskProfileMasterDTO.setNumberOfRiskProfiles(RiskProfileMasterList.size());
				riskProfileMasterListDTO.add(riskProfileMasterDTO);

			  }
			 }else {
				    advisorMaster = advisorMasterRepository.findOne(advisorID);
			        List<RiskProfileQuestionnaire> questionnaireList = advisorMaster.getRiskProfileQuestionnaires();
			        if(!questionnaireList.isEmpty()){
			        for(RiskProfileQuestionnaire riskProfileQuestionnaire : questionnaireList) {
			        	riskProfileQuestionnaireRepository.delete(riskProfileQuestionnaire.getId());
			        }
			        }  
			        List<RiskProfileMaster> riskProfileMasters = advisorMaster.getRiskProfileMasters();
			        if(!riskProfileMasters.isEmpty()){
			        for(RiskProfileMaster riskProfileMaster : riskProfileMasters) {
			        	riskProfileMasterRepository.delete(riskProfileMaster.getId());
			        }
			        }
			 }
			return riskProfileMasterListDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}	

	@Override
	public List<RiskProfileQuestionnaireDTO> getAllQuestionWithResponse(int advisorID) throws RuntimeException {

		try {
			AdvisorMaster advisorMaster = advisorMasterRepository.findOne(advisorID);
			List<RiskProfileQuestionnaire> riskProfileQuestionnaireList = advisorMaster.getRiskProfileQuestionnaires();

			List<RiskProfileQuestionnaireDTO> RiskProfileQuestionnaireDTOList = new ArrayList<RiskProfileQuestionnaireDTO>();

			for (RiskProfileQuestionnaire riskProfileQuestionnaire : riskProfileQuestionnaireList) {

				RiskProfileQuestionnaireDTO riskProfileQuestionnaireDTO = mapper.map(riskProfileQuestionnaire,
						RiskProfileQuestionnaireDTO.class);

				riskProfileQuestionnaireDTO.setAdvisorId(advisorID);

				List<RiskProfileResponseBasedScoreDTO> riskProfileResponseBasedScoreDTOList = new ArrayList<RiskProfileResponseBasedScoreDTO>();

				for (RiskProfileResponseBasedScore riskProfileResponseBasedScore : riskProfileQuestionnaire
						.getRiskProfileResponseBasedScores()) {
					RiskProfileResponseBasedScoreDTO riskProfileResponseBasedScoreDTO = mapper
							.map(riskProfileResponseBasedScore, RiskProfileResponseBasedScoreDTO.class);
					riskProfileResponseBasedScoreDTOList.add(riskProfileResponseBasedScoreDTO);

				}
				riskProfileQuestionnaireDTO.setRiskProfileResponseBasedScoresDTO(riskProfileResponseBasedScoreDTOList);
				RiskProfileQuestionnaireDTOList.add(riskProfileQuestionnaireDTO);
			}

			return RiskProfileQuestionnaireDTOList;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	public void DeleteAllRiskProfile(int advisorID) {
		
		AdvisorMaster advisorMaster = null;
	    try {
//			advisorMaster = advisorMasterRepository.findOne(advisorID);
//			for (AdvisorUser user : advisorMaster.getAdvisorUsers()) {
//				for (ClientMaster client : user.getClientMasters()) {
//					clientRiskProfileResponseRepository.deleteByClienId(client.getId());
//				}
//
//			}
//			riskProfileResponseBasedScoreRepository.deleteByadvisorId(advisorID);
	    	advisorMaster = advisorMasterRepository.findOne(advisorID);
	        List<RiskProfileQuestionnaire> questionnaireList = advisorMaster.getRiskProfileQuestionnaires();
	        for(RiskProfileQuestionnaire riskProfileQuestionnaire : questionnaireList) {
	        	riskProfileQuestionnaireRepository.delete(riskProfileQuestionnaire.getId());
	        }
	        List<RiskProfileMaster> riskProfileMasters = advisorMaster.getRiskProfileMasters();
	        for(RiskProfileMaster riskProfileMaster : riskProfileMasters) {
	        	riskProfileMasterRepository.delete(riskProfileMaster.getId());
	        }
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public List<RiskProfileQuestionnaire> autosaveQuestion(List<RiskProfileQuestionnaireDTO> RiskProfileQuestionnaireDTOList)
			throws RuntimeException {
		RiskProfileQuestionnaire riskProfileQuestionnaire = null;
		List<RiskProfileQuestionnaire> riskProfileQuestionnaireList = new ArrayList<RiskProfileQuestionnaire>();
      //  System.out.println("RiskProfileQuestionnaireDTOList "+RiskProfileQuestionnaireDTOList.size());
		try {
			for (RiskProfileQuestionnaireDTO riskProfileQuestionnaireDTO : RiskProfileQuestionnaireDTOList) {

				AdvisorMaster advisorMaster = advisorMasterRepository
						.findOne(riskProfileQuestionnaireDTO.getAdvisorId());

				riskProfileQuestionnaireDTO.setQuestion(riskProfileQuestionnaireDTO.getQuestion());
			    riskProfileQuestionnaire = mapper.map(riskProfileQuestionnaireDTO,
						RiskProfileQuestionnaire.class);

				riskProfileQuestionnaire.setAdvisorMaster(advisorMaster);
				log.debug("id " + riskProfileQuestionnaire.getId() + "question  " + riskProfileQuestionnaire.getId());
				riskProfileQuestionnaire = riskProfileQuestionnaireRepository.save(riskProfileQuestionnaire);
				riskProfileQuestionnaireList.add(riskProfileQuestionnaire);
				for (RiskProfileResponseBasedScoreDTO riskProfileResponseBasedScoreDTO : riskProfileQuestionnaireDTO
						.getRiskProfileResponseBasedScoresDTO()) {
					log.debug("riskProfileResponseBasedScoreDTO.getResponseText() " + riskProfileResponseBasedScoreDTO.getResponseText());					
					if (riskProfileResponseBasedScoreDTO.getResponseText().length() != 0) {
						RiskProfileResponseBasedScore riskProfileResponseBasedScore = mapper
								.map(riskProfileResponseBasedScoreDTO, RiskProfileResponseBasedScore.class);
						riskProfileResponseBasedScore.setAdvisorMaster(advisorMaster);						
						riskProfileResponseBasedScore.setRiskProfileQuestionnaire(riskProfileQuestionnaire);
						riskProfileResponseBasedScoreRepository.save(riskProfileResponseBasedScore);
						
					} else {
						if (riskProfileResponseBasedScoreDTO.getId() != 0) {
							RiskProfileResponseBasedScore riskProfileResponseBasedScore = riskProfileResponseBasedScoreRepository
									.findOne(riskProfileResponseBasedScoreDTO.getId());
							
							riskProfileResponseBasedScoreRepository.delete(riskProfileResponseBasedScore.getId());
						}
					}

				}

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return riskProfileQuestionnaireList;

	}
	
	

}
