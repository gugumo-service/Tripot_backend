package com.junior.dto.notice;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record NoticeAdminDto(
        Long id,
        String title
) {

    @QueryProjection
    public NoticeAdminDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
