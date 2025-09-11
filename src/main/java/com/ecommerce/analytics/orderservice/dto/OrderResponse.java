package com.ecommerce.analytics.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Order responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private BigDecimal totalAmount;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
