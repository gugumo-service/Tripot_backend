package com.junior.dto;

import lombok.Getter;

@Getter
public class ActivateMemberDto {
    private String nickname;
    // 서비스 이용 약관 동의 여부
    private Boolean isAgreeTermsUse;
    // 개인정보 수집 및 이용 동의
    private Boolean isAgreeCollectingUsingPersonalInformation;
    // 마케팅 수신 동의
    private Boolean isAgreeMarketing;

    public ActivateMemberDto(String nickname, Boolean isAgreeTermsUse, Boolean isAgreeCollectingUsingPersonalInformation, Boolean isAgreeMarketing) {
        this.nickname = nickname;
        this.isAgreeTermsUse = isAgreeTermsUse;
        this.isAgreeCollectingUsingPersonalInformation = isAgreeCollectingUsingPersonalInformation;
        this.isAgreeMarketing = isAgreeMarketing;
    }
}
