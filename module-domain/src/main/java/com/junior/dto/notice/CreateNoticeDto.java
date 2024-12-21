package com.junior.dto.notice;

import lombok.Builder;


public record CreateNoticeDto(
        String title,
        String content
) {
}
