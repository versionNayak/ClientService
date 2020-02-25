package com.finlabs.finexa.repository;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.finlabs.finexa.dto.AdvisorProductRecoDTO;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AdvisorMaster;
import com.finlabs.finexa.model.AdvisorProductReco;
import com.finlabs.finexa.repository.AdvisorProductRecoRepository;

//@Repository
public class AdvisorProductRecoDaoImpl {/*

	@Override
	public Date saveOrUpdate(AdvisorProductRecoDTO dto,Session session) throws FinexaDaoException{
		// TODO Auto-generated method stub
		try {
			int result;
			Date date = new Date();
			String hql = "select id from advisorProductReco where recoSaveDate = ?";
			SQLQuery query = session.createSQLQuery(hql);
			query.setParameter(1, date);
			Object  = query.uniqueResult();
			
			if(dto.getDate() == null) {
				date = new Date();
				hql = "INSERT INTO advisorProductReco(advisorID,recoSaveDate,module,productPlan) values(?,?,?,?)";
				query = session.createSQLQuery(hql);
				//Query query = session.createQuery(hql);
				query.setParameter(0, dto.getAdviorID());
				query.setParameter(1, date);
				query.setParameter(2, dto.getModule());
				query.setParameter(3, dto.getProductPlan());
				result = query.executeUpdate();
				System.out.println("Rows affected: " + result);
				return date;
			}else {
				 hql = "UPDATE advisorProductReco SET date = :date, module = :module, productPlan = :productPlan where id.advisorUser.id = :advisorID";
				 query = session.createSQLQuery(hql);
				query.setParameter("recoSaveDate",dto.getDate());
				query.setParameter("module",dto.getModule());
				query.setParameter("productPlan",dto.getProductPlan().getBytes());
				result = query.executeUpdate();
				return dto.getDate();
			}
		} catch (Exception e) {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdvisorProductReco> getAllProductRecoByAdvisorId(int advisorID, Session session)
			throws FinexaDaoException {
		// TODO Auto-generated method stub
		List<AdvisorProductReco> recoList = null;
		try {
			Query query = session.createQuery("from AdvisorProductReco where advisorUser.id = :advisorID order by recoSaveDate");
			query.setParameter("advisorID", advisorID);
			recoList = (List<AdvisorProductReco>)query.list();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());
		}
		return recoList;
	}

	@Override
	public List<AdvisorMaster> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdvisorMaster> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdvisorMaster> findAll(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends AdvisorMaster> List<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends AdvisorMaster> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInBatch(Iterable<AdvisorMaster> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AdvisorMaster getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<AdvisorMaster> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends AdvisorMaster> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdvisorMaster findOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(AdvisorMaster entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends AdvisorMaster> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

*/}