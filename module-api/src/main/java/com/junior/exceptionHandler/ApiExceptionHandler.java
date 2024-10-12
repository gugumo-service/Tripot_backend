package com.junior.exceptionHandler;

import com.junior.exception.CustomException;
import com.junior.response.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public CommonResponse handlerCustomException(CustomException e) {
        return CommonResponse.builder()
                .returnCode(e.getReturnCode())
                .returnMessage(e.getReturnMessage())
                .build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public CommonResponse handlerMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return CommonResponse.builder()
                .returnCode(e.getTypeMessageCode())
                .returnMessage(e.getMessage())
                .build();

    }


    /**
     * 랩핑하지 못한 예외가 발생하는 경우
     * 만약 랩핑하지 못한 예외가 발생하는 경우 아래 handlerException 메서드에서 캐치.
     * 이 메서드는 항상 맨 아래에 둘 것.
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse handlerException(Exception e) {
        return CommonResponse.builder()
                .returnCode("500")
                .returnMessage(e.getMessage())
                .build();

    }
}
