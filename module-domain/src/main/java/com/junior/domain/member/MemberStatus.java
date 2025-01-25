package com.junior.domain.member;

public enum MemberStatus {
    /**
     * PREACTIVE: 회원가입 후 추가정보기 입력되지 않은 상태
     * ACTIVE: 회원강비 후 추가정보가 입력된 상태
     * INACTIVE: 모종의 이유로 계정이 비활성화된 상태
     * DELETE: 회원을 탈퇴한 상태
     */
    PREACTIVE, ACTIVE, INACTIVE, DELETE,

}
