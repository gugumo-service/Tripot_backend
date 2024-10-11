package com.junior.dto;

import lombok.Getter;


public record ActivateMemberDto(
        String nickname,
        // 서비스 이용 약관 동의 여부
        Boolean isAgreeTermsUse,
        // 개인정보 수집 및 이용 동의
        Boolean isAgreeCollectingUsingPersonalInformation,
        // 마케팅 수신 동의
        Boolean isAgreeMarketing,
        String recommendLocation

) {

}



