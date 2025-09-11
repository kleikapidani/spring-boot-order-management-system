package com.ecommerce.analytics.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Statistics response for orders
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatsResponse {

    private long totalOrders;
    private long pendingOrders;
    private long confirmedOrders;
    private long shippedOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
}
