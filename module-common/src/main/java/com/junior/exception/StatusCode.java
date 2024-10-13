package com.junior.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCode {

    // STORY 관련 예외
    STORY_CREATE_SUCCESS(200, "STORY-0000", "스토리 생성 성공"),
    STORY_CREATE_FAIL(500, "STORY-0001", "스토리 생성 실패"),
    STORY_BAD_REQUEST(500, "STORY-0002", "잘못된 파라미터"),

    //s3 관련 예외
    S3_UPLOAD_SUCCESS(200, "S3-0000", "이미지 업로드 성공"),
    S3_UPLOAD_FAIL(500, "S3-0001", "이미지 업로드 실패"),
    S3_DUPLICATE_FILE(500, "S3-0002", "파일 중복"),
    S3_NOT_ALLOWED_EXTENSION(500, "S3-0003", "알 수 없는 이미지 확장자"),
    
    // USER 관련 예외


    // 공통 예외
    UNKNOWN_ERROR(9999, "UNKNOWN-ERROR", "정의되지 않은 예외"),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;
}
