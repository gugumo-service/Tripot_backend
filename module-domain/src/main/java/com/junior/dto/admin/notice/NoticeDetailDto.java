package com.junior.dto.admin.notice;

import lombok.Builder;

@Builder
public record NoticeDetailDto(
        String title,
        String content,

        String authorNick
) {

}

