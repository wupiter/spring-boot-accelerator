package com.example.demo.web;

import com.example.demo.error.ApiException;
import com.example.demo.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        return buildErrorResponse(e.getHttpStatus(), e.getMessage(), e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = String.format("The %s %s", e.getFieldError().getField(), e.getFieldError().getDefaultMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error. Please try again", e);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus httpStatus, String message, Exception e) {
        log.error("Failed to process request - HTTP {} - {}", httpStatus.value(), message, e);
        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(message));
    }
}
