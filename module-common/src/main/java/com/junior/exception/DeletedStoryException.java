package com.junior.exception;

import lombok.Getter;

@Getter
public class DeletedStoryException extends RuntimeException {
    private StatusCode statusCode;

    public DeletedStoryException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}