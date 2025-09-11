package com.ecommerce.analytics.orderservice.service;

import com.ecommerce.analytics.orderservice.dto.OrderRequest;
import com.ecommerce.analytics.orderservice.dto.OrderResponse;
import com.ecommerce.analytics.orderservice.exception.OrderNotFoundException;
import com.ecommerce.analytics.orderservice.model.Order;
import com.ecommerce.analytics.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (orderRequest.getCustomerName() != null) {
            existingOrder.setCustomerName(orderRequest.getCustomerName());
        }

        if (orderRequest.getCustomerEmail() != null) {
            existingOrder.setCustomerEmail(orderRequest.getCustomerEmail());
        }

        if (orderRequest.getTotalAmount() != null) {
            existingOrder.setTotalAmount(orderRequest.getTotalAmount());
        }

        if (orderRequest.getNotes() != null) {
            existingOrder.setNotes(orderRequest.getNotes());
        }

        Order updatedOrder = orderRepository.save(existingOrder);

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
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6);
    }
}
