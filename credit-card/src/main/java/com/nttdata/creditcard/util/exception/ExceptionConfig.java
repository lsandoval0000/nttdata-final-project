package com.nttdata.creditcard.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Exception config.
 */
@ControllerAdvice(annotations = RestController.class)
public class ExceptionConfig {
    /**
     * Bad request exception response entity.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestException(Exception exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
