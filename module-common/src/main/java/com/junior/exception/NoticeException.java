package com.junior.exception;

import lombok.Getter;

@Getter
public class NoticeException extends RuntimeException {

    private StatusCode statusCode;

    public NoticeException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
