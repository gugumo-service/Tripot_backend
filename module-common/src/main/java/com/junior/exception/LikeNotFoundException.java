package com.junior.exception;

public class LikeNotFoundException extends RuntimeException {

    private StatusCode statusCode;

    public LikeNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
