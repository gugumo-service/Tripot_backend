package com.junior.dto.oauth2;

import lombok.Builder;


@Builder
public record OAuth2UserInfo(
        Long id,
        String nickname,
        OAuth2Provider provider
) {
}




