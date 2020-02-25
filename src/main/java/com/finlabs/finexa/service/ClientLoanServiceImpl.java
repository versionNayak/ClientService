package com.finlabs.finexa.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientLoanDTO;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupLoanCategory;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientLoanRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.LoanCategoryRepository;

import com.finlabs.finexa.resources.model.SimpleLoanCalculator;
import com.finlabs.finexa.resources.service.SimpleLoanCalEMIBasedService;
import com.finlabs.finexa.resources.service.SimpleLoanCalNonEMIBasedService;

@Service("ClientLoanService")
@Transactional
public class ClientLoanServiceImpl implements ClientLoanService {

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	private static Logger log = LoggerFactory.getLogger(ClientLoanServiceImpl.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;

	@Autowired
	private ClientLoanRepository clientLoanRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private LoanCategoryRepository loanCategoryRepository;
	/*
	 * @Autowired private SimpleLoanCalEMIBasedService
	 * simpleLoanCalEMIBasedService;
	 * 
	 */
	/*
	 * @Autowired private ClientMaster clientMaster;
	 */

	@Override
	public ClientLoanDTO save(ClientLoanDTO clientLoanDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientLoanDTO.getClientID());
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientLoanDTO.getFamilyMemberId());
			ClientLoan clientLoan = new ClientLoan();
			if (clientLoanDTO.getLoanOriginalFlag() == null) {
				clientLoan.setLoanAmount(clientLoanDTO.getLoanAmountNE());
				clientLoan.setLoanStartDate(clientLoanDTO.getLoanStartDateNE());
				clientLoan.setInterestRate(clientLoanDTO.getInterestRateNE());
				clientLoan.setLoanTenure((byte) clientLoanDTO.getLoanTenureNE());
				clientLoan.setOtherLoanCategory(clientLoanDTO.getOtherLoanCategoryNE());
				clientLoan.setInterestPaymentFrequency(clientLoanDTO.getInterestPaymentFrequency());
				clientLoan.setLoanDescription(clientLoanDTO.getLoanDescriptionNE());
				clientLoan.setLoanEndDate(clientLoanDTO.getLoanEndDateNE());
				clientLoan.setLoanType(clientLoanDTO.getLoanType());
				clientLoan.setLoanProviderName(clientLoanDTO.getLoanProviderName());
				clientLoanDTO.setLoanCategoryId(clientLoanDTO.getLoanCategoryIdNE());
				clientLoan.setLoanOriginalFlag("Y");

			} else {
				clientLoan.setOtherLoanCategory(clientLoanDTO.getOtherLoanCategory());
                clientLoan.setEmiLoanProviderName(clientLoanDTO.getEmiLoanProviderName());
				if (!clientLoanDTO.getLoanOriginalFlag().equals("Y")) {
					clientLoan.setEmiAmount(clientLoanDTO.getEmiAmountOut());
					clientLoan.setInterestRate(clientLoanDTO.getInterestRateOut());
					clientLoan.setLoanAmount(new BigDecimal(clientLoanDTO.getOutstandingPrincipalToday()));
					clientLoan.setLoanEndDate(clientLoanDTO.getLoanEndDateOut());
					clientLoan.setLoanStartDate(clientLoanDTO.getLoanStartDateOut());
					clientLoan.setLoanDescription(clientLoanDTO.getLoanDescription());
					clientLoan.setLoanOriginalFlag(clientLoanDTO.getLoanOriginalFlag());
					clientLoan.setLoanTenure(clientLoanDTO.getLoanTenure());
					clientLoan.setLoanType(clientLoanDTO.getLoanType());
					clientLoan.setPendingInstalments(clientLoanDTO.getPendingInstalments());
				} else {

					clientLoan = mapper.map(clientLoanDTO, ClientLoan.class);
				}

			}
			clientLoan.setClientMaster(cm);
			clientLoan.setClientFamilyMember(cfm);
			
			LookupLoanCategory loanCategory = loanCategoryRepository.findOne(clientLoanDTO.getLoanCategoryId());
			clientLoan.setLookupLoanCategory(loanCategory);

			clientLoan = clientLoanRepository.save(clientLoan);
			return clientLoanDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ClientLoanDTO autoSave(ClientLoanDTO clientLoanDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientLoan clientLoan = new ClientLoan();
			clientLoan = mapper.map(clientLoanDTO, ClientLoan.class);

			ClientMaster cm = clientMasterRepository.findOne(clientLoanDTO.getClientID());
			clientLoan.setClientMaster(cm);

			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientLoanDTO.getFamilyMemberId());
			clientLoan.setClientFamilyMember(cfm);
			
			LookupLoanCategory loanCategory = loanCategoryRepository.findOne(clientLoanDTO.getLoanCategoryId());
			clientLoan.setLookupLoanCategory(loanCategory);
			
			clientLoan = clientLoanRepository.save(clientLoan);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return clientLoanDTO;
	}

	@Override
	public ClientLoanDTO update(ClientLoanDTO clientLoanDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientLoanDTO.getClientID());

			ClientLoan clientLoan = new ClientLoan();
			if (clientLoanDTO.getLoanOriginalFlag() == null) {
				clientLoan.setId(clientLoanDTO.getId());
				clientLoan.setLoanAmount(clientLoanDTO.getLoanAmountNE());
				clientLoan.setLoanStartDate(clientLoanDTO.getLoanStartDateNE());
				clientLoan.setInterestRate(clientLoanDTO.getInterestRateNE());
				clientLoan.setLoanTenure((byte) clientLoanDTO.getLoanTenureNE());
				clientLoan.setOtherLoanCategory(clientLoanDTO.getOtherLoanCategoryNE());
				clientLoan.setInterestPaymentFrequency(clientLoanDTO.getInterestPaymentFrequency());
				clientLoan.setLoanDescription(clientLoanDTO.getLoanDescriptionNE());
				clientLoan.setLoanEndDate(clientLoanDTO.getLoanEndDateNE());
				clientLoan.setLoanType(clientLoanDTO.getLoanType());
				clientLoan.setLoanProviderName(clientLoanDTO.getLoanProviderName());
				clientLoanDTO.setLoanCategoryId(clientLoanDTO.getLoanCategoryIdNE());
				clientLoan.setLoanOriginalFlag("Y");
			} else {
				clientLoan.setOtherLoanCategory(clientLoanDTO.getOtherLoanCategory());
				clientLoan.setEmiLoanProviderName(clientLoanDTO.getEmiLoanProviderName());
				if (!clientLoanDTO.getLoanOriginalFlag().equals("Y")) {
					clientLoan.setId(clientLoanDTO.getId());
					clientLoan.setEmiAmount(clientLoanDTO.getEmiAmountOut());
					clientLoan.setInterestRate(clientLoanDTO.getInterestRateOut());
					clientLoan.setLoanAmount(new BigDecimal(clientLoanDTO.getOutstandingPrincipalToday()));
					clientLoan.setLoanEndDate(clientLoanDTO.getLoanEndDateOut());
					clientLoan.setLoanStartDate(clientLoanDTO.getLoanStartDateOut());
					clientLoan.setLoanDescription(clientLoanDTO.getLoanDescription());
					clientLoan.setLoanOriginalFlag(clientLoanDTO.getLoanOriginalFlag());
					clientLoan.setLoanTenure(clientLoanDTO.getLoanTenure());
					clientLoan.setLoanType(clientLoanDTO.getLoanType());
					clientLoan.setPendingInstalments(clientLoanDTO.getPendingInstalments());
					clientLoan.setLoanProviderName(clientLoanDTO.getLoanProviderName());
				} else {

					clientLoan = mapper.map(clientLoanDTO, ClientLoan.class);
				}
			}
			clientLoan.setClientMaster(cm);
			ClientFamilyMember cfm = clientFamilyMemberRepository.findOne(clientLoanDTO.getFamilyMemberId());
			clientLoan.setClientFamilyMember(cfm);
			
			LookupLoanCategory loanCategory = loanCategoryRepository.findOne(clientLoanDTO.getLoanCategoryId());
			clientLoan.setLookupLoanCategory(loanCategory);

			clientLoan = clientLoanRepository.save(clientLoan);
			return clientLoanDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public ClientLoanDTO findById(int id) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientLoanDTO clientLoanDTO = new ClientLoanDTO();
			ClientLoan clientLoan = clientLoanRepository.findOne(id);

			if (clientLoan.getLoanType() == 2) {
				clientLoanDTO.setLoanType(clientLoan.getLoanType());
				clientLoanDTO.setLoanCategoryIdNE(clientLoan.getLookupLoanCategory().getId());
				
				clientLoanDTO.setLoanProviderName(clientLoan.getLoanProviderName());
				
				clientLoanDTO.setLoanDescriptionNE(clientLoan.getLoanDescription());
				clientLoanDTO.setLoanAmountNE(clientLoan.getLoanAmount());
				clientLoanDTO.setOtherLoanCategoryNE(clientLoan.getOtherLoanCategory());
				// clientLoanDTO.setLoanStartDateNE(clientLoan.getLoanStartDate());
				clientLoanDTO.setInterestRateNE(clientLoan.getInterestRate());
				clientLoanDTO.setLoanTenureNE(clientLoan.getLoanTenure());
				// clientLoanDTO.setLoanEndDateNE(clientLoan.getLoanEndDate());
				clientLoanDTO.setInterestPaymentFrequency(clientLoan.getInterestPaymentFrequency());
				String date = formatter.format(clientLoan.getLoanStartDate());
				try {
					clientLoanDTO.setLoanStartDateNE(formatter.parse(date));
					date = formatter.format(clientLoan.getLoanEndDate());
					clientLoanDTO.setLoanEndDateNE(formatter.parse(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				    
				if (!clientLoan.getLoanOriginalFlag().equals("Y")) {
					clientLoanDTO.setLoanType(clientLoan.getLoanType());
					clientLoanDTO.setOutstandingPrincipalToday(String.valueOf(clientLoan.getLoanAmount()));
					clientLoanDTO.setEmiAmountOut(clientLoan.getEmiAmount());
					clientLoanDTO.setInterestRateOut(clientLoan.getInterestRate());
					clientLoanDTO.setPendingInstalments(clientLoan.getPendingInstalments());
					clientLoanDTO.setOtherLoanCategoryNE(clientLoan.getOtherLoanCategory());
					clientLoanDTO.setLoanOriginalFlag(clientLoan.getLoanOriginalFlag());
					clientLoanDTO.setLoanDescription(clientLoan.getLoanDescription());
					clientLoanDTO.setEmiLoanProviderName(clientLoan.getEmiLoanProviderName());
					
					String date2 = formatter.format(clientLoan.getLoanEndDate());
					String date3 = formatter.format(clientLoan.getLoanStartDate());
					try {
						clientLoanDTO.setLoanEndDateOut(formatter.parse(date2));
						clientLoanDTO.setLoanStartDateOut(formatter.parse(date3));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {

					clientLoanDTO = mapper.map(clientLoan, ClientLoanDTO.class);
				}
				clientLoanDTO.setLoanCategoryId(clientLoan.getLookupLoanCategory().getId());
				
				clientLoan.setOtherLoanCategory(clientLoanDTO.getOtherLoanCategory());
				clientLoan.setEmiLoanProviderName(clientLoanDTO.getEmiLoanProviderName());
			}
			clientLoanDTO.setClientID(clientLoan.getClientMaster().getId());
			clientLoanDTO.setFamilyMemberId(clientLoan.getClientFamilyMember().getId());

			return clientLoanDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ClientLoanDTO> findAll() {
		// TODO Auto-generated method stub
		List<ClientLoan> listClientLoan = clientLoanRepository.findAll();

		List<ClientLoanDTO> listDTO = new ArrayList<ClientLoanDTO>();
		for (ClientLoan clientLoan : listClientLoan) {
			ClientLoanDTO dto = mapper.map(clientLoan, ClientLoanDTO.class);
			dto.setClientID(clientLoan.getClientMaster().getId());
			listDTO.add(dto);
		}

		return listDTO;
	}

	@Override
	public List<ClientLoanDTO> findByClientId(int clientId) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			ClientMaster cm = clientMasterRepository.findOne(clientId);
			List<ClientLoanDTO> listDTO = new ArrayList<ClientLoanDTO>();
			for (ClientLoan clientLoan : cm.getClientLoans()) {
				ClientLoanDTO dto = mapper.map(clientLoan, ClientLoanDTO.class);
				dto.setClientID(clientId);
				dto.setOwnerName(clientLoan.getClientFamilyMember().getFirstName() + " "
						+ (clientLoan.getClientFamilyMember().getMiddleName() == null ? " "
								: clientLoan.getClientFamilyMember().getMiddleName())
						+ " " + clientLoan.getClientFamilyMember().getLastName());
				dto.setLoanCategoryName(clientLoan.getLookupLoanCategory().getDescription());
				
				if(clientLoan.getLoanType() == 2) {
					dto.setLoanProviderName(clientLoan.getLoanProviderName());
				}
				dto.setEmiAmount(clientLoan.getEmiAmount());
				listDTO.add(dto);
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) throws RuntimeException {

		try {
			clientLoanRepository.delete(id);
			return 1;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	public ClientLoanDTO findLoanEndDateEMI(ClientLoanDTO clientLoanDTO) throws RuntimeException {

		try {
			log.debug("clientLoanDTO =  " + clientLoanDTO);
			SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			// interestRate=3.22, loanAmount=200000, loanOriginalFlag=Y,
			// loanStartDate=Wed Jul 26 05:30:00 IST 2017, loanTenure=10,

			SimpleLoanCalEMIBasedService simpleLoanCalEMIBasedService = new SimpleLoanCalEMIBasedService();

			if (clientLoanDTO.getEmiAmount() == null) {
				clientLoanDTO.setEmiAmount(new BigDecimal("0"));
			}
			if (!clientLoanDTO.getLoanOriginalFlag().equals("Y")) {
				clientLoanDTO.setEmiAmount(clientLoanDTO.getEmiAmountOut());
				clientLoanDTO.setInterestRate(clientLoanDTO.getInterestRateOut().divide(new BigDecimal(100)));
				clientLoanDTO.setLoanAmount(new BigDecimal(clientLoanDTO.getOutstandingPrincipalToday()));
				clientLoanDTO.setLoanStartDateOut(clientLoanDTO.getLoanStartDateOut());
			} else {
				if (clientLoanDTO.getLoanOriginalFlag().equals("Y")) {
					clientLoanDTO.setInterestRate(clientLoanDTO.getInterestRate().divide(new BigDecimal(100)));
				}
			}
			SimpleLoanCalculator simpleLoanCalculatorEmi = simpleLoanCalEMIBasedService.calculateEMIBasedLoanValue(
					clientLoanDTO.getLoanOriginalFlag(), clientLoanDTO.getInterestRate().doubleValue(),
					clientLoanDTO.getLoanAmount().doubleValue(), clientLoanDTO.getLoanStartDate(),
					clientLoanDTO.getLoanStartDateOut(),
					clientLoanDTO.getEmiAmount().doubleValue(), clientLoanDTO.getLoanTenure());
			clientLoanDTO.setEmiAmount(new BigDecimal(simpleLoanCalculatorEmi.getEmiAmount()));
			// clientLoanDTO.setLoanEndDate(simpleLoanCalculatorEmi.getLoanEndDate());
			clientLoanDTO.setDisplayDate(displayDateFormat.format(simpleLoanCalculatorEmi.getLoanEndDate()));
			clientLoanDTO.setPendingInstalments(simpleLoanCalculatorEmi.getNumberOfEMI());

			// log.debug("clientLoanDTO = " + clientLoanDTO);
			return clientLoanDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public ClientLoanDTO findLoanEndDateNONEMI(ClientLoanDTO clientLoanDTO) throws RuntimeException {

		try {
			log.debug("clientLoanDTO =  " + clientLoanDTO);
			SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			// interestRate=3.22, loanAmount=200000, loanOriginalFlag=Y,
			// loanStartDate=Wed Jul 26 05:30:00 IST 2017, loanTenure=10,

			SimpleLoanCalNonEMIBasedService simpleLoanCalNonEMIBasedService = new SimpleLoanCalNonEMIBasedService();
			SimpleLoanCalculator simpleLoanCalculator = new SimpleLoanCalculator();
			simpleLoanCalculator.setLoanAmount(clientLoanDTO.getLoanAmountNE().doubleValue());
			simpleLoanCalculator.setLoanStartDate(clientLoanDTO.getLoanStartDateNE());
			simpleLoanCalculator.setInterestRate(clientLoanDTO.getInterestRateNE().doubleValue());
			simpleLoanCalculator.setLoanTenure(clientLoanDTO.getLoanTenureNE());
			simpleLoanCalculator.setInterestPaymentFrequency(clientLoanDTO.getInterestPaymentFrequency());
			SimpleLoanCalculator simpleLoanCalculatorNonEmi = simpleLoanCalNonEMIBasedService
					.calculateNonEmiDetails(simpleLoanCalculator);
			clientLoanDTO.setDisplayDate(displayDateFormat.format(simpleLoanCalculatorNonEmi.getLoanEndDate()));

			// log.debug("clientLoanDTO = " + clientLoanDTO);
			return clientLoanDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
