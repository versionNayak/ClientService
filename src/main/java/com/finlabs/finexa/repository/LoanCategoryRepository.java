package com.finlabs.finexa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finlabs.finexa.model.LookupLoanCategory;


public interface LoanCategoryRepository extends JpaRepository<LookupLoanCategory, Byte>{

	//LookupLoanCategory findOne(byte loanCategoryId);

}
