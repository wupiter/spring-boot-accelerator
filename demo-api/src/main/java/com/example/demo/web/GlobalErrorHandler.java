package com.example.demo.web;

import com.example.demo.error.ApiException;
import com.example.demo.model.ErrorResponse;
import com.example.demo.utils.spotbugs.SuppressFBWarnings;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        return buildErrorResponse(e.getHttpStatus(), e.getMessage(), e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        String message = Optional.ofNullable(e.getFieldError())
                .map(fieldError -> String.format("The %s %s",
                        fieldError.getField(), fieldError.getDefaultMessage()))
                .orElse(e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error. Please try again", e);
    }

    @SuppressFBWarnings("CRLF_INJECTION_LOGS")
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus httpStatus, String message, Exception e) {
        log.error("Failed to process request - HTTP {} - {}", httpStatus.value(), message, e);
        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(message));
    }
}
