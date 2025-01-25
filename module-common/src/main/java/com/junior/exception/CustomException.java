package com.junior.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private String returnCode;
    private String returnMessage;

    public CustomException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.returnCode = statusCode.getCustomCode();
        this.returnMessage = statusCode.getCustomMessage();
    }
}