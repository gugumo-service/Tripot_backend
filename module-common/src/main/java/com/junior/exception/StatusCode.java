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
    STORY_CREATE_SUCCESS(200, "STORY-SUCCESS-0000", "스토리 생성 성공"),
    STORY_NOT_FOUND(400, "STORY-ERR-0002", "스토리 불러오기 실패"),
    STORY_EDIT_SUCCESS(200, "STORY-SUCCESS-0001", "스토리 수정 성공"),
    STORY_CREATE_FAIL(500, "STORY-ERR-0001", "스토리 생성 실패"),
    STORY_BAD_REQUEST(500, "STORY-ERR-0002", "잘못된 파라미터"),
    STORY_READ_SUCCESS(200, "STORY-SUCCESS-0001", "스토리 불러오기 성공"),
    STORY_NOT_PERMISSION(400, "STORY-ERR-0003", "권한이 없는 스토리"),
    RECOMMENDED_CITY_SUCCESS(200, "RECOMMENDED-SUCCESS-001", "추천 도시 성공"),
    RECOMMENDED_STORIES_SUCCESS(200, "RECOMMENDED-SUCCESS-002", "추천 스토리 성공"),

    // like 관련 예외
    LIKE_NOT_FOUND(400, "LIKE-ERR-0001", "좋아요 조회 실패"),
    LIKE_CHANGE_SUCCESS(200, "LIKE-CHANGE-SUCCESS-0000", "좋아요 전환 성공"),

    // comment 관련 예외
    COMMENT_CREATE_SUCCESS(200, "COMMENT-SUCCESS-0001", "댓글 저장 성공"),
    COMMENT_READ_SUCCESS(200, "COMMENT-SUCCESS-0002", "댓글 불러오기 성공"),
    COMMENT_EDIT_SUCCESS(200, "COMMENT-SUCCESS-0003", "댓글 수정 성공"),
    COMMENT_DELETE_SUCCESS(200, "COMMENT-SUCCESS-0004", "댓글 삭제 성공"),
    COMMENT_NOT_FOUND(400, "COMMENT-ERR-0001", "댓글 불러오기 실패"),

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
    UPDATE_PROFILE_IMAGE_MEMBER(200, "MEMBER-SUCCESS-006", "회원 프로필 사진 변경 성공"),
    GET_MEMBER_INFO(200, "MEMBER-SUCCESS-007", "회원 정보 조회 성공"),
    INVALID_MEMBER(401, "MEMBER-ERR-001", "유효하지 않은 회원"),
    INVALID_MEMBER_STATUS(401, "MEMBER-ERR-002", "해당 회원 status로 실행할 수 없음"),
    MEMBER_NOT_FOUND(404, "MEMBER-ERR-003", "해당 회원을 찾을 수 없음"),

    // LOGIN 관련 상태코드
    ADMIN_LOGIN_SUCCESS(200, "LOGIN-SUCCESS-001", "관리자 로그인 성공"),
    ADMIN_LOGIN_FAILURE(401, "LOGIN-ERR-001", "관리자 로그인 실패"),

    // JWT 관련 예외
    REISSUE_SUCCESS(200, "JWT-SUCCESS-001", "JWT 재발급 완료"),
    LOGOUT(200, "JWT-SUCCESS-002", "로그아웃 완료"),
    EXPIRED_ACCESS_TOKEN(401, "JWT-ERR-001", "만료된 Access 토큰"),
    TOKEN_NOT_EXIST(400, "JWT-ERR-002", "존재하지 않는 토큰"),
    NOT_REFRESH_TOKEN(401, "JWT-ERR-003", "Refresh token이 아님"),
    EXPIRED_REFRESH_TOKEN(401, "JWT-ERR-004", "만료된 Refresh 토큰"),
    NOT_ACCESS_TOKEN(401, "JWT-ERR-005", "Access token이 아님"),
    ACCESS_DENIED(403, "JWT-ERR-006", "접근이 거부됨"),
    INVALID_TOKEN(401, "JWT-ERR-007", "유효하지 않은 토큰"),

    // 공지사항 관련 상태코드
    NOTICE_CREATE_SUCCESS(201, "NOTICE-SUCCESS-001", "공지사항 업로드 성공"),
    NOTICE_NOT_FOUND(400, "NOTICE-ERR-001", "해당 공지사항을 찾을 수 없음"),
    NOTICE_DELETE_SUCCESS(200, "NOTICE-SUCCESS-002", "공지사항 삭제 성공"),
    NOTICE_UPDATE_SUCCESS(200, "NOTICE-SUCCESS-003", "공지사항 수정 성공"),
    NOTICE_FIND_SUCCESS(200, "NOTICE-SUCCESS-004", "공지사항 조회 성공"),
    NOTICE_FIND_DETAIL_SUCCESS(200, "NOTICE-SUCCESS-005", "공지사항 세부정보 조회 성공"),


    // 공통 예외
    UNKNOWN_ERROR(9999, "UNKNOWN-ERROR", "정의되지 않은 예외");

    private final int httpCode;
    private final String customCode;
    private final String customMessage;

}
