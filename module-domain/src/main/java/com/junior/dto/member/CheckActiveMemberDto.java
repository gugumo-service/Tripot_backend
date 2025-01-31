package com.junior.dto.member;

import lombok.Builder;

@Builder
public record CheckActiveMemberDto (
        String nickname,
        Boolean isActivate

) {

}
