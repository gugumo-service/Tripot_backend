package com.junior.exception;

import lombok.Getter;

@Getter
public class ReportException extends RuntimeException {

    private StatusCode statusCode;

    public ReportException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
