package com.ecommerce.analytics.orderservice.controller;

import com.ecommerce.analytics.orderservice.dto.OrderRequest;
import com.ecommerce.analytics.orderservice.dto.OrderResponse;
import com.ecommerce.analytics.orderservice.dto.OrderSearchCriteria;
import com.ecommerce.analytics.orderservice.dto.OrderStatsResponse;
import com.ecommerce.analytics.orderservice.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Order Management
 * Provides comprehensive CRUD operations and business logic for orders
 *
 * @author Klei Kapidani
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order
     * POST /api/v1/orders
     */
    @PostMapping("/create-order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    /**
     * Get all orders with pagination and sorting
     * GET /api/v1/orders/getOrders?page=0&size=10&sort=createdAt,desc
     */
    @GetMapping("/getOrders")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        List<OrderResponse> orders = orderService.getAllOrders(page, size, sortBy, sortDirection).getContent();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order by ID
     * GET /api/v1/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable @Min(1) Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * Update existing order
     * PUT /api/v1/orders/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse orderResponse = orderService.updateOrder(id, orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * Delete order
     * DELETE /api/v1/orders/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable @Min(1) Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Search orders with multiple criteria
     * GET /api/v1/orders/search?customerName=John&status=PENDING&minAmount=100
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrderResponse>> searchOrder(
            @Valid @RequestBody OrderSearchCriteria orderSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {

        List<OrderResponse> orderResponses = orderService.searchOrders(orderSearchCriteria,
                page,
                size,
                sortBy,
                sortDirection).getContent();

        return ResponseEntity.ok(orderResponses);
    }

    /**
     * Get order statistics
     * GET /api/v1/orders/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<OrderStatsResponse> getOrderStats() {

        OrderStatsResponse orderStatsResponse = orderService.getOrderStatistics();
        return ResponseEntity.ok(orderStatsResponse);
    }

    /**
     * Update order status
     */
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        OrderResponse orderResponse = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(orderResponse);
    }
}
