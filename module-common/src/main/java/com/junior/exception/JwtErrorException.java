package com.junior.exception;

import lombok.Getter;

@Getter
public class JwtErrorException extends RuntimeException {

  private ErrorCode errorCode;

  public JwtErrorException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;

  }
}
