package com.junior.exception;

public class PermissionException extends RuntimeException{
    public PermissionException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
    }
}
