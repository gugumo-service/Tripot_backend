package com.junior.dto.notice;

import lombok.Builder;

@Builder
public record NoticeDetailDto(
        Long id,
        String title,
        String content
) {

}

