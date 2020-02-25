package com.finlabs.finexa.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.finlabs.finexa.dto.ClientMasterDTO;
import com.finlabs.finexa.dto.ClientRiskProfileResponseDTO;
import com.finlabs.finexa.dto.RiskProfileMasterDTO;
import com.finlabs.finexa.dto.RiskProfileQuestionnaireDTO;
import com.finlabs.finexa.dto.RiskProfileResponseBasedScoreDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientRiskProfileResponse;
import com.finlabs.finexa.model.RiskProfileQuestionnaire;
import com.finlabs.finexa.model.RiskProfileResponseBasedScore;
import com.finlabs.finexa.repository.AdvisorMasterRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientRiskProfileResponseRepository;
import com.finlabs.finexa.repository.RiskProfileQuestionnaireRepository;
import com.finlabs.finexa.util.FinexaConstant;

@Service("ClientRiskProfileService")
@Transactional
public class ClientRiskProfileServiceImpl implements ClientRiskProfileService {
	/////
	
	private static Logger log = LoggerFactory.getLogger(ClientRiskProfileServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private AdvisorMasterRepository advisorMasterRepository;

	@Autowired
	private ClientRiskProfileResponseRepository clientRiskProfileResponseRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	RiskProfileQuestionnaireRepository riskProfileQuestionnaireRepository;

	@Autowired
	AdvisorRiskProfileService advisorRiskProfileService;
	@Autowired
	AdvisorService advisorService;
	
	
	@Override
	public List<RiskProfileQuestionnaireDTO> getQuestionList(int advisorid) throws RuntimeException {

		try {
			log.debug("UserId " + advisorid);
			AdvisorMaster master = advisorMasterRepository.findOne(advisorid);

			List<RiskProfileQuestionnaireDTO> questionListDTO = new ArrayList<RiskProfileQuestionnaireDTO>();
			List<RiskProfileResponseBasedScoreDTO> responseListDto;

			int questioncount = 0;
			if (!master.getRiskProfileQuestionnaires().isEmpty()) {
				for (RiskProfileQuestionnaire question : master.getRiskProfileQuestionnaires()) {

					RiskProfileQuestionnaireDTO questionDTO = mapper.map(question, RiskProfileQuestionnaireDTO.class);

					questionDTO.setQuestionCount(++questioncount);
					questionDTO.setAdvisorId(advisorid);
					responseListDto = new ArrayList<RiskProfileResponseBasedScoreDTO>();
					for (RiskProfileResponseBasedScore response : question.getRiskProfileResponseBasedScores()) {
						RiskProfileResponseBasedScoreDTO responseDTO = mapper.map(response,
								RiskProfileResponseBasedScoreDTO.class);
						responseDTO.setAdvisorID(advisorid);
						responseDTO.setQuestionID(response.getRiskProfileQuestionnaire().getId());

						responseListDto.add(responseDTO);
					}
					questionDTO.setRiskProfileResponseBasedScoresDTO(responseListDto);
					questionListDTO.add(questionDTO);

				}
			}

			return questionListDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public List<Integer> getQuestions(int advisorid) throws RuntimeException {

		try {
			log.debug("UserId " + advisorid);
			AdvisorMaster master = advisorMasterRepository.findOne(advisorid);

			List<Integer> questionID = new ArrayList<Integer>();

			if (!master.getRiskProfileQuestionnaires().isEmpty()) {
				for (RiskProfileQuestionnaire question : master.getRiskProfileQuestionnaires()) {
					questionID.add(question.getId());
				}
			}

			return questionID;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/*
	 * @Override public List<ClientRiskProfileResponseDTO>
	 * getQuestionAnswerForclient(int clientId){
	 * 
	 * ClientMaster cm = clientMasterRepository.findOne(clientId);
	 * 
	 * // log.debug("id "+cm.getId()); List<ClientRiskProfileResponseDTO>
	 * clientResponseListDtoList=new ArrayList<ClientRiskProfileResponseDTO>();
	 * 
	 * 
	 * List<ClientRiskProfileResponse>
	 * clientRiskProfileResponseList=cm.getClientRiskProfileResponses();
	 * 
	 * // log.debug("size "+clientRiskProfileResponseList.size());
	 * if(clientRiskProfileResponseList.size()!=0){ for(ClientRiskProfileResponse
	 * clientRiskProfileResponses:cm.getClientRiskProfileResponses()){ //
	 * log.debug("id "+clientRiskProfileResponses.getId());
	 * 
	 * ClientRiskProfileResponseDTO
	 * clientRiskProfileResponseDTO=mapper.map(clientRiskProfileResponses,
	 * ClientRiskProfileResponseDTO.class);
	 * clientRiskProfileResponseDTO.setClientId(clientId);
	 * clientRiskProfileResponseDTO.setQuestionId(clientRiskProfileResponses.
	 * getRiskProfileQuestionnaire().getId());
	 * clientResponseListDtoList.add(clientRiskProfileResponseDTO); } } //
	 * log.debug("size "+clientResponseListDtoList.size()); return
	 * clientResponseListDtoList;
	 * 
	 * }
	 */

	@Override
	public List<RiskProfileQuestionnaireDTO> getQuestionAnswerForClient(int clientId, int masterID)
			throws RuntimeException {

		try {
			//log.debug("UserId " + masterID);
			AdvisorMaster master = advisorMasterRepository.findOne(masterID);
			//System.out.println("master "+master.getId());
			List<RiskProfileQuestionnaireDTO> questionListDTO = new ArrayList<RiskProfileQuestionnaireDTO>();
			List<RiskProfileResponseBasedScoreDTO> responseListDto;
            //System.out.println("master.getRiskProfileQuestionnaires() "+master.getRiskProfileQuestionnaires().size());
			int questioncount = 0;
			for (RiskProfileQuestionnaire question : master.getRiskProfileQuestionnaires()) {
                
				RiskProfileQuestionnaireDTO questionDTO = mapper.map(question, RiskProfileQuestionnaireDTO.class);

				questionDTO.setQuestionCount(++questioncount);
				questionDTO.setAdvisorId(masterID);
				responseListDto = new ArrayList<RiskProfileResponseBasedScoreDTO>();
				 //System.out.println("question.getRiskProfileResponseBasedScores() "+question.getRiskProfileResponseBasedScores().size());
				for (RiskProfileResponseBasedScore response : question.getRiskProfileResponseBasedScores()) {
					RiskProfileResponseBasedScoreDTO responseDTO = mapper.map(response,
							RiskProfileResponseBasedScoreDTO.class);
					responseDTO.setAdvisorID(masterID);
					responseDTO.setQuestionID(response.getRiskProfileQuestionnaire().getId());

					responseListDto.add(responseDTO);
				}
				questionDTO.setRiskProfileResponseBasedScoresDTO(responseListDto);
				// System.out.println("question.getClientRiskProfileResponses().size() "+question.getClientRiskProfileResponses().size());
				if (question.getClientRiskProfileResponses().size() != 0) {
					for (ClientRiskProfileResponse clientResponse : question.getClientRiskProfileResponses()) {
						if (clientResponse.getClientMaster().getId() == clientId) {
							questionDTO.setClientRiskProfileResponsesDTO(
									mapper.map(clientResponse, ClientRiskProfileResponseDTO.class));
						}
					}
					questionListDTO.add(questionDTO);
				}
			}
           // System.out.println("questionListDTO "+questionListDTO.size());
			return questionListDTO;
		} /*catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}*/
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public ClientRiskProfileResponseDTO save(ClientRiskProfileResponseDTO clientRiskProfileResponseDTO)
			throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientRiskProfileResponseDTO.getClientId());

			ClientRiskProfileResponse clientRiskProfileResponse = mapper.map(clientRiskProfileResponseDTO,
					ClientRiskProfileResponse.class);
			clientRiskProfileResponse.setClientMaster(cm);

			RiskProfileQuestionnaire riskProfileQuestionnaire = riskProfileQuestionnaireRepository
					.findOne(clientRiskProfileResponseDTO.getQuestionId());
			clientRiskProfileResponse.setRiskProfileQuestionnaire(riskProfileQuestionnaire);
			log.debug("clientRiskProfileResponse id" + clientRiskProfileResponse.getId());

			clientRiskProfileResponseRepository.save(clientRiskProfileResponse);
		

			return clientRiskProfileResponseDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	@Override
	public void saveScore(int masterId, int clientId,  HttpServletRequest request) {
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			int score = 0;
			for (ClientRiskProfileResponse clientRiskProfileResponses : cm.getClientRiskProfileResponses()) {

				RiskProfileQuestionnaire riskProfileQuestionnaire = clientRiskProfileResponses
						.getRiskProfileQuestionnaire();
				for (RiskProfileResponseBasedScore riskProfileResponseBasedScore : riskProfileQuestionnaire
						.getRiskProfileResponseBasedScores()) {
					if (riskProfileResponseBasedScore.getResponseID() == clientRiskProfileResponses.getResponseID()) {
						score += riskProfileResponseBasedScore.getScore();
					}
				}

			}
			log.debug("save score   " + score);
			List<RiskProfileMasterDTO> RiskProfileMasterDTOList = advisorRiskProfileService
					.getRiskProfileNameList(masterId);

			for (RiskProfileMasterDTO riskProfileMasterDTO : RiskProfileMasterDTOList) {
				if (score >= riskProfileMasterDTO.getScoreFrom() && score <= riskProfileMasterDTO.getScoreTo()) {
					cm.setRiskProfile(riskProfileMasterDTO.getRiskprofileID());
					log.debug("save risk profile " + cm.getRiskProfile());
					break;
				}
			}
			
			advisorService.deletetAUMCacheMap(cm.getAdvisorUser().getId(),request);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) {
		if (id != 0) {
			clientRiskProfileResponseRepository.delete(id);
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * @Override public ClientRiskProfileResponseDTO getQuestionAnswerById(int id) {
	 * ClientRiskProfileResponse
	 * ClientRiskProfileResponse=clientRiskProfileResponseRepository.findOne(id) ;
	 * ClientRiskProfileResponseDTO
	 * clientRiskProfileResponseDTO=mapper.map(ClientRiskProfileResponse,
	 * ClientRiskProfileResponseDTO.class);
	 * 
	 * clientRiskProfileResponseDTO.setClientId(ClientRiskProfileResponse.
	 * getClientMaster().getId());
	 * clientRiskProfileResponseDTO.setQuestionId(ClientRiskProfileResponse.
	 * getRiskProfileQuestionnaire().getId());
	 * 
	 * 
	 * return clientRiskProfileResponseDTO; }
	 */

	@Override
	public ClientMasterDTO getRiskProfile(int clientID, int advisorID) throws RuntimeException {
		int score = 0;
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientID);

			Timestamp lastUpdatedon = null;
			for (ClientRiskProfileResponse clientRiskProfileResponses : cm.getClientRiskProfileResponses()) {

				lastUpdatedon = clientRiskProfileResponses.getLastUpdatedOn();

				RiskProfileQuestionnaire riskProfileQuestionnaire = clientRiskProfileResponses
						.getRiskProfileQuestionnaire();
				for (RiskProfileResponseBasedScore riskProfileResponseBasedScore : riskProfileQuestionnaire
						.getRiskProfileResponseBasedScores()) {
					if (riskProfileResponseBasedScore.getResponseID() == clientRiskProfileResponses.getResponseID()) {
						log.debug("in get Risk profile score " + riskProfileResponseBasedScore.getScore());
						score += riskProfileResponseBasedScore.getScore();
						log.debug("score " + score);

					}
				}

			}
			// cm.setRiskProfile(score);

			// ClientMaster clientMaster=clientMasterRepository.save(cm);

			ClientMasterDTO clientMasteDTO = mapper.map(cm, ClientMasterDTO.class);

			List<RiskProfileMasterDTO> RiskProfileMasterDTOList = advisorRiskProfileService
					.getRiskProfileNameList(advisorID);

			log.debug("outside in get Risk profile score " + score);
			for (RiskProfileMasterDTO riskProfileMasterDTO : RiskProfileMasterDTOList) {
				
			}

			for (RiskProfileMasterDTO riskProfileMasterDTO : RiskProfileMasterDTOList) {
				if (score >= riskProfileMasterDTO.getScoreFrom() && score <= riskProfileMasterDTO.getScoreTo()) {
					clientMasteDTO.setRiskProfileName(riskProfileMasterDTO.getName());
					log.debug("database risk Profile id " + cm.getRiskProfile());
					clientMasteDTO.setRiskProfileScore(cm.getRiskProfile());
					break;
				}
			}

			clientMasteDTO.setLastUpdatedOn(lastUpdatedon);
			return clientMasteDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public void getScoreforAllClient(int advisorID) throws RuntimeException {
		try {
			AdvisorMaster advisorMaster = advisorMasterRepository.findOne(advisorID);

			for (AdvisorUser user : advisorMaster.getAdvisorUsers()) {
				for (ClientMaster cm : user.getClientMasters()) {
					int score = 0;
					Timestamp lastUpdatedon = null;
					for (ClientRiskProfileResponse clientRiskProfileResponses : cm.getClientRiskProfileResponses()) {

						lastUpdatedon = clientRiskProfileResponses.getLastUpdatedOn();

						RiskProfileQuestionnaire riskProfileQuestionnaire = clientRiskProfileResponses
								.getRiskProfileQuestionnaire();
						for (RiskProfileResponseBasedScore riskProfileResponseBasedScore : riskProfileQuestionnaire
								.getRiskProfileResponseBasedScores()) {
							if (riskProfileResponseBasedScore.getResponseID() == clientRiskProfileResponses
									.getResponseID()) {
								score += riskProfileResponseBasedScore.getScore();
							}
						}

					}
					log.debug("score " + cm.getRiskProfile());
					List<RiskProfileMasterDTO> RiskProfileMasterDTOList = advisorRiskProfileService
							.getRiskProfileNameList(advisorID);

					for (RiskProfileMasterDTO riskProfileMasterDTO : RiskProfileMasterDTOList) {
						if (score >= riskProfileMasterDTO.getScoreFrom()
								&& score <= riskProfileMasterDTO.getScoreTo()) {
							log.debug("score " + cm.getRiskProfile());
							cm.setRiskProfile(riskProfileMasterDTO.getRiskprofileID());
							log.debug("score " + cm.getRiskProfile());
							break;
						}
					}

				}

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	@Override
	public ClientMasterDTO getRiskProfileScorByClientId(int clientID) {
		ClientMaster clientMaster = clientMasterRepository.findOne(clientID);
		ClientMasterDTO clientMasteDTO = mapper.map(clientMaster, ClientMasterDTO.class);
		clientMasteDTO.setRiskProfileScore(clientMaster.getRiskProfile());
		return clientMasteDTO;
	}

	@Override
	public void disableRiskResponseData(Integer clientId) throws RuntimeException, CustomFinexaException {

		try {
			clientRiskProfileResponseRepository.disableRiskResponseData(clientId);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new CustomFinexaException(FinexaConstant.MY_CLIENT_RISK_PROFILE_RESPONSE_MODULE, "Nothing Specific",
					"Failed to disable Risk Response Data", e);
		}
	}

	@Override
	public void deleteByAdvisorId(int advisorID) throws RuntimeException {
		try {
			AdvisorMaster advisorMaster = advisorMasterRepository.findOne(advisorID);

			for (AdvisorUser user : advisorMaster.getAdvisorUsers()) {
				for (ClientMaster client : user.getClientMasters()) {
					List<ClientRiskProfileResponse> clientRiskProfileResponse = clientRiskProfileResponseRepository
							.findByClientMaster(client);

					if (!clientRiskProfileResponse.isEmpty()) {
						clientRiskProfileResponseRepository.deleteByClienId(client.getId());
					} else {
						System.out.println("Empty============");
					}
					if (client.getRiskProfile() != null) {
						client.setOlderRiskProfile(client.getRiskProfile());
					}
					client.setRiskProfile(null);
				}

			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public ClientRiskProfileResponse autoSave(ClientRiskProfileResponseDTO clientRiskProfileResponseDTO)
			throws RuntimeException {

		try {
			ClientMaster cm = clientMasterRepository.findOne(clientRiskProfileResponseDTO.getClientId());

			ClientRiskProfileResponse clientRiskProfileResponse = mapper.map(clientRiskProfileResponseDTO,
					ClientRiskProfileResponse.class);
			clientRiskProfileResponse.setClientMaster(cm);
			//System.out.println("clientRiskProfileResponseDTO.getQuestionId() "+clientRiskProfileResponseDTO.getQuestionId());
			RiskProfileQuestionnaire riskProfileQuestionnaire = riskProfileQuestionnaireRepository
					.findOne(clientRiskProfileResponseDTO.getQuestionId());
			//System.out.println("clientRiskProfileResponseDTO.getQuestionId() "+riskProfileQuestionnaire.getId());
			clientRiskProfileResponse.setRiskProfileQuestionnaire(riskProfileQuestionnaire);
			log.debug("clientRiskProfileResponse id" + clientRiskProfileResponse.getId());

		    clientRiskProfileResponse = clientRiskProfileResponseRepository.save(clientRiskProfileResponse);

			return clientRiskProfileResponse;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
	
	@Override
	public void autoSaveScore(int masterId, int clientId) {
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			int score = 0;
			for (ClientRiskProfileResponse clientRiskProfileResponses : cm.getClientRiskProfileResponses()) {

				RiskProfileQuestionnaire riskProfileQuestionnaire = clientRiskProfileResponses
						.getRiskProfileQuestionnaire();
				for (RiskProfileResponseBasedScore riskProfileResponseBasedScore : riskProfileQuestionnaire
						.getRiskProfileResponseBasedScores()) {
					if (riskProfileResponseBasedScore.getResponseID() == clientRiskProfileResponses.getResponseID()) {
						score += riskProfileResponseBasedScore.getScore();
					}
				}

			}
			log.debug("save score   " + score);
			List<RiskProfileMasterDTO> RiskProfileMasterDTOList = advisorRiskProfileService
					.getRiskProfileNameList(masterId);

			for (RiskProfileMasterDTO riskProfileMasterDTO : RiskProfileMasterDTOList) {
				if (score >= riskProfileMasterDTO.getScoreFrom() && score <= riskProfileMasterDTO.getScoreTo()) {
					cm.setRiskProfile(riskProfileMasterDTO.getRiskprofileID());
					log.debug("save risk profile " + cm.getRiskProfile());
					break;
				}
			}
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
}
