package com.junior.dto.oauth2;

import lombok.Builder;

@Builder
public record OAuth2LoginDto(
        String id,
        String nickname
) {
}
