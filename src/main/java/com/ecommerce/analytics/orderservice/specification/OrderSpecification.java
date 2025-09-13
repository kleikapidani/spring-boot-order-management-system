package com.ecommerce.analytics.orderservice.specification;

import com.ecommerce.analytics.orderservice.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Utility class for building JPA Specifications for Order entity
 * Provides reusable predicates for complex queries
 */
public class OrderSpecification {

    /**
     * Create specification for customer name search (case-insensitive, partial match)
     */
    public static Specification<Order> hasCustomerName(String customerName) {

        return (root, query, criteriaBuilder) -> {
            if (customerName == null || customerName.trim().isEmpty()) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("customerName")),
                    "%" + customerName.trim().toLowerCase() + "%"
            );
        };
    }

    /**
     * Create specification for exact status match
     */
    public static Specification<Order> hasStatus(String status) {

        return (root, query, criteriaBuilder) -> {
            if (status == null || status.trim().isEmpty()) {
                return null;
            }

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    /**
     * Create specification for minimum amount filter
     */
    public static Specification<Order> hasMinAmount(BigDecimal minAmount) {

        return (root, query, criteriaBuilder) -> {
            if (minAmount == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), minAmount);
        };
    }

    /**
     * Create specification for amount range filter
     */
    public static Specification<Order> hasAmountBetween(BigDecimal minAmount, BigDecimal maxAmount) {

        return (root, query, criteriaBuilder) -> {
            if (minAmount == null || maxAmount == null) {
                return null;
            }

            return criteriaBuilder.between(root.get("totalAmount"), minAmount, maxAmount);
        };
    }


}
