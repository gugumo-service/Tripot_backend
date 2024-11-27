package com.junior.exception;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
    }
}
