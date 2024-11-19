package com.junior.dto.admin.notice;

import lombok.Builder;


public record CreateNoticeDto(
        String title,
        String content
) {
}
