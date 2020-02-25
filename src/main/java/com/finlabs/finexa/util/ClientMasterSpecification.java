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
import com.finlabs.finexa.model.LookupMaritalStatus;
import com.finlabs.finexa.util.FinexaUtil;

public class ClientMasterSpecification {

	private static Logger log = LoggerFactory.getLogger(ClientMasterSpecification.class);

	public static Specification<ClientMaster> findByCriteria(final SearchClientDTO searchClientDTO) {
		return new Specification<ClientMaster>() {
			@Override
			public Predicate toPredicate(Root<ClientMaster> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

	public static Specification<ClientMaster> findClientRecordsByCriteria(final SearchClientDTO searchClientDTO) {
		return new Specification<ClientMaster>() {
			@Override
			public Predicate toPredicate(Root<ClientMaster> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				try {
					List<Predicate> predicates = new ArrayList<Predicate>();
					if (searchClientDTO.getAdvisorId() != null && !searchClientDTO.getAdvisorId().isEmpty()) {
						final Subquery<Integer> advUserQuery = query.subquery(Integer.class);
						final Root<AdvisorUser> advUser = advUserQuery.from(AdvisorUser.class);
						final Join<AdvisorUser, ClientMaster> clientMasters = advUser.join("clientMasters");
						advUserQuery.select(clientMasters.<Integer>get("id"));
					advUserQuery.where(cb.and(
							cb.equal(advUser.<Integer>get("id"), Integer.parseInt(searchClientDTO.getAdvisorId())),
							cb.equal(clientMasters.get("activeFlag"), String.valueOf('Y'))));
						/*advUserQuery.where(cb.and(
								cb.equal(advUser.<Integer>get("id"), Integer.parseInt(searchClientDTO.getAdvisorId()))));*/
						predicates.add(cb.in(root.get("id")).value(advUserQuery));
					}

					if (searchClientDTO.getClientCity() != null && !searchClientDTO.getClientCity().isEmpty()) {
						// join client contact and ..
						Join<ClientMaster, ClientContact> clientContacts = root.join("clientContacts");

						List<Predicate> cityPred = new ArrayList<Predicate>();
						cityPred.add(cb.like(clientContacts.get("correspondenceCity"), searchClientDTO.getClientCity()));
						cityPred.add(cb.like(clientContacts.get("officeCity"), searchClientDTO.getClientCity()));
						cityPred.add(cb.like(clientContacts.get("permanentCity"), searchClientDTO.getClientCity()));

						predicates.add(cb.or(cityPred.toArray(new Predicate[] {})));

					}

					if (searchClientDTO.getClientMaritalSatusId() != null
							&& !searchClientDTO.getClientMaritalSatusId().isEmpty()) {
						Join<ClientMaster, LookupMaritalStatus> clientMaritalStatus = root.join("lookupMaritalStatus");
						predicates.add(cb.equal(clientMaritalStatus.get("id"),
								Byte.parseByte(searchClientDTO.getClientMaritalSatusId())));

					}

					if (searchClientDTO.getClientGender() != null && !searchClientDTO.getClientGender().isEmpty()) {
						predicates.add(cb.like(root.get("gender"), searchClientDTO.getClientGender()));

					}

					if (searchClientDTO.getClientRtdFlag() != null && !searchClientDTO.getClientRtdFlag().isEmpty()) {
						predicates.add(cb.like(root.get("retiredFlag"), searchClientDTO.getClientRtdFlag()));
					}

					if (searchClientDTO.getClientOrgName() != null && !searchClientDTO.getClientOrgName().isEmpty()) {
						predicates.add(cb.like(root.get("orgName"), searchClientDTO.getClientOrgName()));
					}

					if (searchClientDTO.getClientMinAge() != null && !searchClientDTO.getClientMinAge().isEmpty()
							&& searchClientDTO.getClientMaxAge() != null && !searchClientDTO.getClientMaxAge().isEmpty()) {

						Date beforeDOB = FinexaUtil
								.getDateOfBirthFromAge(Integer.parseInt(searchClientDTO.getClientMaxAge()), 0, 0);
						Date afterDOB = FinexaUtil
								.getDateOfBirthFromAge(Integer.parseInt(searchClientDTO.getClientMinAge()), 0, 0);

						log.debug("beforeDOB : " + beforeDOB.toString());
						log.debug("afterDOB : " + afterDOB.toString());
						predicates.add(cb.between(root.get("birthDate"), beforeDOB, afterDOB));
					}

					// for client info
					
					
//					String name = null;
//					name=root.get("firstName") + " " + root.get("lastName");
//					searchClientDTO.setSearchName(name);
					
					/*if (searchClientDTO.getSearchName() != null && !searchClientDTO.getSearchName().isEmpty()) {
						predicates.add(cb.like(root.get("firstName"+ " " +"lastName"), + searchClientDTO.getSearchName() + p));
					}*/
					if (searchClientDTO.getSearchName() != null && !searchClientDTO.getSearchName().isEmpty()) {
						List<Predicate> namePred = new ArrayList<Predicate>();
						/*namePred.add(cb.like(root.get("firstName"), "%" + searchClientDTO.getSearchName() + "%"));
						namePred.add(cb.like(root.get("middleName"), "%" + searchClientDTO.getSearchName() + "%"));
						namePred.add(cb.like(root.get("lastName"), "%" + searchClientDTO.getSearchName() + "%"));*/
						
						if(searchClientDTO.getSearchNameArr() != null) {
							namePred.add(cb.like(root.get("firstName"), "%" + searchClientDTO.getSearchNameArr()[0] + "%"));
						    //namePred.add(cb.like(root.get("middleName"), "%" + searchClientDTO.getSearchNameArr()[1] + "%"));
							namePred.add(cb.like(root.get("lastName"), "%" + searchClientDTO.getSearchNameArr()[1] + "%"));
						} else {
							namePred.add(cb.like(root.get("firstName"), "%" + searchClientDTO.getSearchName() + "%"));
							namePred.add(cb.like(root.get("middleName"), "%" + searchClientDTO.getSearchName() + "%"));
							namePred.add(cb.like(root.get("lastName"), "%" + searchClientDTO.getSearchName() + "%"));
						}
						
						
						//namePred.add(cb.like(root.get("firstName"+ " " +"lastName"), "%" + searchClientDTO.getSearchName() + "%"));

						predicates.add(cb.or(namePred.toArray(new Predicate[] {})));
					}

					if (searchClientDTO.getSearchEmail() != null && !searchClientDTO.getSearchEmail().isEmpty()) {
						Join<ClientMaster, ClientContact> clientContacts = root.join("clientContacts");
						predicates
								.add(cb.like(clientContacts.get("emailID"), "%" + searchClientDTO.getSearchEmail() + "%"));
					}

					if (searchClientDTO.getSearchPan() != null && !searchClientDTO.getSearchPan().isEmpty()) {
						predicates.add(cb.like(root.get("pan"), searchClientDTO.getSearchPan()));
					}

					if (searchClientDTO.getSearchMobile() != null && !searchClientDTO.getSearchMobile().isEmpty()) {
						if (searchClientDTO.getSearchMobile().matches("[0-9]+")) {
							Join<ClientMaster, ClientContact> clientContacts = root.join("clientContacts");
							predicates.add(cb.equal(clientContacts.get("mobile"),
									new BigInteger(searchClientDTO.getSearchMobile())));
						}
					}

					if (searchClientDTO.getSearchAadhar() != null && !searchClientDTO.getSearchAadhar().isEmpty()) {
						if (searchClientDTO.getSearchAadhar().matches("[0-9]+")) {
							/*
							 * Join<ClientMaster, ClientContact> clientContacts =
							 * root.join("clientContacts");
							 * predicates.add(cb.equal(clientContacts.get("aadhar"), new
							 * BigInteger(searchClientDTO.getSearchAadhar())));
							 */
							predicates.add(cb.equal(root.get("aadhar"), new BigInteger(searchClientDTO.getSearchAadhar())));
						}
					}

					return cb.and(predicates.toArray(new Predicate[] {}));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
	}

}
