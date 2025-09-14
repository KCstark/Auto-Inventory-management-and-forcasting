package com.inventory_forcasting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("succes", false);
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<Object> handleProductValidationException(ProductValidationException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("succes", false);
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<Object> handleInventoryException(InventoryException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("succes", true);
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(SaleFailedException.class)
    public ResponseEntity<Object> handleSaleFailedException(SaleFailedException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("succes", false);
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex){
//        Map<String, Object> response = new HashMap<>();
//        response.put("succes", false);
//        response.put("message", ex.getMessage());
//        response.put("timestamp", LocalDateTime.now());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
}
