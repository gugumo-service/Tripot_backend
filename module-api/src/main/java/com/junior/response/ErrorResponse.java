package com.junior.response;

import com.junior.exception.StatusCode;
import lombok.Builder;

@Builder
public record ErrorResponse(
        String status,
        String errorCode,
        String message
) {
    public static ErrorResponse of(StatusCode statusCode) {
        return ErrorResponse.builder()
                .status("error")
                .errorCode(statusCode.getCode())
                .message(statusCode.getMessage())
                .build();
    }
}
