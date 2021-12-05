package com.test.car.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.test.car.api.exception.DataNotFoundException;
import com.test.car.api.model.ErrorMessage;
import com.test.car.api.model.Severity;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({DataNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNotFoundException(final DataNotFoundException ex,
                                                                final WebRequest request) {
        return new ResponseEntity<>(
            ErrorMessage.builder().message(ex.getMessage()).field(ex.getField()).severity(Severity.DATA).build(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorMessage> handleRuntimeException(final RuntimeException ex,
                                                               final WebRequest request) {
        return new ResponseEntity<>(
            ErrorMessage.builder().message("Internal error").severity(Severity.FATAL).build(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
