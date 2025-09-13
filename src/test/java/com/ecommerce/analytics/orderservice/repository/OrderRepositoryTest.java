package com.ecommerce.analytics.orderservice.repository;

import com.ecommerce.analytics.orderservice.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.math.BigDecimal;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder1;
    private Order testOrder2;
    private Order testOrder3;

    @BeforeEach
    public void setUp() {

        testOrder1 = new Order();
        testOrder1.setOrderNumber("ORD-001");
        testOrder1.setCustomerName("John Doe");
        testOrder1.setCustomerEmail("john@example.com");
        testOrder1.setTotalAmount(BigDecimal.valueOf(150.00));
        testOrder1.setStatus("PENDING");
        testOrder1.setNotes("Test order 1");

        testOrder2 = new Order();
        testOrder2.setOrderNumber("ORD-002");
        testOrder2.setCustomerName("Jane Smith");
        testOrder2.setCustomerEmail("jane@example.com");
        testOrder2.setTotalAmount(BigDecimal.valueOf(250.00));
        testOrder2.setStatus("CONFIRMED");
        testOrder2.setNotes("Test order 2");

        testOrder3 = new Order();
        testOrder3.setOrderNumber("ORD-003");
        testOrder3.setCustomerName("John Wilson");
        testOrder3.setCustomerEmail("johnw@example.com");
        testOrder3.setTotalAmount(BigDecimal.valueOf(75.00));
        testOrder3.setStatus("PENDING");
        testOrder3.setNotes("Test order 3");

        entityManager.persistAndFlush(testOrder1);
        entityManager.persistAndFlush(testOrder2);
        entityManager.persistAndFlush(testOrder3);
    }

    @Test
    public void findByCustomerNameWhenSliceContentNotEmpty() {

        Pageable pageable = PageRequest.of(0, 3);

        Slice<Order> orders = orderRepository.findByCustomerName("John Wilson", pageable);
        Assertions.assertTrue(orders.hasContent());
    }

    @Test
    public void findHighValueOrdersCustomerWhenSliceContentNotEmpty() {

        Slice<Order> orders = orderRepository.findHighValueOrdersCustomer(BigDecimal.valueOf(250.00));
        Assertions.assertTrue(orders.hasContent());
    }
}
