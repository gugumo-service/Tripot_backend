package com.junior.exception;

import lombok.Getter;

@Getter
public class NotValidMemberException extends RuntimeException {

    private ErrorCode errorCode;

    public NotValidMemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
