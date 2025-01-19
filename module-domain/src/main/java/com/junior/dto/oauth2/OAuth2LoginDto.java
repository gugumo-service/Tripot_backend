package com.junior.dto.oauth2;

import lombok.Builder;

@Builder
public record OAuth2LoginDto(
        Long id,
        String nickname
) {
}
