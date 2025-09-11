package com.ecommerce.analytics.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order Entity representing an e-commerce order
 *
 * @author Klei Kapidani
 * @version 1.0
 */
@Entity
@Table(name = "orders",
        uniqueConstraints = @UniqueConstraint(columnNames = "order_number"),
        indexes = {
                @Index(name = "idx_customer_name", columnList = "customer_name"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_created_at", columnList = "created_at"),
                @Index(name = "idx_customer_email", columnList = "customer_email")
        })
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public Order(String customerName, String customerEmail, BigDecimal totalAmount, String orderNumber) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.orderNumber = orderNumber;
    }

    public Order(String customerName, String customerEmail, BigDecimal totalAmount, String notes, String orderNumber) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.orderNumber = orderNumber;
    }

    /**
     * Check if order is in a final state (cannot be modified)
     */
    public boolean isFinalState() {
        return "DELIVERED".equals(this.status) || "CANCELLED".equals(this.status);
    }

    /**
     * Check if order can be cancelled
     */
    public boolean canBeCancelled() {
        return "PENDING".equals(this.status) || "CONFIRMED".equals(this.status);
    }

    /**
     * Update status with validation
     */
    public void updateStatus(String newStatus) {
        if (isFinalState()) {
            throw new IllegalStateException("Cannot update status of order in final state: " + this.status);
        }

        switch (newStatus) {
            case "CONFIRMED" -> {
                if (!"PENDING".equals(this.status)) {
                    throw new IllegalStateException("Can only confirm PENDING orders");
                }
            }
            case "SHIPPED" -> {
                if (!"CONFIRMED".equals(this.status)) {
                    throw new IllegalStateException("Can only ship CONFIRMED orders");
                }
            }
            case "DELIVERED" -> {
                if (!"SHIPPED".equals(this.status)) {
                    throw new IllegalStateException("Can only deliver SHIPPED orders");
                }
            }
            case "CANCELLED" -> {
                if (!canBeCancelled()) {
                    throw new IllegalStateException("Cannot cancel order in current state: " + this.status);
                }
            }
        }

        this.status = newStatus;
    }
}
