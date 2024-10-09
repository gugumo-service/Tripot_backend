package com.junior.response;

import com.junior.exception.ErrorCode;
import lombok.Builder;

@Builder
public record ErrorResponse(
        int status,
        String errorCode,
        String message
) {
    public static ErrorResponse of(int status, ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .build();
    }
}
