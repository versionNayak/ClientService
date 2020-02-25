package com.finlabs.finexa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finlabs.finexa.dto.FamilyAttributeMasterDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AuxillaryInvestorMaster;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.AuxillaryMasterRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;

@Service("FamilyAttributeMasterService")
@Transactional
public class FamilyAttributeMasterServiceImpl implements FamilyAttributeMasterService {
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private AuxillaryMasterRepository auxillaryMasterRepository;
	
	@Autowired
	private AdvisorUserRepository advisorUserRepository;

	@Override
	public FamilyAttributeMasterDTO save(FamilyAttributeMasterDTO familyAttributeMasterDTO) throws RuntimeException {
		// TODO Auto-generated method stub
		
		FamilyAttributeMasterDTO famDTO = new FamilyAttributeMasterDTO();
		
		try {
			ClientMaster cm = clientMasterRepository.findOne(familyAttributeMasterDTO.getClientId());
			
			AdvisorUser advUser = null;
			if(familyAttributeMasterDTO.getAdvUserRMId() != 0) {
				advUser = advisorUserRepository.findOne(familyAttributeMasterDTO.getAdvUserRMId());
			} else {
				if(familyAttributeMasterDTO.getAdvUserSBId() != 0) {
					advUser = advisorUserRepository.findOne(familyAttributeMasterDTO.getAdvUserSBId());
				}
			}
			
			if(advUser != null) {
				cm.setAdvisorUser(advUser);
				cm.setFinexaUser("Y");
				cm = clientMasterRepository.save(cm);
				
				List<AuxillaryInvestorMaster> aimList = auxillaryMasterRepository.findAllByClientMaster(cm);
				for(AuxillaryInvestorMaster obj : aimList) {
					obj.setUserID(cm.getAdvisorUser().getId());
					auxillaryMasterRepository.save(obj);
				}
				
				famDTO.setStatus("Success");
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			famDTO.setStatus("Failure");
			throw new RuntimeException(e);
		}
		
		return famDTO;
	}

}
