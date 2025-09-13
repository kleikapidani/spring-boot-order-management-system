package com.ecommerce.analytics.orderservice.service;

import com.ecommerce.analytics.orderservice.dto.OrderRequest;
import com.ecommerce.analytics.orderservice.dto.OrderResponse;
import com.ecommerce.analytics.orderservice.dto.OrderSearchCriteria;
import com.ecommerce.analytics.orderservice.dto.OrderStatsResponse;
import com.ecommerce.analytics.orderservice.exception.OrderNotFoundException;
import com.ecommerce.analytics.orderservice.model.Order;
import com.ecommerce.analytics.orderservice.repository.OrderRepository;
import com.ecommerce.analytics.orderservice.specification.OrderSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Service layer for Order business logic
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Create a new order
     */
    public OrderResponse createOrder(OrderRequest orderRequest) {

        Order order = new Order(orderRequest.getCustomerName(),
                orderRequest.getCustomerEmail(),
                orderRequest.getTotalAmount(),
                orderRequest.getNotes(),
                generateOrderNumber());

        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public Slice<OrderResponse> getAllOrders(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Slice<Order> orders = orderRepository.findAll(pageable);

        return orders.map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        return convertToResponse(order);
    }

    /**
     * Update existing order
     */
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        existingOrder.setCustomerName(orderRequest.getCustomerName());
        existingOrder.setCustomerEmail(orderRequest.getCustomerEmail());
        existingOrder.setTotalAmount(orderRequest.getTotalAmount());
        existingOrder.setNotes(orderRequest.getNotes());

        Order updatedOrder = orderRepository.save(existingOrder);

        return convertToResponse(updatedOrder);
    }

    /**
     * Delete order
     */
    public void deleteOrder(Long id) {

        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }

        orderRepository.deleteById(id);
    }

    /**
     * Search orders with criteria
     */
    @Transactional(readOnly = true)
    public Slice<OrderResponse> searchOrders(OrderSearchCriteria orderSearchCriteria,
                                             int page,
                                             int size,
                                             String sortBy,
                                             String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Order> orderSpecification = Specification.<Order>unrestricted()
                .and(OrderSpecification.hasCustomerName(orderSearchCriteria.getCustomerName()))
                .and(OrderSpecification.hasStatus(orderSearchCriteria.getStatus()))
                .and(OrderSpecification.hasAmountBetween(orderSearchCriteria.getMinAmount(), orderSearchCriteria.getMaxAmount()))
                .and(OrderSpecification.hasMinAmount(orderSearchCriteria.getMinAmount()));

        Slice<Order> orders = orderRepository.findAll(orderSpecification, pageable);

        return orders.map(this::convertToResponse);
    }

    /**
     * Get orders by customer name with pagination
     */
    @Transactional(readOnly = true)
    public Slice<OrderResponse> getOrdersByCustomerName(String customerName,
                                                        int page,
                                                        int size,
                                                        String sortBy,
                                                        String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Slice<Order> orders = orderRepository.findByCustomerName(customerName, pageable);

        return orders.map(this::convertToResponse);
    }

    /**
     * Get orders by status with pagination
     */
    @Transactional(readOnly = true)
    public Slice<OrderResponse> getOrdersByStatus(String status,
                                                  int page,
                                                  int size,
                                                  String sortBy,
                                                  String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Slice<Order> orders = orderRepository.findByStatus(status, pageable);

        return orders.map(this::convertToResponse);
    }

    /**
     * Get order statistics
     */
    @Transactional(readOnly = true)
    public OrderStatsResponse getOrderStatistics() {

        OrderStatsResponse orderStatsResponse = new OrderStatsResponse();

        orderStatsResponse.setTotalOrders(orderRepository.count());
        orderStatsResponse.setPendingOrders(orderRepository.countByStatus("PENDING"));
        orderStatsResponse.setConfirmedOrders(orderRepository.countByStatus("CONFIRMED"));
        orderStatsResponse.setShippedOrders(orderRepository.countByStatus("SHIPPED"));
        orderStatsResponse.setDeliveredOrders(orderRepository.countByStatus("DELIVERED"));
        orderStatsResponse.setCancelledOrders(orderRepository.countByStatus("CANCELLED"));

        BigDecimal totalRevenueCalc;
        BigDecimal totalRevenueResult = BigDecimal.ZERO;

        int startOffset = 0;
        int endOffset = 50000;

        Slice<Order> orders;

        do {

            Pageable pageable = PageRequest.of(startOffset, endOffset);
            orders = orderRepository.findAll(pageable);

            totalRevenueCalc = orders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalRevenueResult = totalRevenueResult.add(totalRevenueCalc);

            startOffset += endOffset;
            endOffset *= 2;

        } while (orders.hasNext());

        orderStatsResponse.setTotalRevenue(totalRevenueResult);

        if (orderStatsResponse.getTotalOrders() > 0) {
            orderStatsResponse.setAverageOrderValue(
                    totalRevenueResult.divide(BigDecimal.valueOf(orderStatsResponse.getTotalOrders()), 2, RoundingMode.HALF_UP)
            );
        } else {
            orderStatsResponse.setAverageOrderValue(BigDecimal.ZERO);
        }

        return orderStatsResponse;
    }

    /**
     * Update order status
     */
    public OrderResponse updateOrderStatus(Long id, String status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);

        return convertToResponse(updatedOrder);
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(order.getId(),
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getNotes(),
                order.getCreatedAt());
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6);
    }
}
