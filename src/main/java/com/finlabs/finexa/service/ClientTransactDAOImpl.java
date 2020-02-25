package com.finlabs.finexa.service;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.repository.ClientTransactDAO;

public class ClientTransactDAOImpl implements ClientTransactDAO {

	@Override
	public AdvisorUser getAdvisorById(int advisorId, Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		AdvisorUser advisorUser = null;
		try {
			
			Criteria criteria = session.createCriteria(AdvisorUser.class);
			criteria.add(Restrictions.eq("id", (int) advisorId));
			advisorUser = (AdvisorUser) criteria.uniqueResult();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());
		}
		return advisorUser;
	}
	
	
}
