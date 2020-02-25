package com.finlabs.finexa.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.ClientContactDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterPincode;
import com.finlabs.finexa.model.MasterState;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.ClientContactRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.repository.LookupCountryRepository;
import com.finlabs.finexa.repository.MasterPincodeRepository;
import com.finlabs.finexa.repository.MasterStateRepository;
import com.finlabs.finexa.util.EmailUtil;
import com.finlabs.finexa.util.FinexaConstant;

@Service("ClientContactService")
@Transactional
public class ClientContactServiceImpl implements ClientContactService {
	private static Logger log = LoggerFactory.getLogger(ClientContactServiceImpl.class);
	@Autowired
	private Mapper mapper;

	@Autowired
	private ClientContactRepository clientContactRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private LookupCountryRepository lookupCountryRepository;

	@Autowired
	private MasterStateRepository masterStateRepository;

	@Autowired
	private MasterPincodeRepository masterPincodeRepository;
	
	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Override
	public ClientContactDTO save(ClientContactDTO clientContactDTO) throws RuntimeException {
		try {
			boolean sendEmail;
			log.debug("incoming clientContactDTO " + clientContactDTO);
			ClientMaster cm = clientMasterRepository.findOne(clientContactDTO.getClientId());
			
			//set the email flag
			if (!cm.getClientContacts().get(0).getEmailID().equals(clientContactDTO.getEmailID())) {
				sendEmail = true;
			} else {
				sendEmail = false;
			}
			
			ClientContact clientContact = mapper.map(clientContactDTO, ClientContact.class);
			clientContact.setClientMaster(cm);
			clientContact
					.setLookupCountry1(lookupCountryRepository.findOne(clientContactDTO.getLookupOfficeCountryId()));
			if (clientContactDTO.getAddress1DropId() != 0) {
				MasterState state = masterStateRepository.findOne(clientContactDTO.getAddress1DropId());
				clientContact.setOfficeState(state.getState());
			} else {
				clientContact.setOfficeState(clientContactDTO.getOfficeState());
			}

			clientContact
					.setLookupCountry2(lookupCountryRepository.findOne(clientContactDTO.getLookupPermanentCountryId()));
			if (clientContactDTO.getAddress2DropId() != 0) {
				MasterState state1 = masterStateRepository.findOne(clientContactDTO.getAddress2DropId());
				clientContact.setPermanentState(state1.getState());
			} else {
				clientContact.setPermanentState(clientContactDTO.getPermanentState());
			}

			clientContact.setLookupCountry3(
					lookupCountryRepository.findOne(clientContactDTO.getLookupCorrespondenceCountryId()));
			if (clientContactDTO.getAddress3DropId() != 0) {
				MasterState state2 = masterStateRepository.findOne(clientContactDTO.getAddress3DropId());
				clientContact.setCorrespondenceState(state2.getState());
			} else {
				clientContact.setCorrespondenceState(clientContactDTO.getCorrespondenceState());
			}
			//System.out.println("email : " + clientContactDTO.getEmailID());
			cm.setLoginUsername(clientContactDTO.getEmailID());
			clientContact = clientContactRepository.save(clientContact);
			cm.setFinexaUser("Y");
			cm = clientMasterRepository.save(cm);
			clientContactDTO = mapper.map(clientContact, ClientContactDTO.class);
			clientContactDTO.setClientId(clientContact.getClientMaster().getId());
			log.debug("return clientContactDTO " + clientContactDTO);

			//send mail
			if (sendEmail == true) {
				StringBuilder sb = new StringBuilder();
				sb.append("Dear ");
				sb.append(cm.getFirstName() + " " + cm.getLastName() + ",\n");
				sb.append("\n");
				sb.append("Your Credentials for Finexa Application has been changed to:\n"
						+ "Username : " + cm.getLoginUsername() + "\nPassword : ");
				sb.append(cm.getLoginPassword() + "\n\n");
				sb.append("Administrator , Finexa Application \n\n");
				List<String> toList = new ArrayList<String>();
				toList.add(clientContact.getEmailID());

				EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_RESET, sb.toString());
			}
			
			return clientContactDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public ClientContactDTO save(int userId, ClientContactDTO clientContactDTO) throws RuntimeException {
		try {
			boolean sendEmail;
			log.debug("incoming clientContactDTO " + clientContactDTO);
			ClientMaster cm = clientMasterRepository.findOne(clientContactDTO.getClientId());
			
			//set the email flag
			if (!cm.getClientContacts().get(0).getEmailID().equals(clientContactDTO.getEmailID())) {
				sendEmail = true;
			} else {
				sendEmail = false;
			}
			
			ClientContact clientContact = mapper.map(clientContactDTO, ClientContact.class);
			clientContact.setClientMaster(cm);
			clientContact
					.setLookupCountry1(lookupCountryRepository.findOne(clientContactDTO.getLookupOfficeCountryId()));
			if (clientContactDTO.getAddress1DropId() != 0) {
				MasterState state = masterStateRepository.findOne(clientContactDTO.getAddress1DropId());
				clientContact.setOfficeState(state.getState());
			} else {
				clientContact.setOfficeState(clientContactDTO.getOfficeState());
			}

			clientContact
					.setLookupCountry2(lookupCountryRepository.findOne(clientContactDTO.getLookupPermanentCountryId()));
			if (clientContactDTO.getAddress2DropId() != 0) {
				MasterState state1 = masterStateRepository.findOne(clientContactDTO.getAddress2DropId());
				clientContact.setPermanentState(state1.getState());
			} else {
				clientContact.setPermanentState(clientContactDTO.getPermanentState());
			}

			clientContact.setLookupCountry3(
					lookupCountryRepository.findOne(clientContactDTO.getLookupCorrespondenceCountryId()));
			if (clientContactDTO.getAddress3DropId() != 0) {
				MasterState state2 = masterStateRepository.findOne(clientContactDTO.getAddress3DropId());
				clientContact.setCorrespondenceState(state2.getState());
			} else {
				clientContact.setCorrespondenceState(clientContactDTO.getCorrespondenceState());
			}
			//System.out.println("email : " + clientContactDTO.getEmailID());
			cm.setLoginUsername(clientContactDTO.getEmailID());
			clientContact = clientContactRepository.save(clientContact);
			cm.setFinexaUser("Y");
			cm = clientMasterRepository.save(cm);
			clientContactDTO = mapper.map(clientContact, ClientContactDTO.class);
			clientContactDTO.setClientId(clientContact.getClientMaster().getId());
			log.debug("return clientContactDTO " + clientContactDTO);

			//send mail
			if (sendEmail == true) {
				AdvisorUser advisorUser = advisorUserRepository.findOne(userId);
				
				String URLForEmail = "";
				Properties prop = new Properties();
				InputStream input = null;

				try {
				    input = new FileInputStream("/home/forgot_password.properties");
				    prop.load(input);
				    URLForEmail = prop.getProperty("URLForEmail");
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
				
				StringBuilder sb = new StringBuilder();
				sb.append("Dear ");
				sb.append(cm.getFirstName() + " " + cm.getLastName() + ",\n");
				sb.append("\nGreeting from " + advisorUser.getFirstName() + " " + advisorUser.getLastName() + "\n\n");
				sb.append("Please find below your new login credentials to access the portal:\n"
						+ "Username : " + cm.getLoginUsername() + "\nPassword : ");
				sb.append(cm.getLoginPassword() + "\nURL : " + URLForEmail + "\n\n");
				sb.append("In case of any queries, feel free to reach on " + advisorUser.getEmailID() + " and " 
						+ advisorUser.getPhoneNo() + "\n\n");
				sb.append("Regards,\n" + advisorUser.getFirstName() + " " + advisorUser.getLastName());
				List<String> toList = new ArrayList<String>();
				toList.add(clientContact.getEmailID());

				EmailUtil.sendEmailMain(FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL, FinexaConstant.FROM_EMAIL_PASSWORD, toList, FinexaConstant.FINEXA_PASSWORD_UPDATE, sb.toString());
			}
			
			return clientContactDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	
	@Override 
	public ClientContactDTO update(ClientContactDTO clientContactDTO) { 
		ClientMaster cm = clientMasterRepository.findOne(clientContactDTO.getClientId());
		ClientContact clientContact = mapper.map(clientContactDTO, ClientContact.class);
		clientContact.setClientMaster(cm);
		clientContactRepository.save(clientContact); 
		clientContactDTO = mapper.map(clientContact, ClientContactDTO.class);
		clientContactDTO.setClientId(clientContact.getClientMaster().getId());
		
		return clientContactDTO;
	}
	 
	@Override
	public List<ClientContactDTO> findByClientId(int clientId) throws RuntimeException {
		try {
			ClientMaster clientMaster = clientMasterRepository.findOne(clientId);
			List<ClientContactDTO> listDTO = new ArrayList<ClientContactDTO>();

			for (ClientContact clientContact : clientMaster.getClientContacts()) {

				int oCountryId = (clientContact.getLookupCountry1() != null) ? clientContact.getLookupCountry1().getId()
						: 0;
				int pCountryId = (clientContact.getLookupCountry2() != null) ? clientContact.getLookupCountry2().getId()
						: 0;
				int cCountryId = (clientContact.getLookupCountry3() != null) ? clientContact.getLookupCountry3().getId()
						: 0;
				
				String oCountryName = (clientContact.getLookupCountry1() != null) ? clientContact.getLookupCountry1().getName()
						: "";
				String pCountryName = (clientContact.getLookupCountry2() != null) ? clientContact.getLookupCountry2().getName()
						: "";
				String cCountryName = (clientContact.getLookupCountry3() != null) ? clientContact.getLookupCountry3().getName()
						: "";

				/*
				 * if(oCountryId==99){ state =
				 * masterStateRepository.findByState(clientContact.
				 * getOfficeState());
				 * clientContact.setOfficeState(state.getState()); }else{
				 * clientContact.setOfficeState(clientContact.getLookupCountry1(
				 * ).getName()); }
				 */
				ClientContactDTO cDTO = mapper.map(clientContact, ClientContactDTO.class);
				cDTO.setClientId(clientId);
				/* if(clientContact.getLookupCountry1().getId()==99) */if (oCountryId == 99) {
					MasterState state = masterStateRepository.findByState(clientContact.getOfficeState());
					cDTO.setAddress1DropId(state.getId());
				}
				/* if(clientContact.getLookupCountry2().getId()==99) */if (pCountryId == 99) {
					MasterState state1 = masterStateRepository.findByState(clientContact.getPermanentState());
					if (state1 != null && state1.getId() > 0) {
						cDTO.setAddress2DropId(state1.getId());
					}
				}
				/* if(clientContact.getLookupCountry3().getId()==99) */if (cCountryId == 99) {
					MasterState state2 = masterStateRepository.findByState(clientContact.getCorrespondenceState());
					cDTO.setAddress3DropId(state2.getId());
				}
				cDTO.setLookupOfficeCountryId(oCountryId);
				cDTO.setLookupPermanentCountryId(pCountryId);
				cDTO.setLookupCorrespondenceCountryId(cCountryId);
				
				cDTO.setLookupOfficeCountryName(oCountryName);
				cDTO.setLookupPermanentCountryName(pCountryName);
				cDTO.setLookupCorrespondenceCountryName(cCountryName);
				listDTO.add(cDTO);
			}

			return listDTO;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int delete(int id) {
		if (id != 0) {
			clientContactRepository.delete(id);
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public ClientContactDTO findById(int id) {
		ClientContactDTO clientContactDTO = mapper.map(clientContactRepository.findOne(id), ClientContactDTO.class);
		clientContactDTO.setClientId(clientContactRepository.findOne(id).getClientMaster().getId());
		MasterState state = masterStateRepository.findByState(clientContactRepository.findOne(id).getOfficeState());
		clientContactDTO.setAddress1DropId(state.getId());

		MasterState state1 = masterStateRepository.findByState(clientContactRepository.findOne(id).getPermanentState());
		clientContactDTO.setAddress2DropId(state1.getId());

		MasterState state2 = masterStateRepository
				.findByState(clientContactRepository.findOne(id).getCorrespondenceState());
		clientContactDTO.setAddress3DropId(state2.getId());
		return clientContactDTO;
	}

	@Override
	public List<ClientContactDTO> findAll() {
		ClientContactDTO ClientContactDTO;
		List<ClientContact> listClientContact = clientContactRepository.findAll();

		List<ClientContactDTO> listDTO = new ArrayList<ClientContactDTO>();
		for (ClientContact clientContact : listClientContact) {

			ClientContactDTO = mapper.map(clientContact, ClientContactDTO.class);
			ClientContactDTO.setClientId(clientContact.getClientMaster().getId());
			listDTO.add(ClientContactDTO);
		}

		return listDTO;
	}

	@Override
	public boolean checkEmailExists(String email, int clientId) throws RuntimeException {
		try {
			boolean flag = true;
			
			AdvisorUser advisorUser = advisorUserRepository.findByEmailID(email);
			if (advisorUser != null) {
				flag = false;
			} else {
				List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");

				for (ClientMaster clientMaster : listClientMaster) {
					for (ClientContact clientContact : clientMaster.getClientContacts()) {
						log.debug("clientContact.getClientMaster().getId() " + clientContact.getClientMaster().getId());
						log.debug("clientId " + clientId);
						if (clientContact.getClientMaster().getId() != clientId) {
							log.debug("clientContact.getEmailID() " + clientContact.getEmailID());
							log.debug("email " + email);
							if (clientContact.getEmailID().equals(email)) {
								log.debug("same");
								flag = false;
								break;
							}
						}
					}
				}
			}
			log.debug("flag " + flag);
			return flag;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkMobileExists(long mobile, int clientId) throws RuntimeException {
		try {
			boolean flag = true;
			List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");

			for (ClientMaster clientMaster : listClientMaster) {
				for (ClientContact clientContact : clientMaster.getClientContacts()) {
					log.debug("clientContact.getClientMaster().getId() " + clientContact.getClientMaster().getId());
					log.debug("clientId " + clientId);
					if (clientContact.getClientMaster().getId() != clientId) {
						log.debug("clientContact.getEmailID() " + clientContact.getMobile());
						log.debug("mobile " + mobile);
						if (clientContact.getMobile().longValue() == mobile) {
							log.debug("same");
							flag = false;
							break;
						}
					}
				}
			}
			log.debug("flag " + flag);
			return flag;
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkUniqueEmail(String email) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			int cc = 0;
			
			AdvisorUser advisorUser = advisorUserRepository.findByEmailID(email);
			if (advisorUser != null) {
				cc = 1;
			} else {
				List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");
				for (ClientMaster clientMaster : listClientMaster) {
					for (ClientContact clientContact : clientMaster.getClientContacts()) {
						// clientContact =
						// clientContactRepository.findByEmailID(email);
						if (clientContact.getEmailID().equals(email)) {
							cc = 1;
							break;
						}
	
					}
				}
			}
			log.debug("cc " + cc);
			return (cc != 0) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean checkUniqueMobile(long mobile) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			List<ClientMaster> listClientMaster = clientMasterRepository.getByActiveFlag("Y");
			int cc = 0;
			for (ClientMaster clientMaster : listClientMaster) {
				for (ClientContact clientContact : clientMaster.getClientContacts()) {
					// clientContact =
					// clientContactRepository.findByEmailID(email);
					if (clientContact.getMobile().longValue() == mobile) {
						cc = 1;
						break;
					}

				}
			}
			log.debug("cc " + cc);
			return (cc != 0) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override

	public boolean checkUniquePincode(int pincode, int stateId) throws RuntimeException{
		// TODO Auto-generated method stub
		try {
			int cc = 0;
			MasterState masterState = masterStateRepository.findOne(stateId);

			for (MasterPincode masterPincode : masterState.getMasterPincodes()) {
				if (masterPincode.getPincode() == pincode) {
					cc = 1;
					break;
				} else {
					cc = 0;
				}
			}
			/*
			 * MasterState masterState = masterStateRepository.findOne(stateId);
			 * 
			 * for(MasterPincode masterPincode:masterPincodes){ for(MasterPincode
			 * masterPincode1:masterState.getMasterpincodes()) { // clientContact =
			 * clientContactRepository.findByEmailID(email);
			 * if(masterPincode1.getPincode()==pincode){ cc=1;
			 * 
			 * }else{ break; } } }
			 */
			// }
			log.debug("cc " + cc);
			return (cc != 0) ? false : true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public List<String> findAllCities() throws RuntimeException {
		try {
			HashSet<String> retset = new HashSet<String>();
			for (ClientContact cc : clientContactRepository.findByCorrespondenceCityIsNotNull()) {
				if (StringUtils.isNotEmpty(cc.getCorrespondenceCity())) {
					retset.add(cc.getCorrespondenceCity());
				}

			}

			for (ClientContact cc : clientContactRepository.findByOfficeCityIsNotNull()) {
				if (StringUtils.isNotEmpty(cc.getOfficeCity())) {
					retset.add(cc.getOfficeCity());
				}
			}

			for (ClientContact cc : clientContactRepository.findByPermanentCityIsNotNull()) {
				if (StringUtils.isNotEmpty(cc.getPermanentCity())) {
					retset.add(cc.getPermanentCity());
				}
			}

			return retset.stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}
	@Override
	public ClientContact autoSave(ClientContactDTO clientContactDTO) throws RuntimeException {
		try {
			log.debug("incoming clientContactDTO " + clientContactDTO);
			ClientMaster cm = clientMasterRepository.findOne(clientContactDTO.getClientId());
			ClientContact clientContact = mapper.map(clientContactDTO, ClientContact.class);
			clientContact.setClientMaster(cm);
			
			String generatedEmail = generateRandomUniqueEmail();
			clientContact.setEmailID(generatedEmail);
			
			BigInteger generatedMobile = generateRandomUniqueMobile();
			clientContact.setMobile(generatedMobile);
			clientContact.setEmergencyContact(generatedMobile);
			
			clientContact.setLookupCountry1(lookupCountryRepository.findOne(clientContactDTO.getLookupOfficeCountryId()));
			
			if (clientContactDTO.getAddress1DropId() != 0) {
				MasterState state = masterStateRepository.findOne(clientContactDTO.getAddress1DropId());
				clientContact.setOfficeState(state.getState());
			} else {
				clientContact.setOfficeState(clientContactDTO.getOfficeState());
			}

			clientContact.setLookupCountry2(lookupCountryRepository.findOne(clientContactDTO.getLookupPermanentCountryId()));
			
			if (clientContactDTO.getAddress2DropId() != 0) {
				MasterState state1 = masterStateRepository.findOne(clientContactDTO.getAddress2DropId());
				clientContact.setPermanentState(state1.getState());
			} else {
				clientContact.setPermanentState(clientContactDTO.getPermanentState());
			}

			clientContact.setLookupCountry3(lookupCountryRepository.findOne(clientContactDTO.getLookupCorrespondenceCountryId()));
			
			if (clientContactDTO.getAddress3DropId() != 0) {
				MasterState state2 = masterStateRepository.findOne(clientContactDTO.getAddress3DropId());
				clientContact.setCorrespondenceState(state2.getState());
			} else {
				clientContact.setCorrespondenceState(clientContactDTO.getCorrespondenceState());
			}
			clientContact = clientContactRepository.save(clientContact);
	

			return clientContact;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}
	
	public String generateRandomUniqueEmail() {
		String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
		if(clientContactRepository.findByEmailID(email) != null) {
			generateRandomUniqueEmail();
		}
		return email;
	}
	
	public BigInteger generateRandomUniqueMobile() {
		//String randomMobile = 79393 + RandomStringUtils.randomNumeric(5);
		BigInteger mobile = new BigInteger(79393 + RandomStringUtils.randomNumeric(5));
		if(clientContactRepository.findByMobile(mobile) != null) {
			generateRandomUniqueMobile();
		}
		return mobile;
	}

	@Override
	public List<String> findAllCities(int advisorID) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			HashSet<String> retset = new HashSet<String>();
			AdvisorUser advisorUser = advisorUserRepository.findById(advisorID);
			List<ClientMaster> clientMasters = clientMasterRepository.findByAdvisorUser(advisorUser);
			
			for (ClientMaster ob : clientMasters) {
				for (ClientContact cc : ob.getClientContacts()) {

					if (cc.getCorrespondenceCity() != null) {
						retset.add(cc.getCorrespondenceCity());
					}

					if (cc.getOfficeCity() != null) {
						retset.add(cc.getOfficeCity());
					}

					if (cc.getPermanentCity() != null) {
						retset.add(cc.getPermanentCity());
					}

				}

			}

			return retset.stream().collect(Collectors.toList());
			
		} catch(RuntimeException e) {
			throw new RuntimeException(e);
		}
		
	}
}
