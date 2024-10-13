package com.junior.response.handler;

import com.junior.exception.JwtErrorException;
import com.junior.exception.NotValidMemberException;
import com.junior.exception.StatusCode;
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
        log.warn("handleJwtErrorException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getStatusCode());
        return ResponseEntity.status(e.getStatusCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(NotValidMemberException.class)
    protected ResponseEntity<ErrorResponse> handleNotValidMemberException(NotValidMemberException e) {
        log.warn("handleNotValidMemberException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e.getStatusCode());
        return ResponseEntity.status(e.getStatusCode().getHttpStatus()).body(response);
    }

/*    *//**
     * 랩핑하지 못한 예외가 발생하는 경우
     * @param e
     * @return
     *
     * 만약 랩핑하지 못한 예외가 발생하는 경우 아래 handlerException 메서드에서 캐치.
     * 이 메서드는 항상 맨 아래에 둘 것.
     */
/*    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownErrorException(Exception e) {
        log.warn("handleUnknownErrorException : {}", e.getMessage());
        return ResponseEntity.status(StatusCode.UNKNOWN_ERROR.getHttpStatus()).body(ErrorResponse.of(StatusCode.UNKNOWN_ERROR));
    }*/
}
