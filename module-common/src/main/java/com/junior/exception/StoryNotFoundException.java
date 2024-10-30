package com.junior.exception;

public class StoryNotFoundException extends RuntimeException{

    public StoryNotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
    }
}
