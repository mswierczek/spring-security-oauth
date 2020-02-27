package com.baeldung.web.controller;

import com.baeldung.multitenancy.MultitenancyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MultitenancyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MultitenancyException.class)
    public ResponseEntity<Object> handleMultitenancyException(MultitenancyException multitenancyException, WebRequest webRequest) {
        return handleExceptionInternal(multitenancyException, multitenancyException.getMessage(), new HttpHeaders(),
            HttpStatus.UNAUTHORIZED, webRequest);
    }

}
