package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.query;

import com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.util.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Log
@AllArgsConstructor
public abstract class GenericSpecificationBuilder<U> {

    private List<SearchCriteria> params;

    public Specification<U> build() {
        if (params.isEmpty()) {
            return null;
        }

        log.info("Adding first specification " + params.get(0));
        Specification<U> result = buildSpecification(params.get(0));
        log.info("Specification: " + result.toString());

        for (int i = 1; i < params.size(); i++) {
            log.info("Adding new specification " + params.get(i));
            result = result.and(buildSpecification(params.get(i)));
            log.info("Specification: " + result.toString());
        }

        log.info("Final Specification: " + result.toString());

        return result;
    }

    private Specification<U> buildSpecification(SearchCriteria criteria) {
        return (Root<U> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            switch (criteria.key()) {
                case "precioMin":
                    return builder.greaterThanOrEqualTo(
                            root.get("precio").as(Double.class),
                            Double.valueOf(criteria.value().toString())
                    );

                case "precioMax":
                    return builder.lessThanOrEqualTo(
                            root.get("precio").as(Double.class),
                            Double.valueOf(criteria.value().toString())
                    );

                case "categoria":
                    return builder.like(
                            root.join("categoria").get("nombre"),
                            "%" + criteria.value() + "%"
                    );

                default:
                    if (criteria.operation().equalsIgnoreCase(">")) {
                        return builder.greaterThanOrEqualTo(
                                root.get(criteria.key()).as(Double.class),
                                Double.valueOf(criteria.value().toString())
                        );
                    } else if (criteria.operation().equalsIgnoreCase("<")) {
                        return builder.lessThanOrEqualTo(
                                root.get(criteria.key()).as(Double.class),
                                Double.valueOf(criteria.value().toString())
                        );
                    } else if (criteria.operation().equalsIgnoreCase(":")) {
                        if (root.get(criteria.key()).getJavaType() == String.class) {
                            return builder.like(
                                    root.get(criteria.key()),
                                    "%" + criteria.value() + "%"
                            );
                        } else {
                            return builder.equal(root.get(criteria.key()), criteria.value());
                        }
                    }
            }
            return null;
        };
    }
}
