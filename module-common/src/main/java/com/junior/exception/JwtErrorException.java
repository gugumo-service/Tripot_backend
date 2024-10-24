package com.junior.exception;

import lombok.Getter;

@Getter
public class JwtErrorException extends RuntimeException {

  private StatusCode statusCode;

  public JwtErrorException(StatusCode statusCode) {
    super(statusCode.getMessage());
    this.statusCode = statusCode;

  }
}
