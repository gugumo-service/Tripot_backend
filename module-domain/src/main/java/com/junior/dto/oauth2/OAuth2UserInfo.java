package com.junior.dto.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;


@Builder
public record OAuth2UserInfo (
        Long id,
        String nickname,
        OAuth2Provider provider
){}




