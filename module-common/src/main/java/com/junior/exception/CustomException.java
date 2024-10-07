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
        super(statusCode.getMessage());
        this.returnCode = statusCode.getCode();
        this.returnMessage = statusCode.getMessage();
    }
}
