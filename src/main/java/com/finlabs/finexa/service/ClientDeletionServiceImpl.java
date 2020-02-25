package com.finlabs.finexa.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.model.AdvisorProductReco;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AuxillaryFamilyMember;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientARNMapping;
import com.finlabs.finexa.model.ClientAccessRight;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientEquity;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFatcaReport;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientFloaterCover;
import com.finlabs.finexa.model.ClientGoal;
import com.finlabs.finexa.model.ClientGuardian;
import com.finlabs.finexa.model.ClientGuardianContact;
import com.finlabs.finexa.model.ClientLifeInsurance;
import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.model.ClientLoginInfo;
import com.finlabs.finexa.model.ClientLumpsumInflow;
import com.finlabs.finexa.model.ClientMandateRegistration;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.ClientNPS;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.model.ClientOtherAlternateAsset;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.ClientPreciousMetal;
import com.finlabs.finexa.model.ClientRealEstate;
import com.finlabs.finexa.model.ClientRiskProfileResponse;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.ClientStructuredProduct;
import com.finlabs.finexa.model.ClientVehicle;
import com.finlabs.finexa.model.User;
import com.finlabs.finexa.repository.AdvisorProductRecoRepository;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AuxillaryFamilyMemberRepository;
import com.finlabs.finexa.repository.AuxillaryMasterRepository;
import com.finlabs.finexa.repository.ClientARNMappingRepository;
import com.finlabs.finexa.repository.ClientAccessRightsRepository;
import com.finlabs.finexa.repository.ClientAnnuityRepository;
import com.finlabs.finexa.repository.ClientAtalPensionYojanaRepository;
import com.finlabs.finexa.repository.ClientCashRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientEpfRepository;
import com.finlabs.finexa.repository.ClientEquityRepository;
import com.finlabs.finexa.repository.ClientExpenseRepository;
import com.finlabs.finexa.repository.ClientFamilyIncomeRepository;
import com.finlabs.finexa.repository.ClientFamilyMemberRepository;
import com.finlabs.finexa.repository.ClientFatcaRepository;
import com.finlabs.finexa.repository.ClientFixedIncomeRepository;
import com.finlabs.finexa.repository.ClientFloaterCoverRepository;
import com.finlabs.finexa.repository.ClientGoalRepository;
import com.finlabs.finexa.repository.ClientGuardianContactRepository;
import com.finlabs.finexa.repository.ClientGuardianRepository;
import com.finlabs.finexa.repository.ClientLifeInsuranceRepository;
import com.finlabs.finexa.repository.ClientLoanRepository;
import com.finlabs.finexa.repository.ClientLoginInfoRepository;
import com.finlabs.finexa.repository.ClientLumpsumRepository;
import com.finlabs.finexa.repository.ClientMandateRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.ClientMeetingRepository;
import com.finlabs.finexa.repository.ClientMutualFundRepository;
import com.finlabs.finexa.repository.ClientNonlifeInsuranceRepository;
import com.finlabs.finexa.repository.ClientNpsRepository;
import com.finlabs.finexa.repository.ClientOtherAlternateAssetRepository;
import com.finlabs.finexa.repository.ClientPpfRepository;
import com.finlabs.finexa.repository.ClientPreciousMetalRepository;
import com.finlabs.finexa.repository.ClientRealEstateRepository;
import com.finlabs.finexa.repository.ClientRiskProfileResponseRepository;
import com.finlabs.finexa.repository.ClientSmallSavingRepository;
import com.finlabs.finexa.repository.ClientStructuredProductRepository;
import com.finlabs.finexa.repository.ClientTaskRepository;
import com.finlabs.finexa.repository.ClientVehicleRepository;
import com.finlabs.finexa.repository.UserRepository;

@Service("ClientDeletionService")
@Transactional
public class ClientDeletionServiceImpl  implements ClientDeletionService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private ClientContactRepository clientContactRepository;
	
	@Autowired
	private ClientFamilyMemberRepository clientFamilyMemberRepository;
	
	@Autowired
	private ClientFamilyIncomeRepository clientFamilyIncomeRepository;
	
	@Autowired
	private AuxillaryMasterRepository auxillaryMasterRepository;
	
	@Autowired
	private AuxillaryFamilyMemberRepository auxillaryFamilyMemberRepository;
	
	@Autowired
	private ClientAccessRightsRepository clientAccessRightsRepository;
	
	@Autowired
	private ClientEpfRepository clientEpfRepository;
	
	@Autowired 
	private ClientAnnuityRepository clientAnnuityRepository;
	
	@Autowired
	private ClientCashRepository clientCashRepository;
	
	@Autowired
	private ClientEquityRepository clientEquityRepository;
	
	@Autowired
	private ClientExpenseRepository clientExpenseRepository;
	
	@Autowired
	private ClientFatcaRepository clientFatcaRepository;
	
	@Autowired
	private ClientFixedIncomeRepository clientFixedIncomeRepository;
	
	@Autowired
	private ClientFloaterCoverRepository clientFloaterCoverRepository;
	
	@Autowired
	private ClientGoalRepository clientGoalRepository;
	
	@Autowired
	private ClientGuardianRepository clientGuardianRepository;
	
	@Autowired
	private ClientGuardianContactRepository clientGuardianContactRepository;
	
	@Autowired
	private ClientLifeInsuranceRepository clientLifeInsuranceRepository;
	
	@Autowired
	private ClientLoanRepository clientLoanRepository;
	
	@Autowired
	private ClientLoginInfoRepository clientLoginInfoRepository;
	
	@Autowired
	private ClientLumpsumRepository clientLumpsumRepository;
	
	@Autowired
	private ClientMandateRepository clientMandateRepository;
	
	@Autowired
	private ClientMeetingRepository clientMeetingRepository;
	
	@Autowired
	private ClientMutualFundRepository clientMutualFundRepository;
	
	@Autowired
	private ClientNonlifeInsuranceRepository clientNonlifeInsuranceRepository;
	
	@Autowired
	private ClientNpsRepository clientNpsRepository;
	
	@Autowired
	private ClientOtherAlternateAssetRepository clientOtherAlternateAssetRepository;
	
	@Autowired
	private ClientPpfRepository clientPpfRepository;
	
	@Autowired
	private ClientPreciousMetalRepository clientPreciousMetalRepository;
	
	@Autowired
	private ClientRealEstateRepository clientRealEstateRepository;
	
	@Autowired
	private ClientRiskProfileResponseRepository clientRiskProfileResponseRepository;
	
	@Autowired
	private ClientSmallSavingRepository clientSmallSavingRepository;
	
	@Autowired
	private ClientStructuredProductRepository clientStructuredProductRepository;
	
	@Autowired
	private ClientTaskRepository clientTaskRepository;
	
	@Autowired
	private ClientVehicleRepository clientVehicleRepository;
	
	@Autowired
	private ClientAtalPensionYojanaRepository clientAtalPensionYojanaRepository;
	
	@Autowired
	private AdvisorProductRecoRepository advisorProductRecoRepository;
	
	@Autowired
	private ClientARNMappingRepository clientARNMappingRepository;
	
	@Override
	public List<ClientMaster> deleteClientByAdvisor(int userID, String isFinexaUserOrNot) {
		// TODO Auto-generated method stub
		List <ClientMaster> clientMasterList = null;
		try {
			
			User user = userRepository.findOne(userID);
			AdvisorUser advisorUser = advisorUserRepository.findByUser(user);
			clientMasterList = clientMasterRepository.findByAdvisorUserAndFinexaUser(advisorUser, isFinexaUserOrNot);
			if (clientMasterList.size() > 0) {
				for (ClientMaster clientMaster : clientMasterList) {
					try {
						//Client contact deleted
						ClientContact clientContact = clientContactRepository.findByClientMaster(clientMaster);
						clientContactRepository.delete(clientContact);
						
						//Client access rights deleted
						ClientAccessRight clientAccessRight = clientAccessRightsRepository.findByClientMaster(clientMaster);
						clientAccessRightsRepository.delete(clientAccessRight);
						
						//Client Annuity deleted
						List<ClientAnnuity> clientAnnuityList = clientMaster.getClientAnnuities();
						clientAnnuityRepository.delete(clientAnnuityList);
						
						//Client EPF deleted
						List<ClientEPF> clientEPFList = clientMaster.getClientEpfs();
						//List<ClientEPF> clientEPFList = clientEpfRepository.findByClientMaster(clientMaster);
						clientEpfRepository.delete(clientEPFList);
						
						//Client AtalPensionYojana deleted
						List<ClientAtalPensionYojana> clientAtalPensionYojanaList = clientMaster.getClientAtalPensionYojanas();
						clientAtalPensionYojanaRepository.delete(clientAtalPensionYojanaList);
						
						//Client ClientCash deleted
						List<ClientCash> clientCashList = clientMaster.getClientCashs();
						clientCashRepository.delete(clientCashList);
						
						List<ClientEquity> clientEquityList = clientMaster.getClientEquities();
						clientEquityRepository.delete(clientEquityList);
						
						List<ClientExpense> clientExpenseList = clientMaster.getClientExpenses();
						clientExpenseRepository.delete(clientExpenseList);
						
						List<ClientFatcaReport> clientFatcaReportList = clientMaster.getClientFatcaReports();
						clientFatcaRepository.delete(clientFatcaReportList);
						
						List<ClientFixedIncome> clientFixedIncomeList = clientMaster.getClientFixedIncomes();
						clientFixedIncomeRepository.delete(clientFixedIncomeList);
						
						List<AdvisorProductReco> advisorProductRecoList = clientMaster.getAdvisorProductReco();
						advisorProductRecoRepository.delete(advisorProductRecoList);
						
						List<ClientGoal> clientGoalList = clientMaster.getClientGoals();
						clientGoalRepository.delete(clientGoalList);						
						
						List<ClientGuardian> clientGuardianList = clientMaster.getClientGuardians();						
						if(clientGuardianList.size() > 0) {
							for (ClientGuardian clientGuardian : clientGuardianList) {
								try {
									
									List<ClientGuardianContact> clientGuardianContactList = clientGuardian.getClientGuardianContacts();
									clientGuardianContactRepository.delete(clientGuardianContactList);
																		
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							//Client Guardian and Family Member deleted
							clientGuardianRepository.delete(clientGuardianList);
						}
						
						List<ClientLifeInsurance> cientLifeInsuranceList = clientMaster.getClientLifeInsurances();
						clientLifeInsuranceRepository.delete(cientLifeInsuranceList);
						
						List<ClientLoan> cientLoanList = clientMaster.getClientLoans();
						clientLoanRepository.delete(cientLoanList);
						
						List<ClientLoginInfo> cientLoginInfoList = clientMaster.getClientLoginInfo();
						clientLoginInfoRepository.delete(cientLoginInfoList);
						
						List<ClientLumpsumInflow> cientLumpsumInflowList = clientMaster.getClientLumpsumInflows();
						clientLumpsumRepository.delete(cientLumpsumInflowList);
						
						List<ClientMandateRegistration> cientMandateRegistrationList = clientMaster.getClientMandateRegistrations();
						clientMandateRepository.delete(cientMandateRegistrationList);
						
						List<ClientMutualFund> cientMutualFundList = clientMaster.getClientMutualFunds();
						clientMutualFundRepository.delete(cientMutualFundList);
						
						List<ClientNPS> cientNPSList = clientMaster.getClientNps();
						clientNpsRepository.delete(cientNPSList);
						
						List<ClientOtherAlternateAsset> cientOtherAlternateAssetList = clientMaster.getClientOtherAlternateAssets();
						clientOtherAlternateAssetRepository.delete(cientOtherAlternateAssetList);
						
						List<ClientPPF> cientPPFList = clientMaster.getClientPpfs(); 
						clientPpfRepository.delete(cientPPFList);
						
						List<ClientPreciousMetal> cientPreciousMetalList = clientMaster.getClientPreciousMetals();
						clientPreciousMetalRepository.delete(cientPreciousMetalList);
						
						List<ClientRealEstate> cientRealEstateList = clientMaster.getClientRealEstates();
						clientRealEstateRepository.delete(cientRealEstateList);
						
						List<ClientRiskProfileResponse> cientRiskProfileResponseList = clientMaster.getClientRiskProfileResponses();
						clientRiskProfileResponseRepository.delete(cientRiskProfileResponseList);
						
						List<ClientSmallSaving> cientSmallSavingList = clientMaster.getClientSmallSavings();
						clientSmallSavingRepository.delete(cientSmallSavingList);
						
						List<ClientStructuredProduct> cientStructuredProductList = clientMaster.getClientStructuredProducts();
						clientStructuredProductRepository.delete(cientStructuredProductList);
						
						List<ClientVehicle> cientVehicleList = clientMaster.getClientVehicles();
						clientVehicleRepository.delete(cientVehicleList);
						
						//Client Family Income and Family Member deleted
						List <ClientFamilyMember> clientFamilyMembersList = clientMaster.getClientFamilyMembers();
						if(clientFamilyMembersList.size() > 0) {
							for (ClientFamilyMember clientFamilyMember : clientFamilyMembersList) {
								try {
									
									List<ClientFloaterCover> clientFloaterCoverList = clientFamilyMember.getClientFloaterCovers();
									clientFloaterCoverRepository.delete(clientFloaterCoverList);
																		
									//Client Family Income deleted
									List<ClientFamilyIncome> clientFamilyIncome = clientFamilyMember.getClientFamilyIncomes();
									clientFamilyIncomeRepository.delete(clientFamilyIncome);
									
									//All auxilliary family members are deleted with respect to this specific Family member
									List <AuxillaryFamilyMember> auxillaryFamilyMembersList = clientFamilyMember.getAuxillaryFamilyMembers();
									if(auxillaryFamilyMembersList.size() > 0)
										auxillaryFamilyMemberRepository.delete(auxillaryFamilyMembersList);
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							//Client Family Income and Family Member deleted
							clientFamilyMemberRepository.delete(clientFamilyMembersList);
						}
						
						List<ClientNonLifeInsurance> cientNonLifeInsuranceList = clientMaster.getClientNonLifeInsurances();
						clientNonlifeInsuranceRepository.delete(cientNonLifeInsuranceList);
												
						//Auxilliary Investor deleted
						List<AuxillaryInvestorMaster> auxillaryInvestorMasterList = auxillaryMasterRepository.findAllByClientMaster(clientMaster);
						auxillaryMasterRepository.delete(auxillaryInvestorMasterList);
						
						List<ClientARNMapping> clientARNMappingList = clientMaster.getClientArnmappings();
						clientARNMappingRepository.delete(clientARNMappingList);
						
						//Client Master deleted
						clientMasterRepository.delete(clientMaster);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		return clientMasterList;
	}

}
