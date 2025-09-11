package com.ecommerce.analytics.orderservice.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Search criteria for filtering orders
 */
@Data
public class OrderSearchCriteria {

    private String customerName;

    @Pattern(regexp = "PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED",
            message = "Status must be one of: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED")
    private String status;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;
}
