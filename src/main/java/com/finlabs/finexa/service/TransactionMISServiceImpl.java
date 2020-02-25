package com.finlabs.finexa.service;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.TransactionMISDTO;
import com.finlabs.finexa.dto.TransactionMISInflowOutflowDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientARNMapping;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.TransactionMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AuxillaryFamilyMemberRepository;
import com.finlabs.finexa.repository.AuxillaryMasterRepository;
import com.finlabs.finexa.repository.ClientARNMappingRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.TransactionMasterBORepository;

@Service("TransactionMISService")
@Transactional
public class TransactionMISServiceImpl implements TransactionMISService {

	@Autowired
	TransactionMasterBORepository transactionMasterBORepository;

	@Autowired
	AdvisorUserRepository advisorUserRepository;
	@Autowired
	ClientARNMappingRepository clientARNMappingRepository;
	@Autowired
	ClientMasterRepository clientMasterRepository;
	@Autowired
	AuxillaryMasterRepository auxillaryMasterRepository;
	@Autowired
	ClientFamilyMemberRepository clientFamilyMemberRepository;
	@Autowired
	AuxillaryFamilyMemberRepository auxillaryFamilyMemberRepository;

	public static SimpleDateFormat formatterDisplay = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public TransactionMISDTO getInflowOutflowList(int advisorId, Date fromDate, Date toDate, String arn)
			throws FinexaBussinessException {
		// TODO Auto-generated method stub

		
		TransactionMISDTO  transactionMISDTO=new TransactionMISDTO();
	
		
		try {
			
			int srNo=1;
			List<TransactionMasterBO> transMasterList = transactionMasterBORepository
					.findByAdvisorIdAndStartDateAndEndDate(advisorId, fromDate, toDate);
			double monthlyInflow = 0.0;
			double monthlyOutflow = 0.0;
			double netInflow = 0.0;
			List<TransactionMISInflowOutflowDTO> inflowList = new ArrayList<TransactionMISInflowOutflowDTO>();
			List<TransactionMISInflowOutflowDTO> outflowList = new ArrayList<TransactionMISInflowOutflowDTO>();
			List<String> namePANMapByTransactionList = new ArrayList<String>();
			Map<String, String> namePANMapByClientId = new HashedMap<String, String>();
			ClientARNMapping arnNo=clientARNMappingRepository.findOne(Integer.valueOf(arn));
			namePANMapByClientId = namePANLIST(arnNo.getArn());
			TransactionMISInflowOutflowDTO transactionMISInflowOutflowColumnDTO = new TransactionMISInflowOutflowDTO();
			transactionMISInflowOutflowColumnDTO.setsNO("SNo");
			transactionMISInflowOutflowColumnDTO.setInvestorName("Investor Name");
			transactionMISInflowOutflowColumnDTO.setAmc("AMC");
			transactionMISInflowOutflowColumnDTO.setSchemeName("Scheme Name");
			transactionMISInflowOutflowColumnDTO.setFolioNo("Folio No.");
			transactionMISInflowOutflowColumnDTO.setTransType("Trxn. Type.");
			transactionMISInflowOutflowColumnDTO.setTransDate("Date.");
			transactionMISInflowOutflowColumnDTO.setTransAmt("Amount");
			transactionMISInflowOutflowColumnDTO.setNav("Nav");
			transactionMISInflowOutflowColumnDTO.setUnits("Unit");
			inflowList.add(transactionMISInflowOutflowColumnDTO);
			outflowList.add(transactionMISInflowOutflowColumnDTO);
			for (TransactionMasterBO obj : transMasterList) {

				namePANMapByTransactionList.add(obj.getInvestorName().trim() + obj.getInvestorPan());
			}
			
			for (TransactionMasterBO obj : transMasterList) {
				
				for (String key : namePANMapByTransactionList) {
					if (namePANMapByClientId.containsKey(key)) {

						TransactionMISInflowOutflowDTO singleObj = new TransactionMISInflowOutflowDTO();
						singleObj.setSRNo(srNo);
						singleObj.setInvestorName(obj.getInvestorName());
						singleObj.setFolioNo(obj.getFolioNo());
						singleObj.setAmc(obj.getAmcCode());
						singleObj.setSchemeName(obj.getSchemeName());
						singleObj.setNav(obj.getNav());
						singleObj.setTransAmt(obj.getTransAmt());
						singleObj.setTransDate(formatterDisplay.format(obj.getTransactionDate()));
						singleObj.setTransType(obj.getLookupTransactionRule().getDescription());
						singleObj.setUnits(obj.getTransUnits());
						//inflowList.add(singleObj);
						
						
						String transactionType = obj.getLookupTransactionRule().getCode();
						if (transactionType != null) {
							switch (transactionType) {
							case "P":
							case "REDR":
							case "SI":
							case "SWIN":
							case "STP In":
							case "STPI":
							case "STPOR":
							case "TI":
							case "DR":
							case "DIR":
							case "Dividend Reinvestment":
							case "New Purchase":
							case "NEWPUR":
							case "SIP":
							case "Additional Purchase":
							case "SWOFR":
							case "ADDPUR":
							case "TMI":
								monthlyInflow += Double.parseDouble(obj.getTransAmt());
								inflowList.add(singleObj);
								srNo++;
								break;
							case "R":
							case "RED":
							case "Redemption":
							case "STPIR":
							case "STP O":
							case "STP Out":
							case "SWINR":
							case "SO":
							case "SWOF":
							case "TO":
							case "SIPR":
							case "ADDPURR":
							case "SWD":
							case "NEWPURR":
							case "TMO":
								srNo=0;
								monthlyOutflow += Double.parseDouble(obj.getTransAmt());
								outflowList.add(singleObj);
								srNo++;
								break;
							case "DP":
							case "J":
							case "All others":
								// noEffect

								break;
							default:
								break;
							}

						}
						break;
					}
					
				}
				
				netInflow = monthlyInflow - monthlyOutflow;
				/*
				Map<String, List<TransactionMISInflowOutflowDTO>> tempMap = new HashedMap<>();
				tempMap.put("Monthly Inflow (Subscriptions)", inflowList);
				transactionMISDTO.setInflowListMap(tempMap);
				
				tempMap = new HashedMap<>();
				tempMap.put("Monthly Outflow (Redemptions)", outflowList);
				transactionMISDTO.setOutflowListMap(tempMap);
				*/
				transactionMISDTO.setInflowList(inflowList);
				transactionMISDTO.setOutflowList(outflowList);
				transactionMISDTO.setNetMonthlyInflow(netInflow);
				transactionMISDTO.setTotalMonthlyInflow(monthlyInflow);
				transactionMISDTO.setTotalMonthlyOutflow(monthlyOutflow);
					}
			System.out.println("Tranaction outflow List" +inflowList);
			for(TransactionMISInflowOutflowDTO transactionMISInflowOutflowDTO : inflowList) {
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getAmc());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getFolioNo());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getInvestorName());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getSRNo());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getSchemeName());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getTransType());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getTransDate());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getTransAmt());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getNav());
			System.out.println("Tranaction inflow List" +transactionMISInflowOutflowDTO.getUnits());
			System.out.println("*****************************************");
			}
			System.out.println("Tranaction outflow List" +outflowList);
			for(TransactionMISInflowOutflowDTO transactionMISInflowOutflowDTO : outflowList) {
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getAmc());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getFolioNo());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getInvestorName());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getSRNo());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getSchemeName());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getTransType());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getTransDate());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getTransAmt());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getNav());
				System.out.println("Tranaction outflowList" +transactionMISInflowOutflowDTO.getUnits());
				System.out.println("*****************************************");
				}
		} catch (Exception e) {

		}
		
		return transactionMISDTO;
	}

	@Override
	public List<Integer> ClientIdList(String arn) {
		List<ClientARNMapping> clientARNMapping = clientARNMappingRepository.findByArn(arn);
		List<Integer> clientIdList = new ArrayList<>();
		for (ClientARNMapping clientARN : clientARNMapping) {
			clientIdList.add(clientARN.getClientMaster().getId());
		}
		return clientIdList;

	}

	/**********************************
	 * Fetching client name Pan list by ClientId
	 *******************/
	@Override
	public Map<String, String> namePANLIST(String arn) {

		Map<String, String> namePANMapByClientId = new HashedMap<String, String>();

		List<Integer> ClientIdList = ClientIdList(arn);
		String Name;
		if (ClientIdList.size() > 0) {
			for (Integer clientId : ClientIdList) {
				try {
				ClientMaster clientMaster = clientMasterRepository.findById(clientId);

				if (clientMaster != null) {
					 Name = clientMaster.getFirstName()
							+ (clientMaster.getMiddleName() == null ? ""
									: (" " + clientMaster.getMiddleName()))
							+ (clientMaster.getLastName() == null ? ""
									: " " + (clientMaster.getLastName()));
					namePANMapByClientId.put(Name.trim() + clientMaster.getPan(), Name.trim());
				}
				List<AuxillaryInvestorMaster> auxillaryInvestorMasterList = auxillaryMasterRepository
						.findAllByClientMaster(clientMaster);
				if (auxillaryInvestorMasterList.size() > 0) {
					for (AuxillaryInvestorMaster auxillaryInvestorMaster : auxillaryInvestorMasterList) {
						 Name = auxillaryInvestorMaster.getFirstName()
									+ (auxillaryInvestorMaster.getMiddleName() == null ? ""
											: (" " + auxillaryInvestorMaster.getMiddleName()))
									+ (auxillaryInvestorMaster.getLastName() == null ? ""
											: " " + (auxillaryInvestorMaster.getLastName()));
						namePANMapByClientId.put(Name.trim() + auxillaryInvestorMaster.getPan(), Name.trim());
					}
				}

				List<ClientFamilyMember> ClientFamilyMemberList = clientFamilyMemberRepository
						.findByClientIdAndRelationId(clientId, (byte) 0);
				if (ClientFamilyMemberList.size() > 0) {
					for (ClientFamilyMember clientFamilyMember : ClientFamilyMemberList) {
						Name = clientFamilyMember.getFirstName()
								+ (clientFamilyMember.getMiddleName() == null ? ""
										: (" " + clientFamilyMember.getMiddleName()))
								+ (clientFamilyMember.getLastName() == null ? ""
										: " " + (clientFamilyMember.getLastName()));

						namePANMapByClientId.put(Name.trim() + clientFamilyMember.getPan(), Name.trim());

						List<AuxillaryFamilyMember> AuxillaryFamilyMemberList = auxillaryFamilyMemberRepository
								.findByClientFamilyMember(clientFamilyMember);
						if (AuxillaryFamilyMemberList.size() > 0) {
							for (AuxillaryFamilyMember auxillaryFamilyMember : AuxillaryFamilyMemberList) {
								Name = auxillaryFamilyMember.getFirstName()
										+ (auxillaryFamilyMember.getMiddleName() == null ? ""
												: (" " + clientFamilyMember.getMiddleName()))
										+ (auxillaryFamilyMember.getLastName() == null ? ""
												: " " + (auxillaryFamilyMember.getLastName()));


								namePANMapByClientId.put(Name.trim() + auxillaryFamilyMember.getPan(), Name.trim());
							}
						}
					}
				}
				}catch (Exception e) {
					e.printStackTrace();
				// TODO: handle exception
			}

			}
		}
		
		return namePANMapByClientId;
	}

}
