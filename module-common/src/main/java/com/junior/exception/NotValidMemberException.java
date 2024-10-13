package com.junior.exception;

import lombok.Getter;

@Getter
public class NotValidMemberException extends RuntimeException {

    private StatusCode statusCode;

    public NotValidMemberException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }
}
