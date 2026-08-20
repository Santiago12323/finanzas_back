package com.ingresos.back.infrastructure.exception;

import com.ingresos.back.domain.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, Object>> handleUserException(Throwable ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}