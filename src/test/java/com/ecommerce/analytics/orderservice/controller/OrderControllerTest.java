package com.ecommerce.analytics.orderservice.controller;

import com.ecommerce.analytics.orderservice.dto.OrderRequest;
import com.ecommerce.analytics.orderservice.dto.OrderResponse;
import com.ecommerce.analytics.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;

    @BeforeEach
    public void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setCustomerName("John Doe");
        orderRequest.setCustomerEmail("john@example.com");
        orderRequest.setTotalAmount(BigDecimal.valueOf(150.00));
        orderRequest.setNotes("Test order");

        orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setOrderNumber("ORD-123456");
        orderResponse.setCustomerName("John Doe");
        orderResponse.setCustomerEmail("john@example.com");
        orderResponse.setTotalAmount(BigDecimal.valueOf(150.00));
        orderResponse.setStatus("PENDING");
        orderResponse.setNotes("Test order");
        orderResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    public void createOrderSuccessfully() throws Exception {

        Mockito.when(orderService.createOrder(Mockito.any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/v1/orders/create-order")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORD-123456"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        Mockito.verify(orderService).createOrder(Mockito.any(OrderRequest.class));
    }
}
