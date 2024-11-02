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



    // STORY 관련 예외
    STORY_CREATE_SUCCESS(200, "STORY-0000", "스토리 생성 성공"),
    STORY_CREATE_FAIL(500, "STORY-0001", "스토리 생성 실패"),
    STORY_BAD_REQUEST(500, "STORY-0002", "잘못된 파라미터"),

    //s3 관련 예외
    S3_UPLOAD_SUCCESS(200, "S3-0000", "이미지 업로드 성공"),
    S3_UPLOAD_FAIL(500, "S3-0001", "이미지 업로드 실패"),
    S3_DUPLICATE_FILE(500, "S3-0002", "파일 중복"),
    S3_NOT_ALLOWED_EXTENSION(500, "S3-0003", "알 수 없는 이미지 확장자"),
    S3_BIGGER_THAN_MAX_SIZE(500, "S3-FAIL-0004", "사진 용량이 너무 큼"),

    // USER 관련 예외
    ACTIVATE_MEMBER(200, "MEMBER-SUCCESS-001", "회원 활성화 성공"),
    CHECK_NICKNAME_MEMBER(200, "MEMBER-SUCCESS-002", "닉네임 사용가능 여부"),
    DELETE_MEMBER(200, "MEMBER-SUCCESS-003", "회원 삭제 성공"),
    OAUTH2_LOGIN_SUCCESS(200, "MEMBER-SUCCESS-004", "소셜 로그인 성공"),
    UPDATE_NICKNAME_MEMBER(200, "MEMBER-SUCCESS-005", "회원 닉네임 변경 성공"),

    // JWT 관련 예외
    EXPIRED_TOKEN(400, "JWT-ERR-001", "만료된 토큰"),
    TOKEN_NOT_EXIST(400, "JWT-ERR-002", "존재하지 않는 토큰"),
    INVALID_REFRESH_TOKEN(400, "JWT-ERR-003", "Refresh token이 아님"),
    INVALID_MEMBER(400, "MEMBER-ERR-001", "유효하지 않은 회원"),

    // 공통 예외
    UNKNOWN_ERROR(9999, "UNKNOWN-ERROR", "정의되지 않은 예외");

    private final int httpCode;
    private final String customCode;
    private final String customMessage;

}
