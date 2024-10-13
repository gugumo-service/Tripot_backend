package com.junior.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCode {

/*
    example)
    NOT_FOUND(404, "COMMON-ERR-404", "존재하지 않는 페이지입니다."),
*/

    ACTIVATE_MEMBER(200, "MEMBER-SUCCESS-001", "회원 활성화 성공"),
    CHECK_NICKNAME_MEMBER(200, "MEMBER-SUCCESS-002", "닉네임 사용가능 여부"),
    DELETE_MEMBER(200, "MEMBER-SUCCESS-003", "회원 삭제 성공"),

    EXPIRED_TOKEN(400, "JWT-ERR-001", "만료된 토큰입니다."),
    TOKEN_NOT_EXIST(400, "JWT-ERR-002", "존재하지 않는 토큰입니다."),
    INVALID_REFRESH_TOKEN(400, "JWT-ERR-003", "Refresh token이 아닙니다."),
    INVALID_MEMBER(400, "MEMBER-ERR-001", "유효하지 않은 회원입니다."),


    // 공통 예외
    UNKNOWN_ERROR(500, "UNKNOWN-ERR-000", "정의되지 않은 예외")
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

}
