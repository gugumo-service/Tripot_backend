package com.junior.dto.admin.notice;

import com.querydsl.core.annotations.QueryProjection;

public record NoticeDto(
        Long id,
        String title
) {

    @QueryProjection
    public NoticeDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
