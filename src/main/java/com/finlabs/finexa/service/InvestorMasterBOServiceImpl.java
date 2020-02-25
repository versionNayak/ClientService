package com.finlabs.finexa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.repository.AdvisorUserRepository;
import com.finlabs.finexa.repository.InvestMasterBORepository;
import com.finlabs.finexa.util.FinexaUtil;

@Service("InvestorMasterBOService")
@Transactional
public class InvestorMasterBOServiceImpl implements InvestorMasterBOService {
	
	private static Logger log = LoggerFactory.getLogger(InvestorMasterBOServiceImpl.class);

	@Autowired
	private AdvisorUserRepository advisorUserRepository;
	
	@Autowired
	private InvestMasterBORepository investMasterBORepository;
	
	@Override
	public List<String> getAllInvestorNameList(int advisorID) throws RuntimeException {
		// TODO Auto-generated method stub
		try {
			
			Set<String> uniqueSet = new HashSet<String>();
			AdvisorUser advUser = advisorUserRepository.findOne(advisorID);
			List<InvestorMasterBO> list = investMasterBORepository.findByAdvisorUser(advUser);
			
			for(InvestorMasterBO obj : list) {
				uniqueSet.add(FinexaUtil.capitailizeWord(obj.getInvestorName().toLowerCase()));
			}
			
			return uniqueSet.stream().sorted().collect(Collectors.toList());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	
}
