package com.junior.dto.member;

import lombok.Builder;

@Builder
public record MemberInfoDto(
        String nickname,
        String profileImageUrl
) {
}
