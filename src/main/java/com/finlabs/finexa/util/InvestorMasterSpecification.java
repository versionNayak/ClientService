package com.finlabs.finexa.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.ClientContact;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.InvestorMasterBO;
import com.finlabs.finexa.model.InvestorMasterBOPK;
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.util.FinexaUtil;

public class InvestorMasterSpecification {

	private static Logger log = LoggerFactory.getLogger(InvestorMasterSpecification.class);

	public static Specification<InvestorMasterBO> findByCriteria(final SearchClientDTO searchClientDTO) {
		return new Specification<InvestorMasterBO>() {
			@Override
			public Predicate toPredicate(Root<InvestorMasterBO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String p = "%";
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (searchClientDTO.getSearchName() != null && !searchClientDTO.getSearchName().isEmpty()) {
					predicates.add(cb.like(root.get("firstName"), p + searchClientDTO.getSearchName() + p));
				}
				if (searchClientDTO.getSearchName() != null && !searchClientDTO.getSearchName().isEmpty()) {
					predicates.add(cb.like(root.get("lastName"), p + searchClientDTO.getSearchName() + p));
				}
				
				
				return cb.or(predicates.toArray(new Predicate[] {}));
			}
		};
	}

	public static Specification<InvestorMasterBO> findClientRecordsByCriteria(final SearchClientDTO searchClientDTO) {
		return new Specification<InvestorMasterBO>() {
			@Override
			public Predicate toPredicate(Root<InvestorMasterBO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String p = "%";
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (searchClientDTO.getSearchName() != null && !searchClientDTO.getSearchName().isEmpty()) {
					predicates.add(cb.like(root.get("investorName"), p + searchClientDTO.getSearchName() + p));
				}
				
				if (searchClientDTO.getSearchPan() != null && !searchClientDTO.getSearchPan().isEmpty()) {
					predicates.add(cb.like(root.get("id").get("investorPAN"), p + searchClientDTO.getSearchPan() + p));
				}
				
				return cb.or(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<InvestorMasterBO> findAllClientRecordsByCriteria(final SearchClientDTO searchClientDTO) {
		return new Specification<InvestorMasterBO>() {
			@Override
			public Predicate toPredicate(Root<InvestorMasterBO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String p = "%";
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (searchClientDTO.getSearchName() != null && !searchClientDTO.getSearchName().isEmpty()) {
					predicates.add(cb.equal(root.get("investorName"),   searchClientDTO.getSearchName()));
				}
				
				if (searchClientDTO.getSearchPan() != null && !searchClientDTO.getSearchPan().isEmpty()) {
					predicates.add(cb.equal(root.get("id").get("investorPAN"),  searchClientDTO.getSearchPan() ));
				}
				
				return cb.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
