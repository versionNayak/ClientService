package com.finlabs.finexa.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.finlabs.finexa.dto.ClientSearchDTO;
import com.finlabs.finexa.dto.SearchClientDTO;
import com.finlabs.finexa.model.ClientMaster;

public class ClientSpecification {

	    public static Specification<ClientMaster> findByCriteria(final SearchClientDTO searchCriteria) {

	        return new Specification<ClientMaster>() {

	            @Override
	            public Predicate toPredicate(
	                Root<ClientMaster> root,
	                CriteriaQuery<?> query, CriteriaBuilder cb) {

	                List<Predicate> predicates = new ArrayList<Predicate>();
	                
	                if (searchCriteria.getSearchName() != null && !searchCriteria.getSearchName().isEmpty()) {
	                    predicates.add(cb.like(root.get("firstName"), searchCriteria.getSearchName()));
	                }
	                if (searchCriteria.getSearchName() != null && !searchCriteria.getSearchName().isEmpty()) {
	                    predicates.add(cb.equal(root.get("lastName"), searchCriteria.getSearchName()));
	                }	                
	                

/*	                if (searchCriteria.getView() != null && !searchCriteria.getView().isEmpty()) {
	                    predicates.add(cb.equal(root.get("viewType"), searchCriteria.getView()));
	                }
	                if (searchCriteria.getFeature() != null && !searchCriteria.getFeature().isEmpty()) {
	                    predicates.add(cb.equal(root.get("title"), searchCriteria.getFeature()));
	                }
	                if (searchCriteria.getEpic() != null && !searchCriteria.getEpic().isEmpty()) {
	                    predicates.add(cb.equal(root.get("epic"), searchCriteria.getEpic()));
	                }
	                if (searchCriteria.getPerformingGroup() != null && !searchCriteria.getPerformingGroup().isEmpty()) {
	                    predicates.add(cb.equal(root.get("performingGroup"), searchCriteria.getPerformingGroup()));
	                }
	                if (searchCriteria.getPlannedStartDate() != null) {
	                    System.out.println("searchCriteria.getPlannedStartDate():" + searchCriteria.getPlannedStartDate());
	                    predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("plndStartDate"), searchCriteria.getPlannedStartDate()));
	                }
	                if (searchCriteria.getPlannedCompletionDate() != null) {
	                    predicates.add(cb.lessThanOrEqualTo(root.<Date>get("plndComplDate"), searchCriteria.getPlannedCompletionDate()));
	                }
	                if (searchCriteria.getTeam() != null && !searchCriteria.getTeam().isEmpty()) {
	                    predicates.add(cb.equal(root.get("agileTeam"), searchCriteria.getTeam()));
	                }
*/
	                return cb.and(predicates.toArray(new Predicate[] {}));
	            }
	        };
	    }
	
}
