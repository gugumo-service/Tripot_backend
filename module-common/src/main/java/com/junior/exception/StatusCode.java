package com.junior.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

/*
    example)
    NOT_FOUND(404, "COMMON-ERR-404", "존재하지 않는 페이지입니다."),
*/

    SUCCESS(404, "SUCCESS", "성공"),
    UNKNOWN_ERROR(9999, "UNKNOWN-ERROR", "알 수 없는 에러"),
    ;

    private int status;
    private String code;
    private String message;

}
