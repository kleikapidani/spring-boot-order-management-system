package com.ecommerce.analytics.orderservice.exception;

import com.ecommerce.analytics.orderservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class OrderControllerAdvice {

    /**
     * Handle Order Not Found Exception
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "ORDER_NOT_FOUND",
                ex.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
