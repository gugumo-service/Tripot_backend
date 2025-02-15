package com.junior.exception;

public class NotificationNotFoundException extends RuntimeException {
    private StatusCode statusCode;

    public NotificationNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
