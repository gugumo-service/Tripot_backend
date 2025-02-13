package com.junior.dto.oauth2;

import lombok.Builder;


@Builder
public record OAuth2UserInfo(
        String id,
        String nickname,
        OAuth2Provider provider
) {
}




