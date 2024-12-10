package com.junior.exception;

import lombok.Getter;

@Getter
public class QnaException extends RuntimeException {

  private StatusCode statusCode;

    public QnaException(StatusCode statusCode) {
      super(statusCode.getCustomMessage());
      this.statusCode = statusCode;
    }
}
