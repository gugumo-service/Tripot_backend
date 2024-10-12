package com.junior.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

/*
    example)
    NOT_FOUND(404, "COMMON-ERR-404", "존재하지 않는 페이지입니다."),
*/

    EXPIRED_TOKEN(400, "JWT-ERR-001", "만료된 토큰입니다."),
    TOKEN_NOT_EXIST(400, "JWT-ERROR-002", "존재하지 않는 토큰입니다."),
    INVALID_REFRESH_TOKEN(400, "JWT-ERROR-003", "Refresh token이 아닙니다."),
    INVALID_MEMBER(400, "MEMBER-ERROR-001", "유효하지 않은 회원입니다.")
    ;

    private int status;
    private String errorCode;
    private String message;

}
