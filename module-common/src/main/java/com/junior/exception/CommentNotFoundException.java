package com.junior.exception;

public class CommentNotFoundException extends RuntimeException {

    private StatusCode statusCode;
    public CommentNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
