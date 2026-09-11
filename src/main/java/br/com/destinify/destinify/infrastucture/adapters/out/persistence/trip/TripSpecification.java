package br.com.destinify.destinify.infrastucture.adapters.out.persistence.trip;

import br.com.destinify.destinify.application.dto.request.TripFilterQuery;
import br.com.destinify.destinify.infrastucture.adapters.out.entity.TripEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TripSpecification {

    public static Specification<TripEntity> withFilter(TripFilterQuery filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.destination() != null && !filter.destination().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("destination")), "%" + filter.destination().toLowerCase() + "%"));
            }

            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }

            if (filter.minPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.minPrice()));
            }

            if (filter.maxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.maxPrice()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}