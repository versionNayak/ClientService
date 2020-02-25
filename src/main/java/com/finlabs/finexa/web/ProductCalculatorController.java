package com.finlabs.finexa.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlabs.finexa.dto.AdvanceLoanCalculatorDTO;
import com.finlabs.finexa.dto.Annuity2ProductCalculatorDTO;
import com.finlabs.finexa.dto.AtalPensionYojanaCalculatorDTO;
import com.finlabs.finexa.dto.BankFDSTDRCDCPDTO;
import com.finlabs.finexa.dto.BankFDTDRPCDTO;
import com.finlabs.finexa.dto.BankRecurringDespositCalculatorDTO;
import com.finlabs.finexa.dto.BondDebentureCalculatorDTO;
import com.finlabs.finexa.dto.EPF2CalculatorDTO;
import com.finlabs.finexa.dto.EquityCalculatorDTO;
import com.finlabs.finexa.dto.ErrorDTO;
import com.finlabs.finexa.dto.KisanVikasPatraDTO;
import com.finlabs.finexa.dto.MutualFundLumpsumPCDTO;
import com.finlabs.finexa.dto.MutualFundSIPPCDTO;
import com.finlabs.finexa.dto.NPSCalculatorDTO;
import com.finlabs.finexa.dto.PONSCCalculatorDTO;
import com.finlabs.finexa.dto.PPFFixedAmountCalculatorDTO;
import com.finlabs.finexa.dto.PerpetualBondCalculatorDTO;
import com.finlabs.finexa.dto.PostOfficeMISCalculatorDTO;
import com.finlabs.finexa.dto.PostOfficeRDCalculatorDTO;
import com.finlabs.finexa.dto.PostOfficeTDCalculatorDTO;
import com.finlabs.finexa.dto.SeniorCitizenSavingSchemeCalculatorDTO;
import com.finlabs.finexa.dto.SimpleLoanEMIBasedCalculatorDTO;
import com.finlabs.finexa.dto.SimpleLoanNonEMIBasedCalculatorDTO;
import com.finlabs.finexa.dto.SukanyaSamriddhiSchemeCalculatorDTO;
import com.finlabs.finexa.dto.ZeroCouponBondCalculatorDTO;
import com.finlabs.finexa.resources.model.AdvanceLoanCalLookup;
import com.finlabs.finexa.resources.model.AdvanceLoanCalculator;
import com.finlabs.finexa.resources.model.Annuity2ProductCalculator;
import com.finlabs.finexa.resources.model.AtalPensionYojana;
import com.finlabs.finexa.resources.model.BankBondDebenturesLookup;
import com.finlabs.finexa.resources.model.BankFDSTDRCDCP;
import com.finlabs.finexa.resources.model.BankFDTDRPC;
import com.finlabs.finexa.resources.model.BankRecurringDeposit;
import com.finlabs.finexa.resources.model.BondDebentures;
import com.finlabs.finexa.resources.model.EPF2Calculator;
import com.finlabs.finexa.resources.model.EquityCalculator;
import com.finlabs.finexa.resources.model.KisanVikasPatra;
import com.finlabs.finexa.resources.model.MutualFundLumpsumSip;
import com.finlabs.finexa.resources.model.NPSCAL;
import com.finlabs.finexa.resources.model.PONSC;
import com.finlabs.finexa.resources.model.PORecurringDeposit;
import com.finlabs.finexa.resources.model.POTimeDeposit;
import com.finlabs.finexa.resources.model.PPFFixedAmountDeposit;
import com.finlabs.finexa.resources.model.PerpetualBond;
import com.finlabs.finexa.resources.model.PerpetualBondLookup;
import com.finlabs.finexa.resources.model.PostOfficeMonthlyIncomeScheme;
import com.finlabs.finexa.resources.model.SeniorCitizenSavingScheme;
import com.finlabs.finexa.resources.model.SimpleLoanCalLookup;
import com.finlabs.finexa.resources.model.SimpleLoanCalculator;
import com.finlabs.finexa.resources.model.SukanyaSamriddhiScheme;
import com.finlabs.finexa.resources.model.ZeroCouponBond;
import com.finlabs.finexa.resources.service.AdvanceLoanCalculatorService;
import com.finlabs.finexa.resources.service.Annuity2ProductService;
import com.finlabs.finexa.resources.service.AtalPensionYojanaService;
import com.finlabs.finexa.resources.service.BankFDSTDRCDCPService;
import com.finlabs.finexa.resources.service.BankFDTDRService;
import com.finlabs.finexa.resources.service.BankRecurringDespositService;
import com.finlabs.finexa.resources.service.BondDebenturesService;
import com.finlabs.finexa.resources.service.EPF2Service;
import com.finlabs.finexa.resources.service.EquityCalculatorService;
import com.finlabs.finexa.resources.service.KisanVikasPatraService;
import com.finlabs.finexa.resources.service.MutualFundLumpsumSipService;
import com.finlabs.finexa.resources.service.NPSCalService;
import com.finlabs.finexa.resources.service.PONSCService;
import com.finlabs.finexa.resources.service.PORecurringDespositService;
import com.finlabs.finexa.resources.service.POTimeDespositService;
import com.finlabs.finexa.resources.service.PPFFixedAmountService;
import com.finlabs.finexa.resources.service.PerpetualBondService;
import com.finlabs.finexa.resources.service.PostOfficeMonthlyIncomeSchemeService;
import com.finlabs.finexa.resources.service.SeniorCitizenSavingSchemeService;
import com.finlabs.finexa.resources.service.SimpleLoanCalEMIBasedService;
import com.finlabs.finexa.resources.service.SimpleLoanCalNonEMIBasedService;
import com.finlabs.finexa.resources.service.SukanyaSamriddhiSchemeService;
import com.finlabs.finexa.resources.service.ZeroCouponService;

@RestController
public class ProductCalculatorController {

	SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MMM-yy");

	private static Logger log = LoggerFactory.getLogger(ProductCalculatorController.class);
	@Autowired
	private AdvanceLoanCalculatorService advanceLoanCalculatorService;
	@Autowired
	private Annuity2ProductService annuity2ProductService;
	@Autowired
	private AtalPensionYojanaService atalPensionYojanaService;
	@Autowired
	private BankFDSTDRCDCPService bankFDSTDRCDCPService;
	@Autowired
	private BankFDTDRService bankFDTDRService;
	@Autowired
	private BankRecurringDespositService bankRecurringDespositService;
	@Autowired
	private BondDebenturesService bondDebenturesService;
	@Autowired
	private EPF2Service ePF2Service;
	@Autowired
	private EquityCalculatorService equityCalculatorService;
	@Autowired
	private KisanVikasPatraService kisanVikasPatraService;
	//@Autowired
	//private MutualFundLumpsumSipService mutualFundLumpsumSipService;
	@Autowired
	private NPSCalService nPSCalService;
	@Autowired
	private PerpetualBondService perpetualBondService;
	@Autowired
	private PONSCService pONSCService;
	@Autowired
	private PORecurringDespositService pORecurringDespositService;
	@Autowired
	private PostOfficeMonthlyIncomeSchemeService postOfficeMonthlyIncomeSchemeService;
	@Autowired
	private POTimeDespositService poTimeDespositService;
	@Autowired
	private PPFFixedAmountService pPFFixedAmountService;
	@Autowired
	private SeniorCitizenSavingSchemeService seniorCitizenSavingSchemeService;
	@Autowired
	private SimpleLoanCalEMIBasedService simpleLoanCalEMIBasedService;
	@Autowired
	private SimpleLoanCalNonEMIBasedService simpleLoanCalNonEMIBasedService;
	@Autowired
	private SukanyaSamriddhiSchemeService sukanyaSamriddhiSchemeService;
	@Autowired
	private ZeroCouponService zeroCouponService;

	@RequestMapping(value = "/advanceLoanCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> advanceLoanCalculator(
			@Valid @RequestBody AdvanceLoanCalculatorDTO advanceLoanCalculatorDTO, Errors errors)
			throws JsonProcessingException {
		AdvanceLoanCalculator advLoanCal = advanceLoanCalculatorService.calculateAdvanceCalculator(
				advanceLoanCalculatorDTO.getLoanType(), advanceLoanCalculatorDTO.getOrignalLoanAmount(),
				advanceLoanCalculatorDTO.getAnnualInterestRate(), advanceLoanCalculatorDTO.getLoanTenure(),
				advanceLoanCalculatorDTO.getStartDateEMI());

		ObjectMapper objectMapper = new ObjectMapper();

		String arrayToJson = objectMapper.writeValueAsString(advanceLoanCalculatorDTO.getActionList());
		JSONArray jsonArr = new JSONArray(arrayToJson);

		List<AdvanceLoanCalLookup> calulatorLookupDetails = advanceLoanCalculatorService.getAdvanceLoanCalculatorTable(
				jsonArr, advLoanCal.getLoanStartDate(), advLoanCal.getInterestRate(), advLoanCal.getLoanTenure(),
				advLoanCal.getLoanEndDate(), advLoanCal.getLoanAmount(), advLoanCal.getOriginalEMiCount(),
				advLoanCal.getOriginalEmiAmount());
		return new ResponseEntity<List<AdvanceLoanCalLookup>>(calulatorLookupDetails, HttpStatus.OK);
	}

	@RequestMapping(value = "/annuity2ProductCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> annuity2ProductCalculator(
			@Valid @RequestBody Annuity2ProductCalculatorDTO annuity2ProductCalculatorDTO, Errors errors) {

		log.debug("Annuity2ProductCalculatorDTO: " + annuity2ProductCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		Annuity2ProductCalculator annuity2ProductCalculator = annuity2ProductService.getAnnuityProductValues(
				annuity2ProductCalculatorDTO.getClientDOB(), annuity2ProductCalculatorDTO.getPensionableCorpus(),
				annuity2ProductCalculatorDTO.getAnnuityRate(), annuity2ProductCalculatorDTO.getLifeExpectancySelf(),
				annuity2ProductCalculatorDTO.getLifeExpectancySpouse(),
				annuity2ProductCalculatorDTO.getAnnuityPayoutFreq(), annuity2ProductCalculatorDTO.getAnnuityStartDate(),
				annuity2ProductCalculatorDTO.getAnnuityType(), annuity2ProductCalculatorDTO.getAnnuityGrowthRate(),
				annuity2ProductCalculatorDTO.getNoOfYearsService());

		return new ResponseEntity<Annuity2ProductCalculator>(annuity2ProductCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/atalPensionYojanaCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> atalPensionYojanaCalculator(
			@Valid @RequestBody AtalPensionYojanaCalculatorDTO atalPensionYojanaCalculatorDTO, Errors errors) {

		log.debug("atalPensionYojanaCalculator: " + atalPensionYojanaCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		AtalPensionYojana atalPensionYojanaCalculator = atalPensionYojanaService.getAtalPensionYojanaCal(
				atalPensionYojanaCalculatorDTO.getClientDOB(), atalPensionYojanaCalculatorDTO.getContrFreq(),
				atalPensionYojanaCalculatorDTO.getMonthlyPenReq(), atalPensionYojanaCalculatorDTO.getRetirementAge(),
				atalPensionYojanaCalculatorDTO.getContrStartDate(), atalPensionYojanaCalculatorDTO.getLifeExptenYear());

		return new ResponseEntity<AtalPensionYojana>(atalPensionYojanaCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/bankFDSTDRCDCP", method = RequestMethod.POST)
	public ResponseEntity<?> bankFDSTDRCDCP(@Valid @RequestBody BankFDSTDRCDCPDTO bankFDSTDRCDCPDTO, Errors errors) {
		log.debug("BankFDSTDRCDCPDTO : " + bankFDSTDRCDCPDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		BankFDSTDRCDCP bankFDSTDRCDCP = bankFDSTDRCDCPService.getFDSTDRCDCPOutputList(
				bankFDSTDRCDCPDTO.getDepositAmount(), bankFDSTDRCDCPDTO.getAnnualInterestRate(),
				bankFDSTDRCDCPDTO.getTenureType(), bankFDSTDRCDCPDTO.getTenure(),
				bankFDSTDRCDCPDTO.getCompoundingFrequency(), bankFDSTDRCDCPDTO.getDepositDate());

		return new ResponseEntity<BankFDSTDRCDCP>(bankFDSTDRCDCP, HttpStatus.OK);
	}

	@RequestMapping(value = "/BankFDTDRPC", method = RequestMethod.POST)
	public ResponseEntity<?> getBankFDTDR(@Valid @RequestBody BankFDTDRPCDTO bankFDTDRPCDTO, Errors errors) {
		log.debug("bankFDTDRPCDTO : " + bankFDTDRPCDTO);
		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		BankFDTDRPC retDTO = bankFDTDRService.getFDTDROutput(bankFDTDRPCDTO.getAmountDeposited(),
				bankFDTDRPCDTO.getRateOfInterest(), bankFDTDRPCDTO.getTenureType(), bankFDTDRPCDTO.getTenure(),
				bankFDTDRPCDTO.getInterestFrequency(), bankFDTDRPCDTO.getDepositDate());

		return new ResponseEntity<BankFDTDRPC>(retDTO, HttpStatus.OK);

	}

	@RequestMapping(value = "/bankRecurringDespositCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> bankRecurringDespositCalculator(
			@Valid @RequestBody BankRecurringDespositCalculatorDTO bankRecurringDespositCalculatorDTO, Errors errors) {

		log.debug("BankRecurringDespositCalculatorDTO: " + bankRecurringDespositCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		BankRecurringDeposit bankRecurringDespositCalculator = bankRecurringDespositService
				.getRecurringDepositCalculatedList(bankRecurringDespositCalculatorDTO.getDeposit(),
						bankRecurringDespositCalculatorDTO.getRateOfInterest(),
						bankRecurringDespositCalculatorDTO.getYear(), bankRecurringDespositCalculatorDTO.getMonth(),
						bankRecurringDespositCalculatorDTO.getRdDepositFreq(),
						bankRecurringDespositCalculatorDTO.getCompundingFreq(),
						bankRecurringDespositCalculatorDTO.getDepositDate());

		return new ResponseEntity<BankRecurringDeposit>(bankRecurringDespositCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/bondDebentureCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> bondDebentureCalculator(
			@Valid @RequestBody BondDebentureCalculatorDTO bondDebentureCalculatorDTO, Errors errors) {

		log.debug("BondDebentureCalculatorDTO: " + bondDebentureCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		BondDebentures bondDebCal = bondDebenturesService.calculateDebenturesValue(
				bondDebentureCalculatorDTO.getTenureType(), bondDebentureCalculatorDTO.getInvestmentDate(),
				bondDebentureCalculatorDTO.getTenure(), bondDebentureCalculatorDTO.getCoupounPayoutFrequency(),
				bondDebentureCalculatorDTO.getInterestCouponRate(), bondDebentureCalculatorDTO.getBondFaceValue(),
				bondDebentureCalculatorDTO.getNumberOfBondsPurchased(), bondDebentureCalculatorDTO.getCurrentYield());

		List<BankBondDebenturesLookup> calulatorLookupDetails = bondDebenturesService.getBondDebentures(
				bondDebCal.getNumberOfBondsPurchased(), bondDebCal.getBondFaceValue(),
				bondDebentureCalculatorDTO.getCoupounPayoutFrequency(), bondDebentureCalculatorDTO.getInvestmentDate(),
				bondDebCal.getDaysToMaturity(), bondDebCal.getCouponReceived(), bondDebCal.getTotalMonths());

		BondDebentureCalculatorDTO retDTO = new BondDebentureCalculatorDTO();
		retDTO.setBondDebCal(bondDebCal);
		retDTO.setBankBondDebenturesLookup(calulatorLookupDetails);

		return new ResponseEntity<BondDebentureCalculatorDTO>(retDTO, HttpStatus.OK);

	}

	@RequestMapping(value = "/epf2Calculator", method = RequestMethod.POST)
	public ResponseEntity<?> epf2Calculator(@Valid @RequestBody EPF2CalculatorDTO epf2CalculatorDTO, Errors errors) {

		log.debug("EPF2CalculatorDTO: " + epf2CalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		EPF2Calculator epf2Calculator = ePF2Service.getEPF2CaculatedValues(epf2CalculatorDTO.getClientDOB(),
				epf2CalculatorDTO.getCurrEPFBal(), epf2CalculatorDTO.getCurrEPSBal(),
				epf2CalculatorDTO.getMonthlyBasicDA(), epf2CalculatorDTO.getExpectedIncreaseSal(),
				epf2CalculatorDTO.getContributionUptoAge(),epf2CalculatorDTO.getWithdrawalAgeUpto(),
				epf2CalculatorDTO.getIncreaseMonth());

		return new ResponseEntity<EPF2Calculator>(epf2Calculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/equityCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> equityCalculator(@Valid @RequestBody EquityCalculatorDTO equityCalculatorDTO,
			Errors errors) {

		log.debug("EquityCalculatorDTO: " + equityCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		EquityCalculator equityCalculator = equityCalculatorService.getEquityCalculation(
				equityCalculatorDTO.getDateOfPurchased(), equityCalculatorDTO.getAmountInvested(),
				equityCalculatorDTO.getNoOfSharesPurchased(), equityCalculatorDTO.getIsin());

		return new ResponseEntity<EquityCalculator>(equityCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/kisanVikasPatraCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> kisanVikasPatraCalculator(@Valid @RequestBody KisanVikasPatraDTO kisanVikasPatraDTO,
			Errors errors) {

		log.debug("KisanVikasPatraDTO: " + kisanVikasPatraDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		KisanVikasPatra kisanVikasPatraCalculator = kisanVikasPatraService
				.getKisanVikasPatraCalc(kisanVikasPatraDTO.getDeposit(), kisanVikasPatraDTO.getDepositDate());

		return new ResponseEntity<KisanVikasPatra>(kisanVikasPatraCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/mutualFundLumpsum", method = RequestMethod.POST)
	public ResponseEntity<?> getMutualFundLumpsum(@Valid @RequestBody MutualFundLumpsumPCDTO mutualFundLumpsumPCDTO,
			Errors errors) {

		log.debug("MutualFundLumpsumPCDTO: " + mutualFundLumpsumPCDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}
		MutualFundLumpsumSipService mutualFundLumpsumSipService = new MutualFundLumpsumSipService();
		MutualFundLumpsumSip retDTO = mutualFundLumpsumSipService.getMutualFundLumpsumCalculation(
				mutualFundLumpsumPCDTO.getAmountInvested(), mutualFundLumpsumPCDTO.getIsin(),
				mutualFundLumpsumPCDTO.getInvestedDate(), mutualFundLumpsumPCDTO.getUnitPurchased());

		return new ResponseEntity<MutualFundLumpsumSip>(retDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/mutualFundSIP", method = RequestMethod.POST)
	public ResponseEntity<?> mutualFundSIP(@Valid @RequestBody MutualFundSIPPCDTO mutualFundSIPPCDTO, Errors errors) {

		log.debug("MutualFundSIPPCDTO: " + mutualFundSIPPCDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}
		MutualFundLumpsumSipService mutualFundLumpsumSipService = new MutualFundLumpsumSipService();
		MutualFundLumpsumSip mutualFundSIP = mutualFundLumpsumSipService.getMutualFundSIPCalculation(
				mutualFundSIPPCDTO.getSipAmount(), mutualFundSIPPCDTO.getIsin(), mutualFundSIPPCDTO.getSipStartDate(),
				mutualFundSIPPCDTO.getSipInstallment(), mutualFundSIPPCDTO.getSipFrequency());

		return new ResponseEntity<MutualFundLumpsumSip>(mutualFundSIP, HttpStatus.OK);
	}

	@RequestMapping(value = "/NPSCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> npsCalculator(@Valid @RequestBody NPSCalculatorDTO npsCalculatorDTO, Errors errors) {

		log.debug("NPSCalculatorDTO: " + npsCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		NPSCAL npsCalculator = nPSCalService.calculateNPSValues(npsCalculatorDTO.getClientDOB(),
				npsCalculatorDTO.getCurrNPSBal(), npsCalculatorDTO.getEmpolyeeCont(),
				npsCalculatorDTO.getEmpolyeeContFreq(), npsCalculatorDTO.getEmpolyerCont(),
				npsCalculatorDTO.getEmpolyerContFreq(), npsCalculatorDTO.getAssetClassEAll(),
				npsCalculatorDTO.getAssetClassCAll(), npsCalculatorDTO.getAssetClassGAll(),
				npsCalculatorDTO.getEmpolyeeContAge(), npsCalculatorDTO.getEmpolyerContAge(),
				npsCalculatorDTO.getRetirementAge(), npsCalculatorDTO.getPlanType());

		return new ResponseEntity<NPSCAL>(npsCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/perpetualBondCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> perpetualBondCalculator(
			@Valid @RequestBody PerpetualBondCalculatorDTO perpetualBondCalculatorDTO, Errors errors) {

		log.debug("perpetualBondCalculator: " + perpetualBondCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		PerpetualBond perpetualBondCal = perpetualBondService.calculateDebenturesValue(
				perpetualBondCalculatorDTO.getTenureType(), perpetualBondCalculatorDTO.getInvestmentDate(),
				perpetualBondCalculatorDTO.getTenure(), perpetualBondCalculatorDTO.getCoupounPayoutFrequency(),
				perpetualBondCalculatorDTO.getInterestCouponRate(), perpetualBondCalculatorDTO.getBondFaceValue(),
				perpetualBondCalculatorDTO.getNumberOfBondsPurchased(), perpetualBondCalculatorDTO.getCurrentYield());

		List<PerpetualBondLookup> calulatorLookupDetails = perpetualBondService.getBondDebentures(
				perpetualBondCalculatorDTO.getNumberOfBondsPurchased() * perpetualBondCalculatorDTO.getBondFaceValue(),
				(int) perpetualBondCalculatorDTO.getCoupounPayoutFrequency(),
				perpetualBondCalculatorDTO.getInvestmentDate(), perpetualBondCal.getDaysToMaturity(),
				perpetualBondCal.getCouponReceived(), perpetualBondCal.getTotalMonths());

		PerpetualBondCalculatorDTO retDTO = new PerpetualBondCalculatorDTO();
		retDTO.setPerpetualBondCal(perpetualBondCal);
		retDTO.setPerpetualBondLookup(calulatorLookupDetails);

		return new ResponseEntity<PerpetualBondCalculatorDTO>(retDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/postOfficeNSCCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> postOfficeNSCCalculator(@Valid @RequestBody PONSCCalculatorDTO poNSCCalculatorDTO,
			Errors errors) {

		log.debug("PONSCCalculatorDTO: " + poNSCCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		PONSC postOfficeNSCCalculator = pONSCService.getPONSCCalculationList(poNSCCalculatorDTO.getDeposit(),
				poNSCCalculatorDTO.getTenure(), poNSCCalculatorDTO.getInterestFrequency(),
				poNSCCalculatorDTO.getDepositDate());

		return new ResponseEntity<PONSC>(postOfficeNSCCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/PORecurringDespositCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> PORecurringDespositCalculator(
			@Valid @RequestBody PostOfficeRDCalculatorDTO postOfficeRDCalculatorDTO, Errors errors) {

		log.debug("PostOfficeRDCalculatorDTO: " + postOfficeRDCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		PORecurringDeposit PORecurringDespositCalculator = pORecurringDespositService.getRecurringDepositCalculatedList(
				postOfficeRDCalculatorDTO.getDeposit(), postOfficeRDCalculatorDTO.getYearsDays(),
				postOfficeRDCalculatorDTO.getRdDepositFreq(), postOfficeRDCalculatorDTO.getCompundingFreq(),
				postOfficeRDCalculatorDTO.getDepositDate());

		return new ResponseEntity<PORecurringDeposit>(PORecurringDespositCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/postOfficeMISCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> postOfficeMISCalculator(
			@Valid @RequestBody PostOfficeMISCalculatorDTO postOfficeMISCalculatorDTO, Errors errors) {

		log.debug("postOfficeMISCalculator: " + postOfficeMISCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		PostOfficeMonthlyIncomeScheme postOfficeMISCalculator = postOfficeMonthlyIncomeSchemeService
				.getPostOfficeMISCal(postOfficeMISCalculatorDTO.getDeposit(), postOfficeMISCalculatorDTO.getYears(),
						postOfficeMISCalculatorDTO.getInterestFrequency(), postOfficeMISCalculatorDTO.getDepositDate());

		return new ResponseEntity<PostOfficeMonthlyIncomeScheme>(postOfficeMISCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/postOfficeTDCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> postOfficeTDCalculator(
			@Valid @RequestBody PostOfficeTDCalculatorDTO postOfficeTDCalculatorDTO, Errors errors) {

		log.debug("postOfficeTDCalculator: " + postOfficeTDCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		POTimeDeposit postOfficeTDCalculator = poTimeDespositService.getTimeDepositCalculatedList(
				postOfficeTDCalculatorDTO.getDeposit(), postOfficeTDCalculatorDTO.getYearsDays(),
				postOfficeTDCalculatorDTO.getRdDepositFreq(), postOfficeTDCalculatorDTO.getCompundingFreq(),
				postOfficeTDCalculatorDTO.getDepositDate(), postOfficeTDCalculatorDTO.getInterestRate());

		return new ResponseEntity<POTimeDeposit>(postOfficeTDCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/ppfFixedAmountCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> ppfFixedAmountCalculator(
			@Valid @RequestBody PPFFixedAmountCalculatorDTO ppfFixedAmountCalculatorDTO, Errors errors) {

		log.debug("PPFFixedAmountCalculatorDTO: " + ppfFixedAmountCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		PPFFixedAmountDeposit ppfFAmountD = null, ppfFAmountD2 = null;

		ppfFAmountD = pPFFixedAmountService.getPPFFixedAmountCalculationDetails(
				ppfFixedAmountCalculatorDTO.getCurrentBalance(),
				ppfFixedAmountCalculatorDTO.getDeposit(), ppfFixedAmountCalculatorDTO.getTenureType(),
				ppfFixedAmountCalculatorDTO.getTenure(), ppfFixedAmountCalculatorDTO.getRdDepositFreq(),
				ppfFixedAmountCalculatorDTO.getCompundingFreq(), ppfFixedAmountCalculatorDTO.getDepositDate());

		if (ppfFixedAmountCalculatorDTO.getExtensionFlag() == 1) {

			ppfFAmountD2 = pPFFixedAmountService.getPPFExtensionCalcuation(
					ppfFixedAmountCalculatorDTO.getDepositAmountExt(), ppfFixedAmountCalculatorDTO.getTenureTypeExt(),
					ppfFixedAmountCalculatorDTO.getTermExt(), ppfFixedAmountCalculatorDTO.getDepositFrequencyExt(),
					ppfFixedAmountCalculatorDTO.getCompoundingFrequencyExt(), ppfFAmountD.getDepositDate(),
					ppfFAmountD.getMaturityAmount(), ppfFAmountD.getTotalAmountDeposited());

		}

		List<PPFFixedAmountDeposit> ppfAmountDepList = new ArrayList<PPFFixedAmountDeposit>();
		ppfFAmountD.setMaturityDisplayDate(displayDateFormat.format(ppfFAmountD.getMaturityDate()));
		ppfAmountDepList.add(ppfFAmountD);
		if (ppfFAmountD2 != null) {
			ppfFAmountD2.setDepositDisplayDate(displayDateFormat.format(ppfFAmountD.getDepositDate()));
			ppfFAmountD2.setMaturityDisplayDate(displayDateFormat.format(ppfFAmountD2.getMaturityDate()));
			ppfAmountDepList.add(ppfFAmountD2);
		}

		return new ResponseEntity<List<PPFFixedAmountDeposit>>(ppfAmountDepList, HttpStatus.OK);
	}

	@RequestMapping(value = "/seniorCitizenSavingSchemeCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> seniorCitizenSavingSchemeCalculator(
			@Valid @RequestBody SeniorCitizenSavingSchemeCalculatorDTO seniorCitizenSavingSchemeCalculatorDTO,
			Errors errors) {

		log.debug("SeniorCitizenSavingSchemeCalculatorDTO: " + seniorCitizenSavingSchemeCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		SeniorCitizenSavingScheme seniorCitizenSavingSchemeCalculator = seniorCitizenSavingSchemeService
				.getSeniorCitizenSaingSchemeCal(seniorCitizenSavingSchemeCalculatorDTO.getDeposit(),
						seniorCitizenSavingSchemeCalculatorDTO.getYears(),
						seniorCitizenSavingSchemeCalculatorDTO.getInterestFrequency(),
						seniorCitizenSavingSchemeCalculatorDTO.getDepositDate());

		return new ResponseEntity<SeniorCitizenSavingScheme>(seniorCitizenSavingSchemeCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/simpleLoanEMIBasedCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> simpleLoanCalEMIBased(
			@Valid @RequestBody SimpleLoanEMIBasedCalculatorDTO simpleLoanEMIBasedCalculatorDTO, Errors errors) {

		log.debug("SimpleLoanEMIBasedCalculatorDTO: " + simpleLoanEMIBasedCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		SimpleLoanCalculator simpleLoanCal = simpleLoanCalEMIBasedService.calculateEMIBasedLoanValue(
				simpleLoanEMIBasedCalculatorDTO.getLoan_original_flag(),
				simpleLoanEMIBasedCalculatorDTO.getInterestRate(), simpleLoanEMIBasedCalculatorDTO.getLoanAmount(),
				simpleLoanEMIBasedCalculatorDTO.getLoanStartDate(), simpleLoanEMIBasedCalculatorDTO.getEmiAmount(),
				simpleLoanEMIBasedCalculatorDTO.getLoanTenure());

		List<SimpleLoanCalLookup> calulatorLookupDetails = simpleLoanCalEMIBasedService.getEMIBasedLoanList(
				simpleLoanCal.getNumberOfEMI(), simpleLoanEMIBasedCalculatorDTO.getLoanStartDate(),
				simpleLoanCal.getLoanAmount(), simpleLoanCal.getEmiAmount(), simpleLoanCal.getInterestRate(),
				simpleLoanEMIBasedCalculatorDTO.getLoan_original_flag());

		SimpleLoanEMIBasedCalculatorDTO retDTO = new SimpleLoanEMIBasedCalculatorDTO();
		retDTO.setSimpleLoanCal(simpleLoanCal);
		retDTO.setSimpleLoanCalLookup(calulatorLookupDetails);

		return new ResponseEntity<SimpleLoanEMIBasedCalculatorDTO>(retDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/simpleLoanNonEMIBasedCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> simpleLoanCalNonEMIBased(
			@Valid @RequestBody SimpleLoanNonEMIBasedCalculatorDTO simpleLoanNonEMIBasedCalculatorDTO, Errors errors) {

		log.debug("SimpleLoanNonEMIBasedalculatorDTO: " + simpleLoanNonEMIBasedCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		SimpleLoanCalculator simpleLoanCal = new SimpleLoanCalculator();
		simpleLoanCal.setLoanAmount(simpleLoanNonEMIBasedCalculatorDTO.getLoanAmount());
		simpleLoanCal.setInterestRate(simpleLoanNonEMIBasedCalculatorDTO.getInterestRate());
		simpleLoanCal.setLoanTenure(simpleLoanNonEMIBasedCalculatorDTO.getLoanTenure());
		simpleLoanCal.setInterestPaymentFrequency(simpleLoanNonEMIBasedCalculatorDTO.getInterestPaymentFrequency());
		simpleLoanCal.setLoanStartDate(simpleLoanNonEMIBasedCalculatorDTO.getLoanStartDate());
		simpleLoanCal = simpleLoanCalNonEMIBasedService.calculateNonEmiDetails(simpleLoanCal);

		List<SimpleLoanCalLookup> calulatorLookupDetails = simpleLoanCalNonEMIBasedService.getNonEMIDetails(
				simpleLoanCal.getLoanAmount(), simpleLoanCal.getInterestPaymentFrequency(),
				simpleLoanCal.getLoanStartDate(), simpleLoanCal.getLoanEndDate(), simpleLoanCal.getEmiAmount(),
				simpleLoanCal.getLoanTenure());

		SimpleLoanNonEMIBasedCalculatorDTO retDTO = new SimpleLoanNonEMIBasedCalculatorDTO();
		retDTO.setSimpleLoanCal(simpleLoanCal);
		retDTO.setSimpleLoanCalLookup(calulatorLookupDetails);

		return new ResponseEntity<SimpleLoanNonEMIBasedCalculatorDTO>(retDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/sukanyaSamriddhiSchemeCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> sukanyaSamriddhiSchemeCalculator(
			@Valid @RequestBody SukanyaSamriddhiSchemeCalculatorDTO sukanyaSamriddhiSchemeCalculatorDTO,
			Errors errors) {

		log.debug("SukanyaSamriddhiSchemeCalculatorDTO: " + sukanyaSamriddhiSchemeCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		SukanyaSamriddhiScheme sukanyaSamriddhiSchemeCalculator = sukanyaSamriddhiSchemeService
				.getSukanyaSamriddhiSchemeList(sukanyaSamriddhiSchemeCalculatorDTO.getDeposit(),
						sukanyaSamriddhiSchemeCalculatorDTO.getTenureType(),
						sukanyaSamriddhiSchemeCalculatorDTO.getPaymenttenure(),
						sukanyaSamriddhiSchemeCalculatorDTO.getRdDepositFreq(),
						sukanyaSamriddhiSchemeCalculatorDTO.getCompoundingFreq(),
						sukanyaSamriddhiSchemeCalculatorDTO.getDepositDate(),
						sukanyaSamriddhiSchemeCalculatorDTO.getMaturitytenure());

		return new ResponseEntity<SukanyaSamriddhiScheme>(sukanyaSamriddhiSchemeCalculator, HttpStatus.OK);
	}

	@RequestMapping(value = "/zeroCouponBondCalculator", method = RequestMethod.POST)
	public ResponseEntity<?> zeroCouponBondCalculator(
			@Valid @RequestBody ZeroCouponBondCalculatorDTO zeroCouponBondCalculatorDTO, Errors errors) {

		log.debug("zeroCouponBondCalculator: " + zeroCouponBondCalculatorDTO);

		ErrorDTO result = new ErrorDTO();
		if (errors.hasErrors()) {
			result.setErrorCode("VALIDATION_ERROR");
			result.setErrorMessage(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		ZeroCouponBond zeroCouponBondCalculator = zeroCouponService.getZerocouponBondServiceLookupList(
				zeroCouponBondCalculatorDTO.getDeposit(), zeroCouponBondCalculatorDTO.getRateOfInterest(),
				zeroCouponBondCalculatorDTO.getTenureType(), zeroCouponBondCalculatorDTO.getTenure(),
				zeroCouponBondCalculatorDTO.getInterestFrequency(), zeroCouponBondCalculatorDTO.getDepositDate(),
				zeroCouponBondCalculatorDTO.getNoOfBonds(), zeroCouponBondCalculatorDTO.getBondFaceValue());

		return new ResponseEntity<ZeroCouponBond>(zeroCouponBondCalculator, HttpStatus.OK);
	}

}
