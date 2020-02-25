package com.finlabs.finexa.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finlabs.finexa.dto.AdvisorProductRecoDTO;
import com.finlabs.finexa.dto.ProductRecommendationTransactDTO;
import com.finlabs.finexa.exception.CustomFinexaException;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.AdvisorProductRecoService;
import com.finlabs.finexa.service.ClientTransactService;

@RestController
public class AdvisorProductRecoController {

	private static Logger log = LoggerFactory.getLogger(AdvisorProductRecoController.class);

	@Autowired
	AdvisorProductRecoService advisorProductRecoService;
	
	@Autowired
	ClientTransactService clientTransactService;

	@PreAuthorize("hasAnyRole('Admin','GoalPlanningAddEdit','PortfolioManagementAddEdit','FinancialPlanningAddEdit')")
	@RequestMapping(value = "/saveAdvisorProductReco", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> saveAdvisorProductReco(@RequestParam(value = "advisorId") int advisorId,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "goalId") int goalId,
			@RequestParam(value = "module") String module, @RequestParam(value = "jsonData") Object jsonData) {
		log.info("AdvisorProductRecoController >>> Entering saveAdvisorProductReco() ");
		AdvisorProductRecoDTO advisorProductRecoDTO = null;
		try {
			advisorProductRecoDTO = advisorProductRecoService.save(advisorId, clientId, goalId, module,
					jsonData.toString());
			log.info("AdvisorProductRecoController <<< Exiting saveAdvisorProductReco() ");
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientService", "111",
					"Failed to save Product Recommendation , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);

		}
		return new ResponseEntity<AdvisorProductRecoDTO>(advisorProductRecoDTO, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('Admin','GoalPlanningView','PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getAdvisorProductReco", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getAdvisorProductReco(@RequestParam(value = "advisorId") int advisorId,
			@RequestParam(value = "clientId") int clientId, @RequestParam(value = "goalId") int goalId,
			@RequestParam(value = "module") String module) {
		log.info("AdvisorProductRecoController >>> Entering getAdvisorProductReco() ");

		try {
			List<AdvisorProductRecoDTO> advisorProductRecoDTOList = null;
			advisorProductRecoDTOList = advisorProductRecoService.getAllProductRecoByAdvisorId(advisorId, clientId,
					goalId, module);
			log.info("AdvisorProductRecoController <<< Exiting getAdvisorProductReco() ");
			return new ResponseEntity<List<AdvisorProductRecoDTO>>(advisorProductRecoDTOList, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("AdvisorProductReco", "111",
					"Failed to get Product Recommendation , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		}
	}

}
