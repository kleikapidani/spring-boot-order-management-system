package com.ecommerce.analytics.orderservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Order creation and update requests
 */
@Data
@NoArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid amount format")
    private BigDecimal totalAmount;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    public OrderRequest(String customerName, String customerEmail, BigDecimal totalAmount) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
    }

    public OrderRequest(String customerName, String customerEmail, BigDecimal totalAmount, String notes) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.notes = notes;
    }
}
