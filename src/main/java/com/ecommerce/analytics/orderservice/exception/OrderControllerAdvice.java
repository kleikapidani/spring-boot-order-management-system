package com.ecommerce.analytics.orderservice.exception;

import com.ecommerce.analytics.orderservice.controller.OrderController;
import com.ecommerce.analytics.orderservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestControllerAdvice(assignableTypes = OrderController.class)
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
                HttpStatus.NOT_FOUND.value(),
                new HashMap<>()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle Illegal State Exception
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "ILLEGAL_STATE",
                ex.getMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                new HashMap<>()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
