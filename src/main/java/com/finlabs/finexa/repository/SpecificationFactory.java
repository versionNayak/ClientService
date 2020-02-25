package com.finlabs.finexa.repository;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.finlabs.finexa.dto.ClientSearchDTO;

public final class SpecificationFactory {

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Specification containsLike(String attribute, String value) {
        return (root, query, cb) -> cb.like(root.get(attribute), "%" + value + "%");
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Specification isBetween(String attribute, int min, int max) {
        return (root, query, cb) -> cb.between(root.get(attribute), min, max);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Specification isBetween(String attribute, double min, double max) {
        return (root, query, cb) -> cb.between(root.get(attribute), min, max);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Enum<T>> Specification enumMatcher(String attribute, T queriedValue) {
        return (root, query, cb) -> {
            Path<T> actualValue = root.get(attribute);

            if (queriedValue == null) {
                return null;
            }

            return cb.equal(actualValue, queriedValue);
        };
    }
  /*  
    public static Specification extendedCarSearch(ClientSearchDTO searchObj) {
        return Specifications
                .where(containsLike("plate", q.getPlate()))
                .and(containsLike("type", q.getType())).and(enumMatcher("brand", q.getBrand()))
                .and(isBetween("usage", q.getUsage().getMin(), q.getUsage().getMax()))
                .and(isBetween("maxSpeed", q.getMaxSpeed().getMin(), q.getMaxSpeed().getMax()))
                .and(isBetween("horsePower", q.getHorsePower().getMin(), q.getHorsePower().getMax()))
                .and(enumMatcher("fuelKind", q.getFuelKind()));
    }

    public List extendedSearch(CarSearchQuery query) {
        return repository.findAll(CarSpecificationFactory.extendedCarSearch(query));
    }*/
    
/*    Specifications.where(
    	    (root, query, builder) -> {
    	        final Join<PersonEntity, AddressEntity> addresses = root.join(PersonEntity.address, JoinType.LEFT);
    	        return builder.or(
    	            builder.like(builder.lower(addresses.get(AddressEntity_.addressLine1)), text),
    	            builder.like(builder.lower(addresses.get(AddressEntity_.addressLine2)), text),
    	            builder.like(builder.lower(addresses.get(AddressEntity_.code)), text),
    	        );
    	    }
    	);*/
}
