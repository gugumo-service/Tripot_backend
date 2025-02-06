package com.junior.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
    }
}
