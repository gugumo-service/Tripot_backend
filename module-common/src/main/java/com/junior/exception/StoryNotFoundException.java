package com.junior.exception;

import lombok.Getter;

@Getter
public class StoryNotFoundException extends RuntimeException {

    private StatusCode statusCode;

    public StoryNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
