package com.junior.dto.notice;

import lombok.Builder;

@Builder
public record NoticeDetailDto(
        String title,
        String content,

        String authorNick
) {

}

