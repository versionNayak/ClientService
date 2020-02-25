package com.finlabs.finexa.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.model.StagingInvestorMasterBO;

public class StagingInvestorMasterSpecification {

	private static Logger log = LoggerFactory.getLogger(StagingInvestorMasterSpecification.class);

	public static Specification<StagingInvestorMasterBO> findByCriteria(final SearchClientDTO searchClientDTO) {
		return new Specification<StagingInvestorMasterBO>() {
			@Override
			public Predicate toPredicate(Root<StagingInvestorMasterBO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

	public static Specification<StagingInvestorMasterBO> findClientRecordsByCriteria(final SearchClientDTO searchClientDTO) {
		return new Specification<StagingInvestorMasterBO>() {
			@Override
			public Predicate toPredicate(Root<StagingInvestorMasterBO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String p = "%";
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (searchClientDTO.getSearchName() != null && !searchClientDTO.getSearchName().isEmpty()) {
					predicates.add(cb.like(root.get("investorName"), p + searchClientDTO.getSearchName() + p));
				}
				
				if (searchClientDTO.getSearchPan() != null && !searchClientDTO.getSearchPan().isEmpty()) {
					predicates.add(cb.like(root.get("investorPAN"), p + searchClientDTO.getSearchPan() + p));
				}
				
				return cb.or(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
