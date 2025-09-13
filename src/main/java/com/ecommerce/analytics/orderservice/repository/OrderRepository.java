package com.ecommerce.analytics.orderservice.repository;

import com.ecommerce.analytics.orderservice.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;


public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Slice<Order> findByCustomerName(String customerName, Pageable pageable);

    Slice<Order> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);

    Slice<Order> findByCustomerNameContaining(String customerName, Pageable pageable);

    Slice<Order> findByCustomerNameStartingWithIgnoreCase(String customerNamePrefix, Pageable pageable);

    Slice<Order> findByStatus(String status, Pageable pageable);

    Slice<Order> findTopNByOrderByCreatedAtDesc(int limit, Pageable pageable);

    long countByCustomerName(String customerName);

    long countByStatus(String status);

    boolean existsByStatus(String status);

    @Query("""
            SELECT o FROM Order o WHERE o.totalAmount >= :amount
            ORDER BY o.totalAmount DESC
            """)
    Slice<Order> findHighValueOrdersCustomer(@Param("amount") BigDecimal amount);
}
