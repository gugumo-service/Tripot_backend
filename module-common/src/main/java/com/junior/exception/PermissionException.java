package com.junior.exception;

public class PermissionException extends RuntimeException {
    private StatusCode statusCode;

    public PermissionException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
