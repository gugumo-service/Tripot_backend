package com.junior.handler;

import com.junior.exception.*;
import com.junior.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public CommonResponse handlerCustomException(CustomException e) {
        return CommonResponse.builder()
                .customCode(e.getReturnCode())
                .customMessage(e.getReturnMessage())
                .build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonResponse<Object>> handlerMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        StatusCode statusCode = StatusCode.S3_BIGGER_THAN_MAX_SIZE;
        return ResponseEntity.status(statusCode.getHttpCode()).body(CommonResponse.fail(statusCode));
    }

    @ExceptionHandler(JwtErrorException.class)
    protected ResponseEntity<CommonResponse<Object>> handleJwtErrorException(JwtErrorException e) {
        log.warn("handleJwtErrorException : {}", e.getMessage());
//        final ErrorResponse response = ErrorResponse.of(e.getStatusCode());
//        return ResponseEntity.status(e.getStatusCode().getHttpCode()).body(response);
        StatusCode statusCode = e.getStatusCode();
        return ResponseEntity.status(statusCode.getHttpCode()).body(CommonResponse.fail(statusCode));
    }

    @ExceptionHandler(NotValidMemberException.class)
    protected ResponseEntity<CommonResponse<Object>> handleNotValidMemberException(NotValidMemberException e) {
        log.warn("handleNotValidMemberException : {}", e.getMessage());
//        final ErrorResponse response = ErrorResponse.of(e.getStatusCode());
//        return ResponseEntity.status(e.getStatusCode().getHttpCode()).body(response);
        StatusCode statusCode = e.getStatusCode();
        return ResponseEntity.status(statusCode.getHttpCode()).body(CommonResponse.fail(statusCode));
    }


    @ExceptionHandler(NoticeException.class)
    protected ResponseEntity<CommonResponse<Object>> handleNoticeException(NoticeException e) {
        log.warn("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());
//        final ErrorResponse response = ErrorResponse.of(e.getStatusCode());
//        return ResponseEntity.status(e.getStatusCode().getHttpCode()).body(response);
        StatusCode statusCode = e.getStatusCode();
        return ResponseEntity.status(statusCode.getHttpCode()).body(CommonResponse.fail(statusCode));
    }

    @ExceptionHandler(QnaException.class)
    protected ResponseEntity<CommonResponse<Object>> handleNoticeException(QnaException e) {
        log.warn("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());
//        final ErrorResponse response = ErrorResponse.of(e.getStatusCode());
//        return ResponseEntity.status(e.getStatusCode().getHttpCode()).body(response);
        StatusCode statusCode = e.getStatusCode();
        return ResponseEntity.status(statusCode.getHttpCode()).body(CommonResponse.fail(statusCode));
    }

    @ExceptionHandler(ReportException.class)
    protected ResponseEntity<CommonResponse<Object>> handleNoticeException(ReportException e) {
        log.warn("{} : {}", Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());
//        final ErrorResponse response = ErrorResponse.of(e.getStatusCode());
//        return ResponseEntity.status(e.getStatusCode().getHttpCode()).body(response);
        StatusCode statusCode = e.getStatusCode();
        return ResponseEntity.status(statusCode.getHttpCode()).body(CommonResponse.fail(statusCode));
    }


    /**
     * 랩핑하지 못한 예외가 발생하는 경우
     * 만약 랩핑하지 못한 예외가 발생하는 경우 아래 handlerException 메서드에서 캐치.
     * 이 메서드는 항상 맨 아래에 둘 것.
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse handlerException(Exception e) {
        log.error("[{}] : {}, {}", Thread.currentThread().getStackTrace()[1].getMethodName(), e.getCause(), e.getMessage());
        return CommonResponse.builder()
                .customCode("500")
                .customMessage(e.getMessage())
                .build();
    }
}
