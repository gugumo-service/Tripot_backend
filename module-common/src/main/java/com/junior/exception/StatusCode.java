package com.junior.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCode {    //ErrorCode -> StatusCode error뿐 아니라 성공의 메시지도 담기 때문에 error는 적합하지 않은 것 같음.

    // STORY 관련 예외
    STORY_CREATE_SUCCESS(200, "STORY-0000", "스토리 생성 성공"),
    STORY_CREATE_FAIL(500, "STORY-0001", "스토리 생성 실패"),
    STORY_BAD_REQUEST(500, "STORY-0002", "잘못된 파라미터"),
    
    
    // USER 관련 예외

    
    // 공통 예외
    UNKNOWN_ERROR(9999, "UNKNOWN-ERROR", "정의되지 않은 예외"),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;
}
