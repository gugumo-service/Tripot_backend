package com.junior.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
    }
}
