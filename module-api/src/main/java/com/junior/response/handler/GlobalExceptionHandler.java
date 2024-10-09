package com.junior.response.handler;

import com.junior.exception.JwtErrorException;
import com.junior.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtErrorException.class)
    protected ResponseEntity<ErrorResponse> handleJwtErrorException(JwtErrorException e) {
        log.warn("handleGlobalErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode().getStatus(), e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }
}
